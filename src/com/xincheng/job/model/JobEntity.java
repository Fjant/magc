package com.xincheng.job.model;

import java.util.Date;

import com.xincheng.ibatis.BaseEntity;

public class JobEntity extends BaseEntity {
	public static final int STATUS_RUNNING = 1;
	public static final int STATUS_NOT_RUNNING = 0;
	public static final int CONCURRENT_IS = 1;
	public static final int CONCURRENT_NOT = 0;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 * */
	private long id;

	/**
	 * 任务名称
	 * */
	private String name;

	/**
	 * 任务类型
	 * */
	private int incTypeId;

	/**
	 * 任务组
	 * */
	private String group;

	/**
	 * 任务接口类
	 * */
	private String classPath;

	/**
	 * 任务接口函数
	 * */
	private String method;

	/**
	 * 任务接口参数
	 * */
	private String argument;

	/**
	 * 任务接口参数
	 * */
	private int isConcurrent;

	/**
	 * 任务运行时间
	 * */
	private String cronExpression;

	/**
	 * 任务状态
	 * */
	private int statusId;

	/**
	 * 备注
	 * */
	private String remark;

	/**
	 * 创建者
	 * */
	private String createBy;

	/**
	 * 创建时间
	 * */
	private Date createTime;

	/**
	 * 更新时间
	 * */
	private Date updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIncTypeId() {
		return incTypeId;
	}

	public void setIncTypeId(int incTypeId) {
		this.incTypeId = incTypeId;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getArgument() {
		return argument;
	}

	public void setArgument(String argument) {
		this.argument = argument;
	}

	public int getIsConcurrent() {
		return isConcurrent;
	}

	public void setIsConcurrent(int isConcurrent) {
		this.isConcurrent = isConcurrent;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
