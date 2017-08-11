package com.xpay.pay.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.BaseSpringJunitTest;
import com.xpay.pay.model.ChannelLimit;
import com.xpay.pay.util.TimeUtils;

public class ChannelLimitMapperTest extends BaseSpringJunitTest {
	@Autowired
	protected ChannelLimitMapper mapper;

	@Test
	public void testInsert() {
		ChannelLimit limit = new ChannelLimit();
		limit.setChannelId(60);
		limit.setLimit(50000f);
		mapper.insert(limit);
	}
	
	@Test
	public void testFindById() {
		ChannelLimit limit =mapper.findById(1002);
		System.out.println(limit.getCurrentAmount() + ", "+limit.getLimit()+ ", "+limit.getNotes());
	}
	
	@Test
	public void testFindByChannelIdId() {
		ChannelLimit limit = mapper.findByChannelId(60);
		System.out.println(limit.getCurrentAmount() + ", "+limit.getLimit()+ ", "+limit.getNotes());
	}
	
	@Test
	public void testUpdate() {
		ChannelLimit limit =mapper.findById(1002);
		limit.setLimit(60000f);
		limit.setCurrentAmount(100f);
		limit.setNotes(TimeUtils.formatTime(limit.getUpdateDate(), TimeUtils.TimePatternDate));
		mapper.updateById(limit);
	}
}
