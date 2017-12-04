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
import com.xpay.pay.exception.ApplicationException;
import com.xpay.pay.exception.Assert;
import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.model.Agent;
import com.xpay.pay.model.Agent.Role;
import com.xpay.pay.model.App;
import com.xpay.pay.model.Bill;
import com.xpay.pay.model.Order;
import com.xpay.pay.model.Store;
import com.xpay.pay.model.StoreChannel;
import com.xpay.pay.model.StoreTransaction;
import com.xpay.pay.rest.contract.BaseResponse;
import com.xpay.pay.rest.contract.CreateAppRequest;
import com.xpay.pay.rest.contract.CreateStoreRequest;
import com.xpay.pay.rest.contract.LoginRequest;
import com.xpay.pay.rest.contract.RechargeRequest;
import com.xpay.pay.rest.contract.RechargeResponse;
import com.xpay.pay.rest.contract.StoreResponse;
import com.xpay.pay.rest.contract.UpdateStoreChannelRequest;
import com.xpay.pay.rest.contract.UpdateStoreChannelResponse;
import com.xpay.pay.service.AppService;
import com.xpay.pay.service.OrderService;
import com.xpay.pay.service.PaymentService;
import com.xpay.pay.service.StoreService;
import com.xpay.pay.util.CommonUtils;
import com.xpay.pay.util.IDGenerator;
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
	@Autowired
	private PaymentService paymentService;
	
	@RequestMapping(value = "/agents", method = RequestMethod.GET)
	public BaseResponse<List<Agent>> findAll() {
		this.assertAdmin();
		
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
		this.assertNotStore();
		
		Assert.notNull(agent, "Create account body can not be null");
		Assert.isTrue(StringUtils.isNoneBlank(agent.getAccount(), agent.getPassword(),agent.getName()), "Account name and password can not be null");
		
		Agent dbAgent = agentService.findByAccount(agent.getAccount());
		Assert.isTrue(dbAgent == null, String.format("Account already exit - %s", agent.getAccount()));

		if(agent.getAgentId()==null) {
			agent.setAgentId(id);
		}
		if(agent.getRole() == null) {
			agent.setRole(Role.STORE);
		}
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
		this.assertGeneral(id, dbAgent);
		
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
		dbAgent.setPassword(null);
		response.setData(dbAgent);
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
		this.assertNotStore();
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
		if(channel.getAgentId()==null) {
			channel.setAgentId(id);
		}
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
		this.assertNotStore();
		Assert.notNull(request, "Create store request body can't be null");
		Assert.notNull(request.getName(), "Store name can't be null");
		Assert.notNull(request.getAppId(), "AppId cant' be null");
		
		Store store = storeService.createStore(id, request.getName(), request.getBailPercentage(), request.getAppId(), request.getCsrTel(), request.getProxyUrl(), request.getDailyLimit());
		StoreResponse storeResponse = toStoreResponse(store);
		BaseResponse<StoreResponse> response = new BaseResponse<StoreResponse>();
		response.setData(storeResponse);
		return response;
	}
	
	@RequestMapping(value = "/{id}/stores/{storeId}", method = RequestMethod.PATCH)
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
	
	@RequestMapping(value = "/{id}/stores/{storeId}/free_quota", method = RequestMethod.POST)
	public BaseResponse<StoreResponse> newQuota(@PathVariable long id, 
			@PathVariable long storeId,
			@RequestBody(required = true) RechargeRequest request) {
		validateAgent(id);
		this.assertAdmin();
		
		Assert.isTrue(request!=null && request.getQuota()>=2000f, "Quota amount must be greater than 2000");
		
		Store store = storeService.newQuota(id, storeId, request.getQuota());
		StoreResponse storeResponse = toStoreResponse(store);
		BaseResponse<StoreResponse> response = new BaseResponse<StoreResponse>();
		response.setData(storeResponse);
		return response;
	}
	
	private static final String subject = "纳优游戏充值";
	private static final long BAIL_STORE_ID=1;
	@RequestMapping(value = "/{id}/stores/{storeId}/recharge_order", method = RequestMethod.POST)
	public BaseResponse<RechargeResponse> placeOrder(@PathVariable long id, 
			@PathVariable long storeId,
			@RequestBody(required = true) RechargeRequest request) {
		validateAgent(id);
		Assert.isTrue(request!=null && request.getAmount()>=100f, "Recharge amount must be greater than 100");
		
		Store store = storeService.findById(BAIL_STORE_ID);
		int appId = CommonUtils.toInt(store.getAppId());
		String orderNo = IDGenerator.buildRechargeOrderNo(appId, storeId);
		App app = appService.findById(appId);
		
		
		Order order = paymentService.createOrder(app, orderNo, store, request.getChannel(), null, "127.0.0.1", request.getAmount(), IDGenerator.formatTime(new Date(), IDGenerator.TimePattern14), "", null, null, null, subject, null);
		Assert.notNull(order,"Create order failed");
		Bill bill = null;
		BaseResponse<RechargeResponse> response = new BaseResponse<RechargeResponse>();
		try {
			bill = paymentService.unifiedOrder(order);
			if(bill!=null) {
				StoreTransaction transaction = storeService.rechargeOrder(id, storeId, request.getAmount(),orderNo);
				
				RechargeResponse rechargeResponse = new RechargeResponse();
				rechargeResponse.setTransactionId(transaction.getId());
				rechargeResponse.setCodeUrl(bill.getCodeUrl());
				response.setData(rechargeResponse);
			}
		} catch (GatewayException e) {
			response.setStatus(ApplicationConstants.STATUS_BAD_GATEWAY);
			response.setCode(e.getCode());
			response.setMessage(e.getMessage());
		} catch (ApplicationException e) {
			response.setStatus(ApplicationConstants.STATUS_INTERNAL_SERVER_ERROR);
			response.setCode(e.getCode());
			response.setMessage(e.getMessage());
		} finally {
			paymentService.updateBill(order, bill);
		}
		return response;
	}
	
	@RequestMapping(value = "/{id}/transactions/{transactionId}", method = RequestMethod.GET)
	public BaseResponse<StoreTransaction> listTransaction(@PathVariable long id, 
			@PathVariable  Long transactionId) {
		validateAgent(id);
		
		StoreTransaction transactions = storeService.findTransactionById(transactionId);
		BaseResponse<StoreTransaction> response = new BaseResponse<StoreTransaction>();
		response.setData(transactions);
		return response;
	}
	
	@RequestMapping(value = "/{id}/transactions", method = RequestMethod.GET)
	public BaseResponse<List<StoreTransaction>> listTransaction(@PathVariable long id, 
			@RequestParam(required = false) Long storeId,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate) {
		validateAgent(id);
		
		Date startTime = TimeUtils.parseTime(startDate, TimeUtils.TimePatternDate);
		startTime = startTime == null?TimeUtils.beginOfDay(new Date()): startTime;
		Date endTime = TimeUtils.parseTime(endDate, TimeUtils.TimePatternDate);
		endTime = endTime == null?new Date(): TimeUtils.endOfDay(endTime);

		List<StoreTransaction> transactions = storeService.findTransactions(storeId, startTime, endTime);
		BaseResponse<List<StoreTransaction>> response = new BaseResponse<List<StoreTransaction>>();
		response.setData(transactions);
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
	
	private void assertAdmin() {
		Assert.isTrue(this.getAgent().getId()<=10, ApplicationConstants.STATUS_UNAUTHORIZED, "401", "Unauthorized request");
	}
	
	private void assertNotStore() {
		Assert.isTrue(this.getAgent().getRole()!=Role.STORE, ApplicationConstants.STATUS_UNAUTHORIZED, "401", "Unauthorized request");
	}
	
	private void assertGeneral(Long agentId, Agent agent) {
		Assert.isTrue(agentId<=10 || agentId == agent.getAgentId() || agentId==agent.getId(), ApplicationConstants.STATUS_UNAUTHORIZED, "401", "Unauthorized request");
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
