package com.xpay.pay.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.xpay.pay.dao.StoreChannelMapper;
import com.xpay.pay.dao.StoreMapper;
import com.xpay.pay.model.Store;
import com.xpay.pay.model.StoreChannel;

@Service
public class StoreService {
	protected StoreMapper storeMapper;
	protected StoreChannelMapper storeChannelMapper;
	
	public Store findByCode(String code) {
		Store store = storeMapper.findByCode(code);
		Assert.notNull(store, "Unknow storeId "+code);
		List<StoreChannel> channels = storeChannelMapper.findByStoreId(store.getId());
		Assert.notEmpty(channels, "No valid channel for store "+code);
		store.setChannels(channels);
		return store;
	}

	public Store findById(long id) {
		Store store = storeMapper.findById(id);
		Assert.notNull(store, "Unknow storeId "+id);
		List<StoreChannel> channels = storeChannelMapper.findByStoreId(store.getId());
		Assert.notEmpty(channels, "No valid channel for store "+id);
		store.setChannels(channels);
		return store;
	}
	
	public boolean updateById(Store store) {
		return storeMapper.updateById(store);
	}
}
