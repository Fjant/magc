package com.xincheng.job.plugins.cif.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import jdbchelper.StatementMapper;

import com.xincheng.ibatis.BaseEntity;

public class CifClientRelTemp extends BaseEntity {
	private long id;
	private String clientNum;
	private String relCltNum;
	private String relSys;
	private Date crtDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getClientNum() {
		return clientNum;
	}

	public void setClientNum(String clientNum) {
		this.clientNum = clientNum;
	}

	public String getRelCltNum() {
		return relCltNum;
	}

	public void setRelCltNum(String relCltNum) {
		this.relCltNum = relCltNum;
	}

	public String getRelSys() {
		return relSys;
	}

	public void setRelSys(String relSys) {
		this.relSys = relSys;
	}

	public Date getCrtDate() {
		return crtDate;
	}

	public void setCrtDate(Date crtDate) {
		this.crtDate = crtDate;
	}

	public static StatementMapper<CifClientRelTemp> getMapper() {
		return new StatementMapper<CifClientRelTemp>() {
			public void mapStatement(PreparedStatement stmt, CifClientRelTemp info) throws SQLException {
				stmt.setString(1, info.getClientNum());
				stmt.setString(2, info.getRelCltNum());
				stmt.setString(3, info.getRelSys());
				if (info.getCrtDate() == null) {
					stmt.setTimestamp(4, null);
				} else {
					stmt.setTimestamp(4, new java.sql.Timestamp(info.getCrtDate().getTime()));
				}
			}
		};
	}
}
