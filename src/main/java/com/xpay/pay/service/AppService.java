package com.xpay.pay.service;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.cache.CacheManager;
import com.xpay.pay.cache.ICache;
import com.xpay.pay.dao.AppMapper;
import com.xpay.pay.model.App;
import com.xpay.pay.util.CommonUtils;
import com.xpay.pay.util.IDGenerator;

@Service
public class AppService {
	private static ICache<String, App> cache = CacheManager.create(App.class,100);
	@Autowired
	protected AppMapper mapper;

	public App findByKey(String key) {
		return cache.get(key);
	}
	
	public App findByToken(String token) {
		List<App> apps = cache.values();
		if(CollectionUtils.isEmpty(apps)) {
			return null;
		}
		
		return apps.stream().filter(x -> token.equals(x.getToken())).findFirst().orElse(null);
	}
	
	public App findById(int id) {
		List<App> apps = cache.values();
		if(CollectionUtils.isEmpty(apps)) {
			return null;
		}
		
		return apps.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
	}
	
	public void refreshToken(App app) {
		if(StringUtils.isBlank(app.getToken()) || isTokenExpired(app.getUpdateDate())) {
			app.setToken(IDGenerator.buildKey(32));
			mapper.updateById(app);
		}
	}
	
	private boolean isTokenExpired(Date updateDate) {
		Date hourBefore = CommonUtils.hourBeforeNow(24);
		return updateDate.after(hourBefore);
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
