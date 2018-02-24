package com.xincheng.login.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xincheng.ibatis.MyBatisEntityDao;
import com.xincheng.login.model.User;
@Repository
public class UserDao  extends MyBatisEntityDao<User, Long>{
	
	public User findUserByLogin(Map<String,Object> filter) throws Exception {
		return (User) getSqlSessionTemplate().selectOne("User.select", filter);
	}

}
