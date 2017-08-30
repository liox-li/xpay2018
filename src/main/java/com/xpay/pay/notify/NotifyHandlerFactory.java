package com.xpay.pay.notify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.model.StoreChannel.PaymentGateway;

@Service
public class NotifyHandlerFactory {

	@Autowired
	ChinaUmsNotifyHandler chinaUmsHandler;
	@Autowired
	JuZhenNotifyHandler juZhenHandler;
	@Autowired
	KeFuNotifyHandler keFuHandler;
	
	public INotifyHandler getNotifyHandler(String uri) {
		if (uri.contains(PaymentGateway.CHINAUMS.name().toLowerCase())) {
			return chinaUmsHandler;
		} else if (uri.contains(PaymentGateway.JUZHEN.name().toLowerCase())) {
			return juZhenHandler;
		} else if (uri.contains(PaymentGateway.KEFU.name().toLowerCase())) {
			return keFuHandler;
		} else {
			return null;
		}
	}
}
