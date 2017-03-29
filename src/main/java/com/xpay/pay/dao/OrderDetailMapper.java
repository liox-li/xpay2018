package com.xpay.pay.dao;

import java.util.List;

import com.xpay.pay.dao.entity.OrderDetail;

public interface OrderDetailMapper {
	public void insert(OrderDetail orderDetail);
	
	public List<OrderDetail> findByOrderId(long orderId);
}
