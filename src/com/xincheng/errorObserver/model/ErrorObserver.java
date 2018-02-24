package com.xincheng.errorObserver.model;

import java.util.Date;

import com.xincheng.ibatis.BaseEntity;

public class ErrorObserver extends BaseEntity{

	private static final long serialVersionUID = 4166566303152598797L;

	private long id; 
	 
	private String functionModular; 
	  
	private String operatorId;
  
    private String errorMessage; 
  
    private String errorMemo;

    private Integer errorLevel;
  
    private Date errorTime;
    
    private Integer state;
    
	private String sendContent; 

	private String processingMode;

	private String treatmentResult; 

	private String notifierPhone;

	private String notifierEmail;

	private String notifierWeixin;
	
    private Integer treatmentState;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFunctionModular() {
		return functionModular;
	}

	public void setFunctionModular(String functionModular) {
		this.functionModular = functionModular;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMemo() {
		return errorMemo;
	}

	public void setErrorMemo(String errorMemo) {
		this.errorMemo = errorMemo;
	}

	public Integer getErrorLevel() {
		return errorLevel;
	}

	public void setErrorLevel(Integer errorLevel) {
		this.errorLevel = errorLevel;
	}

	public Date getErrorTime() {
		return errorTime;
	}

	public void setErrorTime(Date errorTime) {
		this.errorTime = errorTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getSendContent() {
		return sendContent;
	}

	public void setSendContent(String sendContent) {
		this.sendContent = sendContent;
	}

	public String getProcessingMode() {
		return processingMode;
	}

	public void setProcessingMode(String processingMode) {
		this.processingMode = processingMode;
	}

	public String getTreatmentResult() {
		return treatmentResult;
	}

	public void setTreatmentResult(String treatmentResult) {
		this.treatmentResult = treatmentResult;
	}

	public String getNotifierPhone() {
		return notifierPhone;
	}

	public void setNotifierPhone(String notifierPhone) {
		this.notifierPhone = notifierPhone;
	}

	public String getNotifierEmail() {
		return notifierEmail;
	}

	public void setNotifierEmail(String notifierEmail) {
		this.notifierEmail = notifierEmail;
	}

	public String getNotifierWeixin() {
		return notifierWeixin;
	}

	public void setNotifierWeixin(String notifierWeixin) {
		this.notifierWeixin = notifierWeixin;
	}

	public Integer getTreatmentState() {
		return treatmentState;
	}

	public void setTreatmentState(Integer treatmentState) {
		this.treatmentState = treatmentState;
	}

}
