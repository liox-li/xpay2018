package com.xpay.pay.util;

import org.junit.Test;

public class IDGeneratorTest {
	@Test
	public void testBuildOrderNo() {
		String orderNo = IDGenerator.buildOrderNo(1, 10);
		System.out.println(orderNo);
	}
	
	@Test
	public void testBuildKey() {
		String key = IDGenerator.buildAuthKey();
		String secret = IDGenerator.buildAuthSecret();
		
		System.out.println(key);
		System.out.println(secret);
	}
	
	@Test
	public void testBuildStoreCode() {
		String storeCode = IDGenerator.buildStoreCode();
		
		System.out.println(storeCode);
	}
}
