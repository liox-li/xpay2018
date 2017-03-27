package com.xpay.pay.rest.contract;

import com.xpay.pay.proxy.PaymentRequest.GoodBean;

public class OrderDetail {
	private String storeName;
	private String operator;
	private String sellerOrderNo;
	private String orderSubject;
	private String orderDesc;
	private GoodBean[] orderItems;
	private String attach;
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
	public String getSellerOrderNo() {
		return sellerOrderNo;
	}
	public void setSellerOrderNo(String sellerOrderNo) {
		this.sellerOrderNo = sellerOrderNo;
	}
	public String getOrderSubject() {
		return orderSubject;
	}
	public void setOrderSubject(String orderSubject) {
		this.orderSubject = orderSubject;
	}
	public String getOrderDesc() {
		return orderDesc;
	}
	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}
	public GoodBean[] getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(GoodBean[] orderItems) {
		this.orderItems = orderItems;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	
	
}
