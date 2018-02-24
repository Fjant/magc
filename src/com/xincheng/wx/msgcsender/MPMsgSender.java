package com.xincheng.wx.msgcsender;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.xincheng.config.SystemConfig;
import com.xincheng.msg.model.MPMsg;
import com.xincheng.utils.URLClient;
import com.xincheng.wx.Env.WxEnv;
import com.xincheng.wx.model.TemplateData;
import com.xincheng.wx.model.WxTemplateMsg;
import com.xincheng.wx.util.WeixinUtil;

import net.sf.json.JSONObject;

public class MPMsgSender {
	private static Logger logger = Logger.getLogger(MPMsgSender.class);
	private static String GET_TOKEN_URL = SystemConfig.getPara("MPGetToken");
	private static String ACCESS_TOKEN = null;

	/**
	 * 发送模板消息
	 * 
	 * @param accessToken
	 * @param MsgTemplate
	 *            模板消息数据
	 * @return
	 */
	public static JSONObject sendMsg(MPMsg mpMsg) {

		try {
			// 初始化消息参数
			Map<String, TemplateData> datas = new HashMap<String, TemplateData>();
			Map<String, String> msgParams = jsonToMap(mpMsg.getContent());
			WxTemplateMsg msgData = new WxTemplateMsg();
			for (String paraKey : msgParams.keySet()) {
				datas.put(paraKey, new TemplateData(msgParams.get(paraKey), "#000000"));
			}
			msgData.setData(datas); // 模板数据
			msgData.setTouser(mpMsg.getOpenId()); // 接收者
			msgData.setTemplate_id(mpMsg.getTemplateId()); // 模板ID
			// 跳转链接
			if (StringUtils.isNotEmpty(mpMsg.getUrl())) {
				msgData.setUrl(mpMsg.getUrl());
			} else {
				msgData.setUrl("");
			}

			if (logger.isDebugEnabled()) {
				logger.debug("SEND_TEMPLATE_MSG_URL =" + WxEnv.propertyUtil.get("SEND_TEMPLATE_MSG_URL"));
				logger.debug("ACCESS_TOKEN_NAME =" + WxEnv.propertyUtil.get("ACCESS_TOKEN_NAME"));
				logger.debug("ACCESS_TOKEN =" + ACCESS_TOKEN);
			}

			if (StringUtils.isEmpty(ACCESS_TOKEN)) {
				try {
					ACCESS_TOKEN = URLClient.httpURLConectionGET(GET_TOKEN_URL);
					logger.info("重新获取 ACCESS_TOKEN =" + ACCESS_TOKEN);
				} catch (Exception ex) {
					logger.error("获取服务号ACCESS_TOKEN 失败", ex);
					return null;
				}
				
				//如果仍然未获取到有效的TOKEN，则直接返回
				if (StringUtils.isEmpty(ACCESS_TOKEN)) {
					logger.error("未获取到有效的服务号ACCESS_TOKEN 失败");
					return null;
				}
			}			

			// 拼发送消息的url
			String url = WxEnv.propertyUtil.get("SEND_TEMPLATE_MSG_URL").replace(WxEnv.propertyUtil.get("ACCESS_TOKEN_NAME"), ACCESS_TOKEN);

			String data = JSONObject.fromObject(msgData).toString(); // 将对象转换对应的json数据
			if (logger.isDebugEnabled()) {
				logger.debug(data);
			}

			JSONObject jsonObject = WeixinUtil.httpRequest(url, "POST", data);
			if (null != jsonObject && jsonObject.containsKey("errcode")) {
				int errCode = jsonObject.getInt("errcode");

				if (0 != errCode && 82001 != errCode) {
					logger.error("服务号消息发送失败 。errcode:{" + jsonObject.getInt("errcode") + "}， errmsg:{" + jsonObject.getString("errmsg") + " ，ACCESS_TOKEN:{" + ACCESS_TOKEN + " }}");
				}

				// 如果ACCESS TOKEN过期，则重新获取TOKEN
				if (errCode == 40001 || errCode == 42001 || errCode == 40014) {
					try {
						ACCESS_TOKEN = URLClient.httpURLConectionGET(GET_TOKEN_URL);
						logger.info("重新获取 ACCESS_TOKEN =" + ACCESS_TOKEN);
					} catch (Exception ex) {
						logger.error("获取服务号ACCESS_TOKEN 失败", ex);
					}

					logger.error("重新获取服务号ACCESS_TOKEN 。errcode:{" + jsonObject.getInt("errcode") + "} ，errmsg:{" + jsonObject.getString("errmsg") + "， ACCESS_TOKEN:{" + ACCESS_TOKEN + " }}");
				}
			}

			return jsonObject;
		} catch (Exception ex) {
			logger.error("发送服务号消息失败", ex);
			return null;
		}
	}

	private static Map<String, String> jsonToMap(String jsonStr) {
		Map<String, String> data = new HashMap<String, String>();
		// 将json字符串转换成jsonObject
		if (StringUtils.isNotEmpty(jsonStr)) {
			JSONObject jsonObject = JSONObject.fromObject(jsonStr);
			Iterator it = jsonObject.keys();
			// 遍历jsonObject数据，添加到Map对象
			while (it.hasNext()) {
				String key = String.valueOf(it.next());
				String value = (String) jsonObject.get(key);
				data.put(key, value);
			}

			return data;
		} else {
			return data;
		}
	}
}
