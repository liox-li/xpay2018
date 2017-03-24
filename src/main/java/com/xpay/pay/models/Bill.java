package com.xpay.pay.models;

import com.xpay.pay.proxy.OrderResonse.TradeStatus;

public class Bill {
	private Order order;
	private String tradeNo;
	private TradeStatus tradeStatus;
	private String buyerId;
	private int totalAmount;
	private int sellerAmount;
	private int sellerCoupon;
	private int buyerAmount;
	private int chnCoupon;
	private int undiscountAmount;
	private String orderTime;
	private String payUrl;
	private String wechatPayId;
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public TradeStatus getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(TradeStatus tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	
	public int getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}
	public int getSellerAmount() {
		return sellerAmount;
	}
	public void setSellerAmount(int sellerAmount) {
		this.sellerAmount = sellerAmount;
	}
	public int getSellerCoupon() {
		return sellerCoupon;
	}
	public void setSellerCoupon(int sellerCoupon) {
		this.sellerCoupon = sellerCoupon;
	}
	public int getBuyerAmount() {
		return buyerAmount;
	}
	public void setBuyerAmount(int buyerAmount) {
		this.buyerAmount = buyerAmount;
	}
	public int getChnCoupon() {
		return chnCoupon;
	}
	public void setChnCoupon(int chnCoupon) {
		this.chnCoupon = chnCoupon;
	}
	public int getUndiscountAmount() {
		return undiscountAmount;
	}
	public void setUndiscountAmount(int undiscountAmount) {
		this.undiscountAmount = undiscountAmount;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String payTime) {
		this.orderTime = payTime;
	}
	public String getPayUrl() {
		return payUrl;
	}
	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
	public String getWechatPayId() {
		return wechatPayId;
	}
	public void setWechatPayId(String wechatPayId) {
		this.wechatPayId = wechatPayId;
	}
}
