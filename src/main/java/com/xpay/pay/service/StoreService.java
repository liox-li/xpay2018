package com.xpay.pay.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.xpay.pay.cache.CacheManager;
import com.xpay.pay.cache.ICache;
import com.xpay.pay.dao.StoreChannelMapper;
import com.xpay.pay.dao.StoreMapper;
import com.xpay.pay.model.Store;
import com.xpay.pay.model.StoreChannel;

@Service
public class StoreService {
	@Autowired
	protected StoreMapper storeMapper;
	@Autowired
	protected StoreChannelMapper storeChannelMapper;
	private static ICache<Long, List<StoreChannel>> channelCache = CacheManager.create(StoreChannel.class, 1000);
	
	public Store findByCode(String code) {
		initStoreChannelCache();
		Store store = storeMapper.findByCode(code);
		Assert.notNull(store, "Unknow storeId "+code);
		List<StoreChannel> channels = channelCache.get(store.getId());
		Assert.notEmpty(channels, "No valid channel for store "+code);
		store.setChannels(channels);
		return store;
	}

	public Store findById(long id) {
		initStoreChannelCache();
		Store store = storeMapper.findById(id);
		Assert.notNull(store, "Unknow storeId "+id);
		List<StoreChannel> channels = channelCache.get(store.getId());
		Assert.notEmpty(channels, "No valid channel for store "+id);
		store.setChannels(channels);
		return store;
	}
	
	public boolean updateById(Store store) {
		return storeMapper.updateById(store);
	}
	
	public StoreChannel findStoreChannelById(long id) {
		initStoreChannelCache();
		return channelCache.values().stream().flatMap(x -> x.stream()).filter(x -> id == x.getId()).findAny().orElse(null);
	}
	
	@PostConstruct
	private void initStoreChannelCache() {
		if(channelCache.size() == 0) {
			List<StoreChannel> channels = storeChannelMapper.findAll();
			Map<Long, List<StoreChannel>> storeChannelMap = channels.stream().collect(Collectors.groupingBy(x -> x.getStoreId()));
			for(Long storeId: storeChannelMap.keySet()) {
				channelCache.put(storeId, storeChannelMap.get(storeId));
			}
		}
	}
}
