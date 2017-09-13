package com.xpay.pay.dao;

import java.util.List;

import com.xpay.pay.model.StoreChannel;

public interface StoreChannelMapper extends BaseMapper<StoreChannel> {
	public List<StoreChannel> findAll();
}
