/**
 * EServiceConfig.java
 * 
 * 功  能：数据字典
 * 类  名：EServiceConfig
 * 
 * ver		变更日		公司				变更人			变更内容
 * ------------------------------------------------------------
 * V1.00	2009-07-22	sinosoft		sinosoft		
 * 
 * Copyright (c) 2008, 2009 Sinosoft Co.,Ltd. All Rights Reserved.
 */
package com.xincheng.config;

import com.sinosoft.common.Data;
import com.xincheng.utils.ParseXML;

import org.apache.log4j.Logger;
import org.dom4j.Element;

/**
 * 数据字典
 * 
 * @author sinosoft
 * @version 1.0 2009-07-22 新建
 */

public class SystemConfig {

	private static Logger logger = Logger.getLogger(SystemConfig.class);// 记录日志
	private static Element ele = null;
	
	static {
		init();
	}

	/**
	 * 读取配置文件信息，保存到内存中
	 */
	private static void init() {
		try {
			ParseXML px = new ParseXML();
			px.initXmlByFile(Data.getProjectLocalPath() + "/WEB-INF/classes/systemConfig.xml");
			ele = px.getElement();
		} catch (Exception e) {
			logger.error("数据字典读取出错", e);
		}
	}

	/**
	 * 获取配置信息
	 * 
	 * @param src
	 *            参数名称
	 * @return 参数对应值
	 */
	public static String getPara(String src) {
		return ele.elementText(src).trim();
	}
}