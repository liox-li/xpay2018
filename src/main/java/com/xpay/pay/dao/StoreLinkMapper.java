package com.xpay.pay.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xpay.pay.model.StoreLink;

public interface StoreLinkMapper extends BaseMapper<StoreLink> {
	public List<StoreLink> findByStoreId(@Param("storeId") long storeId);
}
