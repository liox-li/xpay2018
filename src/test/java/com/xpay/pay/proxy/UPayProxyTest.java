package com.xpay.pay.proxy;

import java.io.FileInputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.proxy.upay.ActiviateResponse;
import com.xpay.pay.proxy.upay.UPayProxy;

public class UPayProxyTest  extends BaseSpringJunitTest {
	@Autowired 
	private UPayProxy uPayProxy;
	
	@Test
	public void testQuery() {
		PaymentRequest request = new PaymentRequest();
		request.setOrderNo("X003006320171017163053738411");
		PaymentResponse response = uPayProxy.query(request);
		System.out.println("response code: "+ response.getCode()+" "+response.getMsg());
		
		System.out.println(response.getBill().getTokenId());
	}
	
	@Test
	public void testRefund() {
		PaymentRequest request = new PaymentRequest();
		request.setOrderNo("X003006320171017163053738411");
		request.setTotalFee("0.01");
		request.setExtStoreId("36694908,100003690002293222,0690940a8521e01b438b45c053cc4397");
		PaymentResponse response = uPayProxy.refund(request);
		System.out.println("response code: "+ response.getCode()+" "+response.getMsg());
		
		System.out.println(response.getBill().getTokenId());
	}
	
	@Test
	public void testActiviate() {
		String code = "85351813";
		String deviceId = "192.0.0.12";
		
		ActiviateResponse response = uPayProxy.activiate(code, deviceId);
		
		System.out.println(code+","+response.getBiz_response().getTerminal_sn()+"," + response.getBiz_response().getTerminal_key());
	}
	
	@Test
	public void testBatchActiviate() {
		String[] codes = {"60945533", "81878173", "26629642", "56282700", "33948002", "93090977", "97792995", "40058043", "51474768", "51392379"};
		
		String sql = "insert into bill_store_channel (ext_store_id, ext_store_name, payment_gateway, bill_type) values ('%ext_store_id%', '%ext_store_name%', '%payment_gateway%', 'T1');";
		String extStoreName = "米动游戏";
		String paymenGateway = "UPAY";
		String deviceId = "192.0.0.";
		int ip=64;
		int i=110;
		for(String code : codes) {
			ActiviateResponse response = uPayProxy.activiate(code, deviceId+ip);
			String extStoreId = code+","+response.getBiz_response().getTerminal_sn()+"," + response.getBiz_response().getTerminal_key();
			String replacedSql = sql.replace("%ext_store_id%", extStoreId)
			.replace("%ext_store_name%", extStoreName+i)
			.replace("%payment_gateway%", paymenGateway);
			System.out.println(replacedSql);
			i++;
			ip++;
		}
	}
}
