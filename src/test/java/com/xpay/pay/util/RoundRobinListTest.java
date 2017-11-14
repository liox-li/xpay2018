package com.xpay.pay.util;

import java.util.List;

import org.junit.Test;

public class RoundRobinListTest {
	@Test
	public void testAdd() {
		RoundRobinList<String> list = new RoundRobinList<String>(3);
		list.add("1");
		println(list);
		System.out.println(list.contains("3"));
		list.add("2");
		println(list);
		System.out.println(list.contains("3"));
		list.add("3");
		println(list);
		System.out.println(list.contains("3"));
		list.add("4");
		println(list);
		System.out.println(list.contains("3"));
		list.add("5");
		println(list);
		System.out.println(list.contains("3"));
		list.add("6");
		println(list);
		System.out.println(list.contains("3"));
	
	}
	
	private void println(List list) {
		for(Object o: list) {
			System.out.print(o);
			System.out.print(",");
		}
		System.out.println();
	}
}
