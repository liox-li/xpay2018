package com.xpay.pay.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("rawtypes")
public class CacheManager {
	private static final  Map<Class, ICache> cacheRepository = new ConcurrentHashMap<Class, ICache>();

	public static ICache getCache(Class clazz) {
		return cacheRepository.get(clazz);
	}
	
	public static ICache register(Class clazz, ICache cache) {
		cacheRepository.put(clazz, cache);
		return cache;
	}
}
