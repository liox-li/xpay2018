package com.xpay.pay.rest.contract;

public class NotificationResponse {
	private String orderNo;
	private String storeId;
	private String sellerOrderNo;
	private String codeUrl;
	private String tokenId;
	private String prepayId;
	private int orderStatus;
	private String totalFee;
	private String extOrderNo;
	private String targetOrderNo;
	private String attach;
	private String payInfo;
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
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public String getPrepayId() {
		return prepayId;
	}
	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
	public String getExtOrderNo() {
		return extOrderNo;
	}
	public void setExtOrderNo(String extOrderNo) {
		this.extOrderNo = extOrderNo;
	}
	public String getTargetOrderNo() {
		return targetOrderNo;
	}
	public void setTargetOrderNo(String targetOrderNo) {
		this.targetOrderNo = targetOrderNo;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getPayInfo() {
		return payInfo;
	}
	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}
}
