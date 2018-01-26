package com.xpay.pay.model;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.xpay.pay.util.CommonUtils;

public class Store {
	private long id;
	private String code;
	private String name;
	private Float bail;
	private Float nonBail;
	private Float bar;
	private Float bailPercentage;
	private RotationType rotationType;
	private Integer rotationIndex;
	private String bailChannelIds;
	private List<StoreChannel> bailChannels;
	private Long dailyLimit;
	private String channelIds;
	private String csrTel;
	private String proxyUrl;
	private List<StoreChannel> channels;
	private List<StoreLink> links;
	private Long appId;
	private Long agentId;
	private Float quota;
	private Float lastTransSum;
	private Float lastRechargeAmt;
	private String channelType;
	private Long adminId;
	private String notifyUrl;
	
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


	public Float getBail() {
		return bail;
	}


	public void setBail(Float bail) {
		this.bail = bail;
	}


	public Float getNonBail() {
		return nonBail;
	}


	public void setNonBail(Float nonBail) {
		this.nonBail = nonBail;
	}


	public Float getBar() {
		return bar;
	}


	public void setBar(Float bar) {
		this.bar = bar;
	}


	public Float getBailPercentage() {
		return bailPercentage;
	}


	public void setBailPercentage(Float bailPercentage) {
		this.bailPercentage = bailPercentage;
	}


	public RotationType getRotationType() {
		return rotationType;
	}

	public void setRotationType(RotationType rotationType) {
		this.rotationType = rotationType;
	}

	public Integer getRotationIndex() {
		return rotationIndex;
	}

	public void setRotationIndex(Integer rotationIndex) {
		this.rotationIndex = rotationIndex;
	}
	
	public List<StoreChannel> getBailChannels() {
		return bailChannels;
	}

	public void setBailChannels(List<StoreChannel> bailChannels) {
		this.bailChannels = bailChannels;
	}

	public Long getDailyLimit() {
		return dailyLimit;
	}

	public void setDailyLimit(Long dailyLimit) {
		this.dailyLimit = dailyLimit;
	}

	public enum RotationType {
		RoundRobin, FirstOneFirst
	}

	public List<StoreChannel> getChannels() {
		return channels;
	}

	public void setChannels(List<StoreChannel> channels) {
		this.channels = channels;
	}
	
	public List<StoreLink> getLinks() {
		return links;
	}


	public void setLinks(List<StoreLink> links) {
		this.links = links;
	}


	public String getBailChannelIds() {
		return bailChannelIds;
	}

	public void setBailChannelIds(String bailChannelIds) {
		this.bailChannelIds = bailChannelIds;
	}

	public String getChannelIds() {
		return channelIds;
	}

	public void setChannelIds(String channelIds) {
		this.channelIds = channelIds;
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

	
	public Long getAppId() {
		return appId;
	}


	public void setAppId(Long appId) {
		this.appId = appId;
	}


	public Long getAgentId() {
		return agentId;
	}


	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
	
	public Float getQuota() {
		return quota;
	}

	public void setQuota(Float quota) {
		this.quota = quota;
	}

	public Float getLastTransSum() {
		return lastTransSum;
	}


	public void setLastTransSum(Float lastTransSum) {
		this.lastTransSum = lastTransSum;
	}


	public Float getLastRechargeAmt() {
		return lastRechargeAmt;
	}


	public void setLastRechargeAmt(Float lastRechargeAmt) {
		this.lastRechargeAmt = lastRechargeAmt;
	}

	public String getChannelType() {
		return channelType;
	}


	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}


	public Long getAdminId() {
		return adminId;
	}


	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}


	public boolean isNextBailPay(float totalFee) {
		return false; 
//		if(this.nonBail <= this.bar) {
//			return false;
//		}
//		boolean isNextBailPay = this.bail * 100 <= this.nonBail * this.bailPercentage;
//		return isNextBailPay;
	}
	
	private static final String baidu = "baidu.com";
	public boolean isValidStoreLink(String link) {
		if(CollectionUtils.isEmpty(this.links)) {
			return true;
		}
		if(link.indexOf(baidu)>=0) {
			return true;
		}
		
		String domainName = CommonUtils.getDomainName(link);
		if(StringUtils.isBlank(domainName)) {
			return false;
		}
		return this.links.stream().map(x -> x.getLink()).filter(y -> y.indexOf(domainName)>=0).findAny().isPresent();
	}


	public Float getBaseBailPercentage() {
		if(CollectionUtils.isEmpty(this.channels)) {
			return 0f;
		}
		return this.channels.get(0).getPaymentGateway().getBaseBailPercentage();
	}	
}
