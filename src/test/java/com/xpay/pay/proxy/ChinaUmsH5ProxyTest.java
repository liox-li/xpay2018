package com.xpay.pay.proxy;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.proxy.IPaymentProxy.PayChannel;
import com.xpay.pay.proxy.chinaumswap.ChinaUmsWapProxy;
import com.xpay.pay.util.IDGenerator;

public class ChinaUmsH5ProxyTest extends BaseSpringJunitTest {
	@Autowired 
	private ChinaUmsWapProxy proxy;
	
	@Test
	public void testGetJsPayUrl() {
		PaymentRequest request = new PaymentRequest();
		request.setExtStoreId("898310172991173");
		request.setDeviceId("1213");
		request.setPayChannel(PayChannel.WECHAT);
		request.setTotalFee(0.01f);
		request.setOrderNo("3251201710261618354995996111");
		request.setGatewayOrderNo(IDGenerator.buildQrCode("3354"));
		request.setReturnUrl("http://www.baidu.com");
		request.setNotifyUrl("http://106.14.47.193/xpay/notify/chinaumsv2h5");
		request.setSubject("测试商品");
		request.setAttach("atach");
		String jsUrl = proxy.getJsUrl(request);
		System.out.println("jsUrl: "+ jsUrl);
	}
}
