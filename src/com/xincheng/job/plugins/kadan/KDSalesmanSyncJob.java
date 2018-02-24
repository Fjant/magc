package com.xincheng.job.plugins.kadan;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.xincheng.job.model.JobLog;
import com.xincheng.job.plugins.BaseJob;
import com.xincheng.job.plugins.salesman.LDSalesmanSyncJob;
import com.xincheng.job.service.JobLogService;
import com.xincheng.salesman.model.SalesmanInfoTemp;

/**
 * 营销员信息同步
 * 
 * */
@Service
public class KDSalesmanSyncJob implements BaseJob {

	private static Logger logger = LoggerFactory.getLogger(KDSalesmanSyncJob.class);

	@Autowired
	private ErrorRecordService errorRecordService;

	@Autowired
	private JobLogService joblogService;

	public static void main(String[] a) {
		LDSalesmanSyncJob impl = new LDSalesmanSyncJob();
		impl.execute(null);
		// impl.synchronizeToWx(true);
	}

	// AMS对接营销员的库表
	@Override
	public void execute(JobEntity jobEntity) {
		Date syncStartTime = new Date();
		int staffCount = 0;
		boolean isHasError = false;
		final ResourceBundle sqlBundle = ResourceBundle.getBundle("jdbc_sqls");
		final String selectAllStaff = sqlBundle.getString("KD_SALESMAN_SELECT_ALL");
		final String clearTemp = sqlBundle.getString("KD_SALESMAN_CLEAR_TEMP_TABLE");
		final String insertToTemp = sqlBundle.getString("KD_SALESMAN_INSERT_TEMP");

		JdbcHelper kadanDBJdbc = null;
		JdbcHelper cl4DBJdbc = null;

		try {
			kadanDBJdbc = new JdbcHelper(DataSourceUtil.getDataSource("oracle_kadan"));
			cl4DBJdbc = new JdbcHelper(DataSourceUtil.getDataSource("as400_cl4"));

			kadanDBJdbc.execute(clearTemp);
		} catch (Exception ex) {
			logger.error("清空卡单营销员信息临时表失败，同步终止。", ex);
			isHasError = true;
			ex.printStackTrace();
		}

		try {
			// 1、查询数据
			List<SalesmanInfoTemp> staffList = new ArrayList<SalesmanInfoTemp>();
			DateFormat format = new SimpleDateFormat("yyyyMMdd");
			QueryResult result = cl4DBJdbc.query(selectAllStaff);
			result.setAutoClose(true);
			while (result.next()) {

				String companyId = result.getString("COMPANY");
				String branchId = result.getString("BRANCH");
				String agntNum = result.getString("AGNTNUM");
				String fullName = result.getString("SURNAME01");
				String dutydeg = result.getString("DUTYDEG");
				String sex = result.getString("CLTSEX");
				String mobilephone = result.getString("RMBLPHONE");
				Date entryTime = null;
				String dteApp = result.getString("DTEAPP");
				if (StringUtils.isNotEmpty(dteApp)) {
					try {
						entryTime = format.parse(dteApp.trim());
					} catch (Exception parseEx) {
						logger.error("错误的DTEAPP日期：" + dteApp);
					}
				}

				Date quitTime = null;
				String dteTrm = result.getString("DTETRM");
				if (StringUtils.isNotEmpty(dteTrm)) {
					try {
						quitTime = format.parse(dteTrm.trim());
					} catch (Exception parseEx) {
						logger.error("错误的DTETRM日期：" + quitTime);
					}
				}
				int statusId = result.getInt("TERMFLAG");
				String cso = result.getString("CSO");
				String sso = result.getString("SSO");
				String tsaleNumt = result.getString("TSALESUNT");
				String agntNumLevel1 = result.getString("ABOSSNUM");
				String agntNumLevel1_relationship = result.getString("ABOSSRLTS");
				String agntNumLevel2 = result.getString("BBOSSNUM");
				String agntNumLevel2_relationship = result.getString("BBOSSRLTS");
				String agntNumLevel3 = result.getString("CBOSSNUM");
				String agntNumLevel3_relationship = result.getString("CBOSSRLTS");
				String agntNumLevel4 = result.getString("DBOSSNUM");
				String agntNumLevel4_relationship = result.getString("DBOSSRLTS");

				SalesmanInfoTemp staffInfo = new SalesmanInfoTemp();
				staffInfo.setCompanyId(companyId);
				staffInfo.setBranchId(branchId);
				staffInfo.setAgntNum(agntNum);
				staffInfo.setFullName(fullName);
				staffInfo.setDutydeg(dutydeg);
				staffInfo.setSex(sex);
				staffInfo.setMobilephone(mobilephone);
				staffInfo.setEntryTime(entryTime);
				staffInfo.setQuitTime(quitTime);
				staffInfo.setCso(cso);
				staffInfo.setSso(sso);
				staffInfo.setAbossnum(agntNumLevel1);
				staffInfo.setAbossrlts(agntNumLevel1_relationship);
				staffInfo.setBbossnum(agntNumLevel2);
				staffInfo.setBbossrlts(agntNumLevel2_relationship);
				staffInfo.setCbossnum(agntNumLevel3);
				staffInfo.setCbossrlts(agntNumLevel3_relationship);
				staffInfo.setDbossnum(agntNumLevel4);
				staffInfo.setDbossrlts(agntNumLevel4_relationship);
				staffInfo.setTsalesUnt(tsaleNumt);
				staffInfo.setStatusId(statusId);
				// 判断是直属还是所属
				List<String> gtAgentNums = new ArrayList<String>();
				List<String> dtAgentNums = new ArrayList<String>();
				if (StringUtils.isNotEmpty(agntNumLevel1) && StringUtils.isNotEmpty(agntNumLevel1_relationship)) {
					if (agntNumLevel1_relationship.toUpperCase().trim().equals("GT")) {
						gtAgentNums.add(agntNumLevel1.trim());
					} else if (agntNumLevel1_relationship.toUpperCase().trim().equals("DT")) {
						dtAgentNums.add(agntNumLevel1.trim());
					}
				}
				if (StringUtils.isNotEmpty(agntNumLevel2) && StringUtils.isNotEmpty(agntNumLevel2_relationship)) {
					if (agntNumLevel2_relationship.toUpperCase().trim().equals("GT")) {
						gtAgentNums.add(agntNumLevel2.trim());
					} else if (agntNumLevel2_relationship.toUpperCase().trim().equals("DT")) {
						dtAgentNums.add(agntNumLevel2.trim());
					}
				}
				if (StringUtils.isNotEmpty(agntNumLevel3) && StringUtils.isNotEmpty(agntNumLevel3_relationship)) {
					if (agntNumLevel3_relationship.toUpperCase().trim().equals("GT")) {
						gtAgentNums.add(agntNumLevel3.trim());
					} else if (agntNumLevel3_relationship.toUpperCase().trim().equals("DT")) {
						dtAgentNums.add(agntNumLevel3.trim());
					}
				}
				if (StringUtils.isNotEmpty(agntNumLevel4) && StringUtils.isNotEmpty(agntNumLevel4_relationship)) {
					if (agntNumLevel4_relationship.toUpperCase().trim().equals("GT")) {
						gtAgentNums.add(agntNumLevel4.trim());
					} else if (agntNumLevel4_relationship.toUpperCase().trim().equals("DT")) {
						dtAgentNums.add(agntNumLevel4.trim());
					}
				}

				// 设置直属主管
				if (dtAgentNums.size() > 0) {
					if (dtAgentNums.size() == 1) {
						staffInfo.setParentAgntNum(dtAgentNums.get(0));
					} else if (dtAgentNums.size() >= 2) {
						staffInfo.setParentAgntNum(dtAgentNums.get(0));
						staffInfo.setParentAgntNum2(dtAgentNums.get(1));
					}
				}

				// 设置所属主管
				if (gtAgentNums.size() > 0) {
					if (gtAgentNums.size() == 1) {
						staffInfo.setAgntNumLevel1(gtAgentNums.get(0));
					} else if (gtAgentNums.size() == 2) {
						staffInfo.setAgntNumLevel1(gtAgentNums.get(0));
						staffInfo.setAgntNumLevel2(gtAgentNums.get(1));
					} else if (gtAgentNums.size() == 3) {
						staffInfo.setAgntNumLevel1(gtAgentNums.get(0));
						staffInfo.setAgntNumLevel2(gtAgentNums.get(1));
						staffInfo.setAgntNumLevel3(gtAgentNums.get(2));
					} else if (gtAgentNums.size() >= 4) {
						staffInfo.setAgntNumLevel1(gtAgentNums.get(0));
						staffInfo.setAgntNumLevel2(gtAgentNums.get(1));
						staffInfo.setAgntNumLevel3(gtAgentNums.get(2));
						staffInfo.setAgntNumLevel4(gtAgentNums.get(3));
					}
				}

				staffList.add(staffInfo);
			}

			staffCount = staffList.size();

			// 2、保存数据到临时表
			if (staffCount > 0) {
				List<SalesmanInfoTemp> batchStaffList = new ArrayList<SalesmanInfoTemp>();
				for (SalesmanInfoTemp item : staffList) {
					batchStaffList.add(item);
					if (batchStaffList.size() >= 10000 && batchStaffList.size() % 10000 == 0) {
						kadanDBJdbc.executeBatch(insertToTemp, new MappingBatchFeeder<SalesmanInfoTemp>(batchStaffList.iterator(), SalesmanInfoTemp.getMapper()));
						batchStaffList.clear();
					}
				}

				if (batchStaffList.size() > 0) {
					kadanDBJdbc.executeBatch(insertToTemp, new MappingBatchFeeder<SalesmanInfoTemp>(batchStaffList.iterator(), SalesmanInfoTemp.getMapper()));
				}

				// D、执行数据更新
				try {
					kadanDBJdbc.beginTransaction();
					kadanDBJdbc.execute("{call USP_SYNC_SM_BASIC_INFO()}");
					kadanDBJdbc.commitTransaction();
				} catch (Exception syncTrans) {
					kadanDBJdbc.rollbackTransaction();
					logger.error("同步卡单营销员信息到主表时，执行存储过程出错。", syncTrans);
					throw syncTrans;
				}
			}
		} catch (Exception ex) {
			logger.error("同步卡单营销员信息出错", ex);
			isHasError = true;
			ex.printStackTrace();
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
			logger.error("保存卡单营销员同步日志出错。", ex);
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
