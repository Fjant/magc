package com.xincheng.msg.model;

import java.util.Date;

import com.xincheng.ibatis.BaseEntity;

public class MPMsg extends BaseEntity {
	/**
	 * 编号
	 * */
	private long id;

	/**
	 * 客户微信号
	 * */
	private String openId;

	/**
	 * 客户CIF ID
	 * */
	private String cifId;

	/**
	 * 客户姓名
	 * */
	private String customerName;

	/**
	 * 消息内容
	 * */
	private String content;

	/**
	 * 消息内容
	 * */
	private String url;

	/**
	 * 消息模板编号
	 * */
	private String templateId;

	/**
	 * 消息类型
	 * */
	private int typeId;

	/**
	 * 相关子系统
	 * */
	private String relSys;

	/**
	 * 消息类型（1=续期缴费；2=生日提醒）
	 * */
	private String fromServer;

	/**
	 * 消息来源（接收请求的MSGC服务器）
	 * */
	private int statusId;

	/**
	 * 消息状态(0=未发送；1=已发送；)
	 * */
	private int sendType;

	/**
	 * 发送时间
	 * */
	private Date sendTime;

	/**
	 * 发送结果
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
	
	/**
	 *发送开始时间  
	 */
	private Date sendStartDate;
	
	/**
	 * 发送结束时间
	 */
	private Date sendEndDate;
	
	/**
	 * 当天开始发送时间 ，如8：00
	 */
	private String sendStartTime;
	
	/**
	 * 当天结束发送时间，如9：00
	 */
	private String sendEndTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getCifId() {
		return cifId;
	}

	public void setCifId(String cifId) {
		this.cifId = cifId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getRelSys() {
		return relSys;
	}

	public void setRelSys(String relSys) {
		this.relSys = relSys;
	}

	public String getFromServer() {
		return fromServer;
	}

	public void setFromServer(String fromServer) {
		this.fromServer = fromServer;
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

	public Date getSendStartDate() {
		return sendStartDate;
	}

	public void setSendStartDate(Date sendStartDate) {
		this.sendStartDate = sendStartDate;
	}

	public Date getSendEndDate() {
		return sendEndDate;
	}

	public void setSendEndDate(Date sendEndDate) {
		this.sendEndDate = sendEndDate;
	}

	public String getSendStartTime() {
		return sendStartTime;
	}

	public void setSendStartTime(String sendStartTime) {
		this.sendStartTime = sendStartTime;
	}

	public String getSendEndTime() {
		return sendEndTime;
	}

	public void setSendEndTime(String sendEndTime) {
		this.sendEndTime = sendEndTime;
	}

}
