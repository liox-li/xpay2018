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
		request.setExtStoreId("755437000006");
		request.setDeviceId("1213");
		request.setTotalFee("10");
		request.setOrderNo("X110101201703311115387831581");
		request.setSubject("No Subject");
		request.setAttach("atach");
		request.setServerIp("100.234.1.1");
		request.setNotifyUrl("http://100.234.1.1/xpay/notify/X110101201703311115387831581");
		PaymentResponse response = proxy.nativePay(request);
		System.out.println("response code: "+ response.getCode()+" "+response.getMsg());
	}
	
	@Test
	public void testUnifiedOrder() {
		PaymentRequest request = new PaymentRequest();
		request.setExtStoreId("755437000006");
		request.setDeviceId("1213");
		request.setTotalFee("10");
		request.setOrderNo("X110101201703311115387831581");
		request.setSubject("No Subject");
		request.setAttach("atach");
		request.setServerIp("100.234.1.1");
		request.setNotifyUrl("http://100.234.1.1/xpay/notify/X110101201703311115387831581");
		PaymentResponse response = proxy.unifiedOrder(request);
		System.out.println("response code: "+ response.getCode()+" "+response.getMsg());
	}
}
