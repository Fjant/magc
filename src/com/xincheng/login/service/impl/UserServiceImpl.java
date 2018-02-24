package com.xincheng.login.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.xincheng.login.dao.UserDao;
import com.xincheng.login.model.User;
import com.xincheng.login.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
     private UserDao userDao;
		
	@Override
	public User findUserByLogin(String userName) {
		// TODO Auto-generated method stub
		try{
		    Map<String,Object> map = new HashMap<String,Object>();
		    map.put("userName", userName);
		    return this.userDao.findUserByLogin(map);
		}catch(Exception e){
			logger.error("sdfs", e);
		}
		return null;
	}
	
}
