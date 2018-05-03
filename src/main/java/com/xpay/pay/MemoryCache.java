package com.xpay.pay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xpay.pay.model.SubChannel;
import com.xpay.pay.po.SubChannelMatrix;

public class MemoryCache {
	
	//子商户加入黑名单列表，支付失败后，加入黑名单
	public static List<Long> IPS_STORE_CHANNEL_BLACK_LIST = new ArrayList<Long>();
	//可用子商户列表,在下订单入口处缓存
	public static List<SubChannel> IPS_STORE_SUB_CHANNEL = new ArrayList<SubChannel>();
	//子商户使用情况表
	public static List<SubChannelMatrix> SUB_CHANNEL_MATRIX = new ArrayList<SubChannelMatrix>();
	
	
	
}
