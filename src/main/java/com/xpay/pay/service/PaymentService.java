package com.xpay.pay.service;

import static com.xpay.pay.proxy.IPaymentProxy.NO_RESPONSE;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.ApplicationConstants;
import com.xpay.pay.exception.Assert;
import com.xpay.pay.model.App;
import com.xpay.pay.model.Bill;
import com.xpay.pay.model.Order;
import com.xpay.pay.model.Store;
import com.xpay.pay.model.StoreChannel;
import com.xpay.pay.model.StoreChannel.PaymentGateway;
import com.xpay.pay.proxy.IPaymentProxy;
import com.xpay.pay.proxy.IPaymentProxy.PayChannel;
import com.xpay.pay.proxy.PaymentProxyFactory;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.util.AppConfig;
import com.xpay.pay.util.CommonUtils;
import com.xpay.pay.util.IDGenerator;

@Service
public class PaymentService {
	@Autowired
	private PaymentProxyFactory paymentProxyFactory;
	@Autowired
	private OrderService orderService;
	@Autowired
	private StoreService storeService;

	public Order createOrder(App app, String orderNo, Store store, PayChannel channel,
			String deviceId, String ip, Float totalFee, String orderTime,
			String sellerOrderNo, String attach, String notifyUrl,String returnUrl,
			String subject, String storeChannelId) {
		StoreChannel storeChannel = null;
		if(StringUtils.isNotBlank(storeChannelId)) {
			storeService.findStoreChannelById(Long.valueOf(storeChannelId));
		} else {
			storeChannel = orderService.findUnusedChannelByStore(store, orderNo);
			storeChannel = storeChannel == null? orderService.findUnusedChannelByAgent(store.getAgentId(), orderNo): storeChannel;
		}
		Assert.notNull(storeChannel, String.format("No avaiable store channel, please try later, sellerOrderNo: %s", StringUtils.trimToEmpty(sellerOrderNo)));

		Order order = new Order();
		order.setApp(app);
		order.setOrderNo(orderNo);
		order.setStore(store);
		order.setStoreId(store.getId());
		order.setStoreChannel(storeChannel);
		order.setPayChannel(channel);
		order.setDeviceId(deviceId);
		order.setIp(ip);
		order.setTotalFee(totalFee);
		order.setOrderTime(orderTime);
		order.setSellerOrderNo(sellerOrderNo);
		order.setAttach(attach);
		order.setNotifyUrl(notifyUrl);
		order.setReturnUrl(returnUrl);
		order.setSubject(subject);
		orderService.insert(order);

		return order;
	}

	public Bill unifiedOrder(Order order) {
		PaymentRequest request = this.toPaymentRequest(order);
		IPaymentProxy paymentProxy = paymentProxyFactory.getPaymentProxy(order.getStoreChannel().getPaymentGateway());
		PaymentResponse response = paymentProxy.unifiedOrder(request);

		Bill bill = response.getBill();
		Assert.isTrue(!StringUtils.isBlank(bill.getCodeUrl()) || !StringUtils.isBlank(bill.getTokenId()),
				ApplicationConstants.STATUS_BAD_GATEWAY, NO_RESPONSE,
				response.getMsg());
		bill.setOrder(order);
		return bill;
	}

	public boolean updateBill(Order order, Bill bill) {
		if(bill == null) {
			order.setStatus(OrderStatus.PAYERROR);
		} else {
			order.setExtOrderNo(bill.getGatewayOrderNo());
			order.setCodeUrl(bill.getCodeUrl());
			order.setPrepayId(bill.getPrepayId());
			order.setTokenId(bill.getTokenId());
			order.setPayInfo(bill.getPayInfo());
			order.setStatus(bill.getOrderStatus());
		}
		return orderService.update(order);
	}

	public boolean updateTradeAmount(Order order) {
		if(order != null) {
			Store store = order.getStore();
			float newNonBail = store.getNonBail() + order.getTotalFee();
			store.setNonBail(newNonBail);
			return storeService.updateById(store);
		}
		return true;
	}

