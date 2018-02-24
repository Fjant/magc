package com.xincheng.job.plugins.eservice.model;

import java.util.Date;

public class SimpleBillRef {
	private String company;
	private String billNum;
	private String appNum;
	private Date accptDate;
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getBillNum() {
		return billNum;
	}
	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	public String getAppNum() {
		return appNum;
	}
	public void setAppNum(String appNum) {
		this.appNum = appNum;
	}
	public Date getAccptDate() {
		return accptDate;
	}
	public void setAccptDate(Date accptDate) {
		this.accptDate = accptDate;
	}

}
