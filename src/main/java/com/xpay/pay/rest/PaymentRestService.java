package com.xpay.pay.rest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xpay.pay.ApplicationConstants;
import com.xpay.pay.exception.ApplicationException;
import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.model.Bill;
import com.xpay.pay.model.Order;
import com.xpay.pay.model.OrderDetail;
import com.xpay.pay.model.Store;
import com.xpay.pay.proxy.PaymentRequest.PayChannel;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.rest.contract.BaseResponse;
import com.xpay.pay.rest.contract.OrderResponse;
import com.xpay.pay.service.OrderService;
import com.xpay.pay.service.PaymentService;
import com.xpay.pay.service.StoreService;
import com.xpay.pay.util.CommonUtils;

@RestController
@RequestMapping("/v1/pay")
public class PaymentRestService extends AuthRestService {
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/unifiedorder ", method = RequestMethod.POST)
	public BaseResponse<OrderResponse> unifiedOrder(
			@RequestParam String storeId,   //Store.code
			@RequestParam String payChannel, // ALIPAY("1"), WECHAT("2")
			@RequestParam String totalFee, // <=3000yuan
			@RequestParam String orderTime, // yyyyMMddHHmmss
			@RequestParam(required = false) String sellerOrderNo,
			@RequestParam(required = false) String attach,
			@RequestParam(required = false) String deviceId,
			@RequestParam(required = false) String ip,
			@RequestParam(required = false) String notifyUrl,
			@RequestBody(required = false) OrderDetail orderDetail) {
		Assert.isTrue(StringUtils.isNoneBlank(storeId, payChannel, totalFee, orderTime), "StoreId, payChannel, totalFee and orderTime can not be null");
		PayChannel channel = PayChannel.fromValue(payChannel);
		Assert.notNull(channel,"Unknow pay channel");
		float fee = CommonUtils.toFloat(totalFee);
		Assert.isTrue(fee>0 && fee<3000, "Invalid total fee");
		
		Store store = storeService.findByCode(storeId);
		String orderNo = CommonUtils.buildOrderNo(getApp().getId(), store.getId());
		if(orderDetail != null) {
			orderService.insert(orderDetail);
		}
		BaseResponse<OrderResponse> response = new BaseResponse<OrderResponse>();
		Order order = null;
		Bill bill = null;
		do {
			order = paymentService.createOrder(orderNo, store, channel, deviceId, ip, totalFee, orderTime, sellerOrderNo, attach, notifyUrl, orderDetail.getId());
			Assert.notNull(order,"Create order failed");
			
			try {
				bill = paymentService.unifiedOrder(order);
				if(bill!=null) {
					OrderResponse orderResponse = toOrderResponse(bill);
					response.setData(orderResponse);
					return response;
				}
			} catch (GatewayException e) {
				response.setStatus(ApplicationConstants.STATUS_BAD_GATEWAY);
				response.setCode(e.getCode());
				response.setMessage(e.getMessage());
			} catch (ApplicationException e) {
				response.setStatus(ApplicationConstants.STATUS_INTERNAL_SERVER_ERROR);
				response.setCode(e.getCode());
				response.setMessage(e.getMessage());
			} finally {
				paymentService.updateBill(order, bill);
			}
		} while(order != null);
		
		throw new GatewayException("-1", "No avaiable payment gateway");

		
	}

	@RequestMapping(value = "/query/{orderNo} ", method = RequestMethod.GET)
	public BaseResponse<OrderResponse> query(
			@PathVariable String orderNo,
			@RequestParam String storeId,
			@RequestParam(required = false) String deviceId,
			@RequestParam(required = false) String ip) {
		OrderResponse orderResponse = new OrderResponse();
		orderResponse.setOrderNo(orderNo);
		orderResponse.setStoreId(storeId);
		orderResponse.setOrderStatus(OrderStatus.NOTPAY);
		BaseResponse<OrderResponse> response = new BaseResponse<OrderResponse>();
		response.setData(orderResponse);
		return response;
	}
	
	@RequestMapping(value = "/refund/{orderNo} ", method = RequestMethod.DELETE)
	public BaseResponse<OrderResponse> refund(
			@PathVariable String orderNo,
			@RequestParam String storeId,
			@RequestParam(required = false) String deviceId,
			@RequestParam(required = false) String ip) {
		OrderResponse orderResponse = new OrderResponse();
		orderResponse.setOrderNo(orderNo);
		orderResponse.setStoreId(storeId);
		orderResponse.setOrderStatus(OrderStatus.NOTPAY);
		BaseResponse<OrderResponse> response = new BaseResponse<OrderResponse>();
		response.setData(orderResponse);
		return response;
	}

	private OrderResponse toOrderResponse(Bill bill) {
		OrderResponse result = new OrderResponse();
		result.setOrderNo(bill.getOrderNo());
		result.setStoreId(bill.getOrder().getStore().getCode());
		result.setSellerOrderNo(bill.getOrder().getSellerOrderNo());
		result.setCodeUrl(bill.getCodeUrl());
		result.setPrepayId(bill.getPrepayId());
		result.setOrderStatus(bill.getOrderStatus());
		result.setAttach(bill.getOrder().getAttach());
		return result;
	}

}
