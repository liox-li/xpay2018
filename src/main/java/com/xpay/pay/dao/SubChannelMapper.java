package com.xpay.pay.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xpay.pay.model.SubChannel;

public interface SubChannelMapper extends BaseMapper<SubChannel> {
	public List<SubChannel> findAll();	
	public List<SubChannel> findIpsAll();	
	public List<SubChannel> findByStatus(String status);
	public List<SubChannel> findByPoolType(String poolType);
	public void changeSubChannelStatus(@Param("id")Long id,@Param("status")String status);
  
}
