package com.xpay.pay.proxy;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.proxy.IPaymentProxy.PayChannel;
import com.xpay.pay.proxy.rubipay.RubiPayProxy;
import com.xpay.pay.proxy.swiftpass.SwiftpassProxy;

public class RubiPayProxyTest  extends BaseSpringJunitTest {
	@Autowired 
	private RubiPayProxy proxy;
	
	@Autowired
	private SwiftpassProxy swfitpassProxy;
	
	@Test
	public void testUnifiedOrder() {
		PaymentRequest request = new PaymentRequest();
		request.setExtStoreId("103590012926");
		request.setDeviceId("1213");
		request.setTotalFee(0.01f);
		request.setOrderNo("X011010220170507141421434142");
		request.setSubject("No Subject");
		request.setAttach("atach");
		request.setPayChannel(PayChannel.WECHAT);
		request.setServerIp("100.234.1.1");
		request.setNotifyUrl("http://100.234.1.1/xpay/notify/X110101201703311115387831581");
		PaymentResponse response = proxy.unifiedOrder(request);
		System.out.println("response code: "+ response.getCode()+" "+response.getMsg());
		
		System.out.println(response.getBill().getTokenId());
	}
	
	@Test
	public void testQuery() {
		PaymentRequest request = new PaymentRequest();
		request.setExtStoreId("7551000001");
		request.setOrderNo("X011010220170507141421434141");
		PaymentResponse response = proxy.query(request);
		System.out.println("response code: "+ response.getCode()+" "+response.getMsg());
		
		System.out.println(response.getBill().getTokenId());
	}
}
