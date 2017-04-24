package com.xpay.pay.cache;

import java.util.List;


public interface ICache<K, V> {
	public V get(K key);
	
	public void put(K key, V value);
	
	public void put(K key, V value, long ttl);
	
	public void remove(K key);
	
	public List<V> values();
	
	public void destroy();
	
	public long size();
}
