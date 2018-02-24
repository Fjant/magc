package com.xincheng.msg.model;

import com.xincheng.ibatis.BaseEntity;

public class SumMsg extends BaseEntity{
	
	/**
	 * 总的消息条数
	 */
	public long totalNum;
	
	/**
	 * 成功数量
	 */
    public long successNum;
    
    /**
     * 失败数量
     */
    public long failNum;
    
    /**
     * 待发送
     */
    public long armeNum;
    
    /**
     * 成功率
     */
    public String successRate;
    
    /**
     * 失败率
     */
    public String failRate;

	public long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(long totalNum) {
		this.totalNum = totalNum;
	}

	public long getSuccessNum() {
		return successNum;
	}

	public void setSuccessNum(long successNum) {
		this.successNum = successNum;
	}

	public long getFailNum() {
		return failNum;
	}

	public void setFailNum(long failNum) {
		this.failNum = failNum;
	}

	public long getArmeNum() {
		return armeNum;
	}

	public void setArmeNum(long armeNum) {
		this.armeNum = armeNum;
	}

	public String getSuccessRate() {
		return successRate;
	}

	public void setSuccessRate(String successRate) {
		this.successRate = successRate;
	}

	public String getFailRate() {
		return failRate;
	}

	public void setFailRate(String failRate) {
		this.failRate = failRate;
	}
}
