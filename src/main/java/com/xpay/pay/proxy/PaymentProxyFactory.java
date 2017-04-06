package com.xpay.pay.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.model.StoreChannel.PaymentGateway;
import com.xpay.pay.proxy.miaofu.MiaoFuProxy;
import com.xpay.pay.proxy.swiftpass.SwiftpassProxy;

@Service
public class PaymentProxyFactory {
	@Autowired 
	private MiaoFuProxy miaoFuProxy;
	@Autowired
	private SwiftpassProxy swiftpassProxy;
	
	public IPaymentProxy getPaymentProxy(PaymentGateway channel) {
		if(PaymentGateway.MIAOFU.equals(channel)) {
			return miaoFuProxy;
		} else {
			return swiftpassProxy;
		}
	}
}
