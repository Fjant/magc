package com.xincheng.job.plugins.cif;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import jdbchelper.JdbcHelper;
import jdbchelper.MappingBatchFeeder;
import jdbchelper.QueryResult;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xincheng.errorObserver.model.ErrorRecord;
import com.xincheng.errorObserver.service.ErrorRecordService;
import com.xincheng.jdbc.DataSourceUtil;
import com.xincheng.job.model.JobEntity;
import com.xincheng.job.model.JobIncrement;
import com.xincheng.job.model.JobLog;
import com.xincheng.job.plugins.BaseJob;
import com.xincheng.job.plugins.cif.model.CifClientRelTemp;
import com.xincheng.job.service.JobIncrementService;
import com.xincheng.job.service.JobLogService;
import com.xincheng.utils.DateUtils;

/**
 * 客户信息同步
 * 
 * */
@Service
public class CifSyncJob implements BaseJob {

	private static Logger logger = LoggerFactory.getLogger(CifSyncJob.class);

	@Autowired
	private ErrorRecordService errorRecordService;

	@Autowired
	private JobLogService joblogService;

	@Autowired
	private JobIncrementService jobIncrementService;

	public static void main(String[] a) {
		
	}

	// AMS对接客户的库表
	public void execute(JobEntity jobEntity) {
		Date syncStartTime = new Date();
		int customerCount = 0;
		boolean isHasError = false;
		final ResourceBundle dbBundle = ResourceBundle.getBundle("jdbc");
		final ResourceBundle sqlBundle = ResourceBundle.getBundle("jdbc_sqls");
		final String incrementSql = sqlBundle.getString("CIF_CLIENTREL_SELECT");
		final String clearCustomerTemp = sqlBundle.getString("CIF_CLIENTREL_CLEAR_TEMP");
		final String insertCustomerToTemp = sqlBundle.getString("CIF_CLIENTREL_INSERT_TEMP");

		int batchSearchNumber = 10000;
		// 查询开始索引
		int batchStartIndex = 1;
		// 查询结束索引
		int batchEndIndex = batchSearchNumber;
		// 每次查询获取到的数据量
		int batchRetrieveCount = 0;

		JdbcHelper luckydrawDBJdbc = null;
		JdbcHelper cifDBJdbc = null;

		try {
			luckydrawDBJdbc = new JdbcHelper(DataSourceUtil.getDataSource("oracle_luckydraw"));
			cifDBJdbc = new JdbcHelper(DataSourceUtil.getDataSource("db2_cif"));
			luckydrawDBJdbc.execute(clearCustomerTemp);
		} catch (Exception ex) {
			logger.error("清空客户基本信息临时表失败，同步CIF 客户信息信息任务终止 。", ex);

			isHasError = true;
		}

		// 默认取全部数据
		Timestamp inscrementTime = null;
		Date maxUpdateTime = DateUtils.addYears(new Date(), -50);
		Date startSearchTime = new Date();
		JobIncrement existsFlag = null;
		if (!isHasError) {
			try {

				// 查询上次同步信息，获取上次增量同步日期
				existsFlag = jobIncrementService.getByTypeId(jobEntity.getIncTypeId());
				if (existsFlag != null && StringUtils.isNotEmpty(existsFlag.getFlag())) {
					inscrementTime = new Timestamp(Long.parseLong(existsFlag.getFlag()));
				} else {
					inscrementTime = new Timestamp(DateUtils.addYears(new Date(), -50).getTime());
				}

				// 查询所有数据
				while (true) {
					List<CifClientRelTemp> allCustomers = new ArrayList<CifClientRelTemp>();
					QueryResult result = cifDBJdbc.query(incrementSql, inscrementTime, batchStartIndex, batchEndIndex);
					result.setAutoClose(true);

					// 读取数据
					while (result.next()) {
						String clientNum = result.getString("CLIENTNUM");
						String relCltNum = result.getString("RELCLTNUM");
						String relSys = result.getString("RELSYS");
						Date crtDate = result.getDate("CRTDATE");

						// 获取记录更新最大时间
						if (crtDate != null && crtDate.getTime() > maxUpdateTime.getTime() && crtDate.getTime() <= new Date().getTime()) {
							maxUpdateTime = crtDate;
						}

						CifClientRelTemp relTemp = new CifClientRelTemp();
						relTemp.setClientNum(clientNum);
						relTemp.setRelCltNum(relCltNum);
						relTemp.setRelSys(relSys);
						relTemp.setCrtDate(crtDate);

						// 添加CIF 客户信息编号到列表中
						allCustomers.add(relTemp);
					}

					// 本次获取到的记录数
					batchRetrieveCount = allCustomers.size();

					// 3、保存数据到临时表
					if (batchRetrieveCount > 0) {

						batchSaveCustomer(insertCustomerToTemp, luckydrawDBJdbc, allCustomers);

						// 增加本次同步成功数量
						customerCount += allCustomers.size();

						// 6、如果取到的数据小于批量获取的数据，则说明已经取到最后一页，则跳出循环
						if (batchRetrieveCount < batchSearchNumber) {
							break;
						} else {
							batchStartIndex += batchSearchNumber;
							batchEndIndex += batchSearchNumber;
						}
					} else {
						// 7、如果取到的数据小于批量获取的数据，则说明已经取到最后一页，则跳出循环
						break;
					}

					allCustomers = null;

					try {
						System.gc();
					} catch (Exception ex) {
						logger.error("保存CIF 客户信息信息同步内存回收失败。", ex);
					}
				}

				// 如果查询到的数据为0，则更新任务开始时间为新的增量查询时间
				if (customerCount == 0) {
					maxUpdateTime = startSearchTime;
				}

				/* 4、同步客户信息、保单信息到主表 */
				try {
					luckydrawDBJdbc.execute("{call USP_SYNC_CIF_CUSTOMER()}");
				} catch (Exception syncEx) {
					logger.error("同步CIF 客户信息到主表时出错。", syncEx);
					throw syncEx;
				}
			} catch (Exception e) {
				logger.error("同步CIF 客户信息信息失败", e);
				isHasError = true;
			}
		}

		// 记录增量同步标识日志
		try {
			if (!isHasError) {
				if (existsFlag == null) {
					JobIncrement newFlag = new JobIncrement();
					newFlag.setJobName(jobEntity.getName());
					newFlag.setTypeId(jobEntity.getIncTypeId());
					newFlag.setFlag(String.valueOf(maxUpdateTime.getTime()));
					newFlag.setRemark(jobEntity.getName());
					newFlag.setCreateTime(new Date());
					jobIncrementService.save(newFlag);
				} else {
					existsFlag.setFlag(String.valueOf(maxUpdateTime.getTime()));
					existsFlag.setTypeId(jobEntity.getIncTypeId());
					jobIncrementService.update(existsFlag);
				}
			}
		} catch (Exception ex) {
			logger.error("保存同步统计保单电子回执信息任务增加标记失败", ex);
		}

		// 记录日志
		try {
			JobLog logData = new JobLog();
			logData.setJobName(jobEntity.getName());
			logData.setIncTypeId(jobEntity.getIncTypeId());
			logData.setTotalNumber(customerCount);
			logData.setSuccessNumber(isHasError ? 0 : customerCount);
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

	private void batchSaveCustomer(final String insertCustomerToTemp, JdbcHelper oracleJdbc, List<CifClientRelTemp> customerList) {
		List<CifClientRelTemp> batchCustomerList = new ArrayList<CifClientRelTemp>();
		for (CifClientRelTemp item : customerList) {
			batchCustomerList.add(item);
			if (batchCustomerList.size() >= 10000 && batchCustomerList.size() % 10000 == 0) {
				oracleJdbc.executeBatch(insertCustomerToTemp, new MappingBatchFeeder<CifClientRelTemp>(batchCustomerList.iterator(), CifClientRelTemp.getMapper()));
				batchCustomerList.clear();
			}
		}

		if (batchCustomerList.size() > 0) {
			oracleJdbc.executeBatch(insertCustomerToTemp, new MappingBatchFeeder<CifClientRelTemp>(batchCustomerList.iterator(), CifClientRelTemp.getMapper()));
		}
	}

}
