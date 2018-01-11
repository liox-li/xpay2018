package com.xpay.pay.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.dao.DbLockerMapper;
import com.xpay.pay.model.DbLocker;

@Service
public class LockerService {
	@Autowired
	private DbLockerMapper mapper;
	
	public boolean lock(String key, long ms) {
		DbLocker locker = mapper.findByKey(key);
		if(locker == null) {
			locker = new DbLocker();
			locker.setKey(key);
			boolean inserted = mapper.insert(locker);
			if(inserted) {
				return true;
			}
		}
		locker.setUpdateDate(new Date(System.currentTimeMillis() - ms));
		return mapper.lock(locker);
	}
}
