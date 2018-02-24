package com.xincheng.webservice.restful.msg.model;

import com.xincheng.ibatis.BaseEntity;

public class MPMsg extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}
