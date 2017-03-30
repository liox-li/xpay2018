package com.xpay.pay.dao;

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
		mapper.insert(orderDetail);
	}
	
	@Test
	public void testFindById() {
		OrderDetail detail = mapper.findById(1000);
		System.out.println(detail.getStoreName());
	}
}
