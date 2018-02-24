package com.xincheng.wx.Env;

import java.util.Hashtable;

import com.xincheng.utils.PropertyUtil;

public class WxEnv {
	public static PropertyUtil propertyUtil=new PropertyUtil("/com/xincheng/wx/conf/wxconfig.properties");
	public static Hashtable<String,Object>wxInfoMap=new Hashtable<String, Object>();
}
