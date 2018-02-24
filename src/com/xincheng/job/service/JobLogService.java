package com.xincheng.job.service;

import java.util.List;
import java.util.Map;

import com.xincheng.job.model.JobLog;

public interface JobLogService {

	public void save(JobLog entity) throws Exception;

	public JobLog getById(long id) throws Exception;

	public void update(JobLog entity) throws Exception;

	public List<JobLog> getAll() throws Exception;
	
	public List<JobLog> getByPage(Map<String, Object> filters) throws Exception;
	
	public int count(Map<String, Object> filters) throws Exception;
}
