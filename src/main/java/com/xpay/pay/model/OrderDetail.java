package com.xpay.pay.model;


public class OrderDetail {
	private long id;
	private String storeName;
	private String operator;
	private String subject;
	private String itemsJson;
	private String desc;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public String getItemsJson() {
		return itemsJson;
	}
	public void setItemsJson(String itemsJson) {
		this.itemsJson = itemsJson;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
