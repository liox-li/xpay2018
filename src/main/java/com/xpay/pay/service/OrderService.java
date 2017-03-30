package com.xpay.pay.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.xpay.pay.dao.OrderDetailMapper;
import com.xpay.pay.dao.OrderMapper;
import com.xpay.pay.model.Order;
import com.xpay.pay.model.Store;
import com.xpay.pay.model.StoreChannel;
import com.xpay.pay.util.CommonUtils;

@Service
public class OrderService {
	@Autowired
	protected OrderMapper orderMapper;
	@Autowired
	protected OrderDetailMapper orderDetailMapper;
	@Autowired
	protected AppService appService;
	@Autowired
	protected StoreService storeService;

	public List<Order> findByOrderNo(String orderNo) {
		List<Order> orders = orderMapper.findByOrderNo(orderNo);
		Assert.notNull(orders, "Order not found - " + orderNo);
		for (Order order : orders) {
			order.setApp(appService.findById(order.getAppId()));
			order.setStore(storeService.findById(order.getStoreId()));
		}
		return orders;
	}

	public StoreChannel findUnusedChannel(Store store, String orderNo) {
		List<Order> orders = this.findByOrderNo(orderNo);
		List<Long> usedChannels = CollectionUtils.isEmpty(orders) ? null : orders
				.stream().map(x -> x.getStoreChannelId())
				.collect(Collectors.toList());
		List<StoreChannel> channels = store.getChannels();

		StoreChannel channel = channels.stream().filter(x -> !CommonUtils.in(usedChannels, x.getId()))
				.findFirst().orElse(null);
		return channel;
	}
	
	public boolean insert(Order order) {
		return orderMapper.insert(order);
	}
}
