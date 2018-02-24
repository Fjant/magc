package com.xincheng.job.plugins.customer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xincheng.config.SystemConfig;
import com.xincheng.encrypt.CifApiDES;
import com.xincheng.errorObserver.model.ErrorRecord;
import com.xincheng.errorObserver.service.ErrorRecordService;
import com.xincheng.jdbc.DataSourceUtil;
import com.xincheng.job.model.JobEntity;
import com.xincheng.job.model.JobLog;
import com.xincheng.job.plugins.BaseJob;
import com.xincheng.job.plugins.customer.model.EICustomer;
import com.xincheng.job.plugins.eservice.model.BizUsageTemp;
import com.xincheng.job.service.JobLogService;
import com.xincheng.utils.ParseXML;

import jdbchelper.JdbcHelper;
import jdbchelper.MappingBatchFeeder;
import jdbchelper.QueryResult;

/**
 * 纯电子保单抽数,用于0点同步前一天数据
 * 
 * */
@Service
public class LDCustomerSyncJob4LastDay implements BaseJob {

	private static Logger logger = LoggerFactory.getLogger(LDCustomerSyncJob4LastDay.class);
	private static String CIFApiUrl = SystemConfig.getPara("CIFApiUrl");
	private static String CIFApiKey = SystemConfig.getPara("CIFApiKey");

	private static Long ES_ACTIVITY_ID = Long.parseLong(SystemConfig.getPara("ActivityID"));
	@Autowired
	private ErrorRecordService errorRecordService;

	@Autowired
	private JobLogService joblogService;


	public static void main(String[] a) {
		LDCustomerSyncJob4LastDay impl = new LDCustomerSyncJob4LastDay();
		impl.execute(null);

	}

