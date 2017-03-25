package com.xpay.pay.proxy;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.proxy.PaymentRequest.PayChannel;
import com.xpay.pay.proxy.PaymentRequest.TradeNoType;


public class MiaoFuProxyTest extends BaseSpringJunitTest {
	@Autowired 
	private MiaoFuProxy proxy;
	
	@Test
	public void testPlaceOrder() {
		PaymentRequest request = new PaymentRequest();
		request.setBusi_code("T2017032319251974486873");
		request.setDev_id("1908a92d7d33");
		request.setPay_channel(PayChannel.ALIPAY);
		request.setAmount("0.00");
		request.setDown_trade_no("58237477024932JrfDWLbLWR");
		request.setSubject("华素传媒测试门店");
		request.setRaw_data("1.0.5");
		request.setUndiscountable_amount("0.00");
		request.setOper_id("104");
		PaymentResponse response = proxy.unifiedOrder(request);
		System.out.println("response code: "+ response.getCode());
	}
	
	@Test
	public void testQuery() {
		PaymentRequest request = new PaymentRequest();
		request.setBusi_code("T2017032319251974486873");
		request.setDown_trade_no("58237477024932JrfDWLbLWR");
		request.setPay_channel(PayChannel.ALIPAY);
		request.setTrade_no_type(TradeNoType.Gateway);
		PaymentResponse response = proxy.query(request);
		System.out.println("response code: "+ response.getCode());
	}
}
