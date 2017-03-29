package com.xpay.pay.dao;

import com.xpay.pay.dao.entity.Order;

public interface OrderMapper extends BaseMapper<Order> {
	Order findByOrderNo(String orderNo);
}
