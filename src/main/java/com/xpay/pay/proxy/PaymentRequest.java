package com.xpay.pay.proxy;

import com.xpay.pay.model.StoreChannel.ChannelProps;
import com.xpay.pay.proxy.IPaymentProxy.PayChannel;
import com.xpay.pay.proxy.IPaymentProxy.TradeNoType;


public class PaymentRequest {
	private String extStoreId;
	private String deviceId;
	private PayChannel payChannel;
	private Float totalFee;
	private String attach;
	private String orderNo;
	private String refundOrderNo;
	private String refundTime;
	private String gatewayOrderNo;
	private String refundGatewayOrderNo;
	private TradeNoType tradeNoType;
	private String serverIp;
	private String notifyUrl;
	private String returnUrl;
	private String subject;
	private String orderTime;
	private GoodsBean[] goods;
	private ChannelProps channelProps;
	private String userOpenId;
	private String bankId;
	private String cardType;
	
	public String getExtStoreId() {
		return extStoreId;
	}
	public void setExtStoreId(String extStoreId) {
		this.extStoreId = extStoreId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public PayChannel getPayChannel() {
		return payChannel;
	}
	public void setPayChannel(PayChannel payChannel) {
		this.payChannel = payChannel;
	}
	public Float getTotalFee() {
		return totalFee;
	}

	public float getTotalFeeAsFloat() {
		return Float.valueOf(totalFee);
	}
	public void setTotalFee(Float totalFee) {
		this.totalFee = totalFee;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getGatewayOrderNo() {
		return gatewayOrderNo;
	}
	public void setGatewayOrderNo(String gatewayOrderNo) {
		this.gatewayOrderNo = gatewayOrderNo;
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
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
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public TradeNoType getTradeNoType() {
		return tradeNoType;
	}
	public void setTradeNoType(TradeNoType tradeNoType) {
		this.tradeNoType = tradeNoType;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public GoodsBean[] getGoods() {
		return goods;
	}
	public void setGoods(GoodsBean[] goods) {
		this.goods = goods;
	}
	public ChannelProps getChannelProps() {
		return channelProps;
	}
	public void setChannelProps(ChannelProps channelProps) {
		this.channelProps = channelProps;
	}

	public String getRefundOrderNo() {
		return refundOrderNo;
	}

	public void setRefundOrderNo(String refundOrderNo) {
		this.refundOrderNo = refundOrderNo;
	}

	public String getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}

	public String getRefundGatewayOrderNo() {
		return refundGatewayOrderNo;
	}

	public void setRefundGatewayOrderNo(String refundGatewayOrderNo) {
		this.refundGatewayOrderNo = refundGatewayOrderNo;
	}

	public String getUserOpenId() {
		return userOpenId;
	}
	public void setUserOpenId(String userOpenId) {
		this.userOpenId = userOpenId;
	}

	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public static class GoodsBean {
		private String goodsId;
		private String goodsName;
		private String quantity;
		private String price;
		private String goodsCategory;
		private String body;
		public String getGoodsId() {
			return goodsId;
		}
		public void setGoodsId(String goodsId) {
			this.goodsId = goodsId;
		}
		public String getGoodsName() {
			return goodsName;
		}
		public void setGoodsName(String goodsName) {
			this.goodsName = goodsName;
		}
		public String getQuantity() {
			return quantity;
		}
		public void setQuantity(String quantity) {
			this.quantity = quantity;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getGoodsCategory() {
			return goodsCategory;
		}
		public void setGoodsCategory(String goodsCategory) {
			this.goodsCategory = goodsCategory;
		}
		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
		}
		
	}
}
