package com.xincheng.msg.service;

import java.util.Map;

import com.xincheng.msg.model.SumMsg;

public interface SumMsgService {
	
	public SumMsg selectEntMsgRate(Map<String, Object> filters) throws Exception;
	
	public SumMsg selectMPMsgRate(Map<String, Object> filters) throws Exception;
}
