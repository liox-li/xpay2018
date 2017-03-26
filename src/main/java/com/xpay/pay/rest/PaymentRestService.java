package com.xpay.pay.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xpay.pay.models.Bill;
import com.xpay.pay.models.Order;
import com.xpay.pay.proxy.PaymentRequest.GoodBean;
import com.xpay.pay.proxy.PaymentRequest.PayChannel;
import com.xpay.pay.service.PaymentService;

@RestController
@RequestMapping("/v1/pay")
public class PaymentRestService {
	@Autowired
	private PaymentService paymentService;
	
	@RequestMapping(value = "/unifiedorder ", method = RequestMethod.PUT)
	public BaseResponse<Bill> unifiedOrder(
			@RequestParam String storeId,
			@RequestParam PayChannel payChannel,
			@RequestParam String ip,
			@RequestParam String totalFee,
			@RequestParam String orderTime, // yyyyMMddHHmmss
			@RequestParam(required = false) String storeName,
			@RequestParam(required = false) String operator,
			@RequestParam(required = false) String deviceInfo,
			@RequestParam(required = false) String sellerOrderNo,
			@RequestBody(required = false) String orderSubject,
			@RequestBody(required = false) String orderDesc,
			@RequestBody(required = false) GoodBean[] orderItems,
			@RequestParam(required = false) String notifyUrl,
			@RequestParam(required = false) String attach) {
		
		Order order = new Order();
		order.setStoreId(storeId);
		order.setStoreName(storeName);
		order.setOperator(operator);
		order.setDeviceInfo(deviceInfo);
		order.setPayChannel(payChannel);
		order.setIp(ip);
		order.setTotalFee(totalFee);
		order.setAttach(attach);
		order.setSellerOrderNo(sellerOrderNo);
		order.setOrderSubject(orderSubject);
		order.setOrderDesc(orderDesc);
		order.setOrderItems(orderItems);
		order.setNotifyUrl(notifyUrl);
		order.setOrderTime(orderTime);
		
		Bill bill = paymentService.unifiedOrder(order);
		BaseResponse<Bill> response = new BaseResponse<Bill>();
		response.setData(bill);
		return response;
	}
	
	@RequestMapping(value = "/query ", method = RequestMethod.GET)
	public BaseResponse<Bill> query(
			@RequestParam String storeId,
			@RequestParam String ip,
			@RequestParam String orderNo,
			@RequestParam PayChannel payChannel
			) {
		Order order = new Order();
		order.setStoreId(storeId);
		order.setIp(ip);
		order.setOrderNo(orderNo);
		order.setPayChannel(payChannel);
		Bill bill = paymentService.query(order);
		BaseResponse<Bill> response = new BaseResponse<Bill>();
		response.setData(bill);
		return response;
	}
			
}
