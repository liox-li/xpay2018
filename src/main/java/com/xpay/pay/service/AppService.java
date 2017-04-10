package com.xpay.pay.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.cache.CacheManager;
import com.xpay.pay.cache.ICache;
import com.xpay.pay.cache.LocalCache;
import com.xpay.pay.dao.AppMapper;
import com.xpay.pay.model.App;

@Service
public class AppService {
	@SuppressWarnings("unchecked")
	private static ICache<String, App> cache = CacheManager.register(App.class,
			new LocalCache<String, App>(100));
	@Autowired
	protected AppMapper mapper;

	public App findByKey(String key) {
		initCache();
		return cache.get(key);
	}
	
	public App findById(int id) {
		initCache();
		List<App> apps = cache.values();
		if(CollectionUtils.isEmpty(apps)) {
			return null;
		}
		
		return apps.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
	}
	
	@PostConstruct
	private void initCache() {
		if(cache.size() == 0) {
			List<App> apps = mapper.findAll();
			for(App app: apps) {
				cache.put(app.getKey(), app);
			}
		}
	}
	
}
