package com.xincheng.webservice.restful.msg.service;

import javax.ws.rs.FormParam;

import com.xincheng.webservice.model.BaseModel;

public interface WXMsgService {
	/**
	 * 上传考勤需求记录
	 * */
	BaseModel submitEntMsg(@FormParam("sign") String sign, @FormParam("code") String code, @FormParam("msgs") String msgs, String sendImmediately, String relSys);
	
	BaseModel submitMPMsg(@FormParam("sign") String sign, @FormParam("code") String code, @FormParam("msgs") String msgs, String sendImmediately, String relSys);
}
