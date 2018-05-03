package com.xpay.pay;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.model.SubChannel;
import com.xpay.pay.po.SubChannelMatrix;
import com.xpay.pay.service.OrderService;
import com.xpay.pay.service.PaymentService;
import com.xpay.pay.util.CommonUtils;

public class RiskEngine {
	protected final static Logger logger = LogManager.getLogger(RiskEngine.class);
	
	public static long Time_Interval = 1000*90; //1.5分钟
	public static String frequently_code = "-1111111"; //支付太频繁错误代码
	
	public static SubChannel buildSubChannel(String configProps){		
		
		SubChannel targetSubChannel = null;
		//配置了子商户轮询,约定了如果商户使用了子商户池，那么这个值配置的就是pool1或pool2...pooln
		if(configProps.indexOf("pool")>=0){//配置一个子商户池			
			targetSubChannel = RiskEngine.calcSubChannel(MemoryCache.IPS_STORE_SUB_CHANNEL,configProps); //subChannelList.stream().sorted(Comparator.comparing(SubChannel::getTimestamp)).findFirst().orElse(null);					
		}else{//配置一个子商户池id 					
			for(SubChannel item: MemoryCache.IPS_STORE_SUB_CHANNEL){
				if(item.getId().longValue() == Long.parseLong(configProps) && !CommonUtils.in(MemoryCache.IPS_STORE_CHANNEL_BLACK_LIST,item.getId())){
					targetSubChannel = item;
					break;
				}
			}
	   }
	 return targetSubChannel;
	}
	
	public static SubChannel buildSubChannel(List<SubChannel> subChannelList){
		return RiskEngine.calcSubChannel(subChannelList,null);
	}
	
	public static List<SubChannel> filterSubChannel(String pool){
		logger.info("filterSubChannel >>"+pool);
		if(pool == null || pool.indexOf("pool")<0){
			return null;
		}
		List<SubChannel> subChannelList = null;
		subChannelList = MemoryCache.IPS_STORE_SUB_CHANNEL.stream().filter(x->x.getPoolType()!= null 
				&& x.getPoolType().equals(pool)
				&& !CommonUtils.in(MemoryCache.IPS_STORE_CHANNEL_BLACK_LIST,x.getId()
				)).collect(Collectors.toList());
		return subChannelList;
		
	}
	public static SubChannel filterSubChannel(Long subChannelId){
		logger.info("filterSubChannel by subChannelId >>"+subChannelId);
		if(subChannelId == null){
			return null;
		}
		SubChannel subChannel = MemoryCache.IPS_STORE_SUB_CHANNEL.stream().
				filter( x->x.getId().longValue() == subChannelId.longValue() ).findFirst().orElse(null);
		logger.info(MemoryCache.IPS_STORE_SUB_CHANNEL.size()+">>>>>>>"+subChannel);
		return subChannel;
		
	}
	/*
	 * 路由子商户的算法
	 * 
	 */
	private static SubChannel calcSubChannel(List<SubChannel> subChannelList,String pool){
		SubChannel targetSubChannel = null;
		List<SubChannel> calcSubChannelList = subChannelList;
		if(calcSubChannelList == null || calcSubChannelList.size() == 0){
			calcSubChannelList =RiskEngine.filterSubChannel(pool);
		}
		if(calcSubChannelList == null || calcSubChannelList.size() == 0){
			return null;
		}
		long now = System.currentTimeMillis();
		List<SubChannel> filterSubChannel =  calcSubChannelList.stream().filter(x ->(x.getTimestamp() +Time_Interval < now)).collect(Collectors.toList());
		if(filterSubChannel == null || filterSubChannel.size() == 0){
			logger.info("根据规则1.5分钟内不能发生多笔交易过滤完了所有子商户，返回“交易太频繁”");
			 throw new GatewayException(RiskEngine.frequently_code,"交易太频繁！");
		}
		if(MemoryCache.SUB_CHANNEL_MATRIX == null || MemoryCache.SUB_CHANNEL_MATRIX.size() == 0){
			logger.info("subchannel matrix 为空，根据子商户的使用时间倒序选出第一个！");
			targetSubChannel = calcSubChannelList.stream().sorted(Comparator.comparing(SubChannel::getTimestamp)).findFirst().orElse(null);	
			
		}else{
			//按照支付成功率倒序拍，成功率最高的排在越前面；
			List<SubChannelMatrix> subChannelMatrixList  = MemoryCache.SUB_CHANNEL_MATRIX.stream().sorted(Comparator.comparing(SubChannelMatrix::getId).reversed()).collect(Collectors.toList());
			for(SubChannelMatrix item : subChannelMatrixList){
				logger.info(item.getId()+">>>>>"+item.getSuccessRate());
				for(SubChannel subC : filterSubChannel){
					if(subC.getId().longValue() == item.getId().longValue()){
						targetSubChannel = subC;
						break;
					}
				}
				if(targetSubChannel != null){
					break;
				}
				
			}
		}
		//这个需要做判断的，因为当存量的订单没有的时候
		if(targetSubChannel == null ){
			targetSubChannel = filterSubChannel.stream().sorted(Comparator.comparing(SubChannel::getTimestamp)).findFirst().orElse(null);	
		}
		
		logger.info("计算出子商户为>>"+(targetSubChannel== null?null:targetSubChannel.getId()) +">>>>"+ (targetSubChannel== null?null:targetSubChannel.getTimestamp()) );
		return targetSubChannel;
	}
	
	public static void main(String args[]){
		//初始化测试数据
		List<SubChannelMatrix> list = new ArrayList<SubChannelMatrix>();
		List<SubChannel> subChannelList =new ArrayList<SubChannel>();
		for(int i =0 ;i<200;i++){
			SubChannelMatrix su = new SubChannelMatrix();
			MemoryCache.SUB_CHANNEL_MATRIX.add(su);
			
			SubChannel subChannel = new SubChannel();
			MemoryCache.IPS_STORE_SUB_CHANNEL.add(subChannel);
		}
		
		SubChannel rs = calcSubChannel(MemoryCache.IPS_STORE_SUB_CHANNEL,null);
		System.out.println(rs.getId());
		 
	}

}
