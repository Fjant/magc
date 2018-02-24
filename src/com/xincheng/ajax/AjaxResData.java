package com.xincheng.ajax;
/**
 *  ajax返回的实体对象
 * @author sky
 *
 */
public class AjaxResData {
	// 返回的数据集
	 private Object returnData;
	 //返回的成功失败标识,0为失败 1为成功
	 private String retCode;
	 //返回信息提示
	 private String message;
	public Object getReturnData() {
		return returnData;
	}
	public void setReturnData(Object returnData) {
		this.returnData = returnData;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	 
}
