package com.xincheng.wx.model;

import com.xincheng.ibatis.BaseEntity;

public class WxAccessToken extends BaseEntity {
	private String accessToken;
	private int expiresIn;
	private long beginTimeMillis;
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
	public long getBeginTimeMillis() {
		return beginTimeMillis;
	}
	public void setBeginTimeMillis(long beginTimeMillis) {
		this.beginTimeMillis = beginTimeMillis;
	}
	public long getRePickMillis() {
		return rePickMillis;
	}
	public void setRePickMillis(long rePickMillis) {
		this.rePickMillis = rePickMillis;
	}
	private long rePickMillis;
}
