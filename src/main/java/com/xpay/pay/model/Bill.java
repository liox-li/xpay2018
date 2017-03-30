package com.xpay.pay.model;

import com.xpay.pay.proxy.PaymentResponse.TradeStatus;



public class Bill {
	private String orderNo;
	private String gatewayOrderNo;
	private String prepayId;
	private String codeUrl;
	private TradeStatus orderStatus;
	private Order order;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getGatewayOrderNo() {
		return gatewayOrderNo;
	}
	public void setGatewayOrderNo(String gatewayOrderNo) {
		this.gatewayOrderNo = gatewayOrderNo;
	}
	public String getPrepayId() {
		return prepayId;
	}
	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
	public String getCodeUrl() {
		return codeUrl;
	}
	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}
	public TradeStatus getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(TradeStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	
	
}
