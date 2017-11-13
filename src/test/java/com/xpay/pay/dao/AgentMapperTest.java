package com.xpay.pay.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.model.Agent;

public class AgentMapperTest extends BaseSpringJunitTest {
	@Autowired
	protected AgentMapper mapper;
	
	@Test
	public void testInsert() {
		Agent agent = new Agent();
		agent.setAccount("test");
		agent.setPassword("test");
		agent.setName("test");
		mapper.insert(agent);
		System.out.println(agent.getId());
	}
	
	@Test
	public void testFindById() {
		Agent agent = mapper.findById(10);
		System.out.println(agent.getName());
	}
	
	@Test
	public void testFindByKey() {
		Agent agent = mapper.findByAccount("test");
		System.out.println(agent.getName());
	}
	
	@Test
	public void testFindAll() {
		List<Agent> agents = mapper.findAll();
		System.out.println(agents.size());
	}
	
	@Test
	public void testDelete() {
		Agent agent = mapper.findByAccount("test");
		mapper.deleteById(agent.getId());
		agent = mapper.findByAccount("test");
		System.out.println(agent);
	}
}
