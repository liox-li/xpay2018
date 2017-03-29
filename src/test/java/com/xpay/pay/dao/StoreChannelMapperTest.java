package com.xpay.pay.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.models.StoreChannel;
import com.xpay.pay.models.StoreChannel.PaymentGateway;

public class StoreChannelMapperTest extends BaseSpringJunitTest {
	@Autowired
	protected StoreChannelMapper mapper;
	
	@Test
	public void testInert() {
		StoreChannel channel = new StoreChannel();
		channel.setStoreId(100);
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
	public void testFindByStoreId() {
		List<StoreChannel> channels = mapper.findByStoreId(100);
		System.out.println(channels.size());
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
