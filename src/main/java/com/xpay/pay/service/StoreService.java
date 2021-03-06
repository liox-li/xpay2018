package com.xpay.pay.service;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.xpay.pay.MemoryCache;
import com.xpay.pay.cache.CacheManager;
import com.xpay.pay.cache.ICache;
import com.xpay.pay.dao.StoreChannelMapper;
import com.xpay.pay.dao.StoreLinkMapper;
import com.xpay.pay.dao.StoreMapper;
import com.xpay.pay.dao.StoreTransactionMapper;
import com.xpay.pay.dao.SubChannelMapper;
import com.xpay.pay.model.Agent;
import com.xpay.pay.model.Agent.Role;
import com.xpay.pay.model.StoreChannel.PaymentGateway;
import com.xpay.pay.model.Store;
import com.xpay.pay.model.StoreChannel;
import com.xpay.pay.model.StoreGoods;
import com.xpay.pay.model.StoreLink;
import com.xpay.pay.model.StoreTransaction;
import com.xpay.pay.model.StoreTransaction.TransactionType;
import com.xpay.pay.model.SubChannel;
import com.xpay.pay.model.SubChannel.Status;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.util.IDGenerator;

@Service
public class StoreService {
	@Autowired
	protected StoreMapper storeMapper;
	@Autowired
	protected StoreChannelMapper storeChannelMapper;
	@Autowired
	protected SubChannelMapper subChannelMapper;
	@Autowired
	protected StoreTransactionMapper storeTransactionMapper;
	@Autowired
	protected StoreLinkMapper storeLinkMapper;
	@Autowired
	private PaymentService paymentService;
	protected final Logger logger = LogManager.getLogger(StoreService.class);
	
	private static ICache<Long, StoreChannel> channelCache = CacheManager.create(StoreChannel.class, 2000);
	private static ICache<Long, List<StoreLink>> linkCache = CacheManager.create(List.class, 5000);
	
	public Store findByCode(String code) {		
		Store store = storeMapper.findByCode(code);
		Assert.notNull(store, "Unknow storeId "+code);
		List<StoreChannel> channels = this.findChannelByIds(store.getChannelIds());
	//	Assert.notEmpty(channels, "No valid channel for store "+code);
		store.setChannels(channels);
		store.setBailChannels(this.findChannelByIds(store.getBailChannelIds()));
		store.setLinks(this.findStoreLinkByStoreId(store.getId()));
		return store;
	}

	public Store findById(long id) {
		Store store = storeMapper.findById(id);
		Assert.notNull(store, "Unknow storeId "+id);
		List<StoreChannel> channels = this.findChannelByIds(store.getChannelIds());
//		Assert.notEmpty(channels, "No valid channel for store "+id);
		store.setChannels(channels);
		store.setBailChannels(this.findChannelByIds(store.getBailChannelIds()));
		store.setLinks(this.findStoreLinkByStoreId(store.getId()));
		return store;
	}
	
	public List<Store> findByAgent(Agent agent) {
		List<Store> stores = null;
		if(agent.getRole() == Role.ADMIN) {
			stores = storeMapper.findAll();
		} else if(agent.getRole() == Role.AGENT) {
			stores = storeMapper.findByAgentId(agent.getId());
		} else {
			stores = storeMapper.findByAdminId(agent.getId());
		}
		
		List<Store> result = Lists.newArrayList();
		for(Store store : stores) {
			result.add(this.findById(store.getId()));
		}
		return result;
	}
	
	public List<StoreChannel> findChannelsByAgentId(long agentId) {
		return storeChannelMapper.findByAgentId(agentId);
	}
	
	public boolean updateById(Store store) {
		return storeMapper.updateById(store);
	}
	
	public StoreChannel findStoreChannelById(Long id) {
		if(id == null || id<=0) {
			return null;
		}
		return channelCache.get(id);
	}
	
