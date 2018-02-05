package com.xpay.pay.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xpay.pay.model.Order;

public interface OrderMapper extends BaseMapper<Order> {
	List<Order> findByOrderNo(String orderNo);
	
	List<Order> findAnyByOrderNo(String orderNo);
	
	List<Order> findByExtOrderNo(String extOrderNo);
	
	List<Order> findBySellerOrderNo(String sellerOrderNo);
	
	List<Order> findByStoreIdAndTime(@Param("storeId")long storeId, @Param("startTime")Date startTime, @Param("endTime")Date endTime);
	
	List<Order> findByPayChannelAndTime(@Param("payChannel")String payChannel, @Param("startTime")Date startTime, @Param("endTime")Date endTime);
	
	Order findLastByGoodsId(long goodsId);
	
	List<Order> findLastByExtStoreCode(@Param("extStoreCode")String extStoreCode,  @Param("subject")String subject, @Param("startTime")Date startTime, @Param("endTime")Date endTime);
}
