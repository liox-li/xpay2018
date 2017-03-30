package com.xpay.pay.dao;

import com.xpay.pay.model.OrderDetail;

public interface OrderDetailMapper {
	public void insert(OrderDetail orderDetail);
	
	public OrderDetail findById(long id);
}
