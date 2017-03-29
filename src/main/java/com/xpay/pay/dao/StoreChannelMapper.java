package com.xpay.pay.dao;

import java.util.List;

import com.xpay.pay.models.StoreChannel;

public interface StoreChannelMapper extends BaseMapper<StoreChannel> {
	public List<StoreChannel> findByStoreId(long storeId);
}
