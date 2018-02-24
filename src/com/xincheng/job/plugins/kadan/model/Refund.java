package com.xincheng.job.plugins.kadan.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import jdbchelper.BeanCreator;

import com.xincheng.ibatis.BaseEntity;

public class Refund extends BaseEntity implements BeanCreator {
	private long id;
	private String agntNum;
	private String openId;
	private String customerNo;
	private String customerName;
	private long orderId;
	private long orderDetailId;
	private long billRefundId;
	private String productName;
	private String billNo;
	private int amount;
	private Date paymentTime;
	private int refundType;
	private int statusId;
	private Date createTime;
	private Date updateTime;

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

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(long orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public long getBillRefundId() {
		return billRefundId;
	}

	public void setBillRefundId(long billRefundId) {
		this.billRefundId = billRefundId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Date getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}

	public int getRefundType() {
		return refundType;
	}

	public void setRefundType(int refundType) {
		this.refundType = refundType;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public Object createBean(ResultSet rs) throws SQLException {
		Refund refund = new Refund();
	
		refund.setId(rs.getLong("ID"));
		refund.setAgntNum(rs.getString("AGNT_NUM"));
		refund.setOpenId(rs.getString("OPEN_ID"));
		refund.setCustomerNo(rs.getString("REFUND_ID"));
		refund.setCustomerName(rs.getString("CUSTOMER_NAME"));
		refund.setOrderId(rs.getLong("ORDER_ID"));
		refund.setOrderDetailId(rs.getLong("ORDER_DETAIL_ID"));
		refund.setBillRefundId(rs.getLong("BILL_REFUND_ID"));
		refund.setProductName(rs.getString("PRODUCT_NAME"));
		refund.setBillNo(rs.getString("BILL_MO"));
		refund.setAmount(rs.getInt("AMOUNT"));
		refund.setPaymentTime(rs.getTimestamp("PAYMENT_TIME"));
		refund.setRefundType(rs.getInt("REFUND_TYPE"));
		refund.setStatusId(rs.getInt("STATUS_ID"));
		refund.setCreateTime(rs.getTimestamp("CREATE_TIME"));
		refund.setUpdateTime(rs.getTimestamp("UPDATE_TIME"));

		return refund;
	}
}
