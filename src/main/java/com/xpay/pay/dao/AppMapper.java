package com.xpay.pay.dao;

import com.xpay.pay.models.App;

public interface AppMapper extends BaseMapper<App> {
	public App findByKey(String key);
}