	// 纯电子保单抽奖的次数抽数统计
	@Override
	public void execute(JobEntity jobEntity) {
		Date syncStartTime = new Date();
		int customersCount = 0;
		boolean isHasError = false;
		final ResourceBundle sqlBundle = ResourceBundle.getBundle("jdbc_sqls");
		final String geteEICustomer = sqlBundle.getString("LD_CUSTOMER_EI_GET");
		final String clearTemp = sqlBundle.getString("LD_CUSTOMER_CLEAR_TEMP_TABLE");
		final String deleteTemp = sqlBundle.getString("LD_CUSTOMER_DELETE_TEMP_TABLE");
		final String insertToTemp = sqlBundle.getString("BIZ_USAGE_INSERT_TEMP");
		final String countTemp = sqlBundle.getString("LD_CUSTOMER_COUNT_TEMP_TABLE");
		final String insertActivity = sqlBundle.getString("LD_CUSTOMER_INSERT_ACTIVITY");
		final String updateActivity = sqlBundle.getString("LD_CUSTOMER_UPDATE_ACTIVITY");
		final String selectActivity = sqlBundle.getString("LD_CUSTOMER_SELECT_ACTIVITY");
		final String insertBizUage = sqlBundle.getString("LD_CUSTOMER_SAVE_BIZ_USAGE");
		final String insertBizUageError = sqlBundle.getString("LD_CUSTOMER_SAVE_BIZ_USAGE_ERROR");
		final String insertMsgInfo = sqlBundle.getString("LD_CUSTOMER_SAVE_MSG_INFO");
		final String updateMsgInfo = sqlBundle.getString("LD_CUSTOMER_UPDATE_MSG_INFO");
		final String selectBasicInfoByCifId = sqlBundle.getString("LD_CUSTOMER_SELECT_BASIC_INFO");
		final String selectCustomerByCifId = sqlBundle.getString("LD_CUSTOMER_SELECT_CUSTOMER");
		final String insertCustomer = sqlBundle.getString("LD_CUSTOMER_SAVE_BASIC_INFO");
		final String updateCustomer = sqlBundle.getString("LD_CUSTOMER_UPDATE_CUSTOMER");
		JdbcHelper luckydrawDBJdbc = null;
		JdbcHelper cl4DBJdbc = null;
		JdbcHelper eseDBJdbc = null;
		String batchNum = UUID.randomUUID().toString().replace("-", "");
		try {
			luckydrawDBJdbc = new JdbcHelper(DataSourceUtil.getDataSource("oracle_luckydraw"));
			cl4DBJdbc = new JdbcHelper(DataSourceUtil.getDataSource("as400_cl4"));
			eseDBJdbc = new JdbcHelper(DataSourceUtil.getDataSource("db2_eservice"));
			luckydrawDBJdbc.execute(clearTemp);
		} catch (Exception ex) {
			logger.error("清空业务信息临时表失败，同步终止。", ex);
			isHasError = true;
			ex.printStackTrace();
		}

		try {

			// 1、查询数据
			logger.info("开始获取纯电子保单数据");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			Calendar calendar = Calendar.getInstance();
			calendar.add(calendar.DATE, -1);
			String yesterday = dateFormat.format(calendar.getTime());
			List<BizUsageTemp> BizUsageTempList = new ArrayList<BizUsageTemp>();
			QueryResult result = cl4DBJdbc.query(geteEICustomer, yesterday);
			result.setAutoClose(true);
			while (result.next()) {

				String bizNum = result.getString("CHDRNUM");
				Date bizDate = dateFormat.parse(String.valueOf(result.getInt("HOISSDTE")));
				String agntNum = result.getString("AGNTNUM");
				String customerNo = result.getString("COWNNUM");

				BizUsageTemp usageTemp = new BizUsageTemp();
				usageTemp.setBillNum(bizNum);
				usageTemp.setBizNum(bizNum);
				usageTemp.setBizTime(bizDate);
				usageTemp.setAgntNum(agntNum);
				usageTemp.setBatchNum(batchNum);
				usageTemp.setCustomerNo(customerNo);
				usageTemp.setTypeId(1);
				BizUsageTempList.add(usageTemp);
			}

			customersCount = BizUsageTempList.size();

			// 2、保存数据到临时表
			logger.info("开始保存纯电子保单数据");
			if (customersCount > 0) {
				List<BizUsageTemp> batchStaffList = new ArrayList<BizUsageTemp>();
				for (BizUsageTemp item : BizUsageTempList) {
					batchStaffList.add(item);
					if (batchStaffList.size() >= 10000 && batchStaffList.size() % 10000 == 0) {
						luckydrawDBJdbc.executeBatch(insertToTemp,
								new MappingBatchFeeder<BizUsageTemp>(batchStaffList.iterator(),
										BizUsageTemp.getMapper()));
						batchStaffList.clear();
					}
				}

				if (batchStaffList.size() > 0) {
					luckydrawDBJdbc.executeBatch(insertToTemp,
							new MappingBatchFeeder<BizUsageTemp>(batchStaffList.iterator(), BizUsageTemp.getMapper()));
				}

				// D、执行数据更新
				try {
					luckydrawDBJdbc.beginTransaction();
					luckydrawDBJdbc.commitTransaction();
				} catch (Exception syncTrans) {
					luckydrawDBJdbc.rollbackTransaction();
					logger.error("同步业务数据到临时表是出错。", syncTrans);
					throw syncTrans;
				}
			}
		} catch (Exception ex) {
			logger.error("同步业务数据到临时表是出错。", ex);
			isHasError = true;
			ex.printStackTrace();
		}


		try {
			logger.info("开始过滤纯电子保单数据");
			// 3、过滤已有的保单数据
			luckydrawDBJdbc.execute(deleteTemp);
			// 4、查询CIF号，增加抽奖次数
			// 4.1统计数据
			logger.info("开始纯电子保单数据处理");
			QueryResult result = luckydrawDBJdbc.query(countTemp);
			result.setAutoClose(true);
			while (result.next()) {
				String count = result.getString("COUNT");
				String cownNum = result.getString("COWNNUM");
				EICustomer eiCustomer = new EICustomer();
				eiCustomer.setCownnum(cownNum);
				eiCustomer.setChdrnum(count);
				// 4.2获取CIFID
				try {
					if (logger.isInfoEnabled()) {
						logger.info("根据客户号查询CIF号：" + cownNum);
					}

					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
					String currentDate = dateFormat.format(new Date());
					String currentTime = timeFormat.format(new Date());
					String clientNum = cownNum;
					String entcryptString = CifApiDES.getEncString("03|" + currentDate + "|" + currentTime, CIFApiKey);
					String queryData = "<TransData>" + "<Header>" + " <SendClient>LAS</SendClient>"
							+ "  <OperateType>03</OperateType>" + "  <Date>" + currentDate + "</Date>" + "  <Time>"
							+ currentTime + "</Time>" + "  <Operator>admin</Operator>" + "  <EncryptString>"
							+ entcryptString + "</EncryptString>" + " </Header>" + " <InputData>" + "  <ClientNum>"
							+ clientNum + "</ClientNum>" + "   <Source></Source>" + "</InputData>" + "</TransData>";
					Object[] queryCIFesult = null;
					ClassLoader cl = Thread.currentThread().getContextClassLoader();
					JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
					Client client = dcf.createClient(CIFApiUrl);
					Thread.currentThread().setContextClassLoader(cl);

					// invoke第一个参数是方法名称，第二个是参数
					queryCIFesult = client.invoke("doRequestService", queryData);
					if (queryCIFesult != null && queryCIFesult.length > 0) {
						ParseXML p = new ParseXML();
						p.initXmlByText(queryCIFesult[0].toString());
						if (logger.isInfoEnabled()) {
							logger.info(queryCIFesult[0].toString());
						}
						Element element = p.getElement();
						if (element.element("OutputData") != null) {
							Iterator policyIterator = element.element("OutputData").elements("ClientInfo").iterator();
							Element elem = null;
							String cifClientId = "";
							while (policyIterator.hasNext()) {
								elem = (Element) policyIterator.next();
								cifClientId = elem.elementTextTrim("ClientNum");
								if (StringUtils.isNotEmpty(cifClientId)) {
									// params.setCifId(cifClientId);
									eiCustomer.setCifid(cifClientId);
									break;
								}
							}
						}
					} else {
						logger.error("查询CIF号失败");
						eiCustomer.setCifid("");
					}

				} catch (Exception poex) {
					logger.error("根据客户号查询CIF号出错", poex);
					isHasError = true;
					eiCustomer.setCifid("");
				}
				luckydrawDBJdbc.beginTransaction();
				// 4.3插入到activity表增加抽奖次数
				if (StringUtils.isNotBlank(eiCustomer.getCifid())) {
					// 查询是否已有数据
					QueryResult activity = luckydrawDBJdbc.query(selectActivity, eiCustomer.getCifid());
					activity.setAutoClose(true);
					String total = count;
					String unuse = count;
					String cif = eiCustomer.getCifid();
					String id = ES_ACTIVITY_ID + cif.substring(5, cif.length());
					boolean flag = activity.next();
					if (flag) {
						total = activity.getString("TOTAL_TIMES");
						unuse = activity.getString("UNUSED_TIMES");
						cif = activity.getString("CIF_ID");
						int totalTimes = Integer.parseInt(total) + Integer.parseInt(count);
						int unuseTimes = Integer.parseInt(unuse) + Integer.parseInt(count);
						// update
						luckydrawDBJdbc.execute(updateActivity, totalTimes, unuseTimes, cif);
					} else {
						// insert
						luckydrawDBJdbc.execute(insertActivity, id, cif, total, unuse);
					}

					// 5、获取opendId更新客户表，微信消息发送
					// 5.1、根据CIFID获取客户信息包含OPENID
					QueryResult basicInfo = eseDBJdbc.query(selectBasicInfoByCifId, cif);
					QueryResult customer = luckydrawDBJdbc.query(selectCustomerByCifId, cif);
					basicInfo.setAutoClose(true);
					customer.setAutoClose(true);
					String custNo = id;
					String cifId = cif;
					String realName = "";
					int sex = 0;
					String openId = "";
					boolean basicInfoFlag = basicInfo.next();
					boolean customerFlag = customer.next();
					if (basicInfoFlag) {
						custNo = basicInfo.getString("CUSTOMERID");
						cifId = basicInfo.getString("CIF_ID");
						realName = basicInfo.getString("REALNAME");
						sex = Integer.parseInt(basicInfo.getString("SEX"));
						openId = basicInfo.getString("OPENID1");
						if (!customerFlag) {
							// insert
							luckydrawDBJdbc.execute(insertCustomer, custNo, cownNum, cifId, realName, sex, openId);
						} else {
							// update
							luckydrawDBJdbc.execute(updateCustomer, realName, sex, openId, cifId);
						}
					} else {
						if (!customerFlag) {
							// insert
							luckydrawDBJdbc.execute(insertCustomer, custNo, cownNum, cifId, realName, sex, openId);
						}
					}
					// 4.4、保存业务数据
					luckydrawDBJdbc.execute(insertBizUage, cownNum);
					// 4.5 保存消息发送状态
					luckydrawDBJdbc.execute(insertMsgInfo, cownNum);
					luckydrawDBJdbc.execute(updateMsgInfo, cif, realName, openId, sex, cownNum);
				} else {
					// 获取CIF为空时，保存到错误数据表，等下当天再次捞数
					luckydrawDBJdbc.execute(insertBizUageError, cownNum);
				}
				luckydrawDBJdbc.commitTransaction();
			}

		} catch (Exception ex) {
			luckydrawDBJdbc.rollbackTransaction();
			logger.error("保存数据异常", ex);
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
