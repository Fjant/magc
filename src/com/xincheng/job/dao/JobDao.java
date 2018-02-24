package com.xincheng.job.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xincheng.ibatis.MyBatisEntityDao;
import com.xincheng.job.model.JobEntity;

@Repository
public class JobDao extends MyBatisEntityDao<JobEntity, Long> {
		
	public List<JobEntity> getList(Map<String, Object> filters) throws Exception{
		return this.getSqlSessionTemplate().selectList("JobEntity.getList", filters);
	}
	
	public List<JobEntity> getByPage(Map<String, Object> filters) throws Exception {
		return this.getSqlSessionTemplate().selectList("JobEntity.getByPage", filters);
	}

	public int count(Map<String, Object> filters) throws Exception {
		return (Integer) this.getSqlSessionTemplate().selectOne("JobEntity.count", filters);
	}
	
	public void startJob(long id) throws Exception {
		getSqlSessionTemplate().update("JobEntity.startJob", id);
	}
	
	public void stopJob(long id) throws Exception {
		getSqlSessionTemplate().update("JobEntity.stopJob", id);
	}
	
	public int selectCount(Map<String, Object> filters) throws Exception {
		return (Integer) this.getSqlSessionTemplate().selectOne("JobEntity.selectCount", filters);
	}
	
}
