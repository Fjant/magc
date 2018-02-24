package com.xincheng.msg.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xincheng.msg.dao.MPMsgDao;
import com.xincheng.msg.model.MPMsg;
import com.xincheng.msg.service.MPMsgService;

@Service
public class MPMsgServiceImpl implements MPMsgService {

	@Autowired
	private MPMsgDao serviceMsgDao;

	@Override
	public void insert(MPMsg entity) throws Exception {
		serviceMsgDao.insert(entity);
	}

	@Override
	public void insertBatch(List<MPMsg> entities) throws Exception {
		serviceMsgDao.insert(entities);
	}

	@Override
	public MPMsg getById(long id) throws Exception {
		return serviceMsgDao.get(id);
	}

	@Override
	public void update(MPMsg entity) throws Exception {
		serviceMsgDao.update(entity);
	}

	@Override
	public List<MPMsg> getList(Map<String, Object> filters)
			throws Exception {
		return serviceMsgDao.getList(filters);
	}

	@Override
	public List<MPMsg> getByPage(Map<String, Object> filters)
			throws Exception {
		return serviceMsgDao.getByPage(filters);
	}

	@Override
	public int count(Map<String, Object> filters) throws Exception {

		return serviceMsgDao.count(filters);
	}

	@Override
	public void removeById(long id) throws Exception {
		
		serviceMsgDao.removeById(id);
		
	}

	@Override
	public List<MPMsg> pageQuery(Map<String, Object> filters) throws Exception {
		// TODO Auto-generated method stub
		return serviceMsgDao.pageQuery(filters);
	}

}
