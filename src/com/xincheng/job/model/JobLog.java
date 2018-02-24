package com.xincheng.job.model;

import java.util.Date;
import com.xincheng.ibatis.BaseEntity;

public class JobLog extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String jobName;
	private int incTypeId;
	private int totalNumber;
	private int successNumber;
	private Date startTime;
	private Date endTime;
	private int result;
	private String remark;
	private Date createTime;
	private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public int getIncTypeId() {
		return incTypeId;
	}

	public void setIncTypeId(int incTypeId) {
		this.incTypeId = incTypeId;
	}

	public int getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}

	public int getSuccessNumber() {
		return successNumber;
	}

	public void setSuccessNumber(int successNumber) {
		this.successNumber = successNumber;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

}
