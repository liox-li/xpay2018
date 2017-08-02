package com.xpay.pay.proxy;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.proxy.IPaymentProxy.PayChannel;
import com.xpay.pay.proxy.PaymentRequest.GoodsBean;
import com.xpay.pay.proxy.chinaumsv2.ChinaUmsV2Proxy;
import com.xpay.pay.util.IDGenerator;

public class ChinaUmsV2ProxyTest extends BaseSpringJunitTest {
	@Autowired 
	private ChinaUmsV2Proxy proxy;
	
	//898319848160167
	//898319848160168
	//898319848160169
	//898319848160170
	//898319848160171
	
	//898340149000005
	@Test
	public void testUnifiedOrder() {
		PaymentRequest request = new PaymentRequest();
		request.setExtStoreId("898340149000005");
		request.setDeviceId("1213");
		request.setPayChannel(PayChannel.WECHAT);
		request.setTotalFee("0.01");
		request.setOrderNo("3116201707311003354995996111");
		request.setReturnUrl("http://www.baidu.com");
		request.setNotifyUrl("http://106.14.47.193/xpay/notify/chinaumsv2");
		request.setSubject("测试商品");
		request.setAttach("atach");
		GoodsBean[] goods = new GoodsBean[1];
		GoodsBean good = new GoodsBean();
		good.setGoodsId("001");
		good.setGoodsName("电子商品1");
		good.setPrice("1");
		good.setQuantity("1");
		good.setGoodsCategory("电子商品");
		good.setBody("测试商品详情");
		goods[0] = good;
		request.setGoods(goods);
		PaymentResponse response = proxy.unifiedOrder(request);
		System.out.println("response code: "+ response.getCode()+" "+response.getMsg());
	}
	
	@Test
	public void testQuery() {
		PaymentRequest request = new PaymentRequest();
		request.setExtStoreId("898340149000005");
		request.setDeviceId("1213");
		request.setPayChannel(PayChannel.WECHAT);
		request.setTotalFee("0.01");
		request.setOrderNo("3194201708020912525902559623");
		request.setSubject("No Subject");
		request.setAttach("atach");
		request.setOrderTime(IDGenerator.formatTime(new Date(), IDGenerator.TimePattern14));
		PaymentResponse response = proxy.query(request);
		System.out.println("response code: "+ response.getCode()+" "+response.getMsg());
	}
	
	@Test
	public void testRefund() {
		PaymentRequest request = new PaymentRequest();
		request.setExtStoreId("898340149000005");
		request.setDeviceId("1213");
		request.setPayChannel(PayChannel.WECHAT);
		request.setTotalFee("0.01");
		request.setOrderNo("3194201708020921163077813588");
		request.setSubject("No Subject");
		request.setAttach("atach");
		request.setOrderTime(IDGenerator.formatTime(new Date(), IDGenerator.TimePattern14));
		PaymentResponse response = proxy.refund(request);
		System.out.println("response code: "+ response.getCode()+" "+response.getMsg());
	}
}
