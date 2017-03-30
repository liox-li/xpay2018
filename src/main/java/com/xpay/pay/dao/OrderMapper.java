package com.xpay.pay.dao;

import java.util.List;

import com.xpay.pay.model.Order;

public interface OrderMapper extends BaseMapper<Order> {
	List<Order> findByOrderNo(String orderNo);
}
