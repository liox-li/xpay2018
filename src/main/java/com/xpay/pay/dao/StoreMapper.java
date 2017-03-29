package com.xpay.pay.dao;

import java.util.List;

import com.xpay.pay.models.Store;

public interface StoreMapper extends BaseMapper<Store> {
	public List<Store> findAll();
	
	public Store findByCode(String code);
}
