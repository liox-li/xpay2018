package com.xpay.pay.rest.contract;

import com.xpay.pay.model.StoreTransaction.TransactionType;
import com.xpay.pay.proxy.IPaymentProxy.PayChannel;

public class RechargeRequest {
	private Float amount;
	private PayChannel channel;
	private Integer quota;
	private TransactionType transactionType;

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

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}
}
