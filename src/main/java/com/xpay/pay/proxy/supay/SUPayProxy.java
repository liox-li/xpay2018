package com.xpay.pay.proxy.supay;

import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.model.Bill;
import com.xpay.pay.model.StoreChannel.SUPayProps;
import com.xpay.pay.proxy.IPaymentProxy;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.util.AppConfig;
import com.xpay.pay.util.JsonUtils;
import com.xpay.pay.util.RSASignature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by sunjian on Date: 2017/12/14 下午11:28
 * Description:
 */
@Component
public class SUPayProxy implements IPaymentProxy {

  protected static final Logger logger = LogManager.getLogger(SUPayProxy.class);
  private static final AppConfig config = AppConfig.SUPayConfig;
  private static final String BASE_ENDPOINT = config.getProperty("provider.endpoint");
  private static final String appId = config.getProperty("provider.app.id");
  private static final String privateKey = config.getProperty("provider.private.key");
  public static final String publicKey = config.getProperty("provider.public.key");

  @Autowired
  RestTemplate suPayProxy;

  @Override
  public PaymentResponse unifiedOrder(PaymentRequest request) {

    String url = BASE_ENDPOINT;

    long l = System.currentTimeMillis();
    PaymentResponse response = null;
    try {
      SUPayRequest suPayrequest = this
          .toSUPayRequest((SUPayProps) request.getChannelProps(), request);
      logger.info("unifiedOrder POST: " + url + ", body " + JsonUtils.toJson(suPayrequest));
      HttpHeaders headers = new HttpHeaders();
      headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
      HttpEntity<?> httpEntity = new HttpEntity<>(suPayrequest, headers);
      SUPayResponse suPayResponse = suPayProxy
          .exchange(url, HttpMethod.POST, httpEntity, SUPayResponse.class).getBody();
      logger.info("unifiedOrder result: " + suPayResponse.getCode() + " " + suPayResponse
          .getMsg() + ", took "
          + (System.currentTimeMillis() - l) + "ms");
      response = toPaymentResponse(suPayResponse);
    } catch (RestClientException e) {
      logger.info("unifiedOrder failed, took " + (System.currentTimeMillis() - l) + "ms", e);
      throw e;
    }
    return response;
  }

  private PaymentResponse toPaymentResponse(SUPayResponse suPayResponse) {

    if (!RSASignature.doCheck(
        suPayResponse.getCode() + suPayResponse.getMsg() + suPayResponse.getDate() + suPayResponse
            .getResult(), suPayResponse.getSign(), publicKey)) {
      throw new GatewayException(suPayResponse.getCode(), "签名不匹配");
    }

    if (!"100".equals(suPayResponse.getCode())) {
      throw new GatewayException(suPayResponse.getCode(), suPayResponse.getMsg());
    }
    PaymentResponse response = new PaymentResponse();

    PaymentResult paymentResult = JsonUtils
        .fromJson(suPayResponse.getResult(), PaymentResult.class);
    response.setCode(PaymentResponse.SUCCESS);
    Bill bill = new Bill();
    bill.setCodeUrl(paymentResult.getPayCode());
    bill.setOrderNo(paymentResult.getMerOrderId());
    bill.setGatewayOrderNo(paymentResult.getPlatformOrderId());
    bill.setOrderStatus(OrderStatus.NOTPAY);
    response.setBill(bill);
    return response;
  }

  private SUPayRequest toSUPayRequest(SUPayProps channelProps, PaymentRequest request) {
    Payment payment = new Payment();
    payment.setAmount(Float.valueOf(request.getTotalFee() * 100).intValue());
    payment.setItemId(channelProps.getItemId());
    payment.setItemName(request.getSubject());
    payment.setPayType(channelProps.getPayType());
    payment.setNotifyUrl(request.getNotifyUrl());
    String params = JsonUtils.toJson(payment);
    SUPayRequest suPayRequest = new SUPayRequest();
    suPayRequest.setDate(String.valueOf(System.currentTimeMillis()));
    suPayRequest.setMerchantNo(appId);
    suPayRequest.setMerOrderId(request.getOrderNo());
    suPayRequest.setParams(params);
    suPayRequest.setServerCode(channelProps.getServerCode());
    suPayRequest.setVersion("1.0");
    suPayRequest.setSign(RSASignature.sign(
        suPayRequest.getMerOrderId() + suPayRequest.getMerchantNo() + suPayRequest.getDate()
            + params, privateKey));
    return suPayRequest;
  }

  @Override
  public PaymentResponse query(PaymentRequest request) {
    return null;
  }

  @Override
  public PaymentResponse refund(PaymentRequest request) {
    return null;
  }
}