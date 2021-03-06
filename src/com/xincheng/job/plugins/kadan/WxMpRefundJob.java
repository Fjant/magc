package com.xincheng.job.plugins.kadan;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import jdbchelper.JdbcHelper;

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
import com.xincheng.job.service.JobIncrementService;
import com.xincheng.job.service.JobLogService;
import com.xincheng.utils.HttpHelper;
import com.xincheng.utils.PaymentUtil;
@Service
public class WxMpRefundJob implements BaseJob {

	private static Logger logger = LoggerFactory.getLogger(WxMpRefundJob.class);
	private static String MP_REFUND_URI = SystemConfig.getPara("KadanServiceUri") + "/mpRefund";
	private static String API_SECRET_KEY = SystemConfig.getPara("KadanServiceApiSecretKey");

	@Autowired
	private JobLogService joblogService;

	@Autowired
	private JobIncrementService jobIncrementService;

	@Autowired
	private ErrorRecordService errorRecordService;

	@Override
	public void execute(JobEntity jobEntity) {
		try {
			logger.info("执行微信服务号退款任务--开始");

			Date syncStartTime = new Date();
			boolean isHasError = false;
			int bizCount = 0;

			// 初始化
			ResourceBundle sqlBundle = null;
			String selectRefund = null;
			JdbcHelper kadanDBJdbc = null;
			try {
				logger.info("初始化微信服务号退款数据源及脚本");

				sqlBundle = ResourceBundle.getBundle("jdbc_sqls");
				selectRefund = sqlBundle.getString("KADAN_REFUND_MP_SELECT");

				kadanDBJdbc = new JdbcHelper(DataSourceUtil.getDataSource("oracle_kadan"));
			} catch (Exception ex) {
				logger.error("初始化连接失败，执行微信服务号退款任务终止 。", ex);
				isHasError = true;
			}

			// 默认取全部数据
			if (!isHasError) {
				try {
					
					/* 1、先处理退款信息，然后将待退款信息写入服务号退款表*/
					try {
						logger.info("处理待退款信息（如不足一元钱的退款）--开始");
						kadanDBJdbc.execute("{call USP_KD_REFUND_HANDLE}");
						logger.info("处理待退款信息（如不足一元钱的退款）--结束");
					} catch (Exception syncEx) {
						logger.error("处理待退款信息（如不足一元钱的退款）出错。", syncEx);
					}
					
					logger.info("查询待微信服务号退款数据--开始");
					
					// 2、查询本地数据库获取数据
					List<String> bizList = kadanDBJdbc.queryForStringList(selectRefund);
					logger.info("查询待微信服务号退款数据--结束");

					bizCount = bizList.size();

					if (bizList.size() > 0) {
						Map<String, String> paramsMap = new HashMap<String, String>();
						for (String refundMPId : bizList) {
							try {
								long timestamp = new Date().getTime();
								paramsMap.put("refundMPId", refundMPId);
								paramsMap.put("timestamp", String.valueOf(timestamp));

								String sign = PaymentUtil.buildRequestMysign(paramsMap, API_SECRET_KEY, "UTF-8");
								String params = "refundMPId=" + refundMPId + "&sign=" + sign + "&timestamp=" + timestamp;
								String result = HttpHelper.httpRequest(MP_REFUND_URI, "POST", params);
								logger.info("微信服务号退款,refundMpId=" + refundMPId + "," + result);
							} catch (Exception syncEx) {
								logger.error("处理微信服务号退款出错，refundMpId=" + refundMPId, syncEx);
							}
						}
					}
				} catch (Exception e) {
					logger.error("处理微信服务号退款出错。", e);
					isHasError = true;
					e.printStackTrace();
				}
			}
			// 记录日志
			try {
				JobLog logData = new JobLog();
				logData.setJobName(jobEntity.getName());
				logData.setIncTypeId(jobEntity.getIncTypeId());
				logData.setTotalNumber(bizCount);
				logData.setSuccessNumber(isHasError ? 0 : bizCount);
				logData.setResult(isHasError ? 0 : 1);
				logData.setStartTime(syncStartTime);
				logData.setEndTime(new Date());
				logData.setCreateTime(new Date());
				logData.setUpdateTime(new Date());
				logData.setRemark(jobEntity.getName());
				joblogService.save(logData);
			} catch (Exception ex) {
				logger.error("保存微信服务号退款日志失败。", ex);
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
		} catch (Exception allEx) {
			logger.error("执行微信服务号退款任务失败。", allEx);
		}

	}
}
