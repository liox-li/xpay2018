package com.xpay.pay.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.model.StoreChannel;
import com.xpay.pay.model.StoreChannel.ChannelProps;
import com.xpay.pay.model.StoreChannel.ChinaUmsProps;
import com.xpay.pay.model.StoreChannel.PaymentGateway;

public class StoreChannelMapperTest extends BaseSpringJunitTest {
	@Autowired
	protected StoreChannelMapper mapper;
	
	@Test
	public void testInert() {
		StoreChannel channel = new StoreChannel();
		channel.setExtStoreId("123");
		channel.setPaymentGateway(PaymentGateway.CHINAUMSH5);
		ChinaUmsProps props = new ChinaUmsProps();
		props.setMsgSrc("WWW.TEST.COM");
		props.setMsgSrcId("3251");
		props.setTid("80000001");
		props.setSignKey("asdgbxcweqeqdcasd");
		channel.setChannelProps(props);
		mapper.insert(channel);
	}
	
	@Test
	public void testFindById() {
		StoreChannel storeChannel = mapper.findById(200);
		System.out.println(storeChannel.getPaymentGateway());
		ChinaUmsProps channelProps = (ChinaUmsProps)storeChannel.getChannelProps();
		channelProps.setSignKey("cdef");
		storeChannel.setChannelProps(channelProps);
		mapper.updateById(storeChannel);
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
