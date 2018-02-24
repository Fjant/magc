package com.xincheng.msg.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xincheng.ibatis.MyBatisEntityDao;
import com.xincheng.msg.model.EntMsg;
import com.xincheng.msg.model.MPMsg;

@Repository
public class EntMsgDao extends MyBatisEntityDao<EntMsg, Long> {

	public void insertBatch(List<EntMsg> entities) throws Exception {
		getSqlSessionTemplate().insertBatch("EntMsg.insert_batch", entities);
	}

	public List<EntMsg> getList(Map<String, Object> filters) throws Exception {
		return this.getSqlSessionTemplate().selectList("EntMsg.getList", filters);
	}

	public List<EntMsg> getByPage(Map<String, Object> filters) throws Exception {
		return this.getSqlSessionTemplate().selectList("EntMsg.getByPage", filters);
	}

	public int count(Map<String, Object> filters) throws Exception {
		return (Integer) this.getSqlSessionTemplate().selectOne("EntMsg.count", filters);
	}
}
