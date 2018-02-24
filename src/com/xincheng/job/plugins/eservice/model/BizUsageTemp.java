package com.xincheng.job.plugins.eservice.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import jdbchelper.StatementMapper;
import com.xincheng.ibatis.BaseEntity;

public class BizUsageTemp extends BaseEntity {
	private int id;
	private String companyNo;
	private String agntNum;
	private String customerNo;
	private String cifId;
	private String billNum;
	private String bizNum;
	private Date bizTime;
	private String relSys = "LAS";
	private Integer typeId;
	private String batchNum;
	private String createBy;
	private Date createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}

	public String getAgntNum() {
		return agntNum;
	}

	public void setAgntNum(String agntNum) {
		this.agntNum = agntNum;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getCifId() {
		return cifId;
	}

	public void setCifId(String cifId) {
		this.cifId = cifId;
	}

	public String getBillNum() {
		return billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	public String getBizNum() {
		return bizNum;
	}

	public void setBizNum(String bizNum) {
		this.bizNum = bizNum;
	}

	public Date getBizTime() {
		return bizTime;
	}

	public void setBizTime(Date bizDate) {
		this.bizTime = bizDate;
	}

	public String getRelSys() {
		return relSys;
	}

	public void setRelSys(String relSys) {
		this.relSys = relSys;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public static StatementMapper<BizUsageTemp> getMapper() {
		return new StatementMapper<BizUsageTemp>() {
			public void mapStatement(PreparedStatement stmt, BizUsageTemp info) throws SQLException {
				stmt.setString(1, info.getAgntNum());
				stmt.setString(2, info.getCustomerNo());
				stmt.setString(3, info.getCifId());
				stmt.setString(4, info.getBillNum());
				stmt.setString(5, info.getBizNum());
				if (info.getBizTime() == null) {
					stmt.setTimestamp(6, null);
				} else {
					stmt.setTimestamp(6, new java.sql.Timestamp(info.getBizTime().getTime()));
				}
				stmt.setString(7, info.getRelSys());
				stmt.setInt(8, info.getTypeId());
				stmt.setString(9, info.getBatchNum());
			}
		};
	}
}
