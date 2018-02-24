package com.xincheng.job.plugins.amactivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ws.rs.core.MultivaluedMap;

import jdbchelper.JdbcHelper;
import jdbchelper.MappingBatchFeeder;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.xincheng.config.SystemConfig;
import com.xincheng.encrypt.AesUtils;
import com.xincheng.encrypt.MD5Utils;
import com.xincheng.errorObserver.model.ErrorRecord;
import com.xincheng.errorObserver.service.ErrorRecordService;
import com.xincheng.httpProxy.JerseyClient;
import com.xincheng.jdbc.DataSourceUtil;
import com.xincheng.job.model.JobEntity;
import com.xincheng.job.model.JobIncrement;
import com.xincheng.job.model.JobLog;
import com.xincheng.job.plugins.BaseJob;
import com.xincheng.job.plugins.amactivity.model.SnActivityAgnt;
import com.xincheng.job.service.JobIncrementService;
import com.xincheng.job.service.JobLogService;
import com.xincheng.msg.service.EntMsgService;
import com.xincheng.webservice.model.BaseModel;
import com.xincheng.wx.util.WeixinUtil;

@Service
public class ActivityDataSyncJob extends JerseyClient implements BaseJob {

	private static Logger logger = LoggerFactory.getLogger(ActivityDataSyncJob.class);

	private static String Secret = SystemConfig.getPara("AMDataSyncEncryptKey");
	private static String AesKey = SystemConfig.getPara("AMDataSyncAesKey");
	private static String DOWNLOAD_DATA_URL = SystemConfig.getPara("AMDownloadActivityData");
	private static int DOWNLOAD_BATCH = Integer.parseInt(SystemConfig.getPara("AMDownloadBatch"));

	@Autowired
	private JobLogService joblogService;

	@Autowired
	private JobIncrementService jobIncrementService;

	@Autowired
	private ErrorRecordService errorRecordService;

	@Autowired
	private EntMsgService entMsgService;

	// @Autowired
	// private JerseyClient baseClient;

