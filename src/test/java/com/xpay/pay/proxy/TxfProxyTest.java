package com.xpay.pay.proxy;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.proxy.IPaymentProxy.PayChannel;
import com.xpay.pay.proxy.txf.TxfProxy;
import com.xpay.pay.util.IDGenerator;

public class TxfProxyTest extends BaseSpringJunitTest {
	@Autowired 
	private TxfProxy proxy;
	
	@Test
	public void testGetJsPayUrl() {
		PaymentRequest request = new PaymentRequest();
		request.setDeviceId("1213");
		request.setPayChannel(PayChannel.WECHAT);
		request.setTotalFee(0.01f);
		request.setOrderNo("3251201710261618354995996111");
		request.setGatewayOrderNo(IDGenerator.buildQrCode("3354"));
		request.setReturnUrl("http://www.baidu.com");
		request.setNotifyUrl("http://106.14.47.193/xpay/notify/txf");
		request.setSubject("测试商品");
		request.setAttach("atach");
		request.setBankId("6666");
		request.setCardType("1");
		PaymentResponse response = proxy.unifiedOrder(request);
		System.out.println("codeUrl: "+ response.getBill().getCodeUrl());
	}
}
