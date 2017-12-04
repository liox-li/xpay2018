package com.xpay.pay.rest.contract;

public class UpdateStoreChannelResponse {
	private long storeId;
	private long[] channels;
	public long getStoreId() {
		return storeId;
	}
	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}
	public long[] getChannels() {
		return channels;
	}
	public void setChannels(long[] channels) {
		this.channels = channels;
	}
}
