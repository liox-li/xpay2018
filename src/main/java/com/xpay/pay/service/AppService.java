package com.xpay.pay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.dao.AppMapper;
import com.xpay.pay.model.App;

@Service
public class AppService {
	@Autowired
	protected AppMapper mapper;
	
	public App findByKey(String key) {
		return mapper.findByKey(key);
	}
	
	public App findById(int id) {
		return mapper.findById(id);
	}
}
