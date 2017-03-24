package com.xpay.pay.proxy;


public interface IOrderProxy {
	public OrderResonse microPay(OrderRequest orderRequest);
	
	public OrderResonse placeOrder(OrderRequest orderRequest);
	
	public OrderResonse query(OrderRequest orderRequest);
	
}
