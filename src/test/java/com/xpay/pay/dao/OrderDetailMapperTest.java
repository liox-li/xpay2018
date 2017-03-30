package com.xpay.pay.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.model.OrderDetail;

public class OrderDetailMapperTest extends BaseSpringJunitTest {
	@Autowired
	protected OrderDetailMapper mapper;
	
	@Test
	public void testInsert() {
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setStoreName("123");
		orderDetail.setOrderId(1001);
		mapper.insert(orderDetail);
	}
	
	@Test
	public void testFindByOrderId() {
		List<OrderDetail> list = mapper.findByOrderId(1001);
		System.out.println(list.size());
	}
}