	public Bill query(Long appId, String orderNo, String storeCode, boolean isCsr) {
		Order order = orderService.findActiveByOrderNo(orderNo);
		Assert.isTrue(storeCode.equals(order.getStore().getCode()), "No such order found for the store");
		Assert.isTrue(appId == order.getAppId(), "No such order found under the app");
		Assert.isTrue(order.isSettle() || CommonUtils.isWithinHours(order.getOrderTime(), IDGenerator.TimePattern14, 24), "Order expired");
		if(isCsr || (order.isRemoteQueralbe() && CommonUtils.isWithinHours(order.getOrderTime(), IDGenerator.TimePattern14, 24))) {
			try {
				PaymentRequest paymentRequest = toQueryRequest(order);
				IPaymentProxy paymentProxy = paymentProxyFactory.getPaymentProxy(order.getStoreChannel().getPaymentGateway());
				PaymentResponse response = paymentProxy.query(paymentRequest);
				Bill bill = response.getBill();
				bill.setOrder(order);
				if(bill!=null && !bill.getOrderStatus().equals(order.getStatus())) {
					order.setStatus(bill.getOrderStatus());
					if(StringUtils.isNotBlank(bill.getTargetOrderNo())) {
						order.setTargetOrderNo(bill.getTargetOrderNo());
					}
					orderService.update(order);
				}
				return bill;
			} catch(Exception e) {

			}
		}
		return toBill(order);
	}

	public Bill refund(Long appId, String orderNo, String storeCode, boolean isCsr) {
		Order order = orderService.findActiveByOrderNo(orderNo);
		Assert.isTrue(storeCode.equals(order.getStore().getCode()), "No such order found for the store");
		Assert.isTrue(appId == order.getAppId(), "No such order found under the app");
		Assert.isTrue(!order.isRechargeOrder(), "Recharge order can't be refunded");

		if(isCsr || (order.isRefundable()  && CommonUtils.isWithinHours(order.getOrderTime(), IDGenerator.TimePattern14, 24))) {
			PaymentRequest paymentRequest = toQueryRequest(order);
			paymentRequest.setTotalFee(order.getTotalFee());
			IPaymentProxy paymentProxy = paymentProxyFactory.getPaymentProxy(order.getStoreChannel().getPaymentGateway());
			PaymentResponse response = paymentProxy.refund(paymentRequest);

			Bill bill = response.getBill();
			if(bill !=null && OrderStatus.REFUND.equals(bill.getOrderStatus()) || OrderStatus.REVOKED.equals(bill.getOrderStatus())) {
				bill.setOrder(order);
				order.setStatus(bill.getOrderStatus());
				orderService.update(order);
			}
			return bill;
		} else {
			return toBill(order);
		}
	}

	private static final String LOCAL_ID = AppConfig.XPayConfig.getProperty("inRequest.address", CommonUtils.getLocalIP());
	private static final String DEFAULT_NOTIFY_URL = AppConfig.XPayConfig.getProperty("notify.endpoint");
	public PaymentRequest toPaymentRequest(Order order) {
		PaymentRequest request = new PaymentRequest();
		request.setExtStoreId(order.getStoreChannel().getExtStoreId());
		request.setChannelProps(order.getStoreChannel().getChannelProps());
		String deviceId = order.getDeviceId();
		deviceId = StringUtils.isBlank(deviceId)?order.getIp():deviceId;
		request.setDeviceId(deviceId);
		request.setPayChannel(order.getPayChannel());
		request.setTotalFee(order.getTotalFee());
		request.setAttach(order.getAttach());
		request.setOrderNo(order.getOrderNo());
		request.setNotifyUrl(DEFAULT_NOTIFY_URL+order.getStoreChannel().getPaymentGateway().toString().toLowerCase());

		PaymentGateway gateway = order.getStoreChannel().getPaymentGateway();
		if(isDirectReturnChannel(gateway) ) {
			request.setReturnUrl(order.getReturnUrl());
		}
		if(PaymentGateway.JUZHEN.equals(gateway) || PaymentGateway.KEKEPAY.equals(gateway) || PaymentGateway.QFTXMP.equals(gateway)) {
			request.setServerIp(LOCAL_ID);
		} else if(PaymentGateway.MIAOFU.equals(gateway)) {
			String notifyUrl = request.getNotifyUrl() + "/"+request.getOrderNo();
			request.setNotifyUrl(notifyUrl);
		} else if(PaymentGateway.IPSSCAN.equals(gateway)
				|| PaymentGateway.IPSQUICK.equals(gateway)){
			request.setOrderTime(order.getOrderTime());
		}
//		else if(PaymentGateway.RUBIPAY.equals(order.getStoreChannel().getPaymentGateway())) {
//			request.setServerIp(LOCAL_ID);
//			request.setNotifyUrl(DEFAULT_NOTIFY_URL+order.getStoreChannel().getPaymentGateway().toString().toLowerCase());
//		}
//		else if(PaymentGateway.SWIFTPASS.equals(order.getStoreChannel().getPaymentGateway())) {
//			request.setServerIp(LOCAL_ID);
//			request.setNotifyUrl(DEFAULT_NOTIFY_URL+order.getStoreChannel().getPaymentGateway().toString().toLowerCase());
//		}
//

		if (StringUtils.isNotBlank(order.getSubject())) {
			request.setSubject(order.getSubject());
		} else {
			request.setSubject(DEFAULT_SUBJECT);
		}
		request.setSubject(this.customizeCsrTel(request.getSubject(), order));
		return request;
	}

