package com.xpay.pay.dao;

import java.util.List;

import com.xpay.pay.models.App;

public interface AppMapper extends BaseMapper<App> {
	public List<App> findAll();


	public App findByKey(String key);
}
