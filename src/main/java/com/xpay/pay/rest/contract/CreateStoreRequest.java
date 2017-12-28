package com.xpay.pay.rest.contract;

public class CreateStoreRequest {
	private String name;
	private Float bailPercentage;
	private Long appId;
	private String csrTel;
	private String proxyUrl;
	private Long dailyLimit;
	private Long adminId;
	private Long agentId;
	
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
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
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
	public Long getDailyLimit() {
		return dailyLimit;
	}
	public void setDailyLimit(Long dailyLimit) {
		this.dailyLimit = dailyLimit;
	}
	
	public Long getAdminId() {
		return adminId;
	}
	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}
	public Long getAgentId() {
		return agentId;
	}
	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
	
}
