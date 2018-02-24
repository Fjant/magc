package com.xincheng.customer.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xincheng.customer.model.CustomerMsg;
import com.xincheng.ibatis.MyBatisEntityDao;

@Repository
public class CustomerMsgDao extends MyBatisEntityDao<CustomerMsg, Long>{
	
	public List<CustomerMsg> getByPage(Map<String, Object> filters) throws Exception {
		return this.getSqlSessionTemplate().selectList("CustomerMsg.getByPage", filters);
	}

	public int count(Map<String, Object> filters) throws Exception {
		return (Integer) this.getSqlSessionTemplate().selectOne("CustomerMsg.count", filters);
	}

}
