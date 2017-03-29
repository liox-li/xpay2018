package com.xpay.pay.models;

import java.util.List;

public class Store {
	private int id;
	private String code;
	private String name;
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
}
