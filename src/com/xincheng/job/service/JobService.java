package com.xincheng.job.service;

import java.util.List;
import java.util.Map;

import com.xincheng.job.model.JobEntity;

public interface JobService {

	public void save(JobEntity entity) throws Exception;

	public JobEntity getById(long id) throws Exception;

	public void update(JobEntity entity) throws Exception;

	public List<JobEntity> getAll() throws Exception;
	
	public List<JobEntity> getByPage(Map<String, Object> filters) throws Exception;
	
	public int count(Map<String, Object> filters) throws Exception;
	
	public void startJob(long id) throws Exception;
	
	public void stopJob(long id) throws Exception;
	
	public void removeById(long id) throws Exception;
	
	public int selectCount(Map<String, Object> filters) throws Exception;
}
