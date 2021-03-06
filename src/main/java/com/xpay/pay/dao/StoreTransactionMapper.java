package com.xpay.pay.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xpay.pay.model.StoreTransaction;

public interface StoreTransactionMapper extends BaseMapper<StoreTransaction> {
	public List<StoreTransaction> findByStoreIdAndTime(@Param("storeId")long storeId,  @Param("startTime")Date startTime, @Param("endTime")Date endTime);
	
	public List<StoreTransaction> findByAgentIdAndTime(@Param("agentId")long agentId,  @Param("startTime")Date startTime, @Param("endTime")Date endTime);
	
	public StoreTransaction findByOrderNo(@Param("orderNo")String orderNo);

}
