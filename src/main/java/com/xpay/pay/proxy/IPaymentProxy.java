package com.xpay.pay.proxy;


public interface IPaymentProxy {
	public static final String SUCCESS = "0";
	public static final String NO_RESPONSE = "-100";
	public PaymentResponse microPay(PaymentRequest orderRequest);
	
	public PaymentResponse unifiedOrder(PaymentRequest orderRequest);
	
	public PaymentResponse query(PaymentRequest orderRequest);
	
	public PaymentResponse refund(PaymentRequest orderRequest);
	
}
