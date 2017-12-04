package com.xpay.pay.model;

import java.util.Date;

import com.xpay.pay.proxy.PaymentResponse.OrderStatus;

public class StoreTransaction {
	private Long id;
	private Long storeId;
	private String orderNo;
	private TransactionType operation;
	private Long agentId;
	private Float amount;
	private Float quota;
	private Float bailPercentage;
	private OrderStatus status;
	private Date createDate;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public TransactionType getOperation() {
		return operation;
	}

	public void setOperation(TransactionType operation) {
		this.operation = operation;
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public Float getQuota() {
		return quota;
	}

	public void setQuota(Float quota) {
		this.quota = quota;
	}

	public Float getBailPercentage() {
		return bailPercentage;
	}

	public void setBailPercentage(Float bailPercentage) {
		this.bailPercentage = bailPercentage;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public static enum TransactionType {
		INIT_FREE, RECHARGE, CONSUME, FREE, OFFLINE; 
	}
}
