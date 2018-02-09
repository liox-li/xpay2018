package com.xpay.pay.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.dao.MissedOrderMapper;
import com.xpay.pay.dao.StoreGoodsMapper;
import com.xpay.pay.dao.StoreMapper;
import com.xpay.pay.model.MissedOrder;
import com.xpay.pay.model.Store;
import com.xpay.pay.model.StoreGoods;

@Service
public class MissedOrderService {

	@Autowired
	private MissedOrderMapper mapper;
	@Autowired
	private StoreGoodsMapper goodsMapper;
	@Autowired
	private StoreMapper storeMapper;
	
	public boolean insert(MissedOrder missedOrder) {
		if(missedOrder == null) {
			return false;
		}
		return mapper.insert(missedOrder);
	}
	
	public  boolean update(MissedOrder missedOrder) {
		if(missedOrder == null) {
			return false;
		}
		return mapper.updateById(missedOrder);
	}
	
	public List<MissedOrder> findAllUnresolved() {
		return mapper.findAllUnresovled();
	}
	
	public List<MissedOrder> findAllUnresolved(Long adminId) {
		List<Store> stores = storeMapper.findByAdminId(adminId);
		if(CollectionUtils.isEmpty(stores)) {
			return null;
		}
		List<StoreGoods> goodsList = stores.stream().map(x -> goodsMapper.findByStoreId(x.getId())).flatMap(y -> y.stream()).collect(Collectors.toList());
		if(CollectionUtils.isEmpty(goodsList)) {
			return null;
		}
		List<String> extStoreIds = goodsList.stream().map(x -> x.getExtStoreId()).distinct().collect(Collectors.toList());
		if(CollectionUtils.isEmpty(extStoreIds)) {
			return null;
		}
		List<MissedOrder> missedOrders = mapper.findAllUnresovled();
		
		return missedOrders.stream().filter(x -> extStoreIds.contains(x.getExtStoreId())).collect(Collectors.toList());
	}
	
	
	public MissedOrder findByOrderNo(String orderNo) {
		return mapper.findByOrderNo(orderNo);
	}
	
}
