package com.xpay.pay.proxy;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.proxy.IPaymentProxy.PayChannel;
import com.xpay.pay.proxy.baifu.BaiFuProxy;

public class BaiFuProxyTest extends BaseSpringJunitTest {
	@Autowired 
	private BaiFuProxy proxy;
	
	@Test
	public void testUnifiedOrder() {
		PaymentRequest request = new PaymentRequest();
		request.setExtStoreId("80000193");
		request.setDeviceId("1213");
		request.setTotalFee("0.01");
		request.setOrderNo("X011010214214341422");
		request.setSubject("No Subject");
		request.setAttach("atach");
		request.setPayChannel(PayChannel.WECHAT);
		request.setServerIp("100.234.1.1");
		request.setNotifyUrl("http://100.234.1.1/xpay/notify/X110101201703311115387831581");
		PaymentResponse response = proxy.unifiedOrder(request);
		System.out.println("response code: "+ response.getCode()+" "+response.getMsg());
		
		System.out.println(response.getBill().getTokenId());
	}
}
