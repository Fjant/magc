package com.xincheng.wx.model;

public class WxNewsMsg extends WxMsgBase{
	private WxNewsMsgType news;
	public WxNewsMsgType getNews() {
		return news;
	}
	public void setNews(WxNewsMsgType news) {
		this.news = news;
	}
}
