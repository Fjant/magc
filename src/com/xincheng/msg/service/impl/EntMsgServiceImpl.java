package com.xincheng.msg.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xincheng.msg.dao.EntMsgDao;
import com.xincheng.msg.model.EntMsg;
import com.xincheng.msg.service.EntMsgService;

@Service
public class EntMsgServiceImpl implements EntMsgService {

	@Autowired
	private EntMsgDao entMsgDao;

	@Override
	public void insert(EntMsg entity) throws Exception {
		entMsgDao.insert(entity);
	}

	@Override
	public void insertBatch(List<EntMsg> entities) throws Exception {
		entMsgDao.insertBatch(entities);
	}

	@Override
	public EntMsg getById(long id) throws Exception {
		return entMsgDao.get(id);
	}

	@Override
	public void update(EntMsg entity) throws Exception {
		entMsgDao.update(entity);
	}

	@Override
	public List<EntMsg> getList(Map<String, Object> filters)
			throws Exception {
		return entMsgDao.getList(filters);
	}

	@Override
	public List<EntMsg> getByPage(Map<String, Object> filters)
			throws Exception {
		return entMsgDao.getByPage(filters);
	}

	@Override
	public int count(Map<String, Object> filters) throws Exception {

		return entMsgDao.count(filters);
	}

	@Override
	public void removeById(long id) throws Exception{
		entMsgDao.removeById(id);
		
	}

}
