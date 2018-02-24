package com.xincheng.job.plugins.renewal.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbchelper.BeanCreator;
import jdbchelper.StatementMapper;

public class IdentityBiz implements BeanCreator {
	private String clientNum;
	private String secuityNo;

	public String getClientNum() {
		return clientNum;
	}

	public void setClientNum(String clientNum) {
		this.clientNum = clientNum;
	}

	public String getSecuityNo() {
		return secuityNo;
	}

	public void setSecuityNo(String secuityNo) {
		this.secuityNo = secuityNo;
	}

	@Override
	public Object createBean(ResultSet rs) throws SQLException {
		IdentityBiz biz = new IdentityBiz();
		biz.setClientNum(rs.getString("CLNTNUM"));
		biz.setSecuityNo(rs.getString("SECUITYNO"));
		return biz;
	}

	public static StatementMapper<IdentityBiz> getMapper() {
		return new StatementMapper<IdentityBiz>() {
			public void mapStatement(PreparedStatement stmt, IdentityBiz info) throws SQLException {
				stmt.setString(1, info.getSecuityNo());
				stmt.setString(2, info.getClientNum());
			}
		};
	}
}
