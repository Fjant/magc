package com.xincheng.job.service;

import java.util.List;
import java.util.Map;

import com.xincheng.job.model.JobIncrement;

public interface JobIncrementService {

	public void save(JobIncrement entity) throws Exception;

	public JobIncrement getById(long id) throws Exception;

	public void update(JobIncrement entity) throws Exception;

	public List<JobIncrement> getAll() throws Exception;
	
	public List<JobIncrement> getByPage(Map<String, Object> filters) throws Exception;
	
	public int count(Map<String, Object> filters) throws Exception;

	public JobIncrement getByTypeId(int typeId) throws Exception;
}
