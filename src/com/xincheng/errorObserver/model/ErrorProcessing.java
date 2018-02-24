package com.xincheng.errorObserver.model;

import java.util.Date;

import com.xincheng.ibatis.BaseEntity;

/**
 * 异常处理记录
 * @author Administrator
 *  SYS_ERROR_PROCESSING_RECORD
 */

public class ErrorProcessing extends BaseEntity{

	private static final long serialVersionUID = 4686158212498999597L;

	private long id; 
 
	private String errorIdArr; 
	  
	private String sendContent; 
	
	private String notifierPhone;
  
    private String notifierPhonemsgResult; 
  
    private String notifierEmail;

    private String notifierEmailResult;
    
    private Integer notifierWeixin;
    
    private Integer notifierWeixinResult;

    private Date createTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getErrorIdArr() {
		return errorIdArr;
	}

	public void setErrorIdArr(String errorIdArr) {
		this.errorIdArr = errorIdArr;
	}

	public String getSendContent() {
		return sendContent;
	}

	public void setSendContent(String sendContent) {
		this.sendContent = sendContent;
	}

	public String getNotifierPhone() {
		return notifierPhone;
	}

	public void setNotifierPhone(String notifierPhone) {
		this.notifierPhone = notifierPhone;
	}

	public String getNotifierPhonemsgResult() {
		return notifierPhonemsgResult;
	}

	public void setNotifierPhonemsgResult(String notifierPhonemsgResult) {
		this.notifierPhonemsgResult = notifierPhonemsgResult;
	}

	public String getNotifierEmail() {
		return notifierEmail;
	}

	public void setNotifierEmail(String notifierEmail) {
		this.notifierEmail = notifierEmail;
	}

	public String getNotifierEmailResult() {
		return notifierEmailResult;
	}

	public void setNotifierEmailResult(String notifierEmailResult) {
		this.notifierEmailResult = notifierEmailResult;
	}

	public Integer getNotifierWeixin() {
		return notifierWeixin;
	}

	public void setNotifierWeixin(Integer notifierWeixin) {
		this.notifierWeixin = notifierWeixin;
	}

	public Integer getNotifierWeixinResult() {
		return notifierWeixinResult;
	}

	public void setNotifierWeixinResult(Integer notifierWeixinResult) {
		this.notifierWeixinResult = notifierWeixinResult;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
