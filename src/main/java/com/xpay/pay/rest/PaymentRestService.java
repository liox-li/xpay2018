package com.xpay.pay.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xpay.pay.ApplicationConstants;
import com.xpay.pay.exception.ApplicationException;
import com.xpay.pay.exception.Assert;
import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.model.App;
import com.xpay.pay.model.Bill;
import com.xpay.pay.model.Order;
import com.xpay.pay.model.OrderRequest;
import com.xpay.pay.model.Store;
import com.xpay.pay.model.StoreChannel.PaymentGateway;
import com.xpay.pay.proxy.IPaymentProxy.PayChannel;
import com.xpay.pay.rest.contract.BaseResponse;
import com.xpay.pay.rest.contract.OrderResponse;
import com.xpay.pay.service.AppService;
import com.xpay.pay.service.PaymentService;
import com.xpay.pay.service.RiskCheckService;
import com.xpay.pay.service.StoreService;
import com.xpay.pay.util.CommonUtils;
import com.xpay.pay.util.IDGenerator;

@RestController
@RequestMapping("/v1/pay")
public class PaymentRestService extends AuthRestService {
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private AppService appService;
	@Autowired
	private RiskCheckService riskCheckService;
	
	@RequestMapping(value = "/unifiedorder", method = RequestMethod.POST)
	public BaseResponse<OrderResponse> unifiedOrder(
			@RequestParam(required = false) String storeId,   //Store.code
			@RequestParam(required = false) String payChannel, // ALIPAY("1"), WECHAT("2")
			@RequestParam(required = false) String totalFee, // <=3000yuan
			@RequestParam(required = false) String orderTime, // yyyyMMddHHmmss
			@RequestParam(required = false) String sellerOrderNo,
			@RequestParam(required = false) String attach,
			@RequestParam(required = false) String deviceId,
			@RequestParam(required = false) String ip,
			@RequestParam(required = false) String notifyUrl,
			@RequestParam(required = false) String returnUrl,
			@RequestParam(required = false) String subject,
			@RequestParam(required = false) String storeChannel,
			@RequestParam(required = false) String uid,
			@RequestParam(required = false) String subChannelId,
			@RequestBody(required = false) OrderRequest payload) {
		if(StringUtils.isBlank(storeId)) {
			Assert.notNull(payload, "Order request can not be null");
			storeId = payload.getStoreId();
			payChannel = payload.getPayChannel();
			totalFee = payload.getTotalFee();
			orderTime = payload.getOrderTime();
			deviceId = payload.getDeviceId();
			ip = payload.getIp();
			sellerOrderNo = payload.getSellerOrderNo();
			attach = payload.getAttach();
			subject = payload.getSubject();
			notifyUrl = payload.getNotifyUrl();
			returnUrl = payload.getReturnUrl();
			uid = payload.getUid();
			subChannelId = payload.getSubChannelId();
	    }
		Assert.isTrue(StringUtils.isNoneBlank(storeId, payChannel, totalFee, orderTime), "StoreId, payChannel, totalFee and orderTime can not be null");
		Assert.isTrue(StringUtils.isNotBlank(deviceId) || StringUtils.isNotBlank(ip), "DeviceId or ip must be provided");
		PayChannel channel = PayChannel.fromValue(payChannel);
		Assert.notNull(channel,"Unknow pay channel");
		float fee = CommonUtils.toFloat(totalFee);
		Assert.isTrue(fee>=0.01f && fee<=3000, String.format("Invalid total fee: %s, sellerOrderNo: %s",totalFee, StringUtils.trimToEmpty(sellerOrderNo)));
		String orderDate = validateOrderTime(orderTime);

		Store store = storeService.findByCode(storeId);
		paymentService.validateQuota(store);
		
		paymentService.validateStoreLink(store, returnUrl);
		
//		Assert.isTrue(riskCheckService.checkFee(store, CommonUtils.toFloat(totalFee)), String.format("Invalid total fee: %s, sellerOrderNo: %s",totalFee, StringUtils.trimToEmpty(sellerOrderNo)));
		/*float minus=(float)(Math.random()*1);
		minus = (float)(Math.round(minus*10))/10;
		if(minus <= 0f){
			minus = 0.11f;
		}
		fee = fee -minus;
		if(fee <= 0f){
			fee = 0.1f;
		}*/
		App app = getApp();
		String orderNo = IDGenerator.buildOrderNo(app.getId().intValue(), store.getId());
		BaseResponse<OrderResponse> response = new BaseResponse<OrderResponse>();
		Order order = null;
		Bill bill = null;
		int loop = 3; //如果遇到GatewayException 时，我们最多访问3次后退出
		// 同一个orderNo 会有多笔订单
		Long subChanId = null;
		if(subChannelId != null && !subChannelId.equals("")){
			subChanId = Long.parseLong(subChannelId);
		}
		do {
			order = paymentService.createOrder(app, uid, orderNo, store, channel, deviceId, ip, fee, orderDate, sellerOrderNo, attach, notifyUrl, returnUrl, subject, storeChannel,subChanId);
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
				if(--loop <= 0){
					break;
				}
			} catch (ApplicationException e) {
				response.setStatus(ApplicationConstants.STATUS_INTERNAL_SERVER_ERROR);
				response.setCode(e.getCode());
				response.setMessage(e.getMessage());
				break;
			} finally {
				paymentService.updateBill(order, bill);
			}
		} while(order != null);
		
