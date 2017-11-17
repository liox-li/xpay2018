package com.xpay.pay.dao;

import java.util.Date;
import java.util.List;

import com.xpay.pay.model.Store;
import com.xpay.pay.model.StoreChannelStats;

public interface StoreMapper extends BaseMapper<Store> {
	public List<Store> findAll();
	
	public Store findByCode(String code);
	
	public List<Store> findByAgentId(long agentId);
	
//	public List<StoreChannelStats> statsByTime(String channelIds, Date createDate);
}
