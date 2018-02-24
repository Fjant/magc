package com.xincheng.msg.model;

import java.util.Date;

import com.xincheng.ibatis.BaseEntity;

public class EntMsg extends BaseEntity {
	/**
	 * 编号
	 * */
	private long id;

	/**
	 * 营销员工号
	 * */
	private String userId;

	/**
	 * 营销员姓名
	 * */
	private String userName;

	/**
	 * 消息内容
	 * */
	private String content;

	/**
	 * 模板编号
	 * */
	private long templateId;

	/**
	 * 消息类型
	 * */
	private int typeId;

	/**
	 * 消息类型（1=续期缴费；2=生日提醒）
	 * */
	private String fromServer;

	/**
	 * 相关子系统
	 * */
	private String relSys;

	/**
	 * 消息来源（接收请求的MSGC服务器）
	 * */
	private int statusId;

	/**
	 * 发送类型(1=直接发送，2=队列发送)
	 * */
	private int sendType;

	/**
	 * 发送时间
	 * */
	private Date sendTime;

	/**
	 * 发送时间
	 * */
	private String sendResult;

	/**
	 * 备注
	 * */
	private String remark;

	/**
	 * 重发次数
	 * */
	private int resendTimes;

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getFromServer() {
		return fromServer;
	}

	public void setFromServer(String fromServer) {
		this.fromServer = fromServer;
	}

	public String getRelSys() {
		return relSys;
	}

	public void setRelSys(String relSys) {
		this.relSys = relSys;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public int getSendType() {
		return sendType;
	}

	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
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

	public int getResendTimes() {
		return resendTimes;
	}

	public void setResendTimes(int resendTimes) {
		this.resendTimes = resendTimes;
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
