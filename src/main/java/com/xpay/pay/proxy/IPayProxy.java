package com.xpay.pay.proxy;


public interface IPayProxy {
	public PayResponse microPay(PayRequest payRequest);
	
	public PayResponse unifiedOrder(PayRequest payRequest);
	
}
