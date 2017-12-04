package com.xpay.pay.proxy;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.proxy.IPaymentProxy.PayChannel;
import com.xpay.pay.proxy.PaymentRequest.GoodsBean;
import com.xpay.pay.proxy.juzhen.JuZhenProxy;

public class JuZhenProxyTest extends BaseSpringJunitTest {
	@Autowired 
	private JuZhenProxy proxy;
	
	@Test
	public void testUnifiedOrder() {
		PaymentRequest request = new PaymentRequest();
		request.setExtStoreId("999002100009696");
		request.setDeviceId("1213");
		request.setPayChannel(PayChannel.WECHAT);
		request.setTotalFee(0.01f);
		request.setOrderNo("37042410033549959961");
		request.setSubject("测试商品");
		request.setNotifyUrl("http://106.14.47.193/xpay/juzhen");
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
}
