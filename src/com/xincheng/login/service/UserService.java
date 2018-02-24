package com.xincheng.login.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xincheng.ibatis.EntityDao;
import com.xincheng.login.model.User;

public interface UserService {
	  Logger logger = LoggerFactory.getLogger(UserService.class);
	  public User findUserByLogin(String userName) ;

}
