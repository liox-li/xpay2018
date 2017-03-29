package com.xpay.pay.dao.entity;

public class OrderDetail {
	private long orderId;
	private String storeName;
	private String operator;
	private String subject;
	private String desc;
	private String itemsJson;
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getItemsJson() {
		return itemsJson;
	}
	public void setItemsJson(String itemsJson) {
		this.itemsJson = itemsJson;
	}
	
	
}
