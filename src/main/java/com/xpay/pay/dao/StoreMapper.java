package com.xpay.pay.dao;

import java.util.List;

import com.xpay.pay.model.Store;

public interface StoreMapper extends BaseMapper<Store> {
	public List<Store> findAll();
	
	public Store findByCode(String code);
	
	public List<Store> findByAgentId(long agentId);
}
