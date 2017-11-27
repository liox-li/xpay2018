package com.xpay.pay.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.dao.AgentMapper;
import com.xpay.pay.model.Agent;
import com.xpay.pay.util.IDGenerator;

@Service
public class AgentService {
	@Autowired
	protected AgentMapper agentMapper;
	
	public Agent findByAccount(String account) {
		Agent agent = agentMapper.findByAccount(account);
		return agent;
	}
	
	public Agent refreshToken(Agent agent) {
		if(isTokenExpired(agent.getToken())) {
			agent.setToken(this.buildToken());
			agentMapper.updateById(agent);
		}
		return agent;
	}
	
	public Agent findById(Long id) {
		Agent agent = agentMapper.findById(id);
		return agent;
	}
	
	public Agent findByToken(String token) {
		Agent agent = agentMapper.findByToken(token);
		return agent;
	}
	
	public List<Agent> findAll() {
		return agentMapper.findAll();
	}
	
	public static final long token_timeout = 24 * 60 * 60 * 1000L;
	private boolean isTokenExpired(String token) {
		if (StringUtils.isBlank(token) || token.length()<25) {
			return true;
		}
		try {
			long tokenTime = Long.valueOf(token.substring(10, 23));
			return System.currentTimeMillis() - tokenTime > token_timeout;
		} catch (Exception e) {
			return true;
		}
	}
	
	private String buildToken() {
		return IDGenerator.buildKey(10) + System.currentTimeMillis()
				+ IDGenerator.buildKey(9);
	}
}
