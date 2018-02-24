package com.xincheng.job.plugins.amactivity.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import jdbchelper.StatementMapper;

public class SnActivityAgnt {
	private long id;
	private String agntNum;
	private long recordId;
	private long needId;
	private String ruleName;
	private long signTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAgntNum() {
		return agntNum;
	}

	public void setAgntNum(String agntNum) {
		this.agntNum = agntNum;
	}

	public long getRecordId() {
		return recordId;
	}

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	public long getNeedId() {
		return needId;
	}

	public void setNeedId(long needId) {
		this.needId = needId;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public long getSignTime() {
		return signTime;
	}

	public void setSignTime(long signTime) {
		this.signTime = signTime;
	}

	public static StatementMapper<SnActivityAgnt> getMapper() {
		return new StatementMapper<SnActivityAgnt>() {
			public void mapStatement(PreparedStatement stmt, SnActivityAgnt info) throws SQLException {
				stmt.setLong(1, info.getId());
				stmt.setString(2, info.getAgntNum());
				stmt.setLong(3, info.getRecordId());
				stmt.setLong(4, info.getNeedId());
				stmt.setString(5, info.getRuleName());
				stmt.setTimestamp(6, new Timestamp(info.getSignTime()));
			}
		};
	}
}
