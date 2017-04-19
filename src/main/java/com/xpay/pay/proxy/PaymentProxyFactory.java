package com.xpay.pay.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.model.StoreChannel.PaymentGateway;
import com.xpay.pay.proxy.chinaums.ChinaUmsProxy;
import com.xpay.pay.proxy.miaofu.MiaoFuProxy;
import com.xpay.pay.proxy.rubipay.RubiPayProxy;
import com.xpay.pay.proxy.swiftpass.SwiftpassProxy;

@Service
public class PaymentProxyFactory {
	@Autowired 
	private MiaoFuProxy miaoFuProxy;
	@Autowired
	private SwiftpassProxy swiftpassProxy;
	@Autowired
	private ChinaUmsProxy chinaUmsProxy;
	@Autowired
	private RubiPayProxy rubiPayProxy;
	
	public IPaymentProxy getPaymentProxy(PaymentGateway channel) {
		if(PaymentGateway.MIAOFU.equals(channel)) {
			return miaoFuProxy;
		} else if (PaymentGateway.SWIFTPASS.equals(channel)) {
			return swiftpassProxy;
		} else if (PaymentGateway.CHINAUMS.equals(channel)) {
			return chinaUmsProxy;
		} else {
			return rubiPayProxy;
		}
	}
}
