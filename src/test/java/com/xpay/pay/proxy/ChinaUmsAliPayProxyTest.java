package com.xpay.pay.proxy;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.proxy.IPaymentProxy.PayChannel;
import com.xpay.pay.proxy.PaymentRequest.GoodsBean;
import com.xpay.pay.proxy.chinaumsv3.ChinaUmsAliPayProxy;

public class ChinaUmsAliPayProxyTest extends BaseSpringJunitTest {
	@Autowired 
	private ChinaUmsAliPayProxy proxy;
	
//	private String extStoreId = "898310060514010";
	private String extStoreId = "898340149000005";
	
	@Test
	public void testUnifiedOrder() {
		PaymentRequest request = new PaymentRequest();
		request.setExtStoreId(extStoreId);
		request.setSubject("test");
		request.setDeviceId("127.0.0.1");
		request.setServerIp("127.0.0.1");
		request.setReturnUrl("http://www.baidu.com");
		request.setTotalFee(0.01f);
		request.setNotifyUrl("http://106.14.47.193/xpay/notify/chinaumsh5");
		request.setPayChannel(PayChannel.ALIPAY);
		GoodsBean[] goods = new GoodsBean[1];
		GoodsBean bean = new GoodsBean();
		bean.setQuantity("1");
		bean.setPrice("1");
		bean.setBody("test");
		bean.setGoodsId("1");
		bean.setGoodsName("test");
		goods[0] = bean;
		request.setGoods(goods);
		PaymentResponse response = proxy.unifiedOrder(request);
		
		System.out.println(response.getBill().getCodeUrl());
	}
}
