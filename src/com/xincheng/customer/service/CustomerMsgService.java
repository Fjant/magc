package com.xincheng.customer.service;

import java.util.List;
import java.util.Map;

import com.xincheng.customer.model.CustomerMsg;

public interface CustomerMsgService {
	
	public List<CustomerMsg> getByPage(Map<String, Object> filters) throws Exception;
	
	public int count(Map<String, Object> filters) throws Exception;

}
