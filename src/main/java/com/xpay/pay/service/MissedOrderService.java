package com.xpay.pay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.dao.MissedOrderMapper;
import com.xpay.pay.model.MissedOrder;

@Service
public class MissedOrderService {

	@Autowired
	private MissedOrderMapper mapper;
	
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
	
}
