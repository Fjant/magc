package com.xincheng.wx.msgcsender;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import net.sf.json.JSONObject;

import com.xincheng.msg.model.EntMsg;
import com.xincheng.wx.Env.WxEnv;
import com.xincheng.wx.model.WxAccessToken;
import com.xincheng.wx.model.WxMsgBase;
import com.xincheng.wx.model.WxTextMsg;
import com.xincheng.wx.model.WxTextMsgType;
import com.xincheng.wx.util.WeixinUtil;

public class EntMsgSender {
	private static Logger logger = Logger.getLogger(EntMsgSender.class);

	private static String CORP_ID = WxEnv.propertyUtil.get("CORP_ID");
	private static String AGENT_ID = WxEnv.propertyUtil.get("default_agentid_" + CORP_ID);

	public static JSONObject sendMsg(EntMsg msg) {
		JSONObject jsonObject = null;
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("touser", msg.getUserId());
			map.put("agentid", AGENT_ID);
			map.put("safe", "0");
			map.put("msgtype", "text");
			map.put("content", msg.getContent());

			WxMsgBase wxMsg = null;
			// 消息类型，默认为text消息
			String msgtype = map.get("msgtype") == null ? WxEnv.propertyUtil.get("msgtype_text") : map.get("msgtype");
			if (WxEnv.propertyUtil.get("msgtype_text").equals(msgtype)) {
				String content = map.get("content");
				wxMsg = new WxTextMsg();
				WxTextMsgType wxTextMsgType = new WxTextMsgType();
				wxTextMsgType.setContent(content);
				((WxTextMsg) wxMsg).setText(wxTextMsgType);
			}
			String touser = map.get("touser");// 发送人企业id
			String toparty = map.get("toparty");// 发送人部门
			String totag = map.get("totag");
			String agentid = map.get("agentid");// 企业应用号
			String safe = map.get("safe");// 是否保密
			wxMsg.setTouser(touser);
			wxMsg.setToparty(toparty);
			wxMsg.setTotag(totag);
			wxMsg.setMsgtype(msgtype);
			wxMsg.setAgentid(agentid);
			wxMsg.setSafe(safe);

			// 通过corpid和secret获取调用接口的accessToken
			WxAccessToken accessToken = WeixinUtil.getAccessTokenFromWx();

			if (accessToken != null) {
				// 调用微信企业号发送消息的接口
				jsonObject = WeixinUtil.sendWxQyhMsg(wxMsg, accessToken.getAccessToken());
				if (null != jsonObject) {
					if (0 != jsonObject.getInt("errcode")) {
						logger.error("企业号消息发送失败 。errcode:{" + jsonObject.getInt("errcode") + "} errmsg:{" + jsonObject.getString("errmsg") + "}");
					}
				}
			}
		} catch (Exception e) {
			logger.error("发送企业号消息失败", e);
		}
		return jsonObject;
	}
}
