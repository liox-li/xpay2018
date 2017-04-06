package com.xpay.pay.proxy;


public interface IPaymentProxy {
	public static final String SUCCESS = "0";
	public static final String NO_RESPONSE = "-100";
	public PaymentResponse microPay(PaymentRequest paymentRequest);
	
	public PaymentResponse nativePay(PaymentRequest paymentRequest);
	
	public PaymentResponse unifiedOrder(PaymentRequest paymentRequest);
	
	public PaymentResponse query(PaymentRequest paymentRequest);
	
	public PaymentResponse refund(PaymentRequest paymentRequest);
	
}
