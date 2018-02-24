package com.xincheng.msg.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xincheng.msg.dao.SumMsgDao;
import com.xincheng.msg.model.SumMsg;
import com.xincheng.msg.service.SumMsgService;

@Service
public class SumMsgServiceImpl implements SumMsgService{
	
	@Autowired
	private SumMsgDao sumMsgDao;

	@Override
	public SumMsg selectEntMsgRate(Map<String, Object> filters)
			throws Exception {
		// TODO Auto-generated method stub
		return sumMsgDao.selectEntMsgRate(filters);
	}

	@Override
	public SumMsg selectMPMsgRate(Map<String, Object> filters) throws Exception {
		// TODO Auto-generated method stub
		return sumMsgDao.selectMPMsgRate(filters);
	}

}
