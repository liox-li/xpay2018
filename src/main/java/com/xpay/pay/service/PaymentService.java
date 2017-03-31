package com.xpay.pay.service;

import static com.xpay.pay.proxy.IPaymentProxy.NO_RESPONSE;
import static com.xpay.pay.proxy.IPaymentProxy.SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpay.pay.ApplicationConstants;
import com.xpay.pay.exception.Assert;
import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.model.Bill;
import com.xpay.pay.model.Order;
import com.xpay.pay.model.Store;
import com.xpay.pay.model.StoreChannel;
import com.xpay.pay.proxy.IPaymentProxy;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentRequest.PayChannel;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.proxy.PaymentResponse.TradeBean;

@Service
public class PaymentService {
	@Autowired
	private IPaymentProxy paymentProxy;
	@Autowired
	private OrderService orderService;
	@Autowired
	private StoreService storeService;

	public Order createOrder(String orderNo, Store store, PayChannel channel,
			String deviceId, String ip, String totalFee, String orderTime,
			String sellerOrderNo, String attach, String notifyUrl,
			long orderDetailId) {
		StoreChannel storeChannel = null;
		boolean isNextBailPay = store.isNextBailPay();
		if(isNextBailPay) {
			storeChannel = orderService.findUnusedChannel(this.findBailStore(), orderNo);
		} else {
			storeChannel = orderService.findUnusedChannel(store, orderNo);
		}
		Assert.notNull(storeChannel, "No avaiable store channel");
		
		Order order = new Order();
		order.setOrderNo(orderNo);
		order.setStoreId(store.getId());
		order.setStoreChannelId(storeChannel.getId());
		order.setPayChannel(channel);
		order.setDeviceId(deviceId);
		order.setIp(ip);
		order.setTotalFee(totalFee);
		order.setOrderTime(orderTime);
		order.setSellerOrderNo(sellerOrderNo);
		order.setAttach(attach);
		order.setNotifyUrl(notifyUrl);
		order.setDetailId(orderDetailId);
		orderService.insert(order);
		
		return order;
	} 

	public Bill unifiedOrder(Order order) {
		PaymentRequest request = toPaymentRequest(order);

		PaymentResponse response = paymentProxy.unifiedOrder(request);

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
			order.setCodeUrl(bill.getCodeUrl());
			order.setPrepayId(bill.getPrepayId());
			order.setStatus(bill.getOrderStatus());
		}
		return orderService.update(order);
	}
	
	public boolean updateBail(Store store, Bill bill) {
		if(bill != null) {
			boolean isBail = bill.getOrder().getStoreChannelId()<1000;
			if(isBail) {
				store.setBail(store.getBail() + bill.getOrder().getTotalFeeAsFloat());
			} else {
				store.setNonBail(store.getNonBail() + bill.getOrder().getTotalFeeAsFloat());
			}
			return storeService.updateById(store);
		}
		return true;
	}

	public Bill query(String orderNo) {
		return null;
	}

	private PaymentRequest toPaymentRequest(Order order) {
		PaymentRequest request = new PaymentRequest();
		request.setBusi_code(order.getStore().getCode());
		request.setDev_id(order.getDeviceId());
		request.setPay_channel(order.getPayChannel());
		request.setAmount(order.getTotalFee());
		request.setRaw_data(order.getAttach());
		request.setDown_trade_no(order.getOrderNo());
		if (order.getOrderDetail() != null) {
			request.setOper_id(order.getOrderDetail().getOperator());
			request.setSubject(order.getOrderDetail().getSubject());
			request.setGood_details(order.getOrderDetail().getOrderItems());
		}
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
	

	private static final int BAIL_STORE_ID=1;
	public Store findBailStore() {
		return storeService.findById(BAIL_STORE_ID);
	}

}
