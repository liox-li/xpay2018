package com.xpay.pay.proxy;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.proxy.swiftpass.SwiftpassProxy;

public class SwiftpassProxyTest extends BaseSpringJunitTest {
	@Autowired 
	private SwiftpassProxy proxy;
	
	@Test
	public void testNativePay() {
		PaymentRequest request = new PaymentRequest();
		request.setBusi_code("755437000006");
		request.setDev_id("1213");
		request.setAmount("10");
		request.setDown_trade_no("X110101201703311115387831581");
		request.setSubject("No Subject");
		request.setRaw_data("atach");
		request.setIp("100.234.1.1");
		request.setNotifyUrl("http://100.234.1.1/xpay/notify/X110101201703311115387831581");
		PaymentResponse response = proxy.navtivePay(request);
		System.out.println("response code: "+ response.getCode()+" "+response.getMsg());
	}
}
