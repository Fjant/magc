package com.xincheng.job.plugins.kadan.enums;

public enum RefundStatus {

	Unhandle(0, "未处理"), Success(1, "退款成功"), FailForRetry(2, "退款失败，可再次尝试处理"), FailForPause(3, "退款失败，暂停退款");

	private Integer code;
	private String name;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	RefundStatus(int code, String name) {
		this.code = code;
		this.name = name;
	}
}
