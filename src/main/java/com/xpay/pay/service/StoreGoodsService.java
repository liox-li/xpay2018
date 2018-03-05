package com.xpay.pay.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.dao.StoreExtGoodsMapper;
import com.xpay.pay.dao.StoreGoodsMapper;
import com.xpay.pay.exception.Assert;
import com.xpay.pay.model.StoreExtGoods;
import com.xpay.pay.model.StoreGoods;
import com.xpay.pay.model.StoreGoods.ExtGoods;
import com.xpay.pay.rest.contract.ExtStore;
import com.xpay.pay.util.IDGenerator;

@Service
public class StoreGoodsService {
	@Autowired
	private StoreGoodsMapper mapper;
	@Autowired
	private StoreExtGoodsMapper extGoodsMapper;
	
	public List<StoreGoods> findByStoreId(long storeId) {
		Assert.isTrue(storeId>0, "Invalid storeId");
		return mapper.findByStoreId(storeId);
	}
	
	public StoreGoods findByStoreIdAndAmount(long storeId, float amount) {
		 List<StoreGoods> list = findByStoreId(storeId);
		 Assert.isTrue(CollectionUtils.isNotEmpty(list), "No goods found in store "+storeId);
		 return list.stream().filter(x -> x.getAmount() == amount).findAny().orElse(null);
	}
	
	public StoreGoods findByCode(String code) {
		Assert.notNull(code, "Invalid code");
		StoreGoods goods = mapper.findByCode(code);
		return goods;
	}
	
	public boolean create(StoreGoods goods) {
		Assert.isTrue(goods != null, "Invalid good to be inserted");
		if(StringUtils.isBlank(goods.getCode())) {
			goods.setCode(IDGenerator.buildGoodsCode());
		}
		
		boolean result = mapper.insert(goods);
		return result;
	}
	
	public boolean update(Long goodsId, StoreGoods goods) {
		if(goodsId == null || goodsId<=0) {
			return false;
		}
		StoreGoods goodsToBeUpdated = (goods == null)?new StoreGoods():goods; 
		goodsToBeUpdated.setId(goodsId);
		boolean result = mapper.updateById(goodsToBeUpdated);
		return result;
	}
	
	public boolean delete(long goodsId) {
		Assert.isTrue(goodsId>0, "Invalid good to be deleted");
		return mapper.deleteById(goodsId);
	}

	public StoreGoods findById(Long goodsId) {
		if(goodsId == null || goodsId<=0) {
			return null;
		}
		return mapper.findById(goodsId);
	}

}
