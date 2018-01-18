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
import com.xpay.pay.model.StoreChannel.ChannelType;
import com.xpay.pay.model.StoreChannel.PaymentGateway;
import com.xpay.pay.model.StoreTransaction;
import com.xpay.pay.model.StoreTransaction.TransactionType;
import com.xpay.pay.rest.contract.BaseResponse;
import com.xpay.pay.rest.contract.CreateAppRequest;
import com.xpay.pay.rest.contract.CreateStoreChannelRequest;
import com.xpay.pay.rest.contract.CreateStoreRequest;
import com.xpay.pay.rest.contract.LoginRequest;
import com.xpay.pay.rest.contract.QuickCreateStoreRequest;
import com.xpay.pay.rest.contract.RechargeRequest;
import com.xpay.pay.rest.contract.RechargeResponse;
import com.xpay.pay.rest.contract.StoreResponse;
import com.xpay.pay.rest.contract.UpdateStoreChannelRequest;
import com.xpay.pay.rest.contract.UpdateStoreChannelResponse;
import com.xpay.pay.service.AgentService;
import com.xpay.pay.service.AppService;
import com.xpay.pay.service.OrderService;
import com.xpay.pay.service.PaymentService;
import com.xpay.pay.service.StoreService;
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
	@Autowired
	private AgentService agentService;
	
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
		this.assertAdmin();
		
		Assert.notNull(agent, "Create account body can not be null");
		Assert.isTrue(StringUtils.isNoneBlank(agent.getAccount(), agent.getPassword(),agent.getName()), "Account name and password can not be null");
		if(agent.getRole() == null) {
			agent.setRole(Role.STORE);
		}
		
		Assert.isTrue(agent.getRole() == Role.STORE || agent.getRole() == Role.AGENT, "Agent or Store role are supported");
		
		Agent dbAgent = agentService.findByAccount(agent.getAccount());
		Assert.isTrue(dbAgent == null, String.format("Account already exit - %s", agent.getAccount()));

		if(agent.getAgentId()==null) {
			agent.setAgentId(id);
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
		this.assertAdmin();
		
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
		this.assertAdmin();
		
		Assert.notNull(request, "Create app request body can't be null");
		Assert.notNull(request.getName(), "App name can't be null");
		
		Long agentId = id;
		if(request.getAgentId()!=null) {
			agentId = request.getAgentId();
		}
		
		App app = appService.createApp(agentId, request.getName());
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
			@RequestBody(required = true)CreateStoreChannelRequest request) {
		this.assertAdmin();
		
		Assert.isTrue(StringUtils.isNoneBlank(request.getExtStoreId(), request.getExtStoreName()), "ExtStoreId and name can't be null");
		Assert.notNull(request.getPaymentGateway(), "Payment gateway is must");
		
		Long agentId = id;
		if(request.getAgentId()!=null) {
			agentId = request.getAgentId();
		}
		StoreChannel channel = new StoreChannel();
		channel.setAgentId(agentId);
		channel.setExtStoreId(request.getExtStoreId());
		channel.setExtStoreName(request.getExtStoreName());
		channel.setChannelProps(request.getChinaUmsProps());
		channel.setPaymentGateway(request.getPaymentGateway());
	
		storeService.createStoreChannel(channel);
		BaseResponse<StoreChannel> response = new BaseResponse<StoreChannel>();
		response.setData(channel);
		
		return response;
	}
	
	@RequestMapping(value = "/{id}/channels/{channelId}", method = RequestMethod.DELETE)
	public BaseResponse<Boolean> deleteAgentChannel(@PathVariable long id, @PathVariable long channelId) {
		this.assertAdmin();
		
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
		
		Agent agent = agentService.findById(id);
		Assert.notNull(agent, String.format("Agent not found, %s", id));
		
		List<Store> stores = storeService.findByAgent(agent);
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
	
	@RequestMapping(value = "/{id}/stores/{storeId}", method = RequestMethod.GET)
	public BaseResponse<StoreResponse> getAgentStores(@PathVariable long id, @PathVariable long storeId) {
		validateAgent(id);
		
		Store store = storeService.findById(storeId);
		StoreResponse storeResponse = toStoreResponse(store);
		storeResponse.setApp(appService.findById(store.getAppId()));
		BaseResponse<StoreResponse> response = new BaseResponse<StoreResponse>();
		response.setData(storeResponse);
		
		return response;
	}
	
	@RequestMapping(value = "/{id}/stores", method = RequestMethod.PUT)
	public BaseResponse<StoreResponse> createAgentStore(@PathVariable long id, 
			@RequestBody(required = true) CreateStoreRequest request) {
		this.assertAdmin();
		
		Assert.notNull(request, "Create store request body can't be null");
		Assert.notNull(request.getName(), "Store name can't be null");
		Assert.notNull(request.getAppId(), "AppId cant' be null");
		
		Long agentId = request.getAgentId();
		if(agentId == null) {
			agentId = id;
		}
		
		Store store = storeService.createStore(agentId, request.getAdminId(),request.getName(), request.getBailPercentage(), request.getAppId(), request.getCsrTel(), request.getProxyUrl(), request.getDailyLimit(), null, null, request.getNotifyUrl());
		StoreResponse storeResponse = toStoreResponse(store);
		BaseResponse<StoreResponse> response = new BaseResponse<StoreResponse>();
		response.setData(storeResponse);
		return response;
	}
	
	@RequestMapping(value = "/{id}/stores/quick", method = RequestMethod.PUT)
	public BaseResponse<StoreResponse> createStore(@PathVariable long id, 
			@RequestBody(required = true) QuickCreateStoreRequest request) {
		this.assertAdmin();
		
		Assert.notNull(request, "Create store request body can't be null");
		Assert.notNull(request.getName(), "Store name can't be null");
		
		Long agentId = request.getAgentId();
		if(agentId == null) {
			agentId = id;
		}

		String code = IDGenerator.buildStoreCode();
		Agent admin = agentService.createAdmin(request.getName(), code, agentId);
		App app = appService.createApp(id, request.getName());
		Assert.notNull(app, "Can't create App");
		
		
		boolean storeWithChannel = false;
		if((request.getChannelId()!=null && request.getChannelId()>0) || request.getChinaUmsProps()!=null) {
			storeWithChannel = true;
		}
		
		StoreChannel channel = null;
		if(storeWithChannel) {
			if((request.getChannelId() == null || request.getChannelId()<=0)) {
				channel = new StoreChannel();
				channel.setAgentId(agentId);
				channel.setExtStoreId(request.getExtStoreId());
				channel.setExtStoreName(request.getExtStoreName());
				channel.setChannelProps(request.getChinaUmsProps());
				channel.setPaymentGateway(request.getPaymentGateway());
				
				storeService.createStoreChannel(channel);
			} else {
				channel = storeService.findStoreChannelById(request.getChannelId());
			}
		}
		Store store = storeService.createStore(agentId, admin.getId(), request.getName(), request.getBailPercentage(), app.getId(), request.getCsrTel(), request.getProxyUrl(), request.getDailyLimit(),code, request.getQuota(), request.getNotifyUrl());
		
		if(channel!=null) {
			storeService.updateStoreChannels(store.getId(), new long[] {channel.getId()});
		}
		StoreResponse storeResponse = toStoreResponse(store);
		BaseResponse<StoreResponse> response = new BaseResponse<StoreResponse>();
		response.setData(storeResponse);
		return response;
	}
	
	@RequestMapping(value = "/{id}/stores/{storeId}", method = RequestMethod.PATCH)
	public BaseResponse<StoreResponse> updateAgentStore(@PathVariable long id, 
			@PathVariable long storeId, 
			@RequestBody(required = true) CreateStoreRequest request) {
		this.assertAdmin();
		
		Store store = storeService.updateStore(storeId, request.getAgentId(), request.getName(), request.getBailPercentage(), request.getAppId(), request.getCsrTel(), request.getProxyUrl(), request.getDailyLimit(), request.getNotifyUrl());
		StoreResponse storeResponse = toStoreResponse(store);
		BaseResponse<StoreResponse> response = new BaseResponse<StoreResponse>();
		response.setData(storeResponse);
		return response;
	}
	
	@RequestMapping(value = "/{id}/stores/{storeId}/quota", method = RequestMethod.POST)
	public BaseResponse<StoreResponse> newQuota(@PathVariable long id, 
			@PathVariable long storeId,
			@RequestBody(required = true) RechargeRequest request) {
		this.assertAdmin();
		
		Assert.isTrue(request!=null && request.getAmount()>=10f, "Amount must be greater than 10");
		if(request.getTransactionType() == null) {
			request.setTransactionType(TransactionType.FREE);
		}
 		Store store = storeService.newQuota(id, storeId, request.getAmount(), request.getTransactionType());
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
	
		Store store = storeService.findById(BAIL_STORE_ID);
		Long appId = store.getAppId();
		String orderNo = IDGenerator.buildRechargeOrderNo(appId.intValue(), storeId);
		App app = appService.findById(appId);
		
		if(StringUtils.isNoneBlank(request.getCodeUrl())) {
			BaseResponse<RechargeResponse> response = new BaseResponse<RechargeResponse>();
			RechargeResponse rechargeResponse = new RechargeResponse();
			String codeUrl = request.getCodeUrl();
			StringBuffer sb = new StringBuffer();
			sb.append(codeUrl);
			sb.append("?uid=1&orderNo=");
			sb.append(orderNo);
			sb.append("&storeId=");
			sb.append(storeId);
			sb.append("&agentId=");
			sb.append(id);
			rechargeResponse.setCodeUrl(sb.toString());
			response.setData(rechargeResponse);
			return response;
		} else {
			Assert.isTrue(request!=null && request.getAmount()>=100f, "Recharge amount must be greater than 100");
			
			Order order = paymentService.createOrder(app, null, orderNo, store, request.getChannel(), null, "127.0.0.1", request.getAmount(), IDGenerator.formatTime(new Date(), IDGenerator.TimePattern14), "", null, null, null, subject, null);
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
	public BaseResponse<List<StoreTransaction>> listTransactions(@PathVariable long id, 
			@RequestParam(required = false) Long storeId,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate) {
		validateAgent(id);
		
		Date startTime = TimeUtils.parseTime(startDate, TimeUtils.TimePatternDate);
		startTime = startTime == null?TimeUtils.beginOfDay(new Date()): startTime;
		Date endTime = TimeUtils.parseTime(endDate, TimeUtils.TimePatternDate);
		endTime = endTime == null?new Date(): TimeUtils.endOfDay(endTime);

		List<StoreTransaction> transactions = new ArrayList<StoreTransaction>();
		if(storeId!=null) {
			transactions = storeService.findTransactionsByStoreId(storeId, startTime, endTime);
		} else {
			transactions = storeService.findTransactionsByAgentId(id, startTime, endTime);
		}
		
		BaseResponse<List<StoreTransaction>> response = new BaseResponse<List<StoreTransaction>>();
		if(CollectionUtils.isNotEmpty(transactions)) {
			response.setCount(transactions.size());
		}
		response.setData(transactions);
		return response;
	}
	
	@RequestMapping(value = "/{id}/stores/{storeId}/channels", method = RequestMethod.PATCH)
	public BaseResponse<UpdateStoreChannelResponse> updateStoreChannels(@PathVariable long id, 
			@PathVariable long storeId,
			@RequestBody(required = true) UpdateStoreChannelRequest request) {
		this.assertAdmin();
		
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
			Agent agent = agentService.findById(id);
			Assert.notNull(agent, String.format("Agent not found, %s", id));

			orders = orderService.findByAgentAndTime(agent, startTime, endTime);
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
	
	@RequestMapping(value = "/{id}/orders/{orderNo}", method = RequestMethod.GET)
	public BaseResponse<Order> findOrder(@PathVariable long id, 
			@PathVariable String orderNo) {
		validateAgent(id);
		
		Assert.notBlank(orderNo, "Order no can't be null");
		Order order = orderService.findAnyByOrderNo(orderNo);
		Assert.isTrue(order!=null && (id<=10 || id==order.getStore().getAdminId() || id==order.getStore().getAgentId()), "Order not found");
		
		BaseResponse<Order> response = new BaseResponse<Order>();
		response.setData(order);
		return response;
	}
	
	@RequestMapping(value = "/{id}/orders/{orderNo}", method = RequestMethod.DELETE)
	public BaseResponse<Order> refundOrder(@PathVariable long id, 
			@PathVariable String orderNo) {
		validateAgent(id);
		
		Assert.notBlank(orderNo, "Order no can't be null");
		Order order = orderService.findAnyByOrderNo(orderNo);
		Assert.isTrue(order!=null && order.isRefundable(), "Order is not paid or already refunded");
		
		try {
			Bill bill = paymentService.refund(order.getAppId(), order.getOrderNo(), order.getStore().getCode(), true);
		} catch(Exception e) {
			
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		order = orderService.findAnyByOrderNo(orderNo);
		BaseResponse<Order> response = new BaseResponse<Order>();
		response.setData(order);
		return response;

	}

	private StoreResponse toStoreResponse(Store store) {
		StoreResponse storeResponse = new StoreResponse();
		storeResponse.setId(store.getId());
		storeResponse.setCode(store.getCode());
		storeResponse.setName(store.getName());
		storeResponse.setBailPercentage(store.getBailPercentage());
		storeResponse.setCsrTel(store.getCsrTel());
		storeResponse.setApp(appService.findById(store.getAppId()));
		storeResponse.setProxyUrl(store.getProxyUrl());
		storeResponse.setNotifyUrl(store.getNotifyUrl());
		storeResponse.setDailyLimit(store.getDailyLimit());
		storeResponse.setTodayTradeAmount(store.getNonBail());
		storeResponse.setQuota(store.getQuota());
		storeResponse.setAdmin(agentService.findById(store.getAdminId()));
		storeResponse.setAgent(agentService.findById(store.getAgentId()));
		storeResponse.setChannels(store.getChannels());
		storeResponse.setLastRechargeAmount(store.getLastRechargeAmt());
		storeResponse.setLastTradeAmount(store.getLastTransSum());
		ChannelType channelType = StringUtils.isBlank(store.getChannelType())?this.toChannelType(store.getChannels()):ChannelType.valueOf(store.getChannelType());
		storeResponse.setChannelType(channelType);
		return storeResponse;
	}
	
	private ChannelType toChannelType(List<StoreChannel> channels) {
		ChannelType type = ChannelType.WECHAT;
		if(CollectionUtils.isNotEmpty(channels)) {
			StoreChannel storeChannel = channels.get(0);
			PaymentGateway paymentGateway = storeChannel.getPaymentGateway();
			switch(paymentGateway) {
				case KEKEPAY:
				case IPSQUICK:
				type = ChannelType.BANK;
			}
		}
		return type;
	}
}
