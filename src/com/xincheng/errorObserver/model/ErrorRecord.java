package com.xincheng.errorObserver.model;

import java.util.Date;

import com.xincheng.ibatis.BaseEntity;

/**
 * 异常记录表
 * @author Administrator 
 *  SYS_ERROR_RECORD
 */

public class ErrorRecord extends BaseEntity{

	private static final long serialVersionUID = -3742302471745986886L;

	private long id; 
 
	private Integer functionModularId; 
	  
	private String operatorId;
  
    private String errorMessage; 
  
    private String errorMemo;

    private Integer errorLevel;
  
    private Date errorTime;
    
    private Integer state;
    

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getFunctionModularId() {
		return functionModularId;
	}

	public void setFunctionModularId(Integer functionModularId) {
		this.functionModularId = functionModularId;
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
}
