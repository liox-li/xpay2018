package com.xpay.pay.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xpay.pay.exception.Assert;
import com.xpay.pay.model.Agent;
import com.xpay.pay.model.App;
import com.xpay.pay.model.Store;
import com.xpay.pay.model.StoreChannel;
import com.xpay.pay.rest.contract.BaseResponse;
import com.xpay.pay.rest.contract.LoginRequest;
import com.xpay.pay.rest.contract.StoreResponse;
import com.xpay.pay.rest.contract.UpdateStoreChannelRequest;
import com.xpay.pay.service.AgentService;
import com.xpay.pay.service.AppService;
import com.xpay.pay.service.StoreService;

@CrossOrigin(maxAge = 3600)
@RestController
public class AgentRestService {
	@Autowired
	private AgentService agentService;
	@Autowired
	private AppService appService;
	@Autowired
	private StoreService storeService;
	
	@RequestMapping(value = "/agents", method = RequestMethod.GET)
	public BaseResponse<List<Agent>> findAll() {
		List<Agent> agents = agentService.findAll();
		
		BaseResponse<List<Agent>> response = new BaseResponse<List<Agent>>();
		response.setData(agents);
		return response;
	}
	
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
			
	@RequestMapping(value = "/{id}/channels", method = RequestMethod.GET)
	public BaseResponse<List<StoreChannel>> getAgentChannels(@PathVariable long id) {
		List<StoreChannel> channels = storeService.findChannelsByAgentId(id);
		BaseResponse<List<StoreChannel>> response = new BaseResponse<List<StoreChannel>>();
		response.setData(channels);
		return response;
	}
	
	@RequestMapping(value = "/{id}/channels/{channelId}", method = RequestMethod.DELETE)
	public BaseResponse<Boolean> deleteAgentChannel(@PathVariable long id, @PathVariable long channelId) {
		StoreChannel channel = storeService.findStoreChannelById(channelId);
		Assert.notNull(channel, String.format("Channel not found, channelId: %s, agentId: %s", channelId, id));
		boolean deleted = storeService.deleteStoreChannel(channel);
		BaseResponse<Boolean> response = new BaseResponse<Boolean>();
		response.setData(deleted);
		return response;
	}
	
	@RequestMapping(value = "/{id}/stores", method = RequestMethod.GET)
	public BaseResponse<List<StoreResponse>> getAgentStores(@PathVariable long id) {
		List<Store> stores = storeService.findByAgentId(id);
		List<StoreResponse> storeResponses = new ArrayList<StoreResponse>();
		for(Store store: stores) {
			StoreResponse storeResponse = new StoreResponse();
			storeResponse.setId(store.getId());
			storeResponse.setCode(store.getCode());
			storeResponse.setName(store.getName());
			storeResponse.setChannels(store.getChannels());
			storeResponses.add(storeResponse);
		}
		BaseResponse<List<StoreResponse>> response = new BaseResponse<List<StoreResponse>>();
		response.setData(storeResponses);
		return response;
	}
	
	@RequestMapping(value = "/{id}/stores/{storeId}/channels", method = RequestMethod.PATCH)
	public BaseResponse<UpdateStoreChannelRequest> updateStoreChannels(@PathVariable long id, 
			@PathVariable long storeId,
			@RequestBody(required = true) UpdateStoreChannelRequest request) {
		storeService.updateStoreChannels(storeId, request.getChannelIds());
		BaseResponse<UpdateStoreChannelRequest> response = new BaseResponse<UpdateStoreChannelRequest>();
		response.setData(request);
		return response;
	}
}
