package com.xincheng.customer.model;

import java.util.Date;

import com.xincheng.ibatis.BaseEntity;

public class CustomerMsg extends BaseEntity{
	
	private String customerNo;
	
	private Date sendTime;
	
	private String billNo;
	
	private Integer typeId;
	
	private String content;
	
	private Integer statusId;

	private String secuityNo;
	
	private String sendResult;
	
	private String remark;
	
	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getSecuityNo() {
		return secuityNo;
	}

	public void setSecuityNo(String secuityNo) {
		this.secuityNo = secuityNo;
	}

	public String getSendResult() {
		return sendResult;
	}

	public void setSendResult(String sendResult) {
		this.sendResult = sendResult;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
