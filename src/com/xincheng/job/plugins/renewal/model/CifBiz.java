package com.xincheng.job.plugins.renewal.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbchelper.BeanCreator;
import jdbchelper.StatementMapper;

public class CifBiz implements BeanCreator {
	private String clientNum;
	private String relcltNum;

	public String getClientNum() {
		return clientNum;
	}

	public void setClientNum(String clientNum) {
		this.clientNum = clientNum;
	}

	public String getRelcltNum() {
		return relcltNum;
	}

	public void setRelcltNum(String relcltNum) {
		this.relcltNum = relcltNum;
	}

	@Override
	public Object createBean(ResultSet rs) throws SQLException {
		CifBiz biz = new CifBiz();
		biz.setClientNum(rs.getString("CLIENTNUM"));
		biz.setRelcltNum(rs.getString("RELCLTNUM"));

		return biz;
	}

	public static StatementMapper<CifBiz> getMapper() {
		return new StatementMapper<CifBiz>() {
			public void mapStatement(PreparedStatement stmt, CifBiz info) throws SQLException {
				stmt.setString(1, info.getClientNum());
				stmt.setString(2, info.getRelcltNum());
			}
		};
	}
}
