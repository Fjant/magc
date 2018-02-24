package com.xincheng.customer.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xincheng.customer.dao.CustomerMsgDao;
import com.xincheng.customer.model.CustomerMsg;
import com.xincheng.customer.service.CustomerMsgService;

@Service
public class CustomerMsgServiceImpl implements CustomerMsgService {
	
	@Autowired
	private CustomerMsgDao customerMsgDao;

	@Override
	public List<CustomerMsg> getByPage(Map<String, Object> filters)
			throws Exception {
		// TODO Auto-generated method stub
		return customerMsgDao.getByPage(filters);
	}

	@Override
	public int count(Map<String, Object> filters) throws Exception {
		// TODO Auto-generated method stub
		return customerMsgDao.count(filters);
	}

}
