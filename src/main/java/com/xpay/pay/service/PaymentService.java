package com.xpay.pay.service;

import static com.xpay.pay.proxy.IPaymentProxy.NO_RESPONSE;
import static com.xpay.pay.proxy.IPaymentProxy.SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.ApplicationConstants;
import com.xpay.pay.exception.Assert;
import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.model.App;
import com.xpay.pay.model.Bill;
import com.xpay.pay.model.Order;
import com.xpay.pay.model.OrderDetail;
import com.xpay.pay.model.Store;
import com.xpay.pay.model.StoreChannel;
import com.xpay.pay.model.StoreChannel.PaymentGateway;
import com.xpay.pay.proxy.IPaymentProxy;
import com.xpay.pay.proxy.PaymentProxyFactory;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentRequest.Method;
import com.xpay.pay.proxy.PaymentRequest.PayChannel;
import com.xpay.pay.proxy.PaymentRequest.TradeNoType;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.proxy.PaymentResponse.TradeBean;

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
		if(isNextBailPay) {
			PaymentGateway gateway = PaymentGateway.MIAOFU;
			if(Method.NativePay.equals(method)) {
				gateway = PaymentGateway.SWIFTPASS;
			}
			storeChannel = orderService.findUnusedChannel(this.findBailStore(gateway), orderNo);
		} else {
			storeChannel = orderService.findUnusedChannel(store, orderNo);
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
		PaymentRequest request = toPaymentRequest(order);
		IPaymentProxy paymentProxy = paymentProxyFactory.getPaymentProxy(order.getStoreChannel().getPaymentGateway());
		PaymentResponse response = paymentProxy.unifiedOrder(request);

		Bill bill = toBill(order, response);
		Assert.notBlank(bill.getCodeUrl(),
				ApplicationConstants.STATUS_BAD_GATEWAY, NO_RESPONSE,
				response.getMsg());
		
		return bill;
	}
	
	public Bill nativePay(Order order) {
		PaymentRequest request = toPaymentRequest(order);
		IPaymentProxy paymentProxy = paymentProxyFactory.getPaymentProxy(order.getStoreChannel().getPaymentGateway());
		PaymentResponse response = paymentProxy.nativePay(request);

		Bill bill = toBill(order, response);
		Assert.notBlank(bill.getCodeUrl(),
				ApplicationConstants.STATUS_BAD_GATEWAY, NO_RESPONSE,
				response.getMsg());
		
		return bill;
	}
	
	public boolean updateBill(Order order, Bill bill) {
		if(bill == null) {
			order.setStatus(OrderStatus.CHANNEL_ERROR);
		} else {
			order.setExtOrderNo(bill.getGatewayOrderNo());
			order.setCodeUrl(bill.getCodeUrl());
			order.setPrepayId(bill.getPrepayId());
			order.setStatus(bill.getOrderStatus());
		}
		return orderService.update(order);
	}
	
	public boolean updateBail(Store store, Bill bill) {
		if(bill != null) {
			boolean isBail = bill.getOrder().getStoreChannelId()<100;
			if(isBail) {
				store.setBail(store.getBail() + bill.getOrder().getTotalFeeAsFloat());
			} else {
				store.setNonBail(store.getNonBail() + bill.getOrder().getTotalFeeAsFloat());
			}
			return storeService.updateById(store);
		}
		return true;
	}

	public Bill query(String orderNo, String storeCode) {
		Order order = orderService.findActiveByOrderNo(orderNo);
		Assert.isTrue(storeCode.equals(order.getStore().getCode()), "No such order found for the store");
		
		if(!order.isSettle()) {
			PaymentRequest paymentRequest = toQueryRequest(order);
			paymentRequest.setTrade_no_type(TradeNoType.Gateway);
			IPaymentProxy paymentProxy = paymentProxyFactory.getPaymentProxy(order.getStoreChannel().getPaymentGateway());
			PaymentResponse response = paymentProxy.query(paymentRequest);
			Bill bill = toBill(order, response);
			return bill;
		} else {
			return toBill(order);
		}
	}
	
	public Bill refund(String orderNo, String storeCode) {
		Order order = orderService.findActiveByOrderNo(orderNo);
		Assert.isTrue(storeCode.equals(order.getStore().getCode()), "No such order found for the store");
		
		if(!order.isRefundable()) {
			PaymentRequest paymentRequest = toQueryRequest(order);
			paymentRequest.setTrade_no_type(TradeNoType.Gateway);
			IPaymentProxy paymentProxy = paymentProxyFactory.getPaymentProxy(order.getStoreChannel().getPaymentGateway());
			PaymentResponse response = paymentProxy.refund(paymentRequest);
			
			Bill bill = toBill(order, response);
			if(OrderStatus.REFUND.equals(bill.getOrderStatus()) || OrderStatus.REVOKED.equals(bill.getOrderStatus())) {
				order.setStatus(bill.getOrderStatus());
				orderService.update(order);
			}
			return bill;
		} else {
			return toBill(order);
		}
	}

	private static final String DEFAULT_SUBJECT = "订单";
	private static final String LOCAL_ID = "106.14.47.193";
	private static final String DEFAULT_NOTIFY_URL = "http://106.14.47.193/xpay/notify/";
	private PaymentRequest toPaymentRequest(Order order) {
		PaymentRequest request = new PaymentRequest();
		request.setBusi_code(order.getStoreChannel().getExtStoreId());
		request.setDev_id(order.getDeviceId());
		request.setPay_channel(order.getPayChannel());
		request.setAmount(order.getTotalFee());
		request.setRaw_data(order.getAttach());
		request.setDown_trade_no(order.getOrderNo());
		if(PaymentGateway.SWIFTPASS.equals(order.getStoreChannel().getPaymentGateway())) {
			request.setIp(LOCAL_ID);
			request.setNotifyUrl(DEFAULT_NOTIFY_URL+order.getStoreChannel().getPaymentGateway());
		}
		
		
		if (order.getOrderDetail() != null) {
			request.setOper_id(order.getOrderDetail().getOperator());
			request.setSubject(order.getOrderDetail().getSubject());
			request.setGood_details(order.getOrderDetail().getOrderItems());
		} else {
			request.setSubject(DEFAULT_SUBJECT);
		}
		return request;
	}
	
	private PaymentRequest toQueryRequest(Order order) {
		PaymentRequest request = new PaymentRequest();
		
		request.setBusi_code(order.getStoreChannel().getExtStoreId());
		request.setPay_channel(order.getPayChannel());
		request.setDown_trade_no(order.getOrderNo());
		request.setTrade_no_type(TradeNoType.Gateway);

		return request;
	}

	private Bill toBill(Order order, PaymentResponse response) {
		if (response == null || !SUCCESS.equals(response.getCode())
				|| response.getData() == null) {
			String code = response == null ? NO_RESPONSE : response.getCode();
			String msg = response == null ? "No response" : response.getMsg();
			throw new GatewayException(code, msg);
		}
		TradeBean trade = response.getData();
		Bill bill = new Bill();
		bill.setCodeUrl(trade.getCode_url());
		bill.setPrepayId(trade.getPrepay_id());
		bill.setOrderNo(order.getOrderNo());
		bill.setGatewayOrderNo(trade.getTrade_no());
		bill.setOrderStatus(trade.getTrade_status());
		bill.setOrder(order);
		return bill;
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
	

	private static final int BAIL_MIAOFU_STORE_ID=1;
	private static final int BAIL_SWIFTPASS_STORE_ID=2;
	public Store findBailStore(PaymentGateway gateway) {
		if(PaymentGateway.MIAOFU.equals(gateway)) {
			return storeService.findById(BAIL_MIAOFU_STORE_ID);
		} else {
			return storeService.findById(BAIL_SWIFTPASS_STORE_ID);
		}
		
	}

}
