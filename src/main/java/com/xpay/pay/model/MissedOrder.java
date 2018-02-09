package com.xpay.pay.model;

import java.util.Date;

import com.xpay.pay.notify.WechatNotifyHandler;
import com.xpay.pay.util.TimeUtils;

public class MissedOrder {
	private Long id;
	private String orderNo;
	private String payTime;
	private Float amount;
	private String subject;
	private String extStoreId;
	private int status;
	private String createTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
		Date date = WechatNotifyHandler.parseOrderTimeFromOrderNo(orderNo);
		this.createTime = TimeUtils.formatTime(date, TimeUtils.TimePatternTime);
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getExtStoreId() {
		return extStoreId;
	}
	public void setExtStoreId(String extStoreId) {
		this.extStoreId = extStoreId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getCreateTime() {
		return createTime;
	}
}

