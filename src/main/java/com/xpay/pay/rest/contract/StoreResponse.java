package com.xpay.pay.rest.contract;

import java.util.List;

import com.xpay.pay.model.StoreChannel;

public class StoreResponse {
	private long id;
	private String code;
	private String name;
	private Float bailPercentage;
	private long appId;
	private String csrTel;
	private String proxyUrl;
	private Float todayTradeAmount;
	private Long dailyLimit;
	private Float quota;
	private List<StoreChannel> channels;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
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
	public long getAppId() {
		return appId;
	}
	public void setAppId(long appId) {
		this.appId = appId;
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
	public Float getTodayTradeAmount() {
		return todayTradeAmount;
	}
	public void setTodayTradeAmount(Float todayTradeAmount) {
		this.todayTradeAmount = todayTradeAmount;
	}
	public Long getDailyLimit() {
		return dailyLimit;
	}
	public void setDailyLimit(Long dailyLimit) {
		this.dailyLimit = dailyLimit;
	}
	public Float getQuota() {
		return quota;
	}
	public void setQuota(Float quota) {
		this.quota = quota;
	}
	public List<StoreChannel> getChannels() {
		return channels;
	}
	public void setChannels(List<StoreChannel> channels) {
		this.channels = channels;
	}
}
