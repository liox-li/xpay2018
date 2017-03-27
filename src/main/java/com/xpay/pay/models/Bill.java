package com.xpay.pay.models;



public class Bill {
	private String orderNo;
	private String prepayId;
	private String codeUrl;
	private Order orderDetail;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Order getOrderDetail() {
		return orderDetail;
	}
	public void setOrderDetail(Order orderDetail) {
		this.orderDetail = orderDetail;
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
	
	
}
