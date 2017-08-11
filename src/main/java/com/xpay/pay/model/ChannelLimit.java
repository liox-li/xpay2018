package com.xpay.pay.model;

import java.util.Date;

public class ChannelLimit {
	private long id;
	private long channelId;
	private StoreChannel channel;
	private Float limit;
	private Float currentAmount;
	private String notes;
	private Date updateDate;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getChannelId() {
		return channelId;
	}
	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}
	public StoreChannel getChannel() {
		return channel;
	}
	public void setChannel(StoreChannel channel) {
		this.channel = channel;
	}
	public Float getLimit() {
		return limit;
	}
	public void setLimit(Float limit) {
		this.limit = limit;
	}
	public Float getCurrentAmount() {
		return currentAmount;
	}
	public void setCurrentAmount(Float currentAmount) {
		this.currentAmount = currentAmount;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
}
