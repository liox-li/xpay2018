package com.xpay.pay.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.xpay.pay.cache.CacheManager;
import com.xpay.pay.cache.ICache;
import com.xpay.pay.dao.StoreChannelMapper;
import com.xpay.pay.dao.StoreLinkMapper;
import com.xpay.pay.dao.StoreMapper;
import com.xpay.pay.model.Store;
import com.xpay.pay.model.StoreChannel;
import com.xpay.pay.model.StoreLink;

@Service
public class StoreService {
	@Autowired
	protected StoreMapper storeMapper;
	@Autowired
	protected StoreChannelMapper storeChannelMapper;
	@Autowired
	protected StoreLinkMapper storeLinkMapper;
	private static ICache<Long, StoreChannel> channelCache = CacheManager.create(StoreChannel.class, 2000);
	private static ICache<Long, List<StoreLink>> linkCache = CacheManager.create(List.class, 5000);
	
	public Store findByCode(String code) {
		Store store = storeMapper.findByCode(code);
		Assert.notNull(store, "Unknow storeId "+code);
		List<StoreChannel> channels = this.findChannelByIds(store.getChannelIds());
		Assert.notEmpty(channels, "No valid channel for store "+code);
		store.setChannels(channels);
		store.setBailChannels(this.findChannelByIds(store.getBailChannelIds()));
		store.setLinks(this.findStoreLinkByStoreId(store.getId()));
		return store;
	}

	public Store findById(long id) {
		Store store = storeMapper.findById(id);
		Assert.notNull(store, "Unknow storeId "+id);
		List<StoreChannel> channels = this.findChannelByIds(store.getChannelIds());
		Assert.notEmpty(channels, "No valid channel for store "+id);
		store.setChannels(channels);
		store.setBailChannels(this.findChannelByIds(store.getBailChannelIds()));
		store.setLinks(this.findStoreLinkByStoreId(store.getId()));
		return store;
	}
	
	public List<Store> findByAgentId(long agentId) {
		return storeMapper.findByAgentId(agentId);
	}
	
	public List<StoreChannel> findChannelsByAgentId(long agentId) {
		return storeChannelMapper.findByAgentId(agentId);
	}
	
	public boolean updateById(Store store) {
		return storeMapper.updateById(store);
	}
	
	public StoreChannel findStoreChannelById(long id) {
		return channelCache.get(id);
	}
	
	public List<StoreLink> findStoreLinkByStoreId(long storeId) {
		List<StoreLink> links = linkCache.get(storeId);
		if(CollectionUtils.isEmpty(links)) {
			links = storeLinkMapper.findByStoreId(storeId);
			linkCache.put(storeId, links);
		}
		return links;
	}
	
	public void updateStoreChannels(long storeId, String[] channelIds) {
		Store store = new Store();
		store.setId(storeId);
		String channels = StringUtils.join(channelIds, ",");
		store.setChannelIds(channels);
		storeMapper.updateById(store);
	}
	
	public void refreshCache() {
		channelCache.destroy();
		List<StoreChannel> channels = storeChannelMapper.findAll();
		for(StoreChannel channel: channels) {
			channelCache.put(channel.getId(), channel);
		}
		
		linkCache.destroy();
	}
	
	@PostConstruct
	private void initStoreChannelCache() {
		if(channelCache.size() == 0) {
			List<StoreChannel> channels = storeChannelMapper.findAll();
			for(StoreChannel channel: channels) {
				channelCache.put(channel.getId(), channel);
			}
		}
	}
	
	private List<StoreChannel> findChannelByIds(String channelIds) {
		List<StoreChannel> list = Lists.newArrayList();
		String[] idStrs = StringUtils.split(channelIds, ",");
		for(String idStr: idStrs) {
			list.add(channelCache.get(Long.valueOf(idStr)));
		}
		return list;
	}
}
