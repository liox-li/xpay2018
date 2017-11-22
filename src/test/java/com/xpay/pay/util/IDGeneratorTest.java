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
		String sql = "insert into bill_store_channel (ext_store_id, ext_store_name, payment_gateway, bill_type) values ('%ext_store_id%', '%ext_store_name%', '%payment_gateway%', 'T1');";
		String extStoreName = "福建嘉和园电子商务有限公司";
		String paymenGateway = "CHINAUMSH5";
		
		String filePath = "/data/store_id_mumu.txt";
		List<String> lines = IOUtils.readLines(new FileInputStream(filePath));
		int i=1;
		for(String line : lines) {
			String replacedSql = sql.replace("%id%", i+"")
			.replace("%ext_store_id%", line.trim())
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
		
		// T20171019105442016 1535~1574
		
		// jiaheyuan 1917 ~ 2217
		
//		T20171116100636953 | 千汇万兴6
//		 T20171108135349455 | 千汇万兴H55
//		 T20171115171807614 | 千汇万兴H57
//		 T20171026135944105 | 千汇万兴H51
//		 T20171116100636952 | 千汇万兴5
//		 T20171116100636951 | 千汇万兴4
//		 T20171116100636950 | 千汇万兴3
//		 T20171102093434226 | 千汇万兴H52
//		 T20170817172200539 | 千汇万兴2
//		 T20170817172114691 | 千汇万兴1
//		 T20171106150701151 | 千汇万兴H53
//		 T20171108125604706 | 千汇万兴H54
//		 T20171109092733192 | 千汇万兴H56	
		
		
//		String sql = "update bill_store set channels='%channels%', bail_channels='%bail_channels%' where code='%code%';";
		String sql = "update bill_store set channels='%channels%' where code='%code%';";
		String code = "T20171019105442016";
//		String bail_channels="2,3,4,5,6,7,8,9,10";
		long startChannelId = 2218;
		long endChannelId =2227;
		StringBuilder sb = new StringBuilder();
		for(long l=startChannelId; l<=endChannelId; l++) {
			sb.append(l);
			sb.append(",");
		}
		sb.setLength(sb.length()-1);
		
		String replacedSql = sql.replace("%channels%", sb.toString())
//				.replace("%bail_channels%", bail_channels)
				.replace("%code%", code);
		System.out.println(replacedSql);
	}
}
