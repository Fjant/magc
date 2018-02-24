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
public class KaPaymentJob implements BaseJob {

	private static Logger logger = LoggerFactory.getLogger(KaPaymentJob.class);
	private static String KA_PAYMENT_URI = SystemConfig.getPara("KadanServiceUri") + "/kaPayment";
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
			logger.info("执行付款写账任务--开始");

			Date syncStartTime = new Date();
			boolean isHasError = false;
			int bizCount = 0;

			// 初始化
			ResourceBundle sqlBundle = null;
			String selectPayment = null;
			JdbcHelper kadanDBJdbc = null;
			try {
				logger.info("初始化付款写账数据源及脚本");

				sqlBundle = ResourceBundle.getBundle("jdbc_sqls");
				selectPayment = sqlBundle.getString("KADAN_PAYMENT_KA_SELECT");

				kadanDBJdbc = new JdbcHelper(DataSourceUtil.getDataSource("oracle_kadan"));
			} catch (Exception ex) {
				logger.error("初始化连接失败，执行付款写账任务终止 。", ex);
				isHasError = true;
			}

			// 默认取全部数据
			if (!isHasError) {
				try {

					logger.info("查询待付款写账数据--开始");
					// 1、查询本地数据库获取数据
					List<String> bizList = kadanDBJdbc.queryForStringList(selectPayment);
					logger.info("查询待付款写账数据--结束");

					bizCount = bizList.size();

					if (bizList.size() > 0) {
						Map<String, String> paramsMap = new HashMap<String, String>();
						for (String orderDetailId : bizList) {
							/* 处理消息发送 */
							try {
								long timestamp = new Date().getTime();
								paramsMap.put("orderDetailId", orderDetailId);
								paramsMap.put("timestamp", String.valueOf(timestamp));

								String sign = PaymentUtil.buildRequestMysign(paramsMap, API_SECRET_KEY, "UTF-8");
								String params = "orderDetailId=" + orderDetailId + "&sign=" + sign + "&timestamp=" + timestamp;
								String result = HttpHelper.httpRequest(KA_PAYMENT_URI, "POST", params);
								logger.info("付款写账,orderDetailId=" + orderDetailId + "," + result);
							} catch (Exception syncEx) {
								logger.error("处理付款写账出错，orderDetailId=" + orderDetailId, syncEx);
							}
						}
					}
				} catch (Exception e) {
					logger.error("处理付款写账出错。", e);
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
				logger.error("保存付款写账日志失败。", ex);
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
			logger.error("执行付款写账任务失败。", allEx);
		}

	}
}