	private static final Float INIT_FREE_QUOTA = 2000f;
	private static final Long DEFAULT_DAILY_LIMIT = 50000L;
	private static final Float DEFAULT_BAR = 1000f;
	public Store createStore(long agentId, long adminId, String name, Float bailPercentage, long appId, String csrTel, String proxyUrl, Long dailyLimit, String code, Long quota, String notifyUrl, String returnUrl, Float bar) {
		Float thisBaiPercentage = bailPercentage>0 && bailPercentage<10?bailPercentage:2;
		Store store = new Store();
		store.setAgentId(agentId);
		store.setAdminId(adminId);
		store.setAppId(appId);
		String thisCode = StringUtils.isBlank(code)?IDGenerator.buildStoreCode():code;
		store.setCode(thisCode);
		store.setName(name);
		store.setBailPercentage(thisBaiPercentage);
		store.setCsrTel(csrTel);
		store.setProxyUrl(proxyUrl);
		store.setReturnUrl(returnUrl);
		if(bar!=null && bar>1000f) {
			store.setBar(DEFAULT_BAR);
		} else {
			store.setBar(DEFAULT_BAR);
		}
		store.setNotifyUrl(notifyUrl);
		Float thisQuota = quota == null ?INIT_FREE_QUOTA:quota;
		store.setQuota(thisQuota.floatValue());
		long thisDailyLimit = dailyLimit == null ?DEFAULT_DAILY_LIMIT:dailyLimit;
		store.setDailyLimit(thisDailyLimit);
		storeMapper.insert(store);
		
		StoreTransaction transaction = new StoreTransaction();
		transaction.setAgentId(agentId);
		transaction.setAmount(0f);
		transaction.setQuota(INIT_FREE_QUOTA);
		transaction.setOperation(TransactionType.INIT_FREE);
		transaction.setStoreId(store.getId());
		transaction.setBailPercentage(store.getBailPercentage());
		transaction.setOrderNo(IDGenerator.buildShortOrderNo());
		transaction.setStatus(OrderStatus.SUCCESS);
		storeTransactionMapper.insert(transaction);
		
		return store;
	}
	
	public Store updateStore(Long storeId, Long agentId, Long adminId, String name, Float bailPercentage, Long appId, String csrTel, String proxyUrl, Long dailyLimit, String notifyUrl, String returnUrl) {
		Store store = storeMapper.findById(storeId);
		if(StringUtils.isNotBlank(name)) {
			store.setName(name);
		}
		if(bailPercentage!=null && bailPercentage >0) {
			store.setBailPercentage(bailPercentage);
		}
		if(appId!=null && appId>0) {
			store.setAppId(appId);
		}
		if(StringUtils.isNotBlank(csrTel)) {
			store.setCsrTel(csrTel);
		}
		if(StringUtils.isNotBlank(proxyUrl)) {
			store.setProxyUrl(proxyUrl);
		}
		if(dailyLimit!=null && dailyLimit>0) {
			store.setDailyLimit(dailyLimit);
		}
		if(agentId !=null && agentId>0) {
			store.setAgentId(agentId);
		}
		if(adminId!=null && adminId>0) {
			store.setAdminId(adminId);
		}
		if(StringUtils.isNotBlank(notifyUrl)) {
			store.setNotifyUrl(notifyUrl);
		}
		if(StringUtils.isNotBlank(returnUrl)) {
			store.setReturnUrl(returnUrl);
		}
		storeMapper.updateById(store);
		return store;
	}
	
	public StoreTransaction rechargeOrder(long agentId, long storeId, Float amount, String orderNo) {
		Store store = this.findById(storeId);
		int addQuota = (int)(amount *100 / (store.getBailPercentage()-store.getBaseBailPercentage()));
//		store.setQuota(store.getQuota()+addQuota);
//		storeMapper.updateById(store);
		
		StoreTransaction transaction = new StoreTransaction();
		transaction.setAgentId(agentId);
		transaction.setAmount(amount);
		transaction.setQuota(Float.valueOf(addQuota));
		transaction.setOperation(TransactionType.RECHARGE);
		transaction.setStoreId(store.getId());
		transaction.setBailPercentage(store.getBailPercentage());
		transaction.setOrderNo(orderNo);
		transaction.setStatus(OrderStatus.NOTPAY);
		storeTransactionMapper.insert(transaction);
		
		return transaction;
	}
	
	public StoreTransaction rechargeOrder(long agentId, Store store, StoreGoods goods, String orderNo) {
		int addQuota = (int)(goods.getAmount() *100 / (store.getBailPercentage()-0.6f));
//		store.setQuota(store.getQuota()+addQuota);
//		storeMapper.updateById(store);
		
		StoreTransaction transaction = new StoreTransaction();
		transaction.setAgentId(agentId);
		transaction.setAmount(goods.getAmount());
		transaction.setQuota(Float.valueOf(addQuota));
		transaction.setOperation(TransactionType.RECHARGE);
		transaction.setStoreId(store.getId());
		transaction.setBailPercentage(store.getBailPercentage());
		transaction.setOrderNo(orderNo);
		transaction.setStatus(OrderStatus.NOTPAY);
		storeTransactionMapper.insert(transaction);
		
		return transaction;
	}
	
	
	public List<StoreTransaction> findTransactionsByStoreId(long storeId, Date startTime, Date endTime) {
		return storeTransactionMapper.findByStoreIdAndTime(storeId, startTime, endTime);
	}
	
	public List<StoreTransaction> findTransactionsByAgentId(long agentId, Date startTime, Date endTime) {
		return storeTransactionMapper.findByAgentIdAndTime(agentId, startTime, endTime);
	}
	
	public StoreTransaction findTransactionById(long transId) {
		return storeTransactionMapper.findById(transId);
	}
	
