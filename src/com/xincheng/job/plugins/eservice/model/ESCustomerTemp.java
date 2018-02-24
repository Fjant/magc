package com.xincheng.job.plugins.eservice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import jdbchelper.BeanCreator;
import jdbchelper.StatementMapper;

import com.xincheng.ibatis.BaseEntity;
import com.xincheng.job.plugins.renewal.model.CifBiz;

public class ESCustomerTemp extends BaseEntity implements BeanCreator {
	private long id;
	private String cifId;
	private String userName;
	private String userType;
	private String sex;
	private Date birthday;
	private String mobile;
	private String telephone;
	private String email;
	private String openId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCifId() {
		return cifId;
	}

	public void setCifId(String cifId) {
		this.cifId = cifId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public static StatementMapper<ESCustomerTemp> getMapper() {
		return new StatementMapper<ESCustomerTemp>() {
			public void mapStatement(PreparedStatement stmt, ESCustomerTemp info) throws SQLException {
				stmt.setString(1, info.getCifId());
				stmt.setString(2, info.getUserName());
				stmt.setString(3, info.getUserType());
				stmt.setString(4, info.getSex());
				if (info.getBirthday() == null) {
					stmt.setTimestamp(5, null);
				} else {
					stmt.setTimestamp(5, new Timestamp(info.getBirthday().getTime()));
				}
				stmt.setString(6, info.getOpenId());
				stmt.setString(7, info.getMobile());
				stmt.setString(8, info.getTelephone());
				stmt.setString(9, info.getEmail());

			}
		};
	}

	@Override
	public Object createBean(ResultSet rs) throws SQLException {
		ESCustomerTemp customerTemp = new ESCustomerTemp();
		customerTemp.setCifId(rs.getString("CIF_ID"));
		customerTemp.setUserName(rs.getString("REALNAME"));
		customerTemp.setUserType(rs.getString("USERTYPE"));
		customerTemp.setSex(rs.getString("SEX"));
		customerTemp.setBirthday(rs.getDate("BIRTHDAY"));
		customerTemp.setOpenId(rs.getString("OPENID1"));
		customerTemp.setMobile(rs.getString("MOBILE"));
		customerTemp.setTelephone(rs.getString("TELEPHONE"));
		customerTemp.setEmail(rs.getString("EMAIL"));

		return customerTemp;
	}
}
