package com.xpay.pay.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.dao.OrderMapper;
import com.xpay.pay.exception.Assert;
import com.xpay.pay.model.Agent;
import com.xpay.pay.model.Order;
import com.xpay.pay.model.Store;
import com.xpay.pay.model.StoreChannel;
import com.xpay.pay.model.StoreGoods;
import com.xpay.pay.model.StoreGoods.ExtGoods;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.util.AppConfig;
import com.xpay.pay.util.CommonUtils;
import com.xpay.pay.util.TimeUtils;

@Service
public class OrderService {
	protected final Logger logger = LogManager.getLogger(OrderService.class);
	@Autowired
	protected OrderMapper orderMapper;
	@Autowired
	protected AppService appService;
	@Autowired
	protected StoreService storeService;
	@Autowired
	protected StoreGoodsService goodsService;
	@Autowired
	private LockerService lockerService;

	public List<Order> findByOrderNo(String orderNo) {
		List<Order> orders = orderMapper.findByOrderNo(orderNo);
		Assert.notNull(orders, "Order not found - " + orderNo);
		for (Order order : orders) {
			order.setApp(appService.findById(order.getAppId()));
			order.setStore(storeService.findById(order.getStoreId()));
			order.setGoods(goodsService.findById(order.getGoodsId()));
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
		order.setGoods(goodsService.findById(order.getGoodsId()));
		return order;
	}
	
	public Order findAnyByOrderNo(String orderNo) {
		List<Order> orders = orderMapper.findAnyByOrderNo(orderNo);
		Assert.notNull(orders, "Order not found - " + orderNo);
		Order order = orders.stream().filter(x -> !x.getStatus().equals(OrderStatus.PAYERROR)).findAny().orElse(null);
		Assert.notNull(order, "Order not found - " + orderNo);
		order.setApp(appService.findById(order.getAppId()));
		order.setStore(storeService.findById(order.getStoreId()));
		order.setStoreChannel(storeService.findStoreChannelById(order.getStoreChannelId()));
		order.setGoods(goodsService.findById(order.getGoodsId()));
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
		order.setGoods(goodsService.findById(order.getGoodsId()));
		return order;
	}
	
	public Order findActiveByOrderTime(String extStoreCode, String extOrderNo, Float amount, String subject, Date orderTime) {
		Date startTime = TimeUtils.timeBefore(orderTime, 60000);
		List<Order> orders = orderMapper.findLastByExtStoreCode(extStoreCode,subject, startTime, orderTime);
		if(CollectionUtils.isNotEmpty(orders)) {
			Order order = orders.stream().filter(x ->  (Math.abs(x.getTotalFee()-amount)<=0.5f)).findFirst().orElse(null);
			if(order == null) {
				return null;
			}
			order.setStore(storeService.findById(order.getStoreId()));
			order.setGoods(goodsService.findById(order.getGoodsId()));
			return order;  
		}
		return null;
	}
	
	public StoreChannel findUnusedChannelByStore(Store store, String orderNo) {
		if(store == null) {
			return null;
		}
		StoreChannel channel = null;
		if(!CollectionUtils.isEmpty(store.getChannels())) {
			channel = findUnusedChannel(store.getChannels(), orderNo);
		} 

		return channel;
	}
	
	public StoreChannel findUnusedChannelByAgent(long agentId, String orderNo) {
		List<StoreChannel> agentChannes = storeService.findChannelsByAgentId(agentId);
		StoreChannel channel = findUnusedChannel(agentChannes, orderNo);
		return channel;
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
		if(channel!=null) {
			channel.setLastUseTime(System.currentTimeMillis());
		}
		return channel;
	}
	
	public List<Order> findByStoreIdAndTime(String storeCode, Date startTime, Date endTime) {
		Store store = storeService.findByCode(storeCode);
		Assert.notNull(store, String.format("Unknown store - %s", storeCode));
		Assert.notNull(startTime, "Start time cant't be null");
		
		Date thisEndTime = endTime==null?new Date():endTime;
		return orderMapper.findByStoreIdAndTime(store.getId(), startTime, thisEndTime);
	}
	
	public List<Order> findByAgentAndTime(Agent agent, Date startTime, Date endTime) {
		Assert.notNull(startTime, "Start time cant't be null");
		
		List<Store> stores = storeService.findByAgent(agent);
		Assert.notEmpty(stores, String.format("No store found under agent - %s", agent.getId()));
		
		List<Order> result = new ArrayList<Order>();
		for(Store store: stores) {
			List<Order> orders = orderMapper.findByStoreIdAndTime(store.getId(), startTime, endTime);
			if(CollectionUtils.isNotEmpty(orders)) {
				result.addAll(orders);
			}
		}
		
		result.sort((x, y) -> {
			return (int)(x.getId()-y.getId());
		});
		
		return result;
	}
	
	public boolean insert(Order order) {
		return orderMapper.insert(order);
	}
	
	public boolean update(Order order) {
		return orderMapper.updateById(order);
	}

	private static final long bailStoreId = 1L;
	private static final long storeLockTime = AppConfig.XPayConfig.getProperty("store.lock.time", 10000L);
	private static final long goodsLockTime = AppConfig.XPayConfig.getProperty("goods.lock.time", 10000L);
	private static final long checkInterval = 1000L;
	public String findAvaiableQrCode(Store store, StoreGoods goods) {
		if(storeLockTime>0) {
			boolean stoceLock  = aquireLock(store.getCode(), storeLockTime, checkInterval);
			if(!stoceLock) {
				logger.error("No lock found: "+store.getCode());
			}
			Assert.isTrue(stoceLock, "No avaiable channel");
		}
		
		StoreGoods thisGoods = null;
		if(store.isNextBailPay(goods.getAmount())) {
			thisGoods = goodsService.findByStoreIdAndAmount(bailStoreId, goods.getAmount());
		}
		thisGoods = thisGoods == null? goods: thisGoods;
		
		String qrCode = lockerService.findOldestByKeys(thisGoods.getExtQrCodes());
		ExtGoods extGoods = thisGoods.getExtGoodsList().stream().filter(x -> x.getExtQrCode().equals(qrCode)).findAny().orElse(null);
		
		if(extGoods!=null) {
			goods.setName(StringUtils.trim(goods.getName())+StringUtils.trim(extGoods.getNote()));
		}
		
		if(goodsLockTime>0) {
			boolean lock = aquireLock(qrCode, goodsLockTime, checkInterval);
			if(!lock) {
				logger.error("No lock found: "+qrCode);
			}
			Assert.isTrue(lock, "No avaiable channel");
		}
		
		return qrCode;
	}

	public boolean aquireLock(String key, long lockTime, long checkInterval) {
		boolean lock = false;
		for(int i=0;i<10;i++) {
			lock = lockerService.lock(key, lockTime);
			if(lock) {
				break;
			}
			try {
				Thread.sleep(checkInterval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return lock;
	}
	
}
