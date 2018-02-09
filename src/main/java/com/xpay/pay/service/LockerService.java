package com.xpay.pay.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
	
	public String findOldestByKeys(String[] keys) {
		if(keys == null || keys.length<1) {
			return null;
		}
		List<DbLocker> lockers = mapper.findByKeys(keys);
		if(CollectionUtils.isEmpty(lockers)) {
			return keys[0];
		}
		
		List<String> keysInDb = lockers.stream().sorted(Comparator.comparing(DbLocker::getUpdateDate)).map(x -> x.getKey()).collect(Collectors.toList());
		String key = Arrays.stream(keys).filter(x -> !keysInDb.contains(x)).findFirst().orElse(null);
		key = StringUtils.isBlank(key)?keysInDb.get(0):key;
		return key;
	}
}
