package com.xpay.pay.proxy;

import com.xpay.pay.model.Bill;

public class PaymentResponse {
	public static final String SUCCESS = "0";
	private String code;
	private String msg;
	private Bill bill;
	
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}

	public enum OrderStatus {
		SUCCESS(0), REFUND(1), REVOKED(2), USERPAYING(3), NOTPAY(4), CLOSED(5), REFUNDING(6), PAYERROR(-1);
		int value;
		
		OrderStatus(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}

	public enum PayType {
		MICROPAY, NATIVE, JSAPI
	}
}
