package com.xpay.pay.model;

import java.util.List;

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


	public List<StoreChannel> getChannels() {
		return channels;
	}


	public void setChannels(List<StoreChannel> channels) {
		this.channels = channels;
	}


	public enum RotationType {
		RoundRobin, FirstOneFirst
	}
	
	private static final int SECURE_LOW_BOUNDER = 100;
	private static final int SECURE_UP_BOUNDER = 500;
	public boolean isNextBailPay(float totalFee) {
		if(this.nonBail <= this.bar) {
			return false;
		}
		boolean isNextBailPay = this.bail * 100 <= this.nonBail * this.bailPercentage;
		if(!isNextBailPay) {
			isNextBailPay = totalFee>=SECURE_LOW_BOUNDER & totalFee<SECURE_UP_BOUNDER && this.bail * 100 <=  this.nonBail * this.bailPercentage * 2;
		}
		return isNextBailPay;
	}
}
