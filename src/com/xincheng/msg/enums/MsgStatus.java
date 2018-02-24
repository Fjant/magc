package com.xincheng.msg.enums;

public enum MsgStatus {
	Init("直接发送", 0), HadSent("队列发送", 1);
	private MsgStatus(String comment, int code) {
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
