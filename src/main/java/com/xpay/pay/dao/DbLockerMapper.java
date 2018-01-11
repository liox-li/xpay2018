package com.xpay.pay.dao;

import com.xpay.pay.model.DbLocker;

public interface DbLockerMapper {
	public DbLocker findByKey(String key);
	
	public boolean insert(DbLocker locker);
	
	public boolean delete(DbLocker locker);
	
	public boolean lock(DbLocker locker);
}
