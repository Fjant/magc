package com.xincheng.job.plugins.renewal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import jdbchelper.JdbcHelper;
import jdbchelper.MappingBatchFeeder;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
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
import com.xincheng.job.plugins.eservice.model.ESCustomerTemp;
import com.xincheng.job.plugins.renewal.model.CifBiz;
import com.xincheng.job.plugins.renewal.model.IdentityBiz;
import com.xincheng.job.plugins.renewal.model.RenewalBiz;
import com.xincheng.job.service.JobIncrementService;
import com.xincheng.job.service.JobLogService;
import com.xincheng.msg.service.EntMsgService;
import com.xincheng.msg.service.TemplateService;

@Service
public class CustomerRenewalMsgJob implements BaseJob {

	private static Logger logger = LoggerFactory.getLogger(CustomerRenewalMsgJob.class);

	@Autowired
	private JobLogService joblogService;

	@Autowired
	private JobIncrementService jobIncrementService;

	@Autowired
	private ErrorRecordService errorRecordService;

	@Autowired
	private TemplateService templateService;

	@Autowired
	private EntMsgService entMsgService;

	public void execute(JobEntity jobEntity) {

		try {
			logger.info("客户续期缴费消息发送任务开始");

			boolean isHasError = false;
			int bizCount = 0;
			List<String> customerNos = new ArrayList<String>();
			// 初始化
			ResourceBundle sqlBundle = null;
			String select30DayCustomerNo = null;
			String select2YearCustomerNo = null;
			String selectCustomerCifId = null;
			String insertBizToTemp = null;
			String updateCustomerCifId = null;
			String selectCustomerSecuityno = null;
			String updateCustomerSecuityno = null;
			String selectCustomerByCifId = null;
			String insertCustomerToTemp = null;
			JdbcHelper cl4DBJdbc = null;
			JdbcHelper msgcDBJdbc = null;
			JdbcHelper cifDBJdbc = null;
			JdbcHelper eserviceDBJdbc = null;

			try {
				logger.info("初始化续期缴费数据源及脚本");

				sqlBundle = ResourceBundle.getBundle("jdbc_sqls");
				select30DayCustomerNo = sqlBundle.getString("CUSTOMER_RENEWAL_BIZ_SELECT");
				select30DayCustomerNo = new String(select30DayCustomerNo.getBytes("ISO-8859-1"), "UTF-8");
				select2YearCustomerNo = sqlBundle.getString("CUSTOMER_RENEWAL_BIZ_2YEAR_SELECT");
				select2YearCustomerNo = new String(select2YearCustomerNo.getBytes("ISO-8859-1"), "UTF-8");
				selectCustomerCifId = sqlBundle.getString("CUSTOMER_RENEWAL_CIF_ID");
				insertBizToTemp = sqlBundle.getString("CUSTOMER_RENEWAL_BIZ_INSERT");
				updateCustomerCifId = sqlBundle.getString("CUSTOMER_RENEWAL_UPDATE_CIF_ID");
				selectCustomerSecuityno = sqlBundle.getString("CUSTOMER_RENEWAL_SECUITYNO_SELECT");
				selectCustomerByCifId = sqlBundle.getString("ES_CUSTOMER_SELECT_BY_CIF_ID");
				insertCustomerToTemp = sqlBundle.getString("ES_CUSTOMER_INSERT_TEMP");
				updateCustomerSecuityno = sqlBundle.getString("CUSTOMER_RENEWAL_UPDATE_SECUITYNO");

				cl4DBJdbc = new JdbcHelper(DataSourceUtil.getDataSource(("as400_cl4")));
				msgcDBJdbc = new JdbcHelper(DataSourceUtil.getDataSource(("oracle_msgc")));
				cifDBJdbc = new JdbcHelper(DataSourceUtil.getDataSource(("db2_cif")));
				eserviceDBJdbc = new JdbcHelper(DataSourceUtil.getDataSource(("db2_eservice")));
			} catch (Exception ex) {
				logger.error("初始化连接失败，同步统计保单电子回执信息任务终止 。", ex);
				isHasError = true;
			}

			// 默认取全部数据
			JobIncrement existsFlag = null;
			DateFormat format = new SimpleDateFormat("yyyyMMdd");
			if (!isHasError) {

				try {

					existsFlag = jobIncrementService.getByTypeId(jobEntity.getIncTypeId());
					// 判断当天是否同步，同步则跳出，否则执行（一天只执行一次）
					if (existsFlag != null && existsFlag.getFlag().equals(format.format(new Date()))) {
						logger.info(format.format(new Date()) + "：续期缴费数据已经同步！");
						return;
					}

					Object[] ptds = new Object[2];
					ptds[0] = Integer.valueOf(format.format(DateUtils.addDays(new Date(), -25)));
					ptds[1] = Integer.valueOf(format.format(DateUtils.addDays(new Date(), -58)));

					logger.info("查询数据开始");

					// 1、查询本地数据库获取数据
					// 获取25/58天期保单
					List<RenewalBiz> bizList = cl4DBJdbc.queryForList(select30DayCustomerNo, new RenewalBiz(), ptds);
					String ptdatesStr = "";
					Date startDate = DateUtils.addYears(new Date(), -2);
					ptdatesStr += format.format(DateUtils.addDays(new Date(), -25));
					int index = 1;
					while (true) {
						Date stepDate = DateUtils.addDays(new Date(), -58 - 25 * (index - 1));
						if (stepDate.getTime() > startDate.getTime()) {
							ptdatesStr += "," + format.format(stepDate);
							index++;
						} else {
							break;
						}
					}
					// 将PTDATES 替换SQL中的占位符
					select2YearCustomerNo = select2YearCustomerNo.replace("?", ptdatesStr);

					// 获取两年期保单
					List<RenewalBiz> twoYearBiz = cl4DBJdbc.queryForList(select2YearCustomerNo, new RenewalBiz());

					List<RenewalBiz> allBiz = new ArrayList<RenewalBiz>();
					if (CollectionUtils.isNotEmpty(bizList)) {
						allBiz.addAll(bizList);
					}

					if (CollectionUtils.isNotEmpty(twoYearBiz)) {
						allBiz.addAll(twoYearBiz);
					}

					logger.info("查询客户续期缴费信息结束，共获得记录数：" + allBiz.size());

					// 同步数据到主表
					bizCount = allBiz.size();
					if (bizCount > 0) {

						logger.info("开始插入客户续期信息");
						List<RenewalBiz> batchBizList = new ArrayList<RenewalBiz>();
						for (RenewalBiz item : allBiz) {
							batchBizList.add(item);
							// 获取客户号
							if (!customerNos.contains(item.getCustomerNo())) {
								customerNos.add(item.getCustomerNo());
							}
							if (batchBizList.size() >= 10000 && batchBizList.size() % 10000 == 0) {
								// 保存数据到数据库
								msgcDBJdbc.executeBatch(insertBizToTemp, new MappingBatchFeeder<RenewalBiz>(batchBizList.iterator(), RenewalBiz.getMapper()));
								batchBizList.clear();
							}
						}

						if (batchBizList.size() > 0) {
							// 保存数据到数据库
							msgcDBJdbc.executeBatch(insertBizToTemp, new MappingBatchFeeder<RenewalBiz>(batchBizList.iterator(), RenewalBiz.getMapper()));
						}
						logger.info("插入客户续期信息结束");

						logger.info("开始查询客户CIF ID及证件号信息");
						// 获取客户CIF ID及证件号码
						Integer customerCount = 0;
						String customerNoList = "";
						for (String customerNo : customerNos) {
							customerNoList += "'" + customerNo + "',";
							customerCount++;
							if (customerCount >= 100 && customerCount % 100 == 0) {
								getCifIdAndIdentityNo(selectCustomerCifId, updateCustomerCifId, selectCustomerSecuityno, updateCustomerSecuityno, cl4DBJdbc, msgcDBJdbc, cifDBJdbc, eserviceDBJdbc, customerNoList, selectCustomerByCifId, insertCustomerToTemp);
								customerNoList = "";
							}
						}

						if (StringUtils.isNotBlank(customerNoList)) {
							getCifIdAndIdentityNo(selectCustomerCifId, updateCustomerCifId, selectCustomerSecuityno, updateCustomerSecuityno, cl4DBJdbc, msgcDBJdbc, cifDBJdbc, eserviceDBJdbc, customerNoList, selectCustomerByCifId, insertCustomerToTemp);
						}

						logger.info("查询客户CIF ID及证件号信息结束");
					}

					/* 同步客户信息、信息到主表 */
					try {
						msgcDBJdbc.execute("{call USP_SYNC_ES_CUSTOMER}");
					} catch (Exception syncEx) {
						logger.error("同步所有客户信息到主表时出错。", syncEx);
						throw syncEx;
					}

					// 处理消息发送
					try {
						msgcDBJdbc.execute("{call USP_CUST_RENEWAL_BIZ_RESULT}");
					} catch (Exception syncEx) {
						logger.error("处理续期消息推送出错。", syncEx);
					}
				} catch (Exception e) {
					logger.error("同步续期消息信息出错。", e);
					isHasError = true;
					e.printStackTrace();
				}
			}

			// 记录增加标记
			try {
				if (!isHasError) {
					if (existsFlag == null) {
						JobIncrement newFlag = new JobIncrement();
						newFlag.setJobName(jobEntity.getName());
						newFlag.setTypeId(jobEntity.getIncTypeId());
						newFlag.setFlag(format.format(new Date()));
						newFlag.setRemark(jobEntity.getName());
						newFlag.setCreateTime(new Date());
						jobIncrementService.save(newFlag);
					} else {
						existsFlag.setFlag(format.format(new Date()));
						jobIncrementService.update(existsFlag);
					}
				}
			} catch (Exception ex) {
				logger.error("保存续期消息标记失败", ex);
			}

			// 记录日志
			try {
				JobLog logData = new JobLog();
				logData.setJobName(jobEntity.getName());
				logData.setIncTypeId(jobEntity.getIncTypeId());
				logData.setTotalNumber(bizCount);
				logData.setSuccessNumber(isHasError ? 0 : bizCount);
				logData.setResult(isHasError ? 0 : 1);
				logData.setStartTime(new Date());
				logData.setEndTime(new Date());
				logData.setCreateTime(new Date());
				logData.setUpdateTime(new Date());
				logData.setRemark(jobEntity.getName());
				joblogService.save(logData);
			} catch (Exception ex) {
				logger.error("保存续期消息任务日志失败。", ex);
				ex.printStackTrace();
			}

			try {
				System.gc();
			} catch (Exception ex) {
				logger.error("同步续期消息内存回收失败。", ex);
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
			logger.error("同步续期任务失败。", allEx);
		}
	}

	/**
	 * 获取用户CIF ID及证件号码
	 * 
	 * @param selectCustomerCifId
	 * @param updateCustomerCifId
	 * @param selectCustomerSecuityno
	 * @param updateCustomerSecuityno
	 * @param extDBJdbc
	 * @param msgcDBJdbc
	 * @param cifDBJdbc
	 * @param cusNos
	 */
	private void getCifIdAndIdentityNo(String selectCustomerCifId, String updateCustomerCifId, String selectCustomerSecuityno, String updateCustomerSecuityno, JdbcHelper cl4DBJdbc, JdbcHelper msgcDBJdbc, JdbcHelper cifDBJdbc, JdbcHelper eserviceDBJdbc, String cusNos, String selectCustomerByCifId, String insertCustomerToTemp) {

		if (StringUtils.isBlank(cusNos) || cusNos.length() <= 1)
			return;

		try {
			cusNos = cusNos.substring(0, cusNos.length() - 1);

			// 根据保单客户号查询cifId
			logger.info("查询cifId开始");
			List<CifBiz> cifBizList = cifDBJdbc.queryForList(selectCustomerCifId.replace("?", cusNos), new CifBiz());
			msgcDBJdbc.executeBatch(updateCustomerCifId, new MappingBatchFeeder<CifBiz>(cifBizList.iterator(), CifBiz.getMapper()));
			logger.info("查询并更新cifId结束");

			// 根据保单客户号查询证件号secuityno
			logger.info("查询security no开始");
			List<IdentityBiz> identityNoBizList = cl4DBJdbc.queryForList(selectCustomerSecuityno.replace("?", cusNos), new IdentityBiz());
			msgcDBJdbc.executeBatch(updateCustomerSecuityno, new MappingBatchFeeder<IdentityBiz>(identityNoBizList.iterator(), IdentityBiz.getMapper()));
			logger.info("查询并更新security no结束");

			// 根据CIF ID查询客户基本信息
			logger.info("查询security no开始");
			if (!CollectionUtils.isEmpty(cifBizList)) {
				List<String> cifIds = new ArrayList<String>();
				for (CifBiz cifBiz : cifBizList) {
					cifIds.add("\'" + cifBiz.getClientNum() + "\'");
				}

				List<ESCustomerTemp> customerTempList = eserviceDBJdbc.queryForList(selectCustomerByCifId.replace("?", StringUtils.join(cifIds, ",")), new ESCustomerTemp());
				msgcDBJdbc.executeBatch(insertCustomerToTemp, new MappingBatchFeeder<ESCustomerTemp>(customerTempList.iterator(), ESCustomerTemp.getMapper()));
			}
			logger.info("查询并更新security no结束");
		} catch (Exception ex) {
			logger.error("查询并更新客户CIF ID 及证件号码时失败", ex);
		}
	}
}
