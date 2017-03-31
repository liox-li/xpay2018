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
	public void testUnifiedOrder() {
		PaymentRequest request = new PaymentRequest();
		request.setBusi_code("T2017032319251974486873");
		request.setDev_id("1213");
		request.setPay_channel(PayChannel.WECHAT);
		request.setAmount("0.01");
		request.setDown_trade_no("X110101201703311115387831581");
		request.setSubject("No Subject");
		request.setRaw_data("atach");
		PaymentResponse response = proxy.unifiedOrder(request);
		System.out.println("response code: "+ response.getCode()+" "+response.getMsg());
	}
	
	@Test
	public void testUnifiedOrderExample() {
		PaymentRequest request = new PaymentRequest();
		request.setBusi_code("T2016060516001813420315");
		request.setDev_id("1908a92d7d33");
		request.setPay_channel(PayChannel.ALIPAY);
		request.setAmount("0.01");
		request.setDown_trade_no("5124");
		request.setSubject("华素传媒测试门店");
		request.setRaw_data("1.0.5.105");
		request.setUndiscountable_amount("0.00");
		request.setOper_id("105");
		PaymentResponse response = proxy.unifiedOrder(request);
		System.out.println("response code: "+ response.getCode()+" "+response.getMsg());
	}
	
	@Test
	public void testQuery() {
		PaymentRequest request = new PaymentRequest();
		request.setBusi_code("T2017032319251974486873");
		request.setDown_trade_no("58237477024932JrfDWLbLWR");
		request.setPay_channel(PayChannel.ALIPAY);
		request.setTrade_no_type(TradeNoType.Gateway);
		PaymentResponse response = proxy.query(request);
		System.out.println("response code: "+ response.getCode()+" "+response.getMsg());
	}
}
