package com.xincheng.wx.service;

import java.util.Map;

import net.sf.json.JSONObject;

public interface WxService {

	public void sendWxMsg();

	public void sendWxEntMsgAt(int startHour, int StartMin, int endHour, int endMin);
	
	public void sendWxServiceMsgAt(int startHour, int StartMin, int endHour, int endMin);

	public JSONObject sendMsg(Map<String, String> map);

	JSONObject wxCommonManager(Map<String, String[]> map);
}
