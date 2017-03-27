package com.xpay.pay.rest;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xpay.pay.ApplicationConstants;
import com.xpay.pay.exception.ApplicationException;
import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.models.Bill;
import com.xpay.pay.models.Order;
import com.xpay.pay.models.OrderDetail;
import com.xpay.pay.proxy.PaymentRequest.PayChannel;
import com.xpay.pay.rest.contract.BaseResponse;
import com.xpay.pay.rest.contract.OrderResponse;
import com.xpay.pay.service.PaymentService;

@RestController
@RequestMapping("/v1/pay")
public class PaymentRestService {
	@Autowired
	private PaymentService paymentService;

	@RequestMapping(value = "/unifiedorder ", method = RequestMethod.POST)
	public BaseResponse<OrderResponse> unifiedOrder(
			@RequestParam String storeId,
			@RequestParam String payChannel, // ALIPAY("1"), WECHAT("2")
			@RequestParam String totalFee, // <=3000yuan
			@RequestParam String orderTime, // yyyyMMddHHmmss
			@RequestParam(required = false) String sellerOrderNo,
			@RequestParam(required = false) String attach,
			@RequestParam(required = false) String deviceId,
			@RequestParam(required = false) String ip,
			@RequestParam(required = false) String notifyUrl,
			@RequestBody(required = false) OrderDetail orderDetail) {

		Order order = new Order();
		order.setStoreId(storeId);
		order.setPayChannel(PayChannel.fromValue(payChannel));
		order.setDeviceId(deviceId);
		order.setIp(ip);
		order.setTotalFee(totalFee);
		order.setOrderTime(orderTime);
		order.setSellerOrderNo(sellerOrderNo);
		order.setAttach(attach);
		order.setNotifyUrl(notifyUrl);
		order.setOrderDetail(orderDetail);
		order.setOrderNo(UUID.randomUUID().toString());
		BaseResponse<OrderResponse> response = new BaseResponse<OrderResponse>();
		try {
			Bill bill = paymentService.unifiedOrder(order);
			OrderResponse orderResponse = toOrderResponse(bill);
			response.setData(orderResponse);
		} catch (GatewayException e) {
			response.setStatus(ApplicationConstants.STATUS_BAD_GATEWAY);
			response.setCode(e.getCode());
			response.setMessage(e.getMessage());
		} catch (ApplicationException e) {
			response.setStatus(ApplicationConstants.STATUS_INTERNAL_SERVER_ERROR);
			response.setCode(e.getCode());
			response.setMessage(e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/query/{orderNo} ", method = RequestMethod.GET)
	public BaseResponse<Bill> query(
			@PathVariable String orderNo,
			@RequestParam String storeId,
			@RequestParam(required = false) String deviceId,
			@RequestParam(required = false) String ip) {
		Order order = new Order();
		order.setStoreId(storeId);
		order.setDeviceId(deviceId);
		order.setIp(ip);
		order.setOrderNo(orderNo);
		// order.setPayChannel(payChannel);
		Bill bill = paymentService.query(orderNo);
		BaseResponse<Bill> response = new BaseResponse<Bill>();
		response.setData(bill);
		return response;
	}

	private OrderResponse toOrderResponse(Bill bill) {
		OrderResponse result = new OrderResponse();
		result.setOrderNo(bill.getOrderNo());
		result.setStoreId(bill.getOrder().getStoreId());
		result.setSellerOrderNo(bill.getOrder().getSellerOrderNo());
		result.setCodeUrl(bill.getCodeUrl());
		result.setPrepayId(bill.getPrepayId());
	//	result.setOrderStatus(bill.getOrderStatus());
		result.setAttach(bill.getOrder().getAttach());
		return result;
	}

}
