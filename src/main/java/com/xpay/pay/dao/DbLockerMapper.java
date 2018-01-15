package com.xpay.pay.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xpay.pay.model.DbLocker;

public interface DbLockerMapper {
	public DbLocker findByKey(String key);
	
	public List<DbLocker> findByKeys(@Param("key1")String key1, @Param("key2")String key2, @Param("key3")String key3, @Param("key4")String key4, @Param("key5")String key5);
	
	public boolean insert(DbLocker locker);
	
	public boolean delete(DbLocker locker);
	
	public boolean lock(DbLocker locker);
}
