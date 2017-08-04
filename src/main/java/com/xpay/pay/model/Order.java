package com.xpay.pay.model;

import com.xpay.pay.proxy.IPaymentProxy.PayChannel;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;

public class Order {
	private long id;
	private String orderNo;
	private int appId;
	private long storeId;
	private StoreChannel storeChannel;
	private long storeChannelId;
	private String totalFee;
	private String orderTime;
	private String sellerOrderNo;
	private String extOrderNo;
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
	private OrderDetail orderDetail;
	private App app;
	private Store store;
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
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
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
	public long getStoreChannelId() {
		return storeChannelId;
	}
	public void setStoreChannelId(long storeChannelId) {
		this.storeChannelId = storeChannelId;
	}
	public String getTotalFee() {
		return totalFee;
	}
	
	public float getTotalFeeAsFloat() {
		return Float.valueOf(totalFee);
	}
	public void setTotalFee(String totalFee) {
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
	public Long getDetailId() {
		return detailId;
	}
	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}
	public OrderDetail getOrderDetail() {
		return orderDetail;
	}
	public void setOrderDetail(OrderDetail orderDetail) {
		this.orderDetail = orderDetail;
		if(orderDetail!=null) {
			this.detailId = orderDetail.getId();
		}
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
	public boolean isRemoteQueralbe() {
		return !OrderStatus.REFUND.equals(this.status) && !OrderStatus.REVOKED.equals(this.status);
	}
	
	public boolean isSettle() {
		return OrderStatus.SUCCESS.equals(this.status) || OrderStatus.REFUND.equals(this.status) || OrderStatus.REVOKED.equals(this.status);
	}
	
	public boolean isRefundable() {
		return OrderStatus.SUCCESS.equals(this.status) || OrderStatus.USERPAYING.equals(this.status);
	}
}
