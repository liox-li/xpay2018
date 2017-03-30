package com.xpay.pay.model;

import java.util.List;

public class Store {
	private int id;
	private String code;
	private String name;
	private float bail;
	private float nonBail;
	private float bar;
	private int bailPercentage;
	private RotationType rotationType;
	private List<StoreChannel> channels;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	public List<StoreChannel> getChannels() {
		return channels;
	}
	public void setChannels(List<StoreChannel> channels) {
		this.channels = channels;
	}
	public float getBail() {
		return bail;
	}
	public void setBail(float bail) {
		this.bail = bail;
	}
	public float getNonBail() {
		return nonBail;
	}
	public void setNonBail(float nonBail) {
		this.nonBail = nonBail;
	}
	public float getBar() {
		return bar;
	}
	public void setBar(float bar) {
		this.bar = bar;
	}
	public int getBailPercentage() {
		return bailPercentage;
	}
	public void setBailPercentage(int bailPercentage) {
		this.bailPercentage = bailPercentage;
	}
	public RotationType getRotationType() {
		return rotationType;
	}
	public void setRotationType(RotationType rotationType) {
		this.rotationType = rotationType;
	}

	public enum RotationType {
		RoundRobin, FirstOneFirst
	}
}
