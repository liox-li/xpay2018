package com.xpay.pay.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.xpay.pay.proxy.IPaymentProxy.PayChannel;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;

public class Order {
	private long id;
	private String orderNo;
	private String refundOrderNo;
	private String extStoreCode;
	private Long appId;
	private long storeId;
	private StoreChannel storeChannel;
	private Long storeChannelId;	private Float totalFee;
	private String orderTime;
	private String refundTime;
	private String sellerOrderNo;
	private String extOrderNo;
	private String refundExtOrderNo;
	private String targetOrderNo;
	private PayChannel payChannel;
	private String attach;
	private String deviceId;
	private String ip;
	private String notifyUrl;
	private String returnUrl;
	private String codeUrl;
	private String prepayId;
	private String tokenId;
	private String payInfo;
	private OrderStatus status;
	private Long detailId;
	private String subject;
	private App app;
	private Store store;
	private Date createDate;
	private Long goodsId;
	private StoreGoods goods;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getExtStoreCode() {
		return extStoreCode;
	}
	public void setExtStoreCode(String extStoreCode) {
		this.extStoreCode = extStoreCode;
	}
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	public long getStoreId() {
		return storeId;
	}
	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}
	public StoreChannel getStoreChannel() {
		return storeChannel;
	}
	public void setStoreChannel(StoreChannel storeChannel) {
		this.storeChannel = storeChannel;
		if(storeChannel!=null) {
			this.storeChannelId = storeChannel.getId();
		}
	}
	public Long getStoreChannelId() {
		return storeChannelId;
	}
	public void setStoreChannelId(Long storeChannelId) {
		this.storeChannelId = storeChannelId;
	}
	
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public Float getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(Float totalFee) {
		this.totalFee = totalFee;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getSellerOrderNo() {
		return sellerOrderNo;
	}
	public void setSellerOrderNo(String sellerOrderNo) {
		this.sellerOrderNo = sellerOrderNo;
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
	public PayChannel getPayChannel() {
		return payChannel;
	}
	public void setPayChannel(PayChannel payChannel) {
		this.payChannel = payChannel;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public String getCodeUrl() {
		return codeUrl;
	}
	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}
	public String getPrepayId() {
		return prepayId;
	}
	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
	
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public String getPayInfo() {
		return payInfo;
	}
	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Long getDetailId() {
		return detailId;
	}
	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}
	public App getApp() {
		return app;
	}
	public void setApp(App app) {
		this.app = app;
		if(app!=null) {
			this.appId = app.getId();
		}
	}
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
		if(store!=null) {
			this.storeId = store.getId();
		}
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public boolean isRemoteQueralbe() {
		return !OrderStatus.REFUND.equals(this.status) && !OrderStatus.REFUNDERROR.equals(this.status) && !OrderStatus.REVOKED.equals(this.status);
	}
	
	public boolean isSettle() {
		return OrderStatus.SUCCESS.equals(this.status) || OrderStatus.REFUND.equals(this.status) || OrderStatus.REVOKED.equals(this.status);
	}
	
	public boolean isRefundable() {
		return OrderStatus.SUCCESS.equals(this.status) || OrderStatus.USERPAYING.equals(this.status);
	}
	
	public boolean isRechargeOrder() {
		return StringUtils.isNotBlank(this.orderNo) && this.orderNo.startsWith("S");
	}

	public String getRefundOrderNo() {
		return refundOrderNo;
	}

	public void setRefundOrderNo(String refundOrderNo) {
		this.refundOrderNo = refundOrderNo;
	}

	public String getRefundExtOrderNo() {
		return refundExtOrderNo;
	}

	public void setRefundExtOrderNo(String refundExtOrderNo) {
		this.refundExtOrderNo = refundExtOrderNo;
	}

	public String getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}
	public StoreGoods getGoods() {
		return goods;
	}
	public void setGoods(StoreGoods goods) {
		this.goods = goods;
	}
}
