package com.xpay.pay.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xpay.pay.model.DbLocker;

public interface DbLockerMapper {
	public DbLocker findByKey(String key);
	
	public List<DbLocker> findByKeys(@Param("keys")String[] keys);
	
	public boolean insert(DbLocker locker);
	
	public boolean delete(DbLocker locker);
	
	public boolean lock(DbLocker locker);
}
