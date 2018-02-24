package com.xincheng.monitor.dao;

import org.springframework.stereotype.Repository;

import com.xincheng.ibatis.MyBatisEntityDao;
import com.xincheng.monitor.model.FunctionEntity;

@Repository
public class FunctionDao extends MyBatisEntityDao<FunctionEntity, Long> {

}
