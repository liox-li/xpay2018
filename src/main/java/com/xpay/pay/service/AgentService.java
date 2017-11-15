package com.xpay.pay.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.dao.AgentMapper;
import com.xpay.pay.model.Agent;

@Service
public class AgentService {
	@Autowired
	protected AgentMapper agentMapper;
	
	public Agent login(String account, String password) {
		if(StringUtils.isAnyBlank(account, password)) {
			return null;
		}
		
		Agent agent = agentMapper.findByAccount(account);
		if(agent!=null && password.equals(agent.getPassword())) {
			agent.setPassword(null);
			return agent;
		}  else {
			return null;
		}
	}
	
	public List<Agent> findAll() {
		return agentMapper.findAll();
	}
	
}
