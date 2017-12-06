package com.xpay.pay.util;

import org.junit.Test;

import com.xpay.pay.model.Order;

public class SerializationUtilsTest {
	@Test
	public void testToOjbect() {
		Order order = new Order();
		order.setAppId(1L);
		order.setAttach("attach");
		order.setCodeUrl("code url");
		order.setTotalFee(0.01f);
		
		byte[] byteArray = SerializationUtils.toByteArray(order);
		
		
		
		System.out.println(byteArray.length);
		System.out.println(JsonUtils.toJson(order).length());
		Order orderNew = SerializationUtils.toOject(byteArray);
		
		System.out.println(orderNew.getAppId());
	}
}