	private PaymentRequest toQueryRequest(Order order) {
		PaymentRequest request = new PaymentRequest();
		PaymentGateway gateway = order.getStoreChannel().getPaymentGateway();
		if(isChinaUmsChannel(gateway) || PaymentGateway.JUZHEN.equals(gateway) || PaymentGateway.KEFU.equals(gateway)) {
			request.setOrderTime(order.getOrderTime());
			request.setGatewayOrderNo(order.getExtOrderNo());
		} else if(PaymentGateway.MIAOFU.equals(gateway)) {
			request.setGatewayOrderNo(order.getExtOrderNo());
		} else if(PaymentGateway.IPSQUICK.equals(gateway)
				||PaymentGateway.IPSSCAN.equals(gateway)) {
			request.setOrderTime(order.getOrderTime());
			request.setTotalFee(order.getTotalFee());
		}
		request.setExtStoreId(order.getStoreChannel().getExtStoreId());
		request.setChannelProps(order.getStoreChannel().getChannelProps());
		request.setPayChannel(order.getPayChannel());
		request.setOrderNo(order.getOrderNo());

		return request;
	}

	private Bill toBill(Order order) {
		Bill bill = new Bill();
		bill.setCodeUrl(order.getCodeUrl());
		bill.setPrepayId(order.getPrepayId());
		bill.setTokenId(order.getTokenId());;
		bill.setPayInfo(order.getPayInfo());
		bill.setOrderNo(order.getOrderNo());
		bill.setGatewayOrderNo(order.getExtOrderNo());
		bill.setOrderStatus(order.getStatus());
		bill.setOrder(order);
		return bill;
	}

	private static final String DEFAULT_SUBJECT = "游戏";
	private static final String DEFAULT_SUBJECT_CHINAUMS = "投诉热线:95534";
	private String customizeCsrTel(String subject, Order order) {
		String storeTel = order.getStore().getCsrTel();
		if(StringUtils.isNotBlank(storeTel)) {
			return subject + storeTel;
		} else if(isChinaUmsChannel(order.getStoreChannel().getPaymentGateway())) {
			return subject+"("+DEFAULT_SUBJECT_CHINAUMS+")";
		}
		return subject;
	}

	private boolean isChinaUmsChannel(PaymentGateway gateway) {
		return PaymentGateway.CHINAUMS.equals(gateway) ||
				PaymentGateway.CHINAUMSV2.equals(gateway) ||
				PaymentGateway.CHINAUMSH5.equals(gateway) ||
				PaymentGateway.CHINAUMSV3.equals(gateway) ||
				PaymentGateway.CHINAUMSWAP.equals(gateway);
	}

	private boolean isDirectReturnChannel(PaymentGateway gateway) {
		return PaymentGateway.CHINAUMS.equals(gateway) ||
				PaymentGateway.CHINAUMSV2.equals(gateway) ||
				PaymentGateway.CHINAUMSH5.equals(gateway) ||
				PaymentGateway.CHINAUMSWAP.equals(gateway) ||
				PaymentGateway.CHINAUMSV3.equals(gateway) ||
				PaymentGateway.UPAY.equals(gateway) ||
				PaymentGateway.IPSQUICK.equals(gateway) ||
				PaymentGateway.KEKEPAY.equals(gateway);
	}

}
