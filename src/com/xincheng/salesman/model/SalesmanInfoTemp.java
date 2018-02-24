package com.xincheng.salesman.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import jdbchelper.StatementMapper;

import com.xincheng.ibatis.BaseEntity;

public class SalesmanInfoTemp extends BaseEntity {

	private static final long serialVersionUID = 3997902704241317353L;

	private Long id;
	private String agntNum;
	private String fullName;
	private String sex;
	private String mobilephone;
	private String telephone;
	private String wechatOpenid;
	private String email;
	private String dutydeg;
	private Date entryTime;
	private Date quitTime;
	private Long selfLevel;
	private int statusId;
	private String companyId;
	private String branchId;
	private String tsalesUnt;
	private String cso;
	private String sso;
	private String parentAgntNum;
	private String parentAgntNum2;
	private String agntNumLevel1;
	private String agntNumLevel2;
	private String agntNumLevel3;
	private String agntNumLevel4;
	private String agntNumLevel5;
	private String agntNumLevel6;
	private String agntNumLevel7;
	private String agntNumLevel8;
	private String agntNumLevel9;
	private String agntNumLevel10;
	private String abossnum;
	private String abossrlts;
	private String bbossnum;
	private String bbossrlts;
	private String cbossnum;
	private String cbossrlts;
	private String dbossnum;
	private String dbossrlts;

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

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getWechatOpenid() {
		return wechatOpenid;
	}

	public void setWechatOpenid(String wechatOpenid) {
		this.wechatOpenid = wechatOpenid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDutydeg() {
		return dutydeg;
	}

	public void setDutydeg(String dutydeg) {
		this.dutydeg = dutydeg;
	}

	public Date getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}

	public Date getQuitTime() {
		return quitTime;
	}

	public void setQuitTime(Date quitTime) {
		this.quitTime = quitTime;
	}

	public Long getSelfLevel() {
		return selfLevel;
	}

