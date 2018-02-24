package com.xincheng.msg.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xincheng.ibatis.MyBatisEntityDao;
import com.xincheng.msg.model.MPMsg;

@Repository
public class MPMsgDao extends MyBatisEntityDao<MPMsg, Long> {
		
	public List<MPMsg> getList(Map<String, Object> filters) throws Exception{
		return this.getSqlSessionTemplate().selectList("MPMsg.getList", filters);
	}
	
	public List<MPMsg> getByPage(Map<String, Object> filters) throws Exception {
		return this.getSqlSessionTemplate().selectList("MPMsg.getByPage", filters);
	}
	public List<MPMsg> pageQuery(Map<String, Object> filters) throws Exception {
		return this.getSqlSessionTemplate().selectList("MPMsg.pageQuery", filters);
	}

	public int count(Map<String, Object> filters) throws Exception {
		return (Integer) this.getSqlSessionTemplate().selectOne("MPMsg.count", filters);
	}
	
	public void deleteById(Long id) throws Exception{
		getSqlSessionTemplate().delete("MPMsg.deleteByPrimaryKey", id);
	}
}
