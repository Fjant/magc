package com.xincheng.monitor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xincheng.monitor.dao.FunctionDao;
import com.xincheng.monitor.model.FunctionEntity;
import com.xincheng.monitor.service.FunctionService;

@Service
public class FunctionServiceImpl implements FunctionService {
	@Autowired
	private FunctionDao accessDao;

	@Override
	public void save(FunctionEntity entity) {
		try {
			accessDao.insert(entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
