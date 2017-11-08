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
	private Integer bailPercentage;
	private RotationType rotationType;
	private Integer rotationIndex;
	private String bailChannelIds;
	private List<StoreChannel> bailChannels;
	private long dailyLimit;
	private String channelIds;
	private String csrTel;
	private String proxyUrl;
	private List<StoreChannel> channels;
	private List<StoreLink> links;

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


	public Integer getBailPercentage() {
		return bailPercentage;
	}


	public void setBailPercentage(Integer bailPercentage) {
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

	public long getDailyLimit() {
		return dailyLimit;
	}

	public void setDailyLimit(long dailyLimit) {
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

	private static final int SECURE_LOW_BOUNDER = 50;
	private static final int SECURE_UP_BOUNDER = 300;
	public boolean isNextBailPay(float totalFee) {
		if(this.nonBail <= this.bar) {
			return false;
		}
		boolean isNextBailPay = this.bail * 100 <= this.nonBail * this.bailPercentage;
		if(!isNextBailPay) {
			isNextBailPay = totalFee>=SECURE_LOW_BOUNDER & totalFee<SECURE_UP_BOUNDER && (this.bail + totalFee ) * 100 <=  this.nonBail * this.bailPercentage * 2;
		}
		return isNextBailPay;
	}
	
	public boolean isValidStoreLink(String link) {
		if(CollectionUtils.isEmpty(this.links)) {
			return true;
		}
		String domainName = CommonUtils.getDomainName(link);
		if(StringUtils.isBlank(domainName)) {
			return false;
		}
		return this.links.stream().map(x -> x.getLink()).filter(y -> y.indexOf(domainName)>=0).findAny().isPresent();
	}
}
