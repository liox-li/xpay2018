package com.xpay.pay.rest.contract;

import com.xpay.pay.proxy.IPaymentProxy.PayChannel;

public class RechargeRequest {
	private Float amount;
	private PayChannel channel;
	private Integer quota;

	public Float getAmount() {
		return amount;
	}

	public PayChannel getChannel() {
		return channel;
	}

	public void setChannel(PayChannel channel) {
		this.channel = channel;
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
