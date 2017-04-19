package com.xpay.pay.proxy;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.proxy.IPaymentProxy.PayChannel;
import com.xpay.pay.proxy.rubipay.RubiPayProxy;

public class RubiPayProxyTest  extends BaseSpringJunitTest {
	@Autowired 
	private RubiPayProxy proxy;
	
	@Test
	public void testUnifiedOrder() {
		PaymentRequest request = new PaymentRequest();
		request.setExtStoreId("7551000001");
		request.setDeviceId("1213");
		request.setTotalFee("0.01");
		request.setOrderNo("X011010220170407141421434142");
		request.setSubject("No Subject");
		request.setAttach("atach");
		request.setPayChannel(PayChannel.WECHAT);
		request.setServerIp("100.234.1.1");
		request.setNotifyUrl("http://100.234.1.1/xpay/notify/X110101201703311115387831581");
		PaymentResponse response = proxy.unifiedOrder(request);
		System.out.println("response code: "+ response.getCode()+" "+response.getMsg());
		System.out.println(response.getBill().getCodeUrl());
	}
}
