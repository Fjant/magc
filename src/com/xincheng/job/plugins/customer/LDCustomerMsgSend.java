package com.xincheng.job.plugins.customer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xincheng.config.SystemConfig;
import com.xincheng.errorObserver.model.ErrorRecord;
import com.xincheng.errorObserver.service.ErrorRecordService;
import com.xincheng.jdbc.DataSourceUtil;
import com.xincheng.job.model.JobEntity;
import com.xincheng.job.model.JobLog;
import com.xincheng.job.plugins.BaseJob;
import com.xincheng.job.service.JobLogService;
import com.xincheng.msg.enums.MsgSendType;
import com.xincheng.msg.model.MPMsg;
import com.xincheng.msg.service.MPMsgService;
import com.xincheng.wx.msgcsender.MPMsgSender;

import jdbchelper.JdbcHelper;
import jdbchelper.QueryResult;
import net.sf.json.JSONObject;

/**
 * 纯电子保单抽数
 * 
 */
@Service
public class LDCustomerMsgSend implements BaseJob {
	private static Logger logger = LoggerFactory.getLogger(LDCustomerMsgSend.class);
	private static String CLIENT_ACTIVITY_URL = SystemConfig.getPara("ESActivityUrlForClient");
	private static String ACTIVITY_MSG_TEMPLATE_ID = SystemConfig.getPara("ESActivityMSGTemplatId");
	private static String ACTIVITY_MSG_CONTENT = SystemConfig.getPara("ESActivityMSGContent");
	private static String ACTIVITY_MSG_TIME = SystemConfig.getPara("ESActivityMSGTtime");
	private static String ACTIVITY_MSG_REMARK = SystemConfig.getPara("ESActivityMSGRemark");
	private static String ACTIVITY_MSG_LOCATION = SystemConfig.getPara("ESActivityMSGLocation");

	@Autowired
	private ErrorRecordService errorRecordService;
	@Autowired
	private MPMsgService mpMsgService;
	@Autowired
	private JobLogService joblogService;
	public static void main(String[] a) {
		LDCustomerMsgSend impl = new LDCustomerMsgSend();
		impl.execute(null);
	}

