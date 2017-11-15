package com.xpay.pay.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
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
	
	@Test
	public void testNewStoreChannels() throws FileNotFoundException, IOException {
		String sql = "insert into bill_store_channel (id, ext_store_id, ext_store_name, payment_gateway, bill_type) values ('%id%', '%ext_store_id%', '%ext_store_name%', '%payment_gateway%', 'T1');";
		String extStoreName = "上海纳优信息技术有限公司";
		String paymenGateway = "CHINAUMSH5";
		
		String filePath = "/data/store_id_nayou.txt";
		List<String> lines = IOUtils.readLines(new FileInputStream(filePath));
		int i=1;
		for(String line : lines) {
			String replacedSql = sql.replace("%id%", i+"")
			.replace("%ext_store_id%", line.replace("    ", ","))
			.replace("%ext_store_name%", extStoreName)
			.replace("%payment_gateway%", paymenGateway);
			System.out.println(replacedSql);
			i++;
		}
	}
	
	@Test
	public void testChangeStoreChannels() throws FileNotFoundException, IOException {
		//total: 1227 ~ 1527
		//group1: 1227 ~ 1327
		//gropu2: 1328 ~ 1427
		//group3: 1428 ~ 1527
		
		String sql = "update bill_store set channels='%channels%', bail_channels='%bail_channels%' where code='%code%';";
		String code = "T20171019105442016";
		String bail_channels="13,14,16,17,19,20,21,22,23";
		long startChannelId = 1535;
		long endChannelId =1574;
		StringBuilder sb = new StringBuilder();
		for(long l=startChannelId; l<=endChannelId; l++) {
			sb.append(l);
			sb.append(",");
		}
		sb.setLength(sb.length()-1);
		
		String replacedSql = sql.replace("%channels%", sb.toString())
				.replace("%bail_channels%", bail_channels)
				.replace("%code%", code);
		System.out.println(replacedSql);
	}
}
