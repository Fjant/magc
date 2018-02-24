package com.xincheng.job.plugins.eservice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import jdbchelper.JdbcHelper;
import jdbchelper.MappingBatchFeeder;
import jdbchelper.NoResultException;
import jdbchelper.QueryResult;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xincheng.config.SystemConfig;
import com.xincheng.errorObserver.model.ErrorRecord;
import com.xincheng.errorObserver.service.ErrorRecordService;
import com.xincheng.jdbc.DataSourceUtil;
import com.xincheng.job.model.JobEntity;
import com.xincheng.job.model.JobIncrement;
import com.xincheng.job.model.JobLog;
import com.xincheng.job.plugins.BaseJob;
import com.xincheng.job.plugins.eservice.model.BizUsageTemp;
import com.xincheng.job.service.JobIncrementService;
import com.xincheng.job.service.JobLogService;
import com.xincheng.msg.model.EntMsg;
import com.xincheng.msg.model.Template;
import com.xincheng.msg.service.EntMsgService;
import com.xincheng.msg.service.TemplateService;

@Service
public class BDDZHZBizUsageSyncJob implements BaseJob {

	private static Logger logger = LoggerFactory.getLogger(BDDZHZBizUsageSyncJob.class);

	private static long MSG_TEMPLATE_ID = Long.parseLong(SystemConfig.getPara("ESActivityENTMsgTemplateId"));

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
			if (logger.isDebugEnabled()) {
				logger.debug("保单电子回执数据同步开始");
			}

			// 开始时间
			String batchNum = UUID.randomUUID().toString().replace("-", "");
			Date syncStartTime = new Date();
			boolean isHasError = false;
			int usageCount = 0;

			// 初始化
			ResourceBundle sqlBundle = null;
			ResourceBundle dbBundle = null;
			String incrementSql = null;
			String selectCustSql = null;
			String insertUsageToTemp = null;
			JdbcHelper luckydrawDBJdbc = null;
			JdbcHelper sqsnewDBJdbc = null;
			JdbcHelper cl4DBJdbc = null;
			JdbcHelper msgcDBJdbc = null;

			try {
				if (logger.isDebugEnabled()) {
					logger.debug("初始化保单电子回执数据源及脚本");
				}

				sqlBundle = ResourceBundle.getBundle("jdbc_sqls");
				dbBundle = ResourceBundle.getBundle("jdbc");
				incrementSql = sqlBundle.getString("STATIC_BIZ_USAGE_BDDZHZ_SELECT_LIST");
				selectCustSql = sqlBundle.getString("STATIC_BIZ_USAGE_BDDZHZ_SELECT_CUST");
				insertUsageToTemp = sqlBundle.getString("BIZ_USAGE_INSERT_TEMP");

				luckydrawDBJdbc = new JdbcHelper(DataSourceUtil.getDataSource("oracle_luckydraw"));
				sqsnewDBJdbc = new JdbcHelper(DataSourceUtil.getDataSource("as400_cl4"));
				cl4DBJdbc = new JdbcHelper(DataSourceUtil.getDataSource("as400_cl4"));
				msgcDBJdbc = new JdbcHelper(DataSourceUtil.getDataSource("oracle_msgc"));
			} catch (Exception ex) {
				logger.error("初始化连接失败，同步统计保单电子回执信息任务终止 。", ex);
				isHasError = true;
			}

			// 默认取全部数据
			Integer inscrementStartDate = null;
			Integer inscrementEndDate = null;
			JobIncrement existsFlag = null;
			DateFormat format = new SimpleDateFormat("yyyyMMdd");
			if (!isHasError) {
				try {
					if (logger.isDebugEnabled()) {
						logger.debug("查询上次同步时间");
					}

					// 查询上次同步信息，获取上次增量同步日期
					existsFlag = jobIncrementService.getByTypeId(jobEntity.getIncTypeId());
					if (existsFlag != null && StringUtils.isNotEmpty(existsFlag.getFlag())) {
						inscrementStartDate = Integer.valueOf(existsFlag.getFlag().trim());
					} else {
						// 如果没有数据，则从指定日期开始
						inscrementStartDate = Integer.valueOf(SystemConfig.getPara("ESLuckydrawDataStaticStartDate").trim());
					}

					// 结束时间为当前时间的后一天
					inscrementEndDate = Integer.valueOf(format.format(DateUtils.addDays(new Date(), -1)));

					List<BizUsageTemp> usageTempList = new ArrayList<BizUsageTemp>();
					List<BizUsageTemp> finalResultList = new ArrayList<BizUsageTemp>();

					if (logger.isDebugEnabled()) {
						logger.debug("开始查询保单数据");
					}
					// 查询数据
					QueryResult result = sqsnewDBJdbc.query(incrementSql, inscrementStartDate, inscrementEndDate);
					result.setAutoClose(true);

					if (logger.isDebugEnabled()) {
						logger.debug("查询数据结束，开始读取保单数据");
					}
					// 读取数据
					while (result.next()) {
						String bizNum = result.getString("INSURNUM");
						Date bizDate = format.parse(String.valueOf(result.getInt("DATEUP01")));
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

						// 添加营销员编号到列表中
						usageTempList.add(usageTemp);
					}

					if (logger.isDebugEnabled()) {
						logger.debug("读取数据结束，得到结果数" + usageTempList.size() + "，开始查询保单是否存在");
					}

					for (BizUsageTemp usageTemp : usageTempList) {
						try {

							if (logger.isDebugEnabled()) {
								logger.debug("查询保单是否存在:" + usageTemp.getBillNum());
							}

							int isExists = cl4DBJdbc.queryForInt(selectCustSql, usageTemp.getBillNum());
							if (isExists > 0) {
								finalResultList.add(usageTemp);
							}
						} catch (NoResultException ne) {
							logger.error("查询不到对应的保单信息", ne);
						} catch (Exception ex) {
							logger.error("获取电子投保客户号失败", ex);
						}
					}

					if (logger.isDebugEnabled()) {
						logger.debug("查询保单是否存在结束 ：" + finalResultList.size() + "");
					}

					usageCount = finalResultList.size();

					if (usageCount > 0) {
						if (logger.isDebugEnabled()) {
							logger.debug("开始批量插入数据到本地库");
						}
						batchSaveUsage(insertUsageToTemp, luckydrawDBJdbc, finalResultList);
						if (logger.isDebugEnabled()) {
							logger.debug("批量插入数据到本地库结束");
						}
						/* 同步信息到主表 */
						try {
							if (logger.isDebugEnabled()) {
								logger.debug("开始执行内部数据处理。");
							}

							luckydrawDBJdbc.execute("{call USP_SYNC_USAGE_INFO(?)}", batchNum);

							if (logger.isDebugEnabled()) {
								logger.debug("结束 内部数据处理。");
							}
						} catch (Exception syncEx) {
							logger.error("同步统计保单电子回执信息任务信息到主表时出错。", syncEx);
							throw syncEx;
						}

						if (logger.isDebugEnabled()) {
							logger.debug("开始插入消息。");
						}

						/* 插入消息 */
						submitEntMsg(finalResultList);

						if (logger.isDebugEnabled()) {
							logger.debug("插入消息結束。");
						}

						usageTempList = null;
						finalResultList = null;
					}
				} catch (Exception e) {
					logger.error("同步统计保单电子回执信息任务失败", e);
					isHasError = true;
				}
			}