	@Override
	public void execute(JobEntity jobEntity) {
		// TODO Auto-generated method stub
		Date syncStartTime = new Date();
		int customersCount = 0;
		boolean isHasError = false;
		final ResourceBundle sqlBundle = ResourceBundle.getBundle("jdbc_sqls");
		final String selectLDMsg = sqlBundle.getString("LD_CUSTOMER_MSG_SELECT");
		final String selectOpenID = sqlBundle.getString("CUSTOMER_SELECT_OPEN_ID");
		final String updateStatus = sqlBundle.getString("LD_CUSTOMER_MSG_UPDATE_STATUS");
		final String updateOpenID = sqlBundle.getString("LD_CUSTOMER_MSG_UPDATE_OPENDID");
		JdbcHelper luckydrawDBJdbc = null;
		JdbcHelper eseDBJdbc = null;
		JdbcHelper msgcDBJdbc = null;
		//1、获取抽奖系统LD_Customer_Msg表中未移交的数据status = 0
		try {
			luckydrawDBJdbc = new JdbcHelper(DataSourceUtil.getDataSource("oracle_luckydraw"));
			eseDBJdbc = new JdbcHelper(DataSourceUtil.getDataSource("db2_eservice"));
			msgcDBJdbc = new JdbcHelper(DataSourceUtil.getDataSource("oracle_msgc"));
			QueryResult result = luckydrawDBJdbc.query(selectLDMsg);
			result.setAutoClose(true);
			while (result.next()) {
				String billNum = result.getString("BILL_NUM");
				String cifId = result.getString("CIF_ID");
				String realName = result.getString("CUSTOMER_NAME");
				String openId = result.getString("OPEN_ID");
				int sex = Integer.parseInt(result.getString("SEX"));
				// 判断openID是否存在，是则保存发送短信，否则获取openId
				if (StringUtils.isNotBlank(openId)) {
					Map<String, Object> contentMap = new HashMap<String, Object>();
					contentMap.put("first", "尊敬的" + realName + (sex == 0 ? "" : (sex == 1 ? "先生" : "女士")) + "，");
					contentMap.put("content", ACTIVITY_MSG_CONTENT);
					contentMap.put("location", ACTIVITY_MSG_LOCATION);
					contentMap.put("time", ACTIVITY_MSG_TIME);
					contentMap.put("remark", ACTIVITY_MSG_REMARK);
					JSONObject jsonObj = JSONObject.fromObject(contentMap);
					MPMsg entity = new MPMsg();
					entity.setCifId(cifId);
					entity.setOpenId(openId);
					entity.setCustomerName(realName);
					entity.setUrl(CLIENT_ACTIVITY_URL);
					entity.setTemplateId(ACTIVITY_MSG_TEMPLATE_ID);
					entity.setContent(jsonObj.toString());
					entity.setRelSys("luckydraw");
					entity.setTypeId(7000);
					entity.setCreateBy("MSGC");
					entity.setCreateTime(new Date());
					entity.setSendType(MsgSendType.Immediately.getCode());
					entity.setStatusId(0);
					entity.setFromServer("");
					JSONObject resultObj = MPMsgSender.sendMsg(entity);
					entity.setResendTimes(1);
					if (resultObj != null && resultObj.containsKey("errcode")) {
						entity.setStatusId(resultObj.getInt("errcode") == 0 ? 0 : 1);
						entity.setSendResult(resultObj.getString("errcode"));
						entity.setSendTime(new Date());
						entity.setRemark(resultObj.getString("errmsg"));
					}
					mpMsgService.insert(entity);
					// 插入成功后回写status
					luckydrawDBJdbc.execute(updateStatus, billNum);
				} else {
					// 根据CIFID获取opendID
					if (StringUtils.isNotBlank(cifId)) {
						QueryResult openIdResult = eseDBJdbc.query(selectOpenID, cifId);
						openIdResult.setAutoClose(true);
						if (openIdResult.next()) {
							openId = openIdResult.getString("OPEN_ID");
							// 回写opendID 等待下一次消息发送捞数
							if (StringUtils.isNotBlank(openId)) {
								luckydrawDBJdbc.execute(updateOpenID, openId, billNum);
							}
						} else {
							logger.error("未查询到OpenID,等待下一次捞数。");
						}

					} else {
						logger.error("查询opendID失败,CIFID为空");

					}
				}
			}
		} catch (Exception ex) {
			logger.error("获取抽奖系统数据失败", ex);
			isHasError = true;
			ex.printStackTrace();
		}
		
		
		// 5、记录日志
		try {
			JobLog logData = new JobLog();
			logData.setJobName(jobEntity.getName());
			logData.setIncTypeId(jobEntity.getIncTypeId());
			logData.setTotalNumber(customersCount);
			logData.setSuccessNumber(isHasError ? 0 : customersCount);
			logData.setResult(isHasError ? 0 : 1);
			logData.setStartTime(syncStartTime);
			logData.setEndTime(new Date());
			logData.setCreateTime(new Date());
			logData.setUpdateTime(new Date());
			logData.setRemark(jobEntity.getName());
			joblogService.save(logData);
		} catch (Exception ex) {
			logger.error("保存微信发送日志出错。", ex);
			ex.printStackTrace();
		}

		if (isHasError) {
			ErrorRecord errorRecord = new ErrorRecord();
			errorRecord.setFunctionModularId(100000);
			errorRecord.setErrorLevel(10);
			errorRecord.setOperatorId("-1");
			errorRecord.setErrorMessage(jobEntity.getName() + " 失败！");
			errorRecord.setErrorTime(new Date());
			errorRecordService.save(errorRecord);
		}
	}

}
