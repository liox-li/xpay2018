package com.xpay.pay.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.model.StoreChannel;
import com.xpay.pay.model.StoreChannel.PaymentGateway;

public class StoreChannelMapperTest extends BaseSpringJunitTest {
	@Autowired
	protected StoreChannelMapper mapper;
	
	@Test
	public void testInert() {
		StoreChannel channel = new StoreChannel();
		channel.setExtStoreId("123");
		channel.setPaymentGateway(PaymentGateway.MIAOFU);
		mapper.insert(channel);
	}
	
	@Test
	public void testFindById() {
		StoreChannel storeChannel = mapper.findById(100);
		System.out.println(storeChannel.getPaymentGateway());
	}
	
	@Test
	public void testUpdate() {
		StoreChannel storeChannel = mapper.findById(100);
		storeChannel.setExtStoreId("new id");
		mapper.updateById(storeChannel);
		storeChannel = mapper.findById(100);
		System.out.println(storeChannel.getExtStoreId());
	}
	
	@Test
	public void testDelete() {
		mapper.deleteById(100);
		StoreChannel storeChannel = mapper.findById(100);
		System.out.println(storeChannel);
	}
}
