package com.xpay.pay.rest.contract;

public class RechargeRequest {
	private Float amount;
	private Integer quota;

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public Integer getQuota() {
		return quota;
	}

	public void setQuota(Integer quota) {
		this.quota = quota;
	}
}
