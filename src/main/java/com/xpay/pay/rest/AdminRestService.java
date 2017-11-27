package com.xpay.pay.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.ApplicationConstants;
import com.xpay.pay.exception.Assert;
import com.xpay.pay.model.Agent;
import com.xpay.pay.service.AgentService;

public abstract class AdminRestService {
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	protected AgentService agentService;
	
	protected Agent getAgent() {
		String token = request.getHeader(ApplicationConstants.HEADER_ACCESS_TOKEN);
		Assert.notBlank(token, "Access token can't be null");
		
		Agent agent = agentService.findByToken(token);
		Assert.notNull(agent, "Invalid token");
		return agent;
	}
}
