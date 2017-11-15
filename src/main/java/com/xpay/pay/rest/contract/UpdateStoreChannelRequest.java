package com.xpay.pay.rest.contract;

public class UpdateStoreChannelRequest {
	private long[] channelIds;

	public long[] getChannelIds() {
		return channelIds;
	}

	public void setChannelIds(long[] channelIds) {
		this.channelIds = channelIds;
	}
}
