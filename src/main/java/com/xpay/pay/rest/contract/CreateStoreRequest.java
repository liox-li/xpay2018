package com.xpay.pay.rest.contract;

public class CreateStoreRequest {
	private String name;
	private Float bailPercentage;
	private long appId;
	private String csrTel;
	private String proxyUrl;
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
	
}
