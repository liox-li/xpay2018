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

	public static <K, V> ICache<K, V> create(Class clazz) {
		return create(clazz, DEFAULT_SIZE);
	}
	
	public static <K, V> ICache<K, V>  create(Class clazz, int size) {
		LocalCache<K, V> cache = new LocalCache<K, V>(size);
		cacheRepository.put(clazz, cache);
		return cache;
	}

	@PreDestroy
	public static void destroyAll() {
		cacheRepository.values().stream().forEach(x -> x.destroy());
		cacheRepository.clear();
	}
}
