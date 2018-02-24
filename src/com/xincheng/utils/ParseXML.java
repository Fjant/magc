/**
 * ParseXML.java
 * 
 * 功  能：解析xml
 * 类  名：ParseXML
 * 
 * ver		变更日		公司				变更人			变更内容
 * ------------------------------------------------------------
 * V1.00	2009-07-22	sinosoft		sinosoft		
 * 
 * Copyright (c) 2008, 2009 Sinosoft Co.,Ltd. All Rights Reserved.
 */

package com.xincheng.utils;

import org.apache.log4j.Logger;
import java.io.File;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import java.io.InputStream;

/**
 * 解析xml
 * 
 * @author 		sinosoft
 * @version		1.0		2009-07-22	新建
 */

public class ParseXML{
	private static Logger log = Logger.getLogger(ParseXML.class);//记录日志
	private Document document = null;
	private Element element = null;
	
	/**
	 * 通过路径获取xml
	 * @param xmlPath	xml路径
	 */
	public void initXmlByFile(String xmlPath){
		try{
			SAXReader reader = new SAXReader();
			document = reader.read(new File(xmlPath));
			element = document.getRootElement();
		}catch(Exception e){
			log.error("初始化出错：" + xmlPath, e);
		}
	}
	
	/**
	 * 通过字符串初始化xml
	 * @param xmlText
	 */
	public void initXmlByText(String xmlText){
		try{
			document = DocumentHelper.parseText(xmlText);
			element = document.getRootElement();
		}catch(Exception e){
			log.error("初始化出错：" + xmlText, e);
		}
	}
	
	/**
	 * 初始化xml输入流
	 * @param in
	 */
	public void initXmlByInputStream(InputStream in){
		try{
			SAXReader reader = new SAXReader();
			this.document = reader.read(in);
			document.setXMLEncoding("GBK");
			element = document.getRootElement();
		}catch(Exception e){
			log.error("初始化出错：", e);
		}
	}
	public Document getDocument(){
		return document;
	}
	
	public Element getElement(){
		return element;
	}
	
	public String addMulData(String nodeName, String[] subNodeNames, String[][] data){
		try {
			if(data.length != 0){
				Element ele = element.element(nodeName);
				if(data.length == 1){
					for(int i = 0; i < subNodeNames.length; i++){
						ele.element("List").element(subNodeNames[i]).setText(data[0][i]);
					}
				}else{
					for(int i = 0; i < subNodeNames.length; i++){
						ele.element("List").element(subNodeNames[i]).setText(data[0][i]);
					}
					for(int i = 1; i < data.length; i++){
						Element e = ele.addElement("List");
						for(int j = 0; j < subNodeNames.length; j++){
							e.addElement(subNodeNames[j]).setText(data[i][j]);
						}
					}
				}
			}
			return document.asXML();
		} catch (Exception e1) {
			return null;
		}
	}
	
	public String addData(String nodeName, String[] subNodeNames, String[] data){
		try{
			if (data.length > 0){
				Element ele = element.element(nodeName);
				for(int i = 0; i < subNodeNames.length; i++){
					ele.element(subNodeNames[i]).setText(data[i]);
				}
			}
			return	document.asXML();
		}catch (Exception e) {
			return null;
		}
	}
}