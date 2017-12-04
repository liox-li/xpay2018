package com.xpay.pay.proxy;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.proxy.IPaymentProxy.PayChannel;
import com.xpay.pay.proxy.miaofu.MiaoFuProxy;


public class MiaoFuProxyTest extends BaseSpringJunitTest {
	@Autowired 
	private MiaoFuProxy proxy;
	
	@Test
	public void testUnifiedOrder() {
		PaymentRequest request = new PaymentRequest();
		request.setExtStoreId("T2017032319251974486873");
		request.setDeviceId("1213");
		request.setPayChannel(PayChannel.WECHAT);
		request.setTotalFee(0.01f);
		request.setOrderNo("X110101201703311115387831502");
		request.setSubject("电子商品");
		request.setAttach("atach");
		PaymentResponse response = proxy.unifiedOrder(request);
		System.out.println("response code: "+ response.getCode()+" "+response.getMsg());
		System.out.println(response.getBill().getCodeUrl());
	}
	
	@Test
	public void testQuery() {
		PaymentRequest request = new PaymentRequest();
		request.setExtStoreId("T2017032319251974486873");
		request.setOrderNo("X110101201703311115387831582");
		request.setPayChannel(PayChannel.WECHAT);
		PaymentResponse response = proxy.query(request);
		System.out.println("response code: "+ response.getCode()+" "+response.getMsg());
	}
	
	@Test
	public void testRefund() {
		PaymentRequest request = new PaymentRequest();
		request.setExtStoreId("T2017032319251974486873");
		//request.setOrderNo("632033114100328792187");
		request.setOrderNo("X110101201703311115387831582");
		request.setPayChannel(PayChannel.WECHAT);
		PaymentResponse response = proxy.query(request);
		System.out.println("response code: "+ response.getCode()+" "+response.getMsg());
	}
}