	public void execute(JobEntity jobEntity) {

		// 判断是否需要对发送时间进行控制
		if (StringUtils.isNotEmpty(jobEntity.getArgument())) {
			String[] times = jobEntity.getArgument().split(",");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				Date startTime = dateFormat.parse(times[0]);
				Date endTime = dateFormat.parse(times[1]);
				Date curTime = new Date();
				// 如果不在任务时间范围内，则退出
				if (startTime.getTime() > curTime.getTime() || curTime.getTime() > endTime.getTime()) {
					return;
				}
			} catch (ParseException e) {
				logger.debug("考勤活动数据同步参数出错", e);
			}
		}

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("考勤活动数据同步开始");
			}

			// 开始时间
			Date syncStartTime = new Date();
			boolean isHasError = false;
			int usageCount = 0;

			// 初始化
			ResourceBundle sqlBundle = null;
			ResourceBundle dbBundle = null;
			String insertSql = null;

			JdbcHelper luckydrawDBJdbc = null;
			JdbcHelper msgcDBJdbc = null;

			try {
				if (logger.isDebugEnabled()) {
					logger.debug("初始化考勤活动数据脚本");
				}

				sqlBundle = ResourceBundle.getBundle("jdbc_sqls");
				dbBundle = ResourceBundle.getBundle("jdbc");
				insertSql = sqlBundle.getString("AM_ACTIVITY_DATA_INSERT");
				luckydrawDBJdbc = new JdbcHelper(DataSourceUtil.getDataSource(dbBundle.getString("oracle_luckydraw")));
				msgcDBJdbc = new JdbcHelper(DataSourceUtil.getDataSource(dbBundle.getString("oracle_msgc")));
			} catch (Exception ex) {
				logger.error("初始化连接失败，同步理赔任务终止 。", ex);
				isHasError = true;
			}

			// 默认取全部数据
			long orginalIncrementId = 0L;
			long newIncrementId = 0L;
			JobIncrement existsFlag = null;
			if (!isHasError) {
				try {
					if (logger.isDebugEnabled()) {
						logger.debug("查询上次同步时间");
					}

					// 查询上次同步信息，获取上次增量同步日期
					existsFlag = jobIncrementService.getByTypeId(jobEntity.getIncTypeId());
					if (existsFlag != null && StringUtils.isNotEmpty(existsFlag.getFlag())) {
						orginalIncrementId = Long.parseLong(existsFlag.getFlag().trim());
						newIncrementId = orginalIncrementId;
					}
					
					if (logger.isDebugEnabled()) {
						logger.debug("开始同步考勤活动数据");
					}
					// 调用下载接口
					BaseModel<List<SnActivityAgnt>> model = callDownloadActivityDataApi(orginalIncrementId, DOWNLOAD_BATCH);
					if (model == null || model.getCode() != 0) {
						logger.error("考勤活动数据同步出错" + JSONUtil.serialize(model));
						throw new Exception("考勤活动数据同步出错");
					}

					// 批量保存到数据中
					List<SnActivityAgnt> amRecordList = (List<SnActivityAgnt>) model.getData().get("Records");

					if (amRecordList != null) {
						usageCount += amRecordList.size();
					}

					if (logger.isDebugEnabled()) {
						logger.debug("查询数据结束，开始读取理赔数据");
					}

					if (amRecordList != null && amRecordList.size() > 0) {
						try {
							luckydrawDBJdbc.beginTransaction();
							List<SnActivityAgnt> batchList = new ArrayList<SnActivityAgnt>();
							for (SnActivityAgnt item : amRecordList) {
								batchList.add(item);
								// 记录本次获取到的最大ID值
								if (item.getId() > newIncrementId) {
									newIncrementId = item.getId();
								}
								if (batchList.size() >= DOWNLOAD_BATCH && batchList.size() % DOWNLOAD_BATCH == 0) {
									luckydrawDBJdbc.executeBatch(insertSql, new MappingBatchFeeder<SnActivityAgnt>(batchList.iterator(), SnActivityAgnt.getMapper()));
									batchList.clear();
								}
							}

							if (batchList.size() > 0) {
								luckydrawDBJdbc.executeBatch(insertSql, new MappingBatchFeeder<SnActivityAgnt>(batchList.iterator(), SnActivityAgnt.getMapper()));
							}

							luckydrawDBJdbc.commitTransaction();
						} catch (Exception ex) {
							luckydrawDBJdbc.rollbackTransaction();
							isHasError = true;
							logger.error("同步签到签退记录到临时表时出错", ex);
							ex.printStackTrace();
							throw ex;
						}

						/* 同步信息到主表 */
						try {
							if (logger.isDebugEnabled()) {
								logger.debug("开始执行内部数据处理。");
							}

							luckydrawDBJdbc.execute("{call USP_SYNC_AGNT_AM_INFO(?)}", orginalIncrementId);

							if (logger.isDebugEnabled()) {
								logger.debug("结束 内部数据处理。");
							}
						} catch (Exception syncEx) {
							logger.error("同步理赔申请任务信息到主表时出错。", syncEx);
							throw syncEx;
						}
					}
				} catch (Exception e) {
					logger.error("同步理赔申请任务失败", e);
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
						newFlag.setFlag(String.valueOf(newIncrementId));
						newFlag.setRemark(jobEntity.getName());
						newFlag.setCreateTime(new Date());
						jobIncrementService.save(newFlag);
					} else {
						existsFlag.setFlag(String.valueOf(newIncrementId));
						existsFlag.setTypeId(jobEntity.getIncTypeId());
						jobIncrementService.update(existsFlag);
					}
				}
			} catch (Exception ex) {
				logger.error("保存同步理赔申请任务增加标记失败", ex);
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
				logger.error("保存同步理赔申请任务日志失败。", ex);
				ex.printStackTrace();
			}

			try {
				System.gc();
			} catch (Exception ex) {
				logger.error("同步理赔申请任务内存回收失败。", ex);
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

	private BaseModel<List<SnActivityAgnt>> callDownloadActivityDataApi(long incrementId, Integer downloadBatch) {

		try {
			String serializeArgs = null;
			MultivaluedMap<String, String> args = new MultivaluedMapImpl();

			// 加密参数
			serializeArgs = AesUtils.aesEncrypt("incrementId=" + incrementId + "_#?downloadBatch=" + downloadBatch, AesKey);
			args.add("sign", MD5Utils.encrypt(Secret + serializeArgs));
			args.add("code", serializeArgs);

			JSONObject resultObj = null;

			if (DOWNLOAD_DATA_URL.startsWith("https"))
				resultObj = WeixinUtil.httpsRequest(DOWNLOAD_DATA_URL + "?incrementId=" + incrementId + "&downloadBatch=" + downloadBatch, "GET", null);
			else
				resultObj = WeixinUtil.httpRequest(DOWNLOAD_DATA_URL + "?incrementId=" + incrementId + "&downloadBatch=" + downloadBatch, "GET", null);

			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setRootClass(SnActivityAgnt.class);
			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" }));
			JSONArray json = JSONArray.fromObject(resultObj.getJSONObject("data").get("Records"), jsonConfig);
			List<SnActivityAgnt> tempNeeds = (List<SnActivityAgnt>) JSONArray.toCollection(json, jsonConfig);
			Map<String, List<SnActivityAgnt>> data = new HashMap<String, List<SnActivityAgnt>>();
			data.put("Records", tempNeeds == null ? new ArrayList<SnActivityAgnt>() : tempNeeds);
			return new BaseModel<List<SnActivityAgnt>>(0, data);

			// // 调用接口
			// return
			// getClient().resource(DOWNLOAD_DATA_URL).accept(MediaType.APPLICATION_JSON).post(new
			// GenericType<BaseModel<List<SnActivityAgnt>>>() {
			// }, args);
		} catch (JSONException ex) {
			logger.error("签到请求参数JSON序列化出错", ex);
		} catch (Exception e) {
			logger.error("签到签退数据下载出错", e);
		}

		return null;
	}
}
