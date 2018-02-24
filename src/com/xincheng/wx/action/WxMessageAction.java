package com.xincheng.wx.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionSupport;
import com.xincheng.utils.JsonUtils;
import com.xincheng.wx.model.TemplateData;
import com.xincheng.wx.model.WxTemplateMsg;
import com.xincheng.wx.msgcsender.MPMsgSender;

import net.sf.json.JSONObject;

@Namespace("/WeChat")
@Action(value = "WxMessage")
@Results({ @Result(name = "success", type = "json") })
public class WxMessageAction extends ActionSupport {

	public String pushMessage() {

		// String appID =
		// ServletActionContext.getRequest().getParameter("appID");
		// String secret =
		// ServletActionContext.getRequest().getParameter("secret");

		// WxAccessToken accessToken = WeixinUtil.getAccessTokenFromWx(appID,
		// secret,1); //获取公众号的accessToken
		String accessToken = "_8HK2--mrQDwK-QQdHuRtAZ0wUba5eKNV_93dfvJKv98TPD-1uI7YpuJkazBsULihuOqES4tJ36ovMnRPXCjhghLxCvukzfKMjwY9nE5d-jAMtdmUEzZ9FfQwJ3gqwGkFLRfAAARXD";
		WxTemplateMsg msgData = new WxTemplateMsg();
		Map<String, TemplateData> datas = new HashMap<String, TemplateData>();
		datas.put("first", new TemplateData("test", "#173177"));
		datas.put("time", new TemplateData("2016-02-14 00:00:00", "#173177"));
		datas.put("remark", new TemplateData("test", "#173177"));

		msgData.setTouser("oo7oGszeubI9DJbDZshAS6q47uXs"); // 接收者
		msgData.setUrl(""); // 跳转链接
		msgData.setTemplate_id("RNJSMzkD7nrVrGtOyUlAAcJatExQjPGEHZklMOIVemk"); // 模板ID
		msgData.setData(datas); // 模板数据

		// JSONObject jsonObject = MPMsgSender.sendMsg(msgData);

		// /JsonUtils.renderJson(jsonObject);

		return null;
	}

}
