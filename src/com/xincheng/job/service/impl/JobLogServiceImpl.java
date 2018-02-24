package com.xincheng.job.service.impl;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xincheng.job.dao.JobDao;
import com.xincheng.job.dao.JobLogDao;
import com.xincheng.job.model.JobLog;
import com.xincheng.job.service.JobLogService;
import com.xincheng.job.service.JobService;
import com.xincheng.utils.SpringUtils;

@Service
public class JobLogServiceImpl implements JobLogService {

	@Autowired
	private JobLogDao jogLogDao;

	@Override
	public void save(JobLog entity) throws Exception {
		jogLogDao.insert(entity);
	}

	@Override
	public JobLog getById(long id) throws Exception {
		return jogLogDao.get(id);
	}

	@Override
	public void update(JobLog entity) throws Exception {
		jogLogDao.update(entity);
	}

	@Override
	public List<JobLog> getAll() throws Exception {
		return jogLogDao.getAll();
	}

	@Override
	public List<JobLog> getByPage(Map<String, Object> filters) throws Exception {
		return jogLogDao.getByPage(filters);
	}

	@Override
	public int count(Map<String, Object> filters) throws Exception {
		return jogLogDao.count(filters);
	}
}
