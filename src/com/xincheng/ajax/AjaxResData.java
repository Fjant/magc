package com.xincheng.ajax;
/**
 *  ajax���ص�ʵ�����
 * @author sky
 *
 */
public class AjaxResData {
	// ���ص����ݼ�
	 private Object returnData;
	 //���صĳɹ�ʧ�ܱ�ʶ,0Ϊʧ�� 1Ϊ�ɹ�
	 private String retCode;
	 //������Ϣ��ʾ
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
