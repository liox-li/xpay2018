package com.xpay.pay.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.model.StoreTransaction;
import com.xpay.pay.model.StoreTransaction.TransactionType;

public class StoreTransactionMapperTest extends BaseSpringJunitTest {
	@Autowired
	protected StoreTransactionMapper mapper;
	
	@Test
	public void testInsert() {
		StoreTransaction transaction =  new StoreTransaction();
		transaction.setAgentId(10L);
		transaction.setOperation(TransactionType.INIT_FREE);
		transaction.setAmount(100f);
		transaction.setStoreId(53L);
		
		mapper.insert(transaction);
	}
	
	@Test
	public void testFindByAgentId() {
		List<StoreTransaction> transactions = mapper.findByAgentIdAndTime(10L, null, null);
		System.out.println(transactions.size());
	}
	
	@Test
	public void testFindByStoreId() {
		List<StoreTransaction> transactions = mapper.findByStoreIdAndTime(53L, null, null);
		System.out.println(transactions.size());
	}
	
}
