package com.xpay.pay.rest.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xpay.pay.model.Agent;
import com.xpay.pay.model.App;
import com.xpay.pay.model.StoreChannel;
import com.xpay.pay.model.StoreChannel.ChannelType;

public class StoreResponse {
	private long id;
	private String code;
	private String name;
	private Float bailPercentage;
	private String csrTel;
	private String proxyUrl;
	private String notifyUrl;
	private String returnUrl;
	private Float todayTradeAmount;
	private Long dailyLimit;
	private Float quota;
	private Agent agent;
	private Agent admin;
	private Float lastTradeAmount;
	private Float lastRechargeAmount;
	private App app;
	private ChannelType channelType;
	private List<StoreChannel> channels;
	
	public Agent getAgent() {
		return agent;
	}
	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	public Agent getAdmin() {
		return admin;
	}
	public void setAdmin(Agent admin) {
		this.admin = admin;
	}

	public Float getLastTradeAmount() {
		return lastTradeAmount;
	}
	public void setLastTradeAmount(Float lastTradeAmount) {
		this.lastTradeAmount = lastTradeAmount;
	}
	public Float getLastRechargeAmount() {
		return lastRechargeAmount;
	}
	public void setLastRechargeAmount(Float lastRechargeAmount) {
		this.lastRechargeAmount = lastRechargeAmount;
	}
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
	
	public App getApp() {
		return app;
	}
	public void setApp(App app) {
		this.app = app;
	}
	public List<StoreChannel> getChannels() {
		return channels;
	}
	public void setChannels(List<StoreChannel> channels) {
		this.channels = channels;
	}
	
	public ChannelType getChannelType() {
		return channelType;
	}
	public void setChannelType(ChannelType channelType) {
		this.channelType = channelType;
	}


}
