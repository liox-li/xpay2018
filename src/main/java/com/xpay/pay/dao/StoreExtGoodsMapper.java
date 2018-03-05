package com.xpay.pay.dao;

import java.util.List;

import com.xpay.pay.model.StoreExtGoods;

public interface StoreExtGoodsMapper extends BaseMapper<StoreExtGoods> {
	public List<StoreExtGoods> findByGoodsId(long goodsId);
	
	public List<StoreExtGoods> findByAdminId(long adminId);
	
	public List<StoreExtGoods> findByExtStoreId(String extStoreId);
	
	public Boolean deleteByExtStoreId(String extStoreId);
}

