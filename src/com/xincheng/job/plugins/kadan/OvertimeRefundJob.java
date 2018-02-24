package com.xincheng.job.plugins.kadan;

import java.util.Date;

import jdbchelper.JdbcHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xincheng.errorObserver.model.ErrorRecord;
import com.xincheng.errorObserver.service.ErrorRecordService;
import com.xincheng.jdbc.DataSourceUtil;
import com.xincheng.job.model.JobEntity;
import com.xincheng.job.model.JobLog;
import com.xincheng.job.plugins.BaseJob;
import com.xincheng.job.service.JobIncrementService;
import com.xincheng.job.service.JobLogService;
@Service
public class OvertimeRefundJob implements BaseJob {

	private static Logger logger = LoggerFactory.getLogger(OvertimeRefundJob.class);

	@Autowired
	private JobLogService joblogService;

	@Autowired
	private JobIncrementService jobIncrementService;

	@Autowired
	private ErrorRecordService errorRecordService;

	@Override
	public void execute(JobEntity jobEntity) {
		try {
			logger.info("订单超时未承保退款任务开始");

			Date syncStartTime = new Date();
			boolean isHasError = false;
			int usageCount = 0;

			// 初始化
			JdbcHelper kadanDBJdbc = null;

			try {
				logger.info("初始化超时未承保退款数据源及脚本");

				kadanDBJdbc = new JdbcHelper(DataSourceUtil.getDataSource("oracle_kadan"));
			} catch (Exception ex) {
				logger.error("初始化连接失败，超时未承保退款处理任务终止 。", ex);
				isHasError = true;
			}

			if (!isHasError) {
				/* 处理消息发送 */
				try {
					logger.info("处理超时未承保订单--开始");
					kadanDBJdbc.execute("{call USP_KD_OVERTIME_REFUND}");
					logger.info("处理超时未承保订单--结束");
				} catch (Exception syncEx) {
					logger.error("处理超时未承保订单出错。", syncEx);
				}
			}

			// 记录日志
			try {
				JobLog logData = new JobLog();
				logData.setJobName(jobEntity.getName());
				logData.setIncTypeId(jobEntity.getIncTypeId());
				logData.setTotalNumber(usageCount);
				logData.setSuccessNumber(isHasError ? 0 : usageCount);
				logData.setResult(isHasError ? 0 : 1);
				logData.setStartTime(syncStartTime);
				logData.setEndTime(new Date());
				logData.setCreateTime(new Date());
				logData.setUpdateTime(new Date());
				logData.setRemark(jobEntity.getName());
				joblogService.save(logData);
			} catch (Exception ex) {
				logger.error("保存超时未承保订单任务日志失败。", ex);
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
			logger.error("处理超时未承保订单失败。", allEx);
		}

	}

}
