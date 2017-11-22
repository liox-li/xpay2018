package com.xpay.pay.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class LocalCache<K, V> implements ICache<K, V>{
	private Map<K,CacheItem<V>> map = new ConcurrentHashMap<K, CacheItem<V>>();
	
	public static final long DEFAULT_TTL = 5 * 60 * 1000L;// 5minutes
	private int maxSize = 100000;
	
	public LocalCache() {
		
	}
	
	public LocalCache(int maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	public V get(K key) {
		CacheItem<V> cacheItem = map.get(key);
		if(cacheItem ==null) {
			return null;
		}
		if(cacheItem.isExpired()) {
			map.remove(key);
			return null;
		}
		return cacheItem.value;
	}

	@Override
	public void put(K key, V value) {
		put(key, value, DEFAULT_TTL);
	}
	
	@Override
	public void put(K key, V value, long ttl) {
		if(value == null) {
			return;
		}
		
		if(map.size() >= maxSize) {
			cleanExpired();
		}
		
		CacheItem<V> cacheItem = new CacheItem<V>();
		cacheItem.value = value;
		cacheItem.ttl = System.currentTimeMillis() + ttl;
		map.put(key, cacheItem);
	}

	@Override
	public void remove(K key) {
		map.remove(key);
	}

	@Override
	public void destroy() {
		map.clear();
	}
	
	@Override
	public long size() {
		return map.values().stream().filter(x -> !x.isExpired()).count();
	}
	
	private synchronized void cleanExpired() {
		List<K> expiredKeys = map.keySet().stream().filter(x -> map.get(x).isExpired()).collect(Collectors.toList());
		for(K key : expiredKeys) {
			map.remove(key);
		}
	}

	public static final class CacheItem<V> {
		V value;
		long ttl;
		
		public boolean isExpired() {
			return System.currentTimeMillis()>ttl;
		}
	}

	@Override
	public List<V> values() {
		return map.values().stream().filter(x -> !x.isExpired()).map(x -> x.value).collect(Collectors.toList());
	}

}
