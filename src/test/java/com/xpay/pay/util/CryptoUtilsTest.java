package com.xpay.pay.util;

import org.junit.Assert;
import org.junit.Test;

public class CryptoUtilsTest {
	@Test
	public void testMd5() {
		String str = "amount=0.01&app_id=148946099217658&busi_code=T2016060516001813420315&dev_id=1908a92d7d33&down_trade_no=58237477024932JrfDWLbLWR&oper_id=105&pay_channel=1&raw_data=1.0.5.105&subject=测试门店1&timestamp=1489463214&undiscountable_amount=0.00&version=v3&APP_SECRET=BTOOuffLDoVTPZsgDZgGapXsoBeKvTMT";
		String md5 = CryptoUtils.md5(str);
		Assert.assertEquals("b0540468252a9fc99cdaa18e0e62dd9c", md5);
	}
	
	@Test 
	public void testMd52() {
		String str = "appid=wxd930ea5d5a258f4f&body=test&device_info=1000&mch_id=10000100&nonce_str=ibuaiVcKdpRxkhJA";
		String stringSignTemp=str+"&key=192006250b4c09247ec02edce69f6a2d";
		String sign=CryptoUtils.md5(stringSignTemp).toUpperCase();
		Assert.assertEquals("9A0A8659F005D6984697E2CA0A9CF3B7", sign);
	}
}
