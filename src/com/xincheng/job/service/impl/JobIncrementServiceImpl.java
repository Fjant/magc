package com.xincheng.job.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xincheng.job.dao.JobIncrementDao;
import com.xincheng.job.model.JobIncrement;
import com.xincheng.job.service.JobIncrementService;

@Service
public class JobIncrementServiceImpl implements JobIncrementService {

	@Autowired
	private JobIncrementDao jobIncrementDao;

	@Override
	public void save(JobIncrement entity) throws Exception {
		jobIncrementDao.insert(entity);
	}

	@Override
	public JobIncrement getById(long id) throws Exception {
		return jobIncrementDao.get(id);
	}

	@Override
	public void update(JobIncrement entity) throws Exception {
		jobIncrementDao.update(entity);
	}

	@Override
	public List<JobIncrement> getAll() throws Exception {
		return jobIncrementDao.getAll();
	}

	@Override
	public List<JobIncrement> getByPage(Map<String, Object> filters) throws Exception {
		return jobIncrementDao.getByPage(filters);
	}

	@Override
	public int count(Map<String, Object> filters) throws Exception {
		return jobIncrementDao.count(filters);
	}

	@Override
	public JobIncrement getByTypeId(int typeId) throws Exception {
		return jobIncrementDao.getByTypeId(typeId);
	}
}
