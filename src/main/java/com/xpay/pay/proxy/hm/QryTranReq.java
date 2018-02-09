package com.xpay.pay.proxy.hm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class QryTranReq {
	private String merCode;// 商户号
	private String orderNo;// 订单号
	private String transDate;// 商户订单发送时间
	private String transSeq;// 流水号
	private String paymentType;// 支付类型
	public String getMerCode() {
		return merCode;
	}
	public void setMerCode(String merCode) {
		this.merCode = merCode;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getTransSeq() {
		return transSeq;
	}
	public void setTransSeq(String transSeq) {
		this.transSeq = transSeq;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
}
