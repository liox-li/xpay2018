package com.xpay.pay.dao;

import java.util.List;

import com.xpay.pay.model.MissedOrder;

public interface MissedOrderMapper extends BaseMapper<MissedOrder> {
	public List<MissedOrder> findAllUnresovled();
	
	public MissedOrder findByOrderNo(String orderNo);
}
