package com.xincheng.webservice.restful.msg.model;

import java.util.Date;

import com.xincheng.ibatis.BaseEntity;

public class EnterpriseMsg extends BaseEntity {

	/**
	 * 营销员工号
	 * */
	private String userId;

	/**
	 * 营销员姓名
	 * */
	private String userName;

	/**
	 * 消息内容(如果指定的消息模板不为空，则根据模板将content中的内容填充模板内容)
	 * */
	private String content;
	
	/**
	 * 消息模板编号
	 * */
	private String templateId;

	/**
	 * 消息类型
	 * */
	private int typeId;
	
	/**
	 * 创建者（各子系统应用）
	 * */
	private String createBy;

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

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
}
