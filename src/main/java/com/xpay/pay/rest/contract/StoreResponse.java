package com.xpay.pay.rest.contract;

import java.util.List;

import com.xpay.pay.model.StoreChannel;

public class StoreResponse {
	private long id;
	private String code;
	private String name;
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
	public List<StoreChannel> getChannels() {
		return channels;
	}
	public void setChannels(List<StoreChannel> channels) {
		this.channels = channels;
	}
}