		throw new GatewayException("-1", "No avaiable payment gateway");		
	}

	private String validateOrderTime(String orderTime) {
		SimpleDateFormat timeFormat = new SimpleDateFormat(IDGenerator.TimePattern14);
		try {
			timeFormat.parse(orderTime);
			return orderTime;
		} catch (ParseException e) {
			return IDGenerator.formatNow(IDGenerator.TimePattern14);
		}
	}

	@RequestMapping(value = "/query/{orderNo} ", method = RequestMethod.GET)
	public BaseResponse<OrderResponse> query(
			@PathVariable String orderNo,
			@RequestParam String storeId,
			@RequestParam(required = false) String deviceId,
			@RequestParam(required = false) String ip,
			@RequestParam(required = false, defaultValue = "false") Boolean isCsr) {
		Assert.isTrue(StringUtils.isNoneBlank(orderNo, storeId), "OrderNo and storeId can not be null");
		Bill bill = paymentService.query(getApp().getId(), orderNo, storeId, isCsr);
		
		BaseResponse<OrderResponse> response = new BaseResponse<OrderResponse>();
		OrderResponse orderResponse = new OrderResponse();
		orderResponse.setOrderNo(orderNo);
		orderResponse.setStoreId(storeId);
		orderResponse.setSellerOrderNo(bill.getOrder().getSellerOrderNo());
		orderResponse.setCodeUrl(bill.getCodeUrl());
		orderResponse.setPrepayId(bill.getPrepayId());
		orderResponse.setOrderStatus(bill.getOrderStatus().getValue());
		orderResponse.setAttach(bill.getOrder().getAttach());
		response.setData(orderResponse);

		return response;
	}
	
	@RequestMapping(value = "/refund/{orderNo} ", method = RequestMethod.DELETE)
	public BaseResponse<OrderResponse> refund(
			@PathVariable String orderNo,
			@RequestParam String storeId,
			@RequestParam(required = false) String deviceId,
			@RequestParam(required = false) String ip,
			@RequestParam(required = false, defaultValue = "false") Boolean isCsr) {
		Assert.isTrue(StringUtils.isNoneBlank(orderNo, storeId), "OrderNo and storeId can not be null");
		Bill bill = paymentService.refund(getApp().getId(), orderNo, storeId, isCsr);
		
		BaseResponse<OrderResponse> response = new BaseResponse<OrderResponse>();
		OrderResponse orderResponse = new OrderResponse();
		orderResponse.setOrderNo(orderNo);
		orderResponse.setStoreId(storeId);
		orderResponse.setSellerOrderNo(bill.getOrder().getSellerOrderNo());
		orderResponse.setCodeUrl(bill.getCodeUrl());
		orderResponse.setPrepayId(bill.getPrepayId());
		orderResponse.setOrderStatus(bill.getOrderStatus().getValue());
		orderResponse.setAttach(bill.getOrder().getAttach());
		response.setData(orderResponse);
		
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/refresh/{domain}", method = RequestMethod.GET)
	public BaseResponse<OrderResponse> refresh(@PathVariable String domain) {
		if("app".equals(domain)) {
			appService.refreshCache();
		} else if("store".equals(domain)) {
			storeService.refreshCache();
		}
		return BaseResponse.OK;
	}
			

	private OrderResponse toOrderResponse(Bill bill) {
		OrderResponse result = new OrderResponse();
		result.setOrderNo(bill.getOrderNo());
		result.setStoreId(bill.getOrder().getStore().getCode());
		result.setSellerOrderNo(bill.getOrder().getSellerOrderNo());
		result.setCodeUrl(bill.getCodeUrl());
		result.setPrepayId(bill.getPrepayId());
		result.setTokenId(bill.getTokenId());
		result.setPayInfo(bill.getPayInfo());
		result.setOrderStatus(bill.getOrderStatus().getValue());
		result.setAttach(bill.getOrder().getAttach());
		return result;
	}
}
