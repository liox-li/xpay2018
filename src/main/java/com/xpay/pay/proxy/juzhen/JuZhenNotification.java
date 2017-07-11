package com.xpay.pay.proxy.juzhen;

public class JuZhenNotification {
	private String merId;
	private String orderId;
	private String transKey;
	private String ordStatus;
	private String transAmt;
	private String feeAmt;
	private String ordInfo;
	private String openId;
	private String bankType;
	private String signature;
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getTransKey() {
		return transKey;
	}
	public void setTransKey(String transKey) {
		this.transKey = transKey;
	}
	public String getOrdStatus() {
		return ordStatus;
	}
	public void setOrdStatus(String ordStatus) {
		this.ordStatus = ordStatus;
	}
	public String getTransAmt() {
		return transAmt;
	}
	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}
	public String getFeeAmt() {
		return feeAmt;
	}
	public void setFeeAmt(String feeAmt) {
		this.feeAmt = feeAmt;
	}
	public String getOrdInfo() {
		return ordInfo;
	}
	public void setOrdInfo(String ordInfo) {
		this.ordInfo = ordInfo;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getBankType() {
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
}
