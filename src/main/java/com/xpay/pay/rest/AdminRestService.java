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
	
	protected void validateAgent(long agentId) {
		Agent agent = this.getAgent();
		Assert.isTrue(agent.getId()<=10 || agentId == agent.getId(), ApplicationConstants.STATUS_UNAUTHORIZED, "401", "Unauthorized request");
	}
	
	protected void assertAdmin() {
		Assert.isTrue(this.getAgent().getId()<=10, ApplicationConstants.STATUS_UNAUTHORIZED, "401", "Unauthorized request");
	}
	
	protected void assertGeneral(Long agentId, Agent agent) {
		Assert.isTrue(agentId<=10 || agentId == agent.getAgentId() || agentId==agent.getId(), ApplicationConstants.STATUS_UNAUTHORIZED, "401", "Unauthorized request");
	}
	
}