	public void setSelfLevel(Long selfLevel) {
		this.selfLevel = selfLevel;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getTsalesUnt() {
		return tsalesUnt;
	}

	public void setTsalesUnt(String tsalesUnt) {
		this.tsalesUnt = tsalesUnt;
	}

	public String getCso() {
		return cso;
	}

	public void setCso(String cso) {
		this.cso = cso;
	}

	public String getSso() {
		return sso;
	}

	public void setSso(String sso) {
		this.sso = sso;
	}

	public String getParentAgntNum() {
		return parentAgntNum;
	}

	public void setParentAgntNum(String parentAgntNum) {
		this.parentAgntNum = parentAgntNum;
	}

	public String getParentAgntNum2() {
		return parentAgntNum2;
	}

	public void setParentAgntNum2(String parentAgntNum2) {
		this.parentAgntNum2 = parentAgntNum2;
	}

	public String getAgntNumLevel1() {
		return agntNumLevel1;
	}

	public void setAgntNumLevel1(String agntNumLevel1) {
		this.agntNumLevel1 = agntNumLevel1;
	}

	public String getAgntNumLevel2() {
		return agntNumLevel2;
	}

	public void setAgntNumLevel2(String agntNumLevel2) {
		this.agntNumLevel2 = agntNumLevel2;
	}

	public String getAgntNumLevel3() {
		return agntNumLevel3;
	}

	public void setAgntNumLevel3(String agntNumLevel3) {
		this.agntNumLevel3 = agntNumLevel3;
	}

	public String getAgntNumLevel4() {
		return agntNumLevel4;
	}

	public void setAgntNumLevel4(String agntNumLevel4) {
		this.agntNumLevel4 = agntNumLevel4;
	}

	public String getAgntNumLevel5() {
		return agntNumLevel5;
	}

	public void setAgntNumLevel5(String agntNumLevel5) {
		this.agntNumLevel5 = agntNumLevel5;
	}

	public String getAgntNumLevel6() {
		return agntNumLevel6;
	}

	public void setAgntNumLevel6(String agntNumLevel6) {
		this.agntNumLevel6 = agntNumLevel6;
	}

	public String getAgntNumLevel7() {
		return agntNumLevel7;
	}

	public void setAgntNumLevel7(String agntNumLevel7) {
		this.agntNumLevel7 = agntNumLevel7;
	}

	public String getAgntNumLevel8() {
		return agntNumLevel8;
	}

	public void setAgntNumLevel8(String agntNumLevel8) {
		this.agntNumLevel8 = agntNumLevel8;
	}

	public String getAgntNumLevel9() {
		return agntNumLevel9;
	}

	public void setAgntNumLevel9(String agntNumLevel9) {
		this.agntNumLevel9 = agntNumLevel9;
	}

	public String getAgntNumLevel10() {
		return agntNumLevel10;
	}

	public void setAgntNumLevel10(String agntNumLevel10) {
		this.agntNumLevel10 = agntNumLevel10;
	}

	public String getAbossnum() {
		return abossnum;
	}

	public void setAbossnum(String abossnum) {
		this.abossnum = abossnum;
	}

	public String getAbossrlts() {
		return abossrlts;
	}

	public void setAbossrlts(String abossrlts) {
		this.abossrlts = abossrlts;
	}

	public String getBbossnum() {
		return bbossnum;
	}

	public void setBbossnum(String bbossnum) {
		this.bbossnum = bbossnum;
	}

	public String getBbossrlts() {
		return bbossrlts;
	}

	public void setBbossrlts(String bbossrlts) {
		this.bbossrlts = bbossrlts;
	}

	public String getCbossnum() {
		return cbossnum;
	}

	public void setCbossnum(String cbossnum) {
		this.cbossnum = cbossnum;
	}

	public String getCbossrlts() {
		return cbossrlts;
	}

	public void setCbossrlts(String cbossrlts) {
		this.cbossrlts = cbossrlts;
	}

	public String getDbossnum() {
		return dbossnum;
	}

	public void setDbossnum(String dbossnum) {
		this.dbossnum = dbossnum;
	}

	public String getDbossrlts() {
		return dbossrlts;
	}

	public void setDbossrlts(String dbossrlts) {
		this.dbossrlts = dbossrlts;
	}

	public static StatementMapper<SalesmanInfoTemp> getMapper() {
		return new StatementMapper<SalesmanInfoTemp>() {
			public void mapStatement(PreparedStatement stmt, SalesmanInfoTemp info) throws SQLException {
				stmt.setString(1, info.getAgntNum());
				stmt.setString(2, info.getFullName());
				stmt.setString(3, info.getSex());
				stmt.setString(4, info.getMobilephone());
				stmt.setString(5, info.getTelephone());
				stmt.setString(6, info.getEmail());
				stmt.setString(7, info.getCompanyId());
				stmt.setString(8, info.getBranchId());
				stmt.setString(9, info.getTsalesUnt());
				stmt.setString(10, info.getCso());
				stmt.setString(11, info.getSso());
				stmt.setString(12, info.getDutydeg());

				if (info.getEntryTime() == null) {
					stmt.setTimestamp(13, null);
				} else {
					stmt.setTimestamp(13, new java.sql.Timestamp(info.getEntryTime().getTime()));
				}

				if (info.getQuitTime() == null) {
					stmt.setTimestamp(14, null);
				} else {
					stmt.setTimestamp(14, new java.sql.Timestamp(info.getQuitTime().getTime()));
				}
				stmt.setString(15, info.getParentAgntNum());
				stmt.setString(16, info.getParentAgntNum2());
				stmt.setString(17, info.getAgntNumLevel1());
				stmt.setString(18, info.getAgntNumLevel2());
				stmt.setString(19, info.getAgntNumLevel3());
				stmt.setString(20, info.getAgntNumLevel4());
				stmt.setString(21, info.getAbossnum());
				stmt.setString(22, info.getAbossrlts());
				stmt.setString(23, info.getBbossnum());
				stmt.setString(24, info.getBbossrlts());
				stmt.setString(25, info.getCbossnum());
				stmt.setString(26, info.getCbossrlts());
				stmt.setString(27, info.getDbossnum());
				stmt.setString(28, info.getDbossrlts());
				stmt.setInt(29, info.getStatusId());
			}
		};
	}
}
