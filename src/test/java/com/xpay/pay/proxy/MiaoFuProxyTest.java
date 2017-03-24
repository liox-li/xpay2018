package com.xpay.pay.proxy;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.proxy.OrderRequest.PayChannel;
import com.xpay.pay.proxy.OrderRequest.TradeNoType;


public class MiaoFuProxyTest extends BaseSpringJunitTest {
	@Autowired 
	private MiaoFuProxy proxy;
	
	@Test
	public void testPlaceOrder() {
		OrderRequest request = new OrderRequest();
		request.setBusi_code("T2017032319251974486873");
		request.setDev_id("1908a92d7d33");
		request.setPay_channel(PayChannel.ALIPAY);
		request.setAmount("0.00");
		request.setDown_trade_no("58237477024932JrfDWLbLWR");
		request.setSubject("华素传媒测试门店");
		OrderResonse response = proxy.placeOrder(request);
		System.out.println("response code: "+ response.getCode());
	}
	
	@Test
	public void testQuery() {
		OrderRequest request = new OrderRequest();
		request.setBusi_code("T2017032319251974486873");
		request.setDown_trade_no("58237477024932JrfDWLbLWR");
		request.setPay_channel(PayChannel.ALIPAY);
		request.setTrade_no_type(TradeNoType.Gateway);
		OrderResonse response = proxy.query(request);
		System.out.println("response code: "+ response.getCode());
	}
}
