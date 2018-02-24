package com.xincheng.webservice.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import com.xincheng.webservice.enums.ServiceCode;

@XmlRootElement
public class BaseModel<T> implements Serializable {
	public BaseModel() {
	}

	public BaseModel(Integer code) {
		this.code = code;
	}

	public BaseModel(Integer code, Map<String, T> data) {
		this.code = code;
		this.data = data;
	}

	public BaseModel(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public BaseModel(Integer code, Map<String, T> data, String message) {
		this.code = code;
		this.data = data;
		this.message = message;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer code = ServiceCode.Normal.getCode();
	private Long serial;
	private Map<String, T> data;
	private String message;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Long getSerial() {
		serial = (new Date()).getTime();
		return serial;
	}

	public Map<String, T> getData() {
		return data == null ? new HashMap<String, T>() : data;
	}

	public void setData(Map<String, T> data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
