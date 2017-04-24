package com.xpay.pay.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

@SuppressWarnings("rawtypes")
public class CacheManager {
	private static final  Map<Class, ICache> cacheRepository = new ConcurrentHashMap<Class, ICache>();
	private static final int DEFAULT_SIZE = 10000;

	public static ICache getCache(Class clazz) {
		return cacheRepository.get(clazz);
	}

	public static <Key, Value> ICache<Key, Value> create(Class clazz) {
		return create(clazz, DEFAULT_SIZE);
	}
	
	public static <Key, Value> ICache<Key, Value>  create(Class clazz, int size) {
		LocalCache<Key, Value> cache = new LocalCache<Key, Value>(size);
		cacheRepository.put(clazz, cache);
		return cache;
	}

	@PreDestroy
	public static void destroyAll() {
		cacheRepository.values().stream().forEach(x -> x.destroy());
		cacheRepository.clear();
	}
}
