package com.xincheng.job.plugins.kadan.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import jdbchelper.BeanCreator;

import com.xincheng.ibatis.BaseEntity;

public class RefundMP extends BaseEntity implements BeanCreator {
	private long id;
	private String agntNum;
	private String openId;
	private long refundId;
	private int amount;
	private String refSSN;
	private String randomStr;
	private String description;
	private int statusId;
	private String returnCode;
	private String returnMsg;
	private String resultCode;
	private String errorCode;
	private String errorMsg;
	private String paymentNo;
	private String paymentTime;
	private String returnXml;
	private Date createTime;
	private Date updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAgntNum() {
		return agntNum;
	}

	public void setAgntNum(String agntNum) {
		this.agntNum = agntNum;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public long getRefundId() {
		return refundId;
	}

	public void setRefundId(long refundId) {
		this.refundId = refundId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getRefSSN() {
		return refSSN;
	}

	public void setRefSSN(String refSSN) {
		this.refSSN = refSSN;
	}

	public String getRandomStr() {
		return randomStr;
	}

	public void setRandomStr(String randomStr) {
		this.randomStr = randomStr;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getPaymentNo() {
		return paymentNo;
	}

	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}

	public String getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime;
	}

	public String getReturnXml() {
		return returnXml;
	}

	public void setReturnXml(String returnXml) {
		this.returnXml = returnXml;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public Object createBean(ResultSet rs) throws SQLException {
		RefundMP refundMP = new RefundMP();
		refundMP.setId(rs.getLong("ID"));
		refundMP.setAgntNum(rs.getString("AGNT_NUM"));
		refundMP.setOpenId(rs.getString("OPEN_ID"));
		refundMP.setRefundId(rs.getLong("REFUND_ID"));
		refundMP.setAmount(rs.getInt("AMOUNT"));
		refundMP.setRefSSN(rs.getString("REF_SSN"));
		refundMP.setRandomStr(rs.getString("RADOM_STR"));
		refundMP.setDescription(rs.getString("DESCRIPTION"));
		refundMP.setStatusId(rs.getInt("STATUS_ID"));
		refundMP.setReturnCode(rs.getString("RETURN_CODE"));
		refundMP.setReturnMsg(rs.getString("RETURN_MSG"));
		refundMP.setResultCode(rs.getString("RESULT_CODE"));
		refundMP.setErrorCode(rs.getString("ERROR_CODE"));
		refundMP.setErrorMsg(rs.getString("ERROR_MSG"));
		refundMP.setReturnXml(rs.getString("RETURN_XML"));
		refundMP.setCreateTime(rs.getTimestamp("CREATE_TIME"));
		refundMP.setUpdateTime(rs.getTimestamp("UPDATE_TIME"));

		return refundMP;
	}
}
