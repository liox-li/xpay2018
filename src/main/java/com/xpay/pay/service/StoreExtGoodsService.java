package com.xpay.pay.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
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
	
	public List<StoreExtGoods> findByAdminId(long adminId) {
		return mapper.findByAdminId(adminId);
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
	
	public List<ExtStore> getExtStorePool(long adminId) {
		return mapper.findByAdminId(adminId).stream().map(x -> new ExtStore(x.getExtStoreId(), x.getExtStoreName())).distinct().collect(Collectors.toList());
	}

	public boolean createExtStore(Long adminId, ExtStore extStore) {
		StoreExtGoods goods = new StoreExtGoods();
		goods.setAdminId(adminId);
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

	public boolean attach(long adminId, long goodsId, List<Long> extGoodsIds) {
		if(goodsId<10 || CollectionUtils.isEmpty(extGoodsIds)) {
			return false;
		}
		extGoodsIds.forEach(x -> {
			StoreExtGoods extGoods = mapper.findById(x);
			if(extGoods.getAdminId() == adminId && (extGoods.getGoodsId() == null || extGoods.getGoodsId()!=goodsId)) {
				extGoods.setGoodsId(goodsId);
				mapper.updateById(extGoods);
			}
		});
		return true;	
	}

	public boolean detach(long adminId, long goodsId, List<Long> extGoodsIds) {
		if(goodsId<10 || CollectionUtils.isEmpty(extGoodsIds)) {
			return false;
		}
		extGoodsIds.forEach(x -> {
			StoreExtGoods extGoods = mapper.findById(x);
			if(extGoods.getAdminId() == adminId && extGoods.getGoodsId() == goodsId) {
				extGoods.setGoodsId(-1L);
				mapper.updateById(extGoods);
			}
		});
		return true;
	}
}
