package com.xpay.pay.dao;

import java.util.List;

import com.xpay.pay.model.Agent;

public interface AgentMapper extends BaseMapper<Agent> {
	public List<Agent> findAll();


	public Agent findByAccount(String account);
	
	public Agent findByToken(String token);
}
