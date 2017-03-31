package com.xpay.pay.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.model.Store;

public class StoreMapperTest extends BaseSpringJunitTest {
	@Autowired
	protected StoreMapper mapper;

	@Test
	public void testInsert() {
		Store store = new Store();
		store.setCode("123");
		store.setName("Test");

		mapper.insert(store);
	}

	@Test
	public void testFindById() {
		Store store = mapper.findById(100);

		System.out.println(store.getName());
	}

	@Test
	public void testFindByCode() {
		Store store = mapper.findByCode("123");

		System.out.println(store.getName());
	}
	
	@Test
	public void testFindAll() {
		List<Store> all = mapper.findAll();
		System.out.println(all.size());
	}
	
	@Test
	public void testUpdate() {
		Store store = mapper.findById(100);
		store.setName("Updated");
		store.setBail(100.01f);
		store.setRotationIndex(2);
		mapper.updateById(store);
		store = mapper.findById(100);
		System.out.println(store.getName());
	}
}
