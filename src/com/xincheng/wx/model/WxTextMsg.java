package com.xincheng.wx.model;

public class WxTextMsg extends WxMsgBase{
	private WxTextMsgType text;
	public WxTextMsgType getText() {
		return text;
	}
	public void setText(WxTextMsgType text) {
		this.text = text;
	}
}
