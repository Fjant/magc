package com.xincheng.monitor.model;

import java.util.Date;

import com.xincheng.ibatis.BaseEntity;

public class FunctionEntity extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String bizId = "";
	private String agntNum = "";
	private String funcInterface = "";
	private String remark = "";
	private Date startTime;
	private Date endTime;
	private long consumeMillTime;
	private String fromServer = "";
	private int result = 0;
	private Date createTime;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the bizId
	 */
	public String getBizId() {
		return bizId;
	}

	/**
	 * @param bizId
	 *            the bizId to set
	 */
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	/**
	 * @return the agntNum
	 */
	public String getAgntNum() {
		return agntNum;
	}

	/**
	 * @param agntNum
	 *            the agntNum to set
	 */
	public void setAgntNum(String agntNum) {
		this.agntNum = agntNum;
	}

	/**
	 * @return the funcInterface
	 */
	public String getFuncInterface() {
		return funcInterface;
	}

	/**
	 * @param funcInterface
	 *            the funcInterface to set
	 */
	public void setFuncInterface(String funcInterface) {
		this.funcInterface = funcInterface;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the consumeMillTime
	 */
	public long getConsumeMillTime() {
		return consumeMillTime;
	}

	/**
	 * @param consumeMillTime
	 *            the consumeMillTime to set
	 */
	public void setConsumeMillTime(long consumeMillTime) {
		this.consumeMillTime = consumeMillTime;
	}

	/**
	 * @return the fromServer
	 */
	public String getFromServer() {
		return fromServer;
	}

	/**
	 * @param fromServer
	 *            the fromServer to set
	 */
	public void setFromServer(String fromServer) {
		this.fromServer = fromServer;
	}

	/**
	 * @return the result
	 */
	public int getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(int result) {
		this.result = result;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
