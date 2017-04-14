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
import com.xpay.pay.model.OrderDetail;
import com.xpay.pay.model.Store;
import com.xpay.pay.model.StoreChannel;
import com.xpay.pay.model.StoreChannel.PaymentGateway;
import com.xpay.pay.proxy.IPaymentProxy;
import com.xpay.pay.proxy.IPaymentProxy.Method;
import com.xpay.pay.proxy.IPaymentProxy.PayChannel;
import com.xpay.pay.proxy.PaymentProxyFactory;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.util.AppConfig;
import com.xpay.pay.util.CommonUtils;

@Service
public class PaymentService {
	@Autowired
	private PaymentProxyFactory paymentProxyFactory;
	@Autowired
	private OrderService orderService;
	@Autowired
	private StoreService storeService;

	public Order createOrder(App app, String orderNo, Store store, PayChannel channel,
			String deviceId, String ip, String totalFee, String orderTime,
			String sellerOrderNo, String attach, String notifyUrl,
			OrderDetail orderDetail, Method method) {
		StoreChannel storeChannel = null;
		boolean isNextBailPay = store.isNextBailPay();
		storeChannel = orderService.findUnusedChannel(store, orderNo);
		if(isNextBailPay) {
			PaymentGateway gateway = storeChannel.getPaymentGateway();
			storeChannel = orderService.findUnusedChannel(this.findBailStore(gateway), orderNo);
		}
		Assert.notNull(storeChannel, "No avaiable store channel");
		
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
		order.setOrderDetail(orderDetail);
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
			order.setStatus(bill.getOrderStatus());
		}
		return orderService.update(order);
	}
	
	public boolean updateBail(Store store, Bill bill, boolean isAdd) {
		if(bill != null) {
			boolean isBail = bill.getOrder().getStoreChannelId()<100;
			if(isBail) {
				float newBail = isAdd? store.getBail() + bill.getOrder().getTotalFeeAsFloat()
						:store.getBail() - bill.getOrder().getTotalFeeAsFloat();
				store.setBail(newBail);
			} else {
				float newNonBail = isAdd? store.getNonBail() + bill.getOrder().getTotalFeeAsFloat()
						:store.getNonBail() - bill.getOrder().getTotalFeeAsFloat();
				store.setNonBail(newNonBail);
			}
			return storeService.updateById(store);
		}
		return true;
	}

	public Bill query(int appId, String orderNo, String storeCode) {
		Order order = orderService.findActiveByOrderNo(orderNo);
		Assert.isTrue(storeCode.equals(order.getStore().getCode()), "No such order found for the store");
		Assert.isTrue(appId == order.getAppId(), "No such order found under the app");
		
		if(!order.isSettle()) {
			try {
				PaymentRequest paymentRequest = toQueryRequest(order);
				IPaymentProxy paymentProxy = paymentProxyFactory.getPaymentProxy(order.getStoreChannel().getPaymentGateway());
				PaymentResponse response = paymentProxy.query(paymentRequest);
				Bill bill = response.getBill();
				bill.setOrder(order);
				if(bill!=null && !bill.getOrderStatus().equals(order.getStatus())) {
					order.setStatus(bill.getOrderStatus());
					orderService.update(order);
				}
				return bill;
			} catch(Exception e) {
				
			}
		} 
		return toBill(order);
	}
	
	public Bill refund(int appId, String orderNo, String storeCode) {
		Order order = orderService.findActiveByOrderNo(orderNo);
		Assert.isTrue(storeCode.equals(order.getStore().getCode()), "No such order found for the store");
		Assert.isTrue(appId == order.getAppId(), "No such order found under the app");
		
		if(!order.isRefundable()) {
			PaymentRequest paymentRequest = toQueryRequest(order);
			paymentRequest.setTotalFee(order.getTotalFee());
			IPaymentProxy paymentProxy = paymentProxyFactory.getPaymentProxy(order.getStoreChannel().getPaymentGateway());
			PaymentResponse response = paymentProxy.refund(paymentRequest);
			
			Bill bill = response.getBill();
			if(bill !=null && OrderStatus.REFUND.equals(bill.getOrderStatus()) || OrderStatus.REVOKED.equals(bill.getOrderStatus())) {
				bill.setOrder(order);
				order.setStatus(bill.getOrderStatus());
				orderService.update(order);
				updateBail(order.getStore(), bill, false);
			}
			return bill;
		} else {
			return toBill(order);
		}
	}

	private static final String DEFAULT_SUBJECT = "电子商品";
	private static final String LOCAL_ID = CommonUtils.getLocalIP();
	private static final String DEFAULT_NOTIFY_URL = AppConfig.XPayConfig.getProperty("notify.endpoint");
	private PaymentRequest toPaymentRequest(Order order) {
		PaymentRequest request = new PaymentRequest();
		request.setExtStoreId(order.getStoreChannel().getExtStoreId());
		request.setDeviceId(order.getDeviceId());
		request.setPayChannel(order.getPayChannel());
		request.setTotalFee(order.getTotalFee());
		request.setAttach(order.getAttach());
		request.setOrderNo(order.getOrderNo());
		if(PaymentGateway.SWIFTPASS.equals(order.getStoreChannel().getPaymentGateway())) {
			request.setServerIp(LOCAL_ID);
			request.setNotifyUrl(DEFAULT_NOTIFY_URL+order.getStoreChannel().getPaymentGateway().toString().toLowerCase());
		}
		if(PaymentGateway.CHINAUMS.equals(order.getStoreChannel().getPaymentGateway())) {
			request.setNotifyUrl(DEFAULT_NOTIFY_URL+order.getStoreChannel().getPaymentGateway().toString().toLowerCase());
		}
		
		if (order.getOrderDetail() != null) {
			request.setSubject(order.getOrderDetail().getSubject());
		} else {
			request.setSubject(DEFAULT_SUBJECT);
		}
		return request;
	}
	
	private PaymentRequest toQueryRequest(Order order) {
		PaymentRequest request = new PaymentRequest();
		if(PaymentGateway.CHINAUMS.equals(order.getStoreChannel().getPaymentGateway())) {
			request.setOrderTime(order.getOrderTime());
			request.setGatewayOrderNo(order.getExtOrderNo());
		}
		request.setExtStoreId(order.getStoreChannel().getExtStoreId());
		request.setPayChannel(order.getPayChannel());
		request.setOrderNo(order.getOrderNo());
		return request;
	}

	private Bill toBill(Order order) {
		Bill bill = new Bill();
		bill.setCodeUrl(order.getCodeUrl());
		bill.setPrepayId(order.getPrepayId());
		bill.setOrderNo(order.getOrderNo());
		bill.setGatewayOrderNo(order.getExtOrderNo());
		bill.setOrderStatus(order.getStatus());
		bill.setOrder(order);
		return bill;
	}
	
	public Store findBailStore(PaymentGateway gateway) {
		return storeService.findById(gateway.getBailStoreId());
	}

}
