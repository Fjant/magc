package com.xincheng.msg.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xincheng.ibatis.MyBatisEntityDao;
import com.xincheng.msg.model.SumMsg;

@Repository
public class SumMsgDao extends MyBatisEntityDao<SumMsg, Long>{
	
	public SumMsg selectEntMsgRate(Map<String, Object> filters) throws Exception {
		return (SumMsg) this.getSqlSessionTemplate().selectOne("SumMsg.entMsgRate", filters);
	}
	
	public SumMsg selectMPMsgRate(Map<String, Object> filters) throws Exception {
		return (SumMsg) this.getSqlSessionTemplate().selectOne("SumMsg.mPMsgRate", filters);
	}
}
