package com.xpay.pay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.dao.StoreExtGoodsMapper;
import com.xpay.pay.model.StoreExtGoods;

@Service
public class StoreExtGoodsService {
	@Autowired
	private StoreExtGoodsMapper mapper;
	
	public List<StoreExtGoods> findByGoodsId(long goodsId) {
		return mapper.findByGoodsId(goodsId);
	}
	
	public List<StoreExtGoods> findByStoreId(long storeId) {
		return mapper.findByStoreId(storeId);
	}
	
	public List<StoreExtGoods> findByExtStoreId(String extStoreId) {
		return mapper.findByExtStoreId(extStoreId);
	}
}
