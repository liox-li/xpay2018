package com.xpay.pay.model;

import com.xpay.pay.proxy.PaymentRequest.GoodBean;
import com.xpay.pay.util.JsonUtils;

public class OrderDetail {
	private long id;
	private String storeName;
	private String operator;
	private String subject = "No Subject";
	private GoodBean[] orderItems;
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
	public GoodBean[] getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(GoodBean[] orderItems) {
		this.orderItems = orderItems;
		itemsJson = JsonUtils.toJson(this.orderItems);
	}
	public String getItemsJson() {
		return itemsJson;
	}
	public void setItemsJson(String itemsJson) {
		this.itemsJson = itemsJson;
		this.orderItems = JsonUtils.fromJson(itemsJson, GoodBean[].class);
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