	public Store newQuota(long agentId, long storeId, Float amount, TransactionType transactionType) {
		Store store = storeMapper.findById(storeId);
		int addQuota = (int)(amount *100 / (store.getBailPercentage()-store.getBaseBailPercentage()));
		store.setQuota(store.getQuota()+addQuota);
		storeMapper.updateById(store);
		
		StoreTransaction transaction = new StoreTransaction();
		transaction.setAgentId(agentId);
		transaction.setAmount(amount.floatValue());
		transaction.setQuota(Float.valueOf(addQuota));
		transaction.setOperation(transactionType);
		transaction.setStoreId(store.getId());
		transaction.setBailPercentage(store.getBailPercentage());
		transaction.setOrderNo(IDGenerator.buildShortOrderNo());
		transaction.setStatus(OrderStatus.SUCCESS);
		storeTransactionMapper.insert(transaction);
		
		return store;
	}
	
	public void settleRechargeTransaction(String orderNo) {
		StoreTransaction transaction = storeTransactionMapper.findByOrderNo(orderNo);
		if(transaction != null && transaction.getStatus() != OrderStatus.SUCCESS) {
			transaction.setStatus(OrderStatus.SUCCESS);
			storeTransactionMapper.updateById(transaction);
			
			Store store = storeMapper.findById(transaction.getStoreId());
			store.setQuota(store.getQuota()+transaction.getQuota());
			store.setLastRechargeAmt(transaction.getAmount());
			storeMapper.updateById(store);
		}
	}
	
	public Boolean createStoreChannel(StoreChannel channel) {
		return storeChannelMapper.insert(channel);
	}
	
	public Boolean deleteStoreChannel(StoreChannel channel) {
		channelCache.remove(channel.getId());
		return storeChannelMapper.deleteById(channel.getId());
	}
	
	public Boolean updateStoreChannel(StoreChannel channel) {
		StoreChannel storeChannel = new StoreChannel();
		storeChannel.setId(channel.getId());
		return storeChannelMapper.updateById(storeChannel);
	}
	
	public List<StoreLink> findStoreLinkByStoreId(long storeId) {
		List<StoreLink> links = linkCache.get(storeId);
		if(CollectionUtils.isEmpty(links)) {
			links = storeLinkMapper.findByStoreId(storeId);
			linkCache.put(storeId, links);
		}
		return links;
	}
	
	public void updateStoreChannels(long storeId, long[] channelIds) {
		Store store = this.findById(storeId);
		String channels = StringUtils.join(channelIds, ',');
		store.setChannelIds(channels);
		storeMapper.updateById(store);
	}
	
	public void refreshCache() {
		channelCache.destroy();
		initStoreChannelCache();
		linkCache.destroy();
	}
	
	@PostConstruct
	private void initStoreChannelCache() {
		/**初始化ips轮询环境，一定要在初始化storechannels 前做 */
		paymentService.initIPSLoopEnv(PaymentGateway.IPSSCAN);
		if(channelCache.size() == 0) {
			List<StoreChannel> channels = storeChannelMapper.findAll();
			for(StoreChannel channel: channels) {
				channelCache.put(channel.getId(), channel);
			}
		}
	}
	
	private List<StoreChannel> findChannelByIds(String channelIds) {
		if(StringUtils.isBlank(channelIds)) {
			return null;
		}
		initStoreChannelCache();
		
		List<StoreChannel> list = Lists.newArrayList();
		String[] idStrs = StringUtils.split(channelIds, ",");
		for(String idStr: idStrs) {
			StoreChannel channel = channelCache.get(Long.valueOf(idStr));
			if(channel!=null) {
				list.add(channel);
			}
		}
		return list;
	}

	public StoreTransaction findTransactionByOrderNo(String orderNo) {
		return storeTransactionMapper.findByOrderNo(orderNo);
	}

	public boolean deleteStore(long storeId) {
		return storeMapper.deleteById(storeId);
	}
	
	public Boolean createSubChannel(SubChannel subChannel) {
		return subChannelMapper.insert(subChannel);
	}
	
	public Boolean deleteSubChannel(SubChannel subChannel) {
		MemoryCache.IPS_STORE_SUB_CHANNEL.remove(subChannel);
		return  subChannelMapper.deleteById(subChannel.getId());
	}
	
	public List<SubChannel> listSubChannel(Status status) {
		if( status == null){
			return subChannelMapper.findAll();
		}else{
			return subChannelMapper.findByStatus(status.name());
		}
		
	}
	
	public List<SubChannel> listSubChannelByPoolType(String poolType) {
		return subChannelMapper.findByPoolType(poolType);
		
	}
	
	public void changeSubChannelStatus(Long subChannelId,Status status) {
		 this.subChannelMapper.changeSubChannelStatus(subChannelId,status.name());
		
	}
	
	public static void main(String arg[]){
		String[] idStrs = StringUtils.split("2266", ",");
		System.out.println(idStrs[0]);
	}

}
