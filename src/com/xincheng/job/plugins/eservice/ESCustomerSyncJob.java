package com.xincheng.job.plugins.eservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import jdbchelper.JdbcHelper;
import jdbchelper.MappingBatchFeeder;
import jdbchelper.QueryResult;
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
import com.xincheng.job.plugins.eservice.model.ESCustomerTemp;
import com.xincheng.job.service.JobLogService;

/**
 * 客户信息同步
 * 
 * */
@Service
public class ESCustomerSyncJob implements BaseJob {

	private static Logger logger = LoggerFactory.getLogger(ESCustomerSyncJob.class);

	@Autowired
	private ErrorRecordService errorRecordService;

	@Autowired
	private JobLogService joblogService;

	public static void main(String[] a) {

	}

	// AMS对接客户的库表
	public void execute(JobEntity jobEntity) {
		Date syncStartTime = new Date();
		int staffCount = 0;
		boolean isHasError = false;
		final ResourceBundle dbBundle = ResourceBundle.getBundle("jdbc");
		final ResourceBundle sqlBundle = ResourceBundle.getBundle("jdbc_sqls");
		final String selectAllCustomer = sqlBundle.getString("ES_CUSTOMER_SELECT");
		final String clearCustomerTemp = sqlBundle.getString("ES_CUSTOMER_CLEAR_TEMP");
		final String insertCustomerToTemp = sqlBundle.getString("ES_CUSTOMER_INSERT_TEMP");

		int batchSearchNumber = 10000;
		// 查询开始索引
		int batchStartIndex = 1;
		// 查询结束索引
		int batchEndIndex = batchSearchNumber;
		// 每次查询获取到的数据量
		int batchRetrieveCount = 0;

		JdbcHelper msgcDBJdbc = null;
		JdbcHelper eserviceDBJdbc = null;

		try {
			msgcDBJdbc = new JdbcHelper(DataSourceUtil.getDataSource(("oracle_msgc")));
			eserviceDBJdbc = new JdbcHelper(DataSourceUtil.getDataSource(("db2_eservice")));
			msgcDBJdbc.execute(clearCustomerTemp);
		} catch (Exception ex) {
			logger.error("清空客户基本信息临时表失败，同步客户信息任务终止 。", ex);
			isHasError = true;
		}

		// 默认取全部数据
		if (!isHasError) {
			try {
				// 查询所有数据
				while (true) {
					List<ESCustomerTemp> allCustomers = new ArrayList<ESCustomerTemp>();
					QueryResult result = eserviceDBJdbc.query(selectAllCustomer, batchStartIndex, batchEndIndex);
					result.setAutoClose(true);

					// 读取数据
					while (result.next()) {
						String cifId = result.getString("CIF_ID");
						String userName = result.getString("REALNAME");
						String userType = result.getString("USERTYPE");
						String sex = result.getString("SEX");
						Date birthday = result.getDate("BIRTHDAY");
						String openId = result.getString("OPENID1");
						String mobile = result.getString("MOBILE");
						String telephone = result.getString("TELEPHONE");
						String email = result.getString("EMAIL");

						ESCustomerTemp customerTemp = new ESCustomerTemp();
						customerTemp.setCifId(cifId);
						customerTemp.setUserName(userName);
						customerTemp.setUserType(userType);
						customerTemp.setSex(sex);
						customerTemp.setBirthday(birthday);
						customerTemp.setOpenId(openId);
						customerTemp.setMobile(mobile);
						customerTemp.setTelephone(telephone);
						customerTemp.setEmail(email);

						// 添加客户编号到列表中
						allCustomers.add(customerTemp);
					}

					// 本次获取到的记录数
					batchRetrieveCount = allCustomers.size();

					// 保存数据到临时表
					if (batchRetrieveCount > 0) {

						batchSaveCustomer(insertCustomerToTemp, msgcDBJdbc, allCustomers);

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
						logger.error("保存客户信息同步内存回收失败。", ex);
					}
				}

				/* 同步客户信息、信息到主表 */
				try {
					msgcDBJdbc.execute("{call USP_SYNC_ES_CUSTOMER}");
				} catch (Exception syncEx) {
					logger.error("同步所有客户信息到主表时出错。", syncEx);
					throw syncEx;
				}
			} catch (Exception e) {
				logger.error("同步客户信息失败", e);
				isHasError = true;
			}
		}

		// 5、记录日志
		try {
			JobLog logData = new JobLog();
			logData.setJobName(jobEntity.getName());
			logData.setIncTypeId(jobEntity.getIncTypeId());
			logData.setTotalNumber(staffCount);
			logData.setSuccessNumber(isHasError ? 0 : staffCount);
			logData.setResult(isHasError ? 0 : 1);
			logData.setStartTime(syncStartTime);
			logData.setEndTime(new Date());
			logData.setCreateTime(new Date());
			logData.setUpdateTime(new Date());
			logData.setRemark(jobEntity.getName());
			joblogService.save(logData);
		} catch (Exception ex) {
			logger.error("保存同步ESERVICE客户日志出错。", ex);
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

	private void batchSaveCustomer(final String insertCustomerToTemp, JdbcHelper oracleJdbc, List<ESCustomerTemp> customerList) {
		List<ESCustomerTemp> batchCustomerList = new ArrayList<ESCustomerTemp>();
		for (ESCustomerTemp item : customerList) {
			batchCustomerList.add(item);
			if (batchCustomerList.size() >= 10000 && batchCustomerList.size() % 10000 == 0) {
				oracleJdbc.executeBatch(insertCustomerToTemp, new MappingBatchFeeder<ESCustomerTemp>(batchCustomerList.iterator(), ESCustomerTemp.getMapper()));
				batchCustomerList.clear();
			}
		}

		if (batchCustomerList.size() > 0) {
			oracleJdbc.executeBatch(insertCustomerToTemp, new MappingBatchFeeder<ESCustomerTemp>(batchCustomerList.iterator(), ESCustomerTemp.getMapper()));
		}
	}

}
