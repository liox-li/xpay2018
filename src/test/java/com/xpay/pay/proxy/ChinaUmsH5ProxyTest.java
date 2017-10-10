package com.xpay.pay.proxy;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.proxy.IPaymentProxy.PayChannel;
import com.xpay.pay.proxy.chinaumsh5.ChinaUmsH5Proxy;
import com.xpay.pay.util.IDGenerator;

public class ChinaUmsH5ProxyTest extends BaseSpringJunitTest {
	@Autowired 
	private ChinaUmsH5Proxy proxy;
	
	@Test
	public void testGetJsPayUrl() {
		PaymentRequest request = new PaymentRequest();
		request.setExtStoreId("898319848160316");
		request.setDeviceId("1213");
		request.setPayChannel(PayChannel.WECHAT);
		request.setTotalFee("0.01");
		request.setOrderNo("3251201710081218354995996111");
		request.setGatewayOrderNo(IDGenerator.buildQrCode("3251"));
		request.setReturnUrl("http://www.baidu.com");
		request.setNotifyUrl("http://106.14.47.193/xpay/notify/chinaumsv2");
		request.setSubject("测试商品");
		request.setAttach("atach");
		String jsUrl = proxy.getJsUrl(request);
		System.out.println("jsUrl: "+ jsUrl);
	}
}
