package com.xpay.pay.rest.contract;

import com.xpay.pay.model.StoreChannel.ChinaUmsProps;
import com.xpay.pay.model.StoreChannel.PaymentGateway;

public class QuickCreateStoreRequest {
	private String name;
	private Float bailPercentage;
	private String csrTel;
	private String proxyUrl;
	private String notifyUrl;
	private String returnUrl;
	private Long dailyLimit;
	private String extStoreId;
	private PaymentGateway paymentGateway;
	private String extStoreName;
	private Long agentId;
	private Long quota;
	private Long channelId;
	private ChinaUmsProps chinaUmsProps;
	private Float bar;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Float getBailPercentage() {
		return bailPercentage;
	}
	public void setBailPercentage(Float bailPercentage) {
		this.bailPercentage = bailPercentage;
	}
	public String getCsrTel() {
		return csrTel;
	}
	public void setCsrTel(String csrTel) {
		this.csrTel = csrTel;
	}
	public String getProxyUrl() {
		return proxyUrl;
	}
	public void setProxyUrl(String proxyUrl) {
		this.proxyUrl = proxyUrl;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public Long getDailyLimit() {
		return dailyLimit;
	}
	public void setDailyLimit(Long dailyLimit) {
		this.dailyLimit = dailyLimit;
	}
	public String getExtStoreId() {
		return extStoreId;
	}
	public void setExtStoreId(String extStoreId) {
		this.extStoreId = extStoreId;
	}
	public PaymentGateway getPaymentGateway() {
		return paymentGateway;
	}
	public void setPaymentGateway(PaymentGateway paymentGateway) {
		this.paymentGateway = paymentGateway;
	}
	public String getExtStoreName() {
		return extStoreName;
	}
	public void setExtStoreName(String extStoreName) {
		this.extStoreName = extStoreName;
	}
	public Long getAgentId() {
		return agentId;
	}
	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
	public ChinaUmsProps getChinaUmsProps() {
		return chinaUmsProps;
	}
	public void setChinaUmsProps(ChinaUmsProps chinaUmsProps) {
		this.chinaUmsProps = chinaUmsProps;
	}
	public Long getQuota() {
		return quota;
	}
	public void setQuota(Long quota) {
		this.quota = quota;
	}
	public Long getChannelId() {
		return channelId;
	}
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
	public Float getBar() {
		return bar;
	}
	public void setBar(Float bar) {
		this.bar = bar;
	}
}
