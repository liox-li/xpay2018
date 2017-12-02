package com.xpay.pay.model;

public class StoreTransaction {
	private Long id;
	private Long storeId;
	private TransactionType operation;
	private Long agentId;
	private Float amount;
	
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

	public static enum TransactionType {
		INIT_FREE, RECHARGE, CONSUME, PROMOTE; 
	}
}
