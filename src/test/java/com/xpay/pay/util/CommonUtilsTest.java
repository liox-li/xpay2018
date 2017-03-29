package com.xpay.pay.util;

import org.junit.Test;

public class CommonUtilsTest {
	@Test
	public void testBuildOrderNo() {
		String orderNo = CommonUtils.buildOrderNo(1, 10);
		System.out.println(orderNo);
	}
}
