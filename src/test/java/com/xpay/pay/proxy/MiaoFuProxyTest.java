package com.xpay.pay.proxy;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.proxy.PayRequest.PayChannel;


public class MiaoFuProxyTest extends BaseSpringJunitTest {
	@Autowired 
	private MiaoFuProxy proxy;
	
	@Test
	public void testUnifiedOrder() {
		PayRequest request = new PayRequest();
		request.setBusi_code("T2017032319251974486873");
		request.setDev_id("1908a92d7d33");
		request.setPay_channel(PayChannel.ALIPAY);
		request.setAmount("0.00");
		request.setDown_trade_no("58237477024932JrfDWLbLWR");
		request.setSubject("华素传媒测试门店");
		PayResponse response = proxy.unifiedOrder(request);
		System.out.println("response code: "+ response.getCode());
	}
}
