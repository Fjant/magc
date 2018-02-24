package com.xincheng.wx.model;

import java.util.Map;

/**
 * 模板消息类
 * @author g
 *
 */
public class WxTemplateMsg {
	private String touser;
	private String template_id; //模板ID
	private String url; //消息跳转链接
	private Map<String,TemplateData> data; //模板格式类
	
	
	
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Map<String,TemplateData> getData() {  
        return data;  
    }  
    public void setData(Map<String,TemplateData> data) {  
        this.data = data;  
    }
	
}
