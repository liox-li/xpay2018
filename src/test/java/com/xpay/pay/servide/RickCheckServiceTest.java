package com.xpay.pay.servide;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.model.Store;
import com.xpay.pay.service.RiskCheckService;
import com.xpay.pay.service.StoreService;

public class RickCheckServiceTest extends BaseSpringJunitTest {
	private RiskCheckService rickCheckService = new RiskCheckService();
	@Autowired
	private StoreService storeServie;
	
	@Test
	public void testCheckFee() {
		Store store = storeServie.findByCode("T20171018153151534");
		System.out.println(rickCheckService.checkFee(store, 0.01f));
		System.out.println(rickCheckService.checkFee(store, 0.01f));
		System.out.println(rickCheckService.checkFee(store, 0.01f));
	}
}
