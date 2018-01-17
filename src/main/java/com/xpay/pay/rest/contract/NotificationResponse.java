package com.xpay.pay.rest.contract;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.util.KeyValuePair;

import com.google.common.collect.Lists;

public class NotificationResponse {
	private String orderNo;
	private String storeId;
	private String storeName;
	private String sellerOrderNo;
	private String codeUrl;
	private String tokenId;
	private String prepayId;
	private int orderStatus;
	private Float totalFee;
	private String extOrderNo;
	private String targetOrderNo;
	private String attach;
	private String payInfo;
	private Long channelNo;
	private String uid;
	private String sign;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getSellerOrderNo() {
		return sellerOrderNo;
	}
	public void setSellerOrderNo(String sellerOrderNo) {
		this.sellerOrderNo = sellerOrderNo;
	}
	public String getCodeUrl() {
		return codeUrl;
	}
	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public String getPrepayId() {
		return prepayId;
	}
	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Float getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(Float totalFee) {
		this.totalFee = totalFee;
	}
	public String getExtOrderNo() {
		return extOrderNo;
	}
	public void setExtOrderNo(String extOrderNo) {
		this.extOrderNo = extOrderNo;
	}
	public String getTargetOrderNo() {
		return targetOrderNo;
	}
	public void setTargetOrderNo(String targetOrderNo) {
		this.targetOrderNo = targetOrderNo;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getPayInfo() {
		return payInfo;
	}
	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}
	public Long getChannelNo() {
		return channelNo;
	}
	public void setChannelNo(Long channelNo) {
		this.channelNo = channelNo;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public List<KeyValuePair> toKeyValuePairs() {
		List<KeyValuePair> keyValues = Lists.newArrayList();
		if(StringUtils.isNotBlank(orderNo)) {
			keyValues.add(new KeyValuePair("orderNo", orderNo));
		}
		if(StringUtils.isNotBlank(storeId)) {
			keyValues.add(new KeyValuePair("storeId", storeId));
		}
		if(StringUtils.isNotBlank(storeName)) {
			keyValues.add(new KeyValuePair("storeName", storeName));
		}
		if(StringUtils.isNotBlank(sellerOrderNo)) {
			keyValues.add(new KeyValuePair("sellerOrderNo", sellerOrderNo));
		}
		if(StringUtils.isNotBlank(codeUrl)) {
			keyValues.add(new KeyValuePair("codeUrl", codeUrl));
		}
		if(StringUtils.isNotBlank(tokenId)) {
			keyValues.add(new KeyValuePair("tokenId", tokenId));
		}
		if(StringUtils.isNotBlank(prepayId)) {
			keyValues.add(new KeyValuePair("prepayId", prepayId));
		}
		keyValues.add(new KeyValuePair("orderStatus", String.valueOf(orderStatus)));
		if(totalFee!=null) {
			keyValues.add(new KeyValuePair("totalFee", String.valueOf(totalFee)));
		}
		if(StringUtils.isNotBlank(extOrderNo)) {
			keyValues.add(new KeyValuePair("extOrderNo", extOrderNo));
		}
		if(StringUtils.isNotBlank(targetOrderNo)) {
			keyValues.add(new KeyValuePair("targetOrderNo", targetOrderNo));
		}
		if(StringUtils.isNotBlank(attach)) {
			keyValues.add(new KeyValuePair("attach", attach));
		}
		if(StringUtils.isNotBlank(payInfo)) {
			keyValues.add(new KeyValuePair("payInfo", payInfo));
		}
		if(StringUtils.isNotBlank(uid)) {
			keyValues.add(new KeyValuePair("uid", uid));
		}
		if(channelNo!=null && channelNo>0) {
			keyValues.add(new KeyValuePair("channelNo", String.valueOf(channelNo)));
		}
		
		return keyValues;
	}
}
