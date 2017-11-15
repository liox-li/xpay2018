package com.xpay.pay.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.dao.OrderMapper;
import com.xpay.pay.exception.Assert;
import com.xpay.pay.model.Order;
import com.xpay.pay.model.StoreChannel;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.util.CommonUtils;

@Service
public class OrderService {
	@Autowired
	protected OrderMapper orderMapper;
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
	
	public Order findActiveByOrderNo(String orderNo) {
		List<Order> orders = orderMapper.findByOrderNo(orderNo);
		Assert.notNull(orders, "Order not found - " + orderNo);
		Order order = orders.stream().filter(x -> !x.getStatus().equals(OrderStatus.PAYERROR)).findAny().orElse(null);
		Assert.notNull(order, "Order not found - " + orderNo);
		order.setApp(appService.findById(order.getAppId()));
		order.setStore(storeService.findById(order.getStoreId()));
		order.setStoreChannel(storeService.findStoreChannelById(order.getStoreChannelId()));
		return order;
	}
	
	public Order findActiveByExtOrderNo(String extOrderNo) {
		List<Order> orders = orderMapper.findByExtOrderNo(extOrderNo);
		Assert.notNull(orders, "Order not found - " + extOrderNo);
		Order order = orders.stream().filter(x -> !x.getStatus().equals(OrderStatus.PAYERROR)).findAny().orElse(null);
		Assert.notNull(order, "Order not found - " + extOrderNo);
		order.setApp(appService.findById(order.getAppId()));
		order.setStore(storeService.findById(order.getStoreId()));
		order.setStoreChannel(storeService.findStoreChannelById(order.getStoreChannelId()));
		return order;
	}

	public StoreChannel findUnusedChannel(List<StoreChannel> channels, String orderNo) {
		List<Order> orders = this.findByOrderNo(orderNo);
		List<Long> usedChannels = CollectionUtils.isEmpty(orders) ? null : orders
				.stream().map(x -> x.getStoreChannelId())
				.collect(Collectors.toList());

		StoreChannel channel = channels.stream().filter(x -> x.available() && !CommonUtils.in(usedChannels, x.getId()))
				.collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
				      Collections.shuffle(collected);
				      return collected.stream();
				  })).findFirst().orElse(null);
		return channel;
	}
	
	public boolean insert(Order order) {
		return orderMapper.insert(order);
	}
	
	public boolean update(Order order) {
		return orderMapper.updateById(order);
	}
	
}
