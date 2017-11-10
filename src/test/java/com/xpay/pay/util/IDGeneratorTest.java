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
	public void testStoreChannels() throws FileNotFoundException, IOException {
		String sql = "insert into bill_store_channel (ext_store_id, ext_store_name, payment_gateway, bill_type) values (%ext_store_id%, %ext_store_name%, %payment_gateway%, 'T1');";
		String extStoreName = "千汇万兴";
		String paymenGateway = "CHINAUMSH5";
		
		String filePath = "/data/store_id_qianhui.txt";
		List<String> lines = IOUtils.readLines(new FileInputStream(filePath));
		
		for(String line : lines) {
			String replacedSql = sql.replace("%ext_store_id%", "'"+line+"'")
			.replace("%ext_store_name%", "'"+extStoreName+"'")
			.replace("%payment_gateway%", "'"+paymenGateway+"'");
			System.out.println(replacedSql);
		}
	}
}
