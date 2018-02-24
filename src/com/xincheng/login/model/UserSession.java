package com.xincheng.login.model;

import com.xincheng.ibatis.BaseEntity;


public class UserSession extends BaseEntity {
  private static final long serialVersionUID = 1L;
  private Long id;
  private String agntNum;
  private String fullName;
  private String sex;
  private String headImg;
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getAgntNum() {
	return agntNum;
}
public void setAgntNum(String agntNum) {
	this.agntNum = agntNum;
}
public String getFullName() {
	return fullName;
}
public void setFullName(String fullName) {
	this.fullName = fullName;
}
public String getSex() {
	return sex;
}
public void setSex(String sex) {
	this.sex = sex;
}
public String getHeadImg() {
	return headImg;
}
public void setHeadImg(String headImg) {
	this.headImg = headImg;
}




}
