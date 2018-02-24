package com.xincheng.job.plugins.renewal.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import jdbchelper.BeanCreator;
import jdbchelper.StatementMapper;

import com.xincheng.ibatis.BaseEntity;

public class RenewalBiz extends BaseEntity implements BeanCreator {

	private long id;
	private String customerNo;
	private String billNo;
	private String productCode;
	private Integer renewalDate;
	private Date createTime;
	private String cifId;
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the customerNo
	 */
	public String getCustomerNo() {
		return customerNo;
	}

	/**
	 * @param customerNo
	 *            the customerNo to set
	 */
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	/**
	 * @return the productCode
	 */
	public String getProductCode() {
		return productCode;
	}

	/**
	 * @param productCode
	 *            the productCode to set
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	/**
	 * @return the renewalDate
	 */
	public Integer getRenewalDate() {
		return renewalDate;
	}

	/**
	 * @param renewalDate
	 *            the renewalDate to set
	 */
	public void setRenewalDate(Integer renewalDate) {
		this.renewalDate = renewalDate;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	public String getCifId() {
		return cifId;
	}

	public void setCifId(String cifId) {
		this.cifId = cifId;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public static StatementMapper<RenewalBiz> getMapper() {
		return new StatementMapper<RenewalBiz>() {
			public void mapStatement(PreparedStatement stmt, RenewalBiz info) throws SQLException {
				stmt.setString(1, info.getCustomerNo());
				stmt.setString(2, info.getBillNo());
				stmt.setString(3, info.getProductCode());
				stmt.setInt(4, info.getRenewalDate());
				stmt.setString(5, info.getCifId());
			}
		};
	}

	@Override
	public Object createBean(ResultSet rs) throws SQLException {
		RenewalBiz biz = new RenewalBiz();
		biz.setCustomerNo(rs.getString("COWNNUM"));
		biz.setRenewalDate(rs.getInt("PTDATE"));
		biz.setBillNo(rs.getString("CHDRNUM"));
		biz.setProductCode(rs.getString("CNTTYPE"));
		return biz;
	}
}
