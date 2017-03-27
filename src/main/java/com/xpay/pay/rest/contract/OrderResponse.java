package com.xpay.pay.rest.contract;

import com.xpay.pay.proxy.PaymentResponse.TradeStatus;

public class OrderResponse {
	private String orderNo;
	private String storeId;
	private String sellerOrderNo;
	private String codeUrl;
	private String prepayId;
	private TradeStatus orderStatus;
	private String attach;
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getSellerOrderNo() {
		return sellerOrderNo;
	}
	public void setSellerOrderNo(String sellerOrderNo) {
		this.sellerOrderNo = sellerOrderNo;
	}
	public String getCodeUrl() {
		return codeUrl;
	}
	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}
	public String getPrepayId() {
		return prepayId;
	}
	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
	public TradeStatus getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(TradeStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
}
