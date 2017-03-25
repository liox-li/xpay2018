package com.xpay.pay.proxy;


public interface IPaymentProxy {
	public PaymentResponse microPay(PaymentRequest orderRequest);
	
	public PaymentResponse unifiedOrder(PaymentRequest orderRequest);
	
	public PaymentResponse query(PaymentRequest orderRequest);
	
}
