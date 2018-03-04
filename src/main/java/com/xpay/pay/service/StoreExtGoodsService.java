package com.xpay.pay.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.dao.StoreExtGoodsMapper;
import com.xpay.pay.model.StoreExtGoods;
import com.xpay.pay.rest.contract.ExtStore;

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

	public boolean detach(long goodsId, long extGoodsId) {
		StoreExtGoods extGoods = mapper.findById(extGoodsId);
		if(goodsId == extGoods.getGoodsId()) {
			extGoods.setGoodsId(-1L);
			return mapper.updateById(extGoods);
		}
		return false;
	}
	
	public List<ExtStore> getExtStorePool(long storeId) {
		return this.mapper.findByStoreId(storeId).stream().map(x -> new ExtStore(x.getExtStoreId(), x.getExtStoreName())).distinct().collect(Collectors.toList());
	}

	public boolean createExtStore(Long storeId, ExtStore extStore) {
		StoreExtGoods goods = new StoreExtGoods();
		goods.setStoreId(storeId);
		goods.setExtStoreId(extStore.getExtStoreId());
		goods.setExtStoreName(extStore.getExtStoreName());
		
		return mapper.insert(goods);
		
	}

	public boolean createExtStoreGoods(StoreExtGoods extGoods) {
		if(extGoods.getId()==null) {
			return mapper.insert(extGoods);
		}
		return mapper.updateById(extGoods);
	}

	public boolean deleteExtStore(String extStoreId) {
		return mapper.deleteByExtStoreId(extStoreId);
	}
}
