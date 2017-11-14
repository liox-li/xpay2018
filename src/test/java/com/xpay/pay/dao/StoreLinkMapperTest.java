package com.xpay.pay.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.model.StoreLink;

public class StoreLinkMapperTest extends BaseSpringJunitTest {
	@Autowired
	protected StoreLinkMapper mapper;
	
	@Test
	public void testInert() {
		StoreLink link = new StoreLink();
		link.setStoreId(11);
		link.setLink("http://www.baidu.com");
		mapper.insert(link);
	}
	
	@Test
	public void testFindById() {
		StoreLink link = mapper.findById(100);
		System.out.println(link.getLink());
	}
	
	@Test
	public void testUpdate() {
		StoreLink link = mapper.findById(100);
		link.setLink("http://www.google.com");
		mapper.updateById(link);
		link = mapper.findById(100);
		System.out.println(link.getLink());
	}
	
	@Test
	public void testDelete() {
		mapper.deleteById(100);
		StoreLink link = mapper.findById(100);
		System.out.println(link);
	}
	
	@Test
	public void testFindByStoreId() {
		List<StoreLink> links = mapper.findByStoreId(11);
		
		System.out.println(links.size());
	}
}
