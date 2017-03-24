package com.xpay.pay.models;

import com.xpay.pay.proxy.OrderRequest.GoodBean;
import com.xpay.pay.proxy.OrderRequest.PayChannel;

public class Order {
	private String orderNum;
	private String storeId;
	private String storeName;
	private String operator;
	private String deviceId;
	private PayChannel payChannel; 
	private String amount;
	private String undiscountableAmount;
	private String orderSubject;
	private GoodBean[] orderItems;
	private String sellerData;
	
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public PayChannel getPayChannel() {
		return payChannel;
	}
	public void setPayChannel(PayChannel payChannel) {
		this.payChannel = payChannel;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getUndiscountableAmount() {
		return undiscountableAmount;
	}
	public void setUndiscountableAmount(String undiscountableAmount) {
		this.undiscountableAmount = undiscountableAmount;
	}
	public String getSellerData() {
		return sellerData;
	}
	public void setSellerData(String sellerData) {
		this.sellerData = sellerData;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getOrderSubject() {
		return orderSubject;
	}
	public void setOrderSubject(String orderSubject) {
		this.orderSubject = orderSubject;
	}
	public GoodBean[] getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(GoodBean[] orderItems) {
		this.orderItems = orderItems;
	}
}