			// 6、记录日志
			try {
				if (!isHasError) {
					if (existsFlag == null) {
						JobIncrement newFlag = new JobIncrement();
						newFlag.setJobName(jobEntity.getName());
						newFlag.setTypeId(jobEntity.getIncTypeId());
						newFlag.setFlag(format.format(syncStartTime));
						newFlag.setRemark(jobEntity.getName());
						newFlag.setCreateTime(new Date());
						jobIncrementService.save(newFlag);
					} else {
						existsFlag.setFlag(format.format(syncStartTime));
						existsFlag.setTypeId(jobEntity.getIncTypeId());
						jobIncrementService.update(existsFlag);
					}
				}
			} catch (Exception ex) {
				logger.error("保存同步统计保单电子回执信息任务增加标记失败", ex);
			}

			/* 处理重复消息 */
			try {
				msgcDBJdbc.execute("{call USP_MSG_DIST_DISPOSAL}");
			} catch (Exception syncEx) {
				logger.error("处理保单电子回执信息失败。", syncEx);
			}

			// 5、记录日志
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
				logger.error("处理重复营销员信息失败（去除重复工号，一天只对一个营销员发一条微信信息）", ex);
				ex.printStackTrace();
			}

			try {
				System.gc();
			} catch (Exception ex) {
				logger.error("同步统计保单电子回执信息任务内存回收失败。", ex);
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
			logger.error("同步理赔任务失败。", allEx);
		}
	}

	/**
	 * @param insertUsageToTemp
	 *            插入临时表SQL
	 * @param oracleJdbc
	 *            数据库链接
	 * @param usageList
	 *            业务列表
	 */
	private void batchSaveUsage(final String insertUsageToTemp, JdbcHelper oracleJdbc, List<BizUsageTemp> usageList) {
		List<BizUsageTemp> batchUsageList = new ArrayList<BizUsageTemp>();
		for (BizUsageTemp item : usageList) {
			batchUsageList.add(item);
			if (batchUsageList.size() >= 10000 && batchUsageList.size() % 10000 == 0) {
				oracleJdbc.executeBatch(insertUsageToTemp, new MappingBatchFeeder<BizUsageTemp>(batchUsageList.iterator(), BizUsageTemp.getMapper()));
				batchUsageList.clear();
			}
		}

		if (batchUsageList.size() > 0) {
			oracleJdbc.executeBatch(insertUsageToTemp, new MappingBatchFeeder<BizUsageTemp>(batchUsageList.iterator(), BizUsageTemp.getMapper()));
		}
	}

	/**
	 * @param usageList
	 *            业务列表
	 */
	private void submitEntMsg(List<BizUsageTemp> usageList) {

		if (usageList == null || usageList.size() == 0) {
			return;
		}

		try {

			Template template = templateService.getById(MSG_TEMPLATE_ID);

			if (template != null) {
				// 过滤重复的营销员工号
				Map<String, String> agentNumList = new HashMap<String, String>();
				for (BizUsageTemp usageTemp : usageList) {
					agentNumList.put(usageTemp.getAgntNum(), usageTemp.getAgntNum());
				}

				// 组装信息
				List<EntMsg> entMsgs = new ArrayList<EntMsg>();
				for (String key : agentNumList.keySet()) {
					String msgContent = template.getContent();
					EntMsg msg = new EntMsg();
					msg.setUserId(key);
					msg.setUserName("");
					msg.setContent(msgContent);
					msg.setTemplateId(MSG_TEMPLATE_ID);
					msg.setTypeId(1);
					msg.setFromServer(null);
					msg.setRelSys("MSGC");
					msg.setStatusId(0);
					msg.setSendType(3);
					msg.setCreateBy("MSGC");
					msg.setCreateTime(new Date());
					entMsgs.add(msg);
				}

				entMsgService.insertBatch(entMsgs);
			}
		} catch (Exception e) {
			logger.error("创建企业号消息数据失败", e);
		}
	}
}
