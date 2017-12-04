package com.xpay.pay.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xpay.pay.ApplicationConstants;
import com.xpay.pay.exception.Assert;
import com.xpay.pay.model.Agent;
import com.xpay.pay.model.App;
import com.xpay.pay.model.Order;
import com.xpay.pay.model.Store;
import com.xpay.pay.model.StoreChannel;
import com.xpay.pay.rest.contract.BaseResponse;
import com.xpay.pay.rest.contract.CreateAppRequest;
import com.xpay.pay.rest.contract.CreateStoreRequest;
import com.xpay.pay.rest.contract.LoginRequest;
import com.xpay.pay.rest.contract.RechargeRequest;
import com.xpay.pay.rest.contract.StoreResponse;
import com.xpay.pay.rest.contract.UpdateStoreChannelRequest;
import com.xpay.pay.rest.contract.UpdateStoreChannelResponse;
import com.xpay.pay.service.AppService;
import com.xpay.pay.service.OrderService;
import com.xpay.pay.service.StoreService;
import com.xpay.pay.util.TimeUtils;

@CrossOrigin(maxAge = 3600)
@RestController
public class AgentRestService extends AdminRestService {
	@Autowired
	private AppService appService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private OrderService orderService;
	
	@RequestMapping(value = "/agents", method = RequestMethod.GET)
	public BaseResponse<List<Agent>> findAll() {
		Agent agent = this.getAgent();
		Assert.isTrue(agent.getId() <= 10, 401, "401", "You are not allowed to access this API.");
		
		List<Agent> agents = agentService.findAll();
		
		BaseResponse<List<Agent>> response = new BaseResponse<List<Agent>>();
		response.setData(agents);
		if(CollectionUtils.isNotEmpty(agents)) {
			response.setCount(agents.size());
		}

		return response;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public BaseResponse<Agent> login(
			@RequestBody(required = false) LoginRequest request) {
		Assert.notNull(request, "Login request can not be null");
		Assert.isTrue(StringUtils.isNoneBlank(request.getAccount(), request.getPassword()), "Login request can not be null");
		
		Agent agent = agentService.findByAccount(request.getAccount());
		Assert.notNull(agent, String.format("Account not found, %s", request.getAccount()));
		Assert.isTrue(request.getPassword().equals(agent.getPassword()), "Invalid password");
		
		agentService.refreshToken(agent);
		
		BaseResponse<Agent> response = new BaseResponse<Agent>();
		agent.setPassword(null);
		response.setData(agent);
		return response;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public BaseResponse<Agent> createAccount(@PathVariable long id,
			@RequestBody(required = false) Agent agent) {
		validateAgent(id);
		
		Assert.notNull(agent, "Create account body can not be null");
		Assert.isTrue(StringUtils.isNoneBlank(agent.getAccount(), agent.getPassword(),agent.getName()), "Account name and password can not be null");
		
		Agent dbAgent = agentService.findByAccount(agent.getAccount());
		Assert.isTrue(dbAgent == null, String.format("Account already exit - %s", agent.getAccount()));
		agent.setAgentId(id);
		agentService.createAccount(agent);
		
		BaseResponse<Agent> response = new BaseResponse<Agent>();
		agent.setPassword(null);
		response.setData(agent);
		return response;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	public BaseResponse<Agent> updateAccount(@PathVariable long id,
			@RequestBody(required = false) Agent agent) {
		validateAgent(id);
		
		Assert.notNull(agent, "Update account body can not be null");
		Assert.isTrue(StringUtils.isNotBlank(agent.getAccount()), "Account name to be updated can not be null");
		Assert.isTrue((StringUtils.isNotBlank(agent.getPassword()) || agent.getRole()!=null), "Either password or role to be updated can't be null");
		
		Agent dbAgent = agentService.findByAccount(agent.getAccount());
		Assert.isTrue(dbAgent != null, String.format("Account not found - %s", agent.getAccount()));
		Assert.isTrue(id<=10 || id == dbAgent.getAgentId(), 401, "401", "Unauthorized request");
		
		if(StringUtils.isNotBlank(agent.getPassword())) {
			dbAgent.setPassword(agent.getPassword());
		}
		if(agent.getRole()!=null) {
			dbAgent.setRole(agent.getRole());
		}
		if(StringUtils.isNotBlank(agent.getName())) {
			dbAgent.setName(agent.getName());
		}
		agentService.updateAccount(dbAgent);
		
		BaseResponse<Agent> response = new BaseResponse<Agent>();
		agent.setPassword(null);
		response.setData(agent);
		return response;
	}
	
	@RequestMapping(value = "/{id}/apps", method = RequestMethod.GET)
	public BaseResponse<List<App>> getAgentApps(@PathVariable long id) {
		validateAgent(id);
		
		List<App> apps = appService.findByAgentId(id);
		BaseResponse<List<App>> response = new BaseResponse<List<App>>();
		response.setData(apps);
		if(CollectionUtils.isNotEmpty(apps)) {
			response.setCount(apps.size());
		}
		return response;
	}
	
	@RequestMapping(value = "/{id}/apps", method = RequestMethod.PUT)
	public BaseResponse<App> createAgentApp(@PathVariable long id, 
			@RequestBody(required = true) CreateAppRequest request) {
		validateAgent(id);
		Assert.notNull(request, "Create app request body can't be null");
		Assert.notNull(request.getName(), "App name can't be null");
		
		App app = appService.createApp(id, request.getName());
		BaseResponse<App> response = new BaseResponse<App>();
		response.setData(app);
		
		return response;
	}
			
	@RequestMapping(value = "/{id}/channels", method = RequestMethod.GET)
	public BaseResponse<List<StoreChannel>> getAgentChannels(@PathVariable long id) {
		validateAgent(id);
		List<StoreChannel> channels = storeService.findChannelsByAgentId(id);
		BaseResponse<List<StoreChannel>> response = new BaseResponse<List<StoreChannel>>();
		response.setData(channels);
		if(CollectionUtils.isNotEmpty(channels)) {
			response.setCount(channels.size());
		}
		return response;
	}
	
	@RequestMapping(value = "/{id}/channels", method = RequestMethod.PUT)
	public BaseResponse<StoreChannel> createAgentChannel(@PathVariable long id,
			@RequestBody(required = true)StoreChannel channel) {
		validateAgent(id);
		
		channel.setAgentId(id);
		storeService.createStoreChannel(channel);
		BaseResponse<StoreChannel> response = new BaseResponse<StoreChannel>();
		response.setData(channel);
		
		return response;
	}
	
	@RequestMapping(value = "/{id}/channels/{channelId}", method = RequestMethod.DELETE)
	public BaseResponse<Boolean> deleteAgentChannel(@PathVariable long id, @PathVariable long channelId) {
		validateAgent(id);
		
		StoreChannel channel = storeService.findStoreChannelById(channelId);
		Assert.notNull(channel, String.format("Channel not found, channelId: %s, agentId: %s", channelId, id));
		boolean deleted = storeService.deleteStoreChannel(channel);
		BaseResponse<Boolean> response = new BaseResponse<Boolean>();
		response.setData(deleted);
		return response;
	}
	
	@RequestMapping(value = "/{id}/stores", method = RequestMethod.GET)
	public BaseResponse<List<StoreResponse>> getAgentStores(@PathVariable long id) {
		validateAgent(id);
		
		List<Store> stores = storeService.findByAgentId(id);
		List<StoreResponse> storeResponses = new ArrayList<StoreResponse>();
		for(Store store: stores) {
			StoreResponse storeResponse = toStoreResponse(store);
			storeResponses.add(storeResponse);
		}
		BaseResponse<List<StoreResponse>> response = new BaseResponse<List<StoreResponse>>();
		response.setData(storeResponses);
		if(CollectionUtils.isNotEmpty(storeResponses)) {
			response.setCount(storeResponses.size());
		}
		return response;
	}
	
	@RequestMapping(value = "/{id}/stores", method = RequestMethod.PUT)
	public BaseResponse<StoreResponse> createAgentStore(@PathVariable long id, 
			@RequestBody(required = true) CreateStoreRequest request) {
		validateAgent(id);
		
		Assert.notNull(request, "Create store request body can't be null");
		Assert.notNull(request.getName(), "Store name can't be null");
		Assert.notNull(request.getAppId(), "AppId cant' be null");
		
		Store store = storeService.createStore(id, request.getName(), request.getBailPercentage(), request.getAppId(), request.getCsrTel(), request.getProxyUrl(), request.getDailyLimit());
		StoreResponse storeResponse = toStoreResponse(store);
		BaseResponse<StoreResponse> response = new BaseResponse<StoreResponse>();
		response.setData(storeResponse);
		return response;
	}
	
	@RequestMapping(value = "/{id}/stores", method = RequestMethod.PATCH)
	public BaseResponse<StoreResponse> updateAgentStore(@PathVariable long id, 
			@PathVariable long storeId, 
			@RequestBody(required = true) CreateStoreRequest request) {
		validateAgent(id);
		
		Store store = storeService.updateStore(storeId, request.getName(), request.getBailPercentage(), request.getAppId(), request.getCsrTel(), request.getProxyUrl(), request.getDailyLimit());
		StoreResponse storeResponse = toStoreResponse(store);
		BaseResponse<StoreResponse> response = new BaseResponse<StoreResponse>();
		response.setData(storeResponse);
		return response;
	}
	
	@RequestMapping(value = "/{id}/stores/{storeId}/recharge", method = RequestMethod.POST)
	public BaseResponse<StoreResponse> recharge(@PathVariable long id, 
			@PathVariable long storeId,
			@RequestBody(required = true) RechargeRequest request) {
		validateAgent(id);
		Assert.isTrue(request!=null && request.getAmount()>=100f, "Recharge amount must be greater than 100");
		
		Store store = storeService.recharge(id, storeId, request.getAmount());
		StoreResponse storeResponse = toStoreResponse(store);
		BaseResponse<StoreResponse> response = new BaseResponse<StoreResponse>();
		response.setData(storeResponse);
		return response;
	}
	
	@RequestMapping(value = "/{id}/stores/{storeId}/channels", method = RequestMethod.PATCH)
	public BaseResponse<UpdateStoreChannelResponse> updateStoreChannels(@PathVariable long id, 
			@PathVariable long storeId,
			@RequestBody(required = true) UpdateStoreChannelRequest request) {
		validateAgent(id);
		
		storeService.updateStoreChannels(storeId, request.getChannels());
		UpdateStoreChannelResponse updateStoreChannelResponse = new UpdateStoreChannelResponse();
		updateStoreChannelResponse.setStoreId(storeId);
		updateStoreChannelResponse.setChannels(request.getChannels());
		BaseResponse<UpdateStoreChannelResponse> response = new BaseResponse<UpdateStoreChannelResponse>();
		response.setData(updateStoreChannelResponse);
		return response;
	}
	
	@RequestMapping(value = "/{id}/orders", method = RequestMethod.GET)
	public BaseResponse<List<Order>> listOrders(@PathVariable long id, 
			@RequestParam(required = false) String storeId,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate) {
		validateAgent(id);
		
		Date startTime = TimeUtils.parseTime(startDate, TimeUtils.TimePatternDate);
		startTime = startTime == null?TimeUtils.beginOfDay(new Date()): startTime;
		Date endTime = TimeUtils.parseTime(endDate, TimeUtils.TimePatternDate);
		endTime = endTime == null?new Date(): TimeUtils.endOfDay(endTime);
		
		Assert.isTrue(TimeUtils.daysBetween(startTime, endTime)<3, "Can only fetch orders within two days.");
		
		List<Order> orders = null;
		if(StringUtils.isBlank(storeId)) {
			orders = orderService.findByAgentIdAndTime(id, startTime, endTime);
		} else {
			orders = orderService.findByStoreIdAndTime(storeId, startTime, endTime);
		}
		
		BaseResponse<List<Order>> response = new BaseResponse<List<Order>>();
		if(CollectionUtils.isNotEmpty(orders)) {
			response.setCount(orders.size());
		}
		response.setData(orders);
		return response;
	}
	
	private void validateAgent(long agentId) {
		Agent agent = this.getAgent();
		Assert.isTrue(agentId == agent.getId(), ApplicationConstants.STATUS_UNAUTHORIZED, "401", "Unauthorized request");
	}
	
	private StoreResponse toStoreResponse(Store store) {
		StoreResponse storeResponse = new StoreResponse();
		storeResponse.setId(store.getId());
		storeResponse.setCode(store.getCode());
		storeResponse.setName(store.getName());
		storeResponse.setBailPercentage(store.getBailPercentage());
		storeResponse.setCsrTel(store.getCsrTel());
		storeResponse.setAppId(store.getAppId());
		storeResponse.setProxyUrl(store.getProxyUrl());
		storeResponse.setDailyLimit(store.getDailyLimit());
		storeResponse.setTodayTradeAmount(store.getNonBail());
		storeResponse.setQuota(store.getQuota());
		return storeResponse;
	}
}
