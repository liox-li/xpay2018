package com.xpay.pay.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.dao.entity.Order;
import com.xpay.pay.proxy.PaymentResponse.TradeStatus;

public class OrderMapperTest extends BaseSpringJunitTest {
	@Autowired
	protected OrderMapper mapper;
	
	@Test
	public void testInsert() {
		Order order = new Order();
		order.setOrderNo("123");
		order.setAppId(10);
		order.setStoreId(100);
		order.setStoreChannelId(100);
		order.setTotalFee("0.01");
		order.setOrderTime("20170329142345");
		order.setPayChannel(1);
		mapper.insert(order);
	}
	
	@Test
	public void testFindById() {
		Order order = mapper.findById(1001);
		System.out.println(order.getOrderNo());
	}
	
	@Test
	public void testFindByOrderNo() {
		Order order = mapper.findByOrderNo("123");
		System.out.println(order.getOrderNo());
	}
	
	@Test
	public void testUpdate() {
		Order order = mapper.findById(1001);
		order.setSellerOrderNo("456");
		order.setExtOrderNo("12a");
		order.setAttach("att=123");
		order.setDeviceId("d");
		order.setIp("1207.0.0.1");
		order.setNotifyUrl("http://localhost:8080/xpay/rest/v1/pay/notify/12a");
		order.setCodeUrl("http://ali.pay.com/asdfv");
		order.setStatus(TradeStatus.NOTPAY);
		mapper.updateById(order);
	}
}
