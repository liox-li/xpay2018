package com.xpay.pay.dao;

import com.xpay.pay.model.ChannelLimit;

public interface ChannelLimitMapper extends BaseMapper<ChannelLimit> {
	ChannelLimit findByChannelId(long channelId);
}
