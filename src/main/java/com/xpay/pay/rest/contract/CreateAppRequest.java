package com.xpay.pay.rest.contract;

public class CreateAppRequest {
	private String name;
	private Long agentId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
	
	
}
