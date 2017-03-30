package com.xpay.pay.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.model.App;

public class AppMapperTest extends BaseSpringJunitTest {
	@Autowired
	protected AppMapper appMapper;
	
	@Test
	public void testInsert() {
		App app = new App();
		app.setKey("123");
		app.setSecret("456");
		
		appMapper.insert(app);
		System.out.println(app.getId());
	}
	
	@Test
	public void testFindById() {
		App app = appMapper.findById(10);
		System.out.println(app.getSecret());
	}
	
	@Test
	public void testFindByKey() {
		App app = appMapper.findByKey("123");
		System.out.println(app.getSecret());
	}
	
	@Test
	public void testFindAll() {
		List<App> apps = appMapper.findAll();
		System.out.println(apps.size());
	}
	
	@Test
	public void testDelete() {
		App app = appMapper.findByKey("123");
		appMapper.deleteById(app.getId());
		app = appMapper.findByKey("123");
		System.out.println(app);
	}
}
