package com.xincheng.job.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xincheng.ibatis.MyBatisEntityDao;
import com.xincheng.job.model.JobLog;
@Repository
public class JobLogDao extends MyBatisEntityDao<JobLog, Long> {
	public List<JobLog> getByPage(Map<String, Object> filters) throws Exception {
		return this.getSqlSessionTemplate().selectList("JobLog.getByPage", filters);
	}

	public int count(Map<String, Object> filters) throws Exception {
		return (Integer) this.getSqlSessionTemplate().selectOne("JobLog.count", filters);
	}
}
