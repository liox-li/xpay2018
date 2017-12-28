package com.xpay.pay.service;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.cache.CacheManager;
import com.xpay.pay.cache.ICache;
import com.xpay.pay.dao.AppMapper;
import com.xpay.pay.model.App;
import com.xpay.pay.util.IDGenerator;

@Service
public class AppService {

	protected final Logger logger = LogManager.getLogger(AppService.class);
	private static ICache<String, App> cache = CacheManager.create(App.class, 100);
	@Autowired
	protected AppMapper mapper;

	public App findByKey(String key) {
		initCache();
		
		return cache.get(key);
	}

	public App findByToken(String token) {
		initCache();
		
		List<App> apps = cache.values();
		if (CollectionUtils.isEmpty(apps)) {
			return null;
		}

		App app = apps.stream().filter(x -> token.equals(x.getToken()))
				.findFirst().orElse(null);
		if (app != null) {
			return this.isTokenExpired(app.getToken()) ? null : app;
		}
		return app;
	}

	public App findById(Long id) {
		if(id == null) {
			return null;
		}
		initCache();
		
		List<App> apps = cache.values();
		if (CollectionUtils.isEmpty(apps)) {
			return null;
		}

		App app = apps.stream().filter(x -> x.getId() == id).findFirst()
				.orElse(null);
		if(app == null) {
			app = mapper.findById(id);
		}
		return app;
	}
	
	public List<App> findByAgentId(long agentId) {
		return mapper.findByAgentId(agentId);
	}

	public App createApp(long agentId, String name) {
		App app = new App();
		app.setAgentId(agentId);
		app.setName(name);
		app.setKey(IDGenerator.buildAuthKey());
		app.setSecret(IDGenerator.buildAuthSecret());
		mapper.insert(app);
		return app;
	}
	
	public void refreshToken(App app) {
		if (isTokenExpired(app.getToken())) {
			app.setToken(buildToken());
			app.setUpdateDate(new Date());
			mapper.updateById(app);
		}
	}

	public static final long token_timeout = 24 * 60 * 60 * 1000L;
	private boolean isTokenExpired(String token) {
		if (StringUtils.isBlank(token) || token.length()<25) {
			return true;
		}
		try {
			long tokenTime = Long.valueOf(token.substring(10, 23));
			return System.currentTimeMillis() - tokenTime > token_timeout;
		} catch (Exception e) {
			return true;
		}
	}

	@PostConstruct
	private void initCache() {
		if (cache.size() == 0) {
			List<App> apps = mapper.findAll();
			for (App app : apps) {
//				logger.info("load app: " + app.getKey());
				cache.put(app.getKey(), app);
			}
		}
	}

	private String buildToken() {
		return IDGenerator.buildKey(10) + System.currentTimeMillis()
				+ IDGenerator.buildKey(9);
	}
	
	public void refreshCache() {
		cache.destroy();
		initCache();
	}

}
