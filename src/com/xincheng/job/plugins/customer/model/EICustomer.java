package com.xincheng.job.plugins.customer.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.xincheng.ibatis.BaseEntity;

import jdbchelper.StatementMapper;

public class EICustomer extends BaseEntity {

	/* 保单号 */
	String chdrnum;
	/* 客户号 */
	String cownnum;
	/* CIF_ID */
	String cifid;
	/* 营销员工号 */
	String agntnum;
	/* 抽奖次数 */
	String count;

	public String getChdrnum() {
		return chdrnum;
	}

	public void setChdrnum(String chdrnum) {
		this.chdrnum = chdrnum;
	}

	public String getCownnum() {
		return cownnum;
	}

	public void setCownnum(String cownnum) {
		this.cownnum = cownnum;
	}

	public String getAgntnum() {
		return agntnum;
	}

	public void setAgntnum(String agntnum) {
		this.agntnum = agntnum;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getCifid() {
		return cifid;
	}

	public void setCifid(String cifid) {
		this.cifid = cifid;
	}

	public static StatementMapper<EICustomer> getMapper() {
		return new StatementMapper<EICustomer>() {
			public void mapStatement(PreparedStatement stmt, EICustomer info) throws SQLException {
				stmt.setString(1, info.getAgntnum());
				stmt.setString(2, info.getCownnum());
				stmt.setString(3, info.getChdrnum());
			}
		};
	}

}
