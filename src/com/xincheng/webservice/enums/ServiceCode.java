package com.xincheng.webservice.enums;

public enum ServiceCode {
	Normal("正常", 0), InvokeIllegal("非法请求", 1000), ParameterNotFound("参数未提供", 1001), ParameterNull("参数不能为空", 1002), ParameterValidationFailed("参数验证不通过", 1003), UnknowedError("未知错误", 1004), SendMsgFailed("请求接收完成 ，但消息发送失败", 1005);
	private ServiceCode(String comment, int code) {
		this.code = code;
		this.comment = comment;
	}

	private int code;
	private String comment;

	public int getCode() {
		return this.code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
