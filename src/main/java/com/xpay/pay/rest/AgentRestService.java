package com.xpay.pay.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xpay.pay.exception.Assert;
import com.xpay.pay.model.Agent;
import com.xpay.pay.model.App;
import com.xpay.pay.rest.contract.BaseResponse;
import com.xpay.pay.rest.contract.LoginRequest;
import com.xpay.pay.service.AgentService;
import com.xpay.pay.service.AppService;

@RestController
public class AgentRestService {
	@Autowired
	private AgentService agentService;
	@Autowired
	private AppService appService;
	
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public BaseResponse<Agent> login(
			@RequestBody(required = false) LoginRequest request) {
		Assert.notNull(request, "Login request can not be null");
		
		Agent agent = agentService.login(request.getAccount(), request.getPassword());
		Assert.notNull(request, String.format("Account not found, %s", request.getAccount()));
		
		BaseResponse<Agent> response = new BaseResponse<Agent>();
		response.setData(agent);
		return response;
	}
	
	@RequestMapping(value = "/{id}/apps", method = RequestMethod.GET)
	public BaseResponse<List<App>> getAgentApps(@PathVariable long id) {
		List<App> apps = appService.findByAgentId(id);
		BaseResponse<List<App>> response = new BaseResponse<List<App>>();
		response.setData(apps);
		return response;
	}
			

}
