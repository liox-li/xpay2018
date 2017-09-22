package com.xpay.pay.proxy.kekepay;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.xpay.pay.ApplicationConstants;
import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.model.Bill;
import com.xpay.pay.proxy.IPaymentProxy;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.util.AppConfig;
import com.xpay.pay.util.CryptoUtils;

@Component
public class KekePayProxy implements IPaymentProxy {

  public static final String PAYED = "PAYED";
  public static final String TOPAY = "TOPAY";
  protected static final Logger logger = LogManager.getLogger("AccessLog");
  private static final AppConfig config = AppConfig.kekePayConfig;
  private static final String BASE_ENDPOINT = config.getProperty("provider.endpoint");
  private static final String PAY_ENDPOINT = BASE_ENDPOINT + config.getProperty("provider.pay");
  private static final String QUERY_ENDPOINT = BASE_ENDPOINT + config.getProperty("provider.query");
  private static final String appId = config.getProperty("provider.app.id");
  private static final String appSecret = config.getProperty("provider.app.secret");
  private static final String PRODUCT_TYPE = config.getProperty("provider.product.type");
  private static final String DEFAULT_JSAPI_URL = AppConfig.XPayConfig
      .getProperty("jsapi.endpoint");
  @Autowired
  RestTemplate kekePayProxy;

  public static OrderStatus toOrderStatus(String ordStatus) {
    if ("FINISH".equalsIgnoreCase(ordStatus) || "SUCCESS".equalsIgnoreCase(ordStatus)) {
      return OrderStatus.SUCCESS;
    } else if (ordStatus.equals("WAITING_PAYMENT")) {
      return OrderStatus.USERPAYING;
    } else if (ordStatus.equals("FAILED")) {
      return OrderStatus.PAYERROR;
    } else {
      return OrderStatus.NOTPAY;
    }
  }

  @Override
  public PaymentResponse unifiedOrder(PaymentRequest request) {
	  if(request.getTotalFeeAsFloat()<10f) {
		  throw new GatewayException(ApplicationConstants.CODE_COMMON, "Total fee must be more than 10.");
	  }

    String url = DEFAULT_JSAPI_URL + TOPAY + "/" + request.getOrderNo();
    PaymentResponse response = new PaymentResponse();
    response.setCode(PaymentResponse.SUCCESS);
    Bill bill = new Bill();
    bill.setCodeUrl(url);
    bill.setOrderStatus(OrderStatus.NOTPAY);
    response.setBill(bill);
    return response;
  }

  public String getJsUrl(PaymentRequest request) {
    KekePayRequest kekePayRequest = this.toKekePayRequest(PRODUCT_TYPE, request);
    MultiValueMap<String, String> keyPairs = this.getKeyPairs(kekePayRequest);
    String requestParam = this.signature(keyPairs, appSecret);
    return PAY_ENDPOINT + "?" + requestParam;
  }

  private String signature(MultiValueMap<String, String> keyPairs, String appSecret) {
    String[] keys = keyPairs.keySet().toArray(new String[keyPairs.size()]);
    Arrays.sort(keys, new Comparator<String>() {
      @Override
      public int compare(String x1, String x2) {
        return x1.compareTo(x2);
      }
    });

    UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
    builder.queryParams(keyPairs);
    try {
      String baseUrl = builder.build().encode("UTF-8").getQuery();
      builder.queryParam("paySecret", appSecret);
      String params = builder.build().toString().substring(1);
      logger.debug("sorted params: " + params);
      String md5 = CryptoUtils.md5(params);
      logger.debug("md5 upper: " + md5.toUpperCase());
      return baseUrl + "&sign=" + md5.toUpperCase();
    } catch (UnsupportedEncodingException e) {
      logger.error(e, e);
    }
    return null;
  }

  private MultiValueMap<String, String> getKeyPairs(PaymentRequest paymentRequest) {
    MultiValueMap<String, String> keyPairs = new LinkedMultiValueMap<>();
    keyPairs.add("payKey", appId);
    keyPairs.add("outTradeNo", paymentRequest.getOrderNo());
    return keyPairs;
  }

  private MultiValueMap<String, String> getKeyPairs(KekePayRequest kekePayRequest) {
    if (kekePayRequest == null) {
      return null;
    }
    MultiValueMap<String, String> keyPairs = new LinkedMultiValueMap<>();

    if (StringUtils.isNotBlank(kekePayRequest.getNotifyUrl())) {
      keyPairs.add("notifyUrl", kekePayRequest.getNotifyUrl());
    }
    if (StringUtils.isNotBlank(kekePayRequest.getOrderIp())) {
      keyPairs.add("orderIp", kekePayRequest.getOrderIp());
    }
    if (StringUtils.isNotBlank(kekePayRequest.getOrderPrice())) {
      keyPairs.add("orderPrice", kekePayRequest.getOrderPrice());
    }
    if (StringUtils.isNotBlank(kekePayRequest.getOrderTime())) {
      keyPairs.add("orderTime", kekePayRequest.getOrderTime());
    }
    if (StringUtils.isNotBlank(kekePayRequest.getOutTradeNo())) {
      keyPairs.add("outTradeNo", kekePayRequest.getOutTradeNo());
    }
    if (StringUtils.isNotBlank(kekePayRequest.getPayBankAccountNo())) {
      keyPairs.add("payBankAccountNo", kekePayRequest.getPayBankAccountNo());
    }
    if (StringUtils.isNotBlank(kekePayRequest.getPayKey())) {
      keyPairs.add("payKey", kekePayRequest.getPayKey());
    }
    if (StringUtils.isNotBlank(kekePayRequest.getProductName())) {
      keyPairs.add("productName", kekePayRequest.getProductName());
    }
    if (StringUtils.isNotBlank(kekePayRequest.getProductType())) {
      keyPairs.add("productType", kekePayRequest.getProductType());
    }
    if (StringUtils.isNotBlank(kekePayRequest.getReturnUrl())) {
      keyPairs.add("returnUrl", kekePayRequest.getReturnUrl());
    }
    return keyPairs;
  }

  private KekePayRequest toKekePayRequest(String productType, PaymentRequest request) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    KekePayRequest kekePayRequest = new KekePayRequest();
    kekePayRequest.setNotifyUrl(request.getNotifyUrl());
    kekePayRequest.setOrderIp(request.getServerIp());
    kekePayRequest.setOrderPrice(request.getTotalFee());
    kekePayRequest.setOrderTime(simpleDateFormat.format(new Date()));
    kekePayRequest.setOutTradeNo(request.getOrderNo());
    kekePayRequest.setPayBankAccountNo("123456789");
    kekePayRequest.setProductName(request.getSubject());
    kekePayRequest.setPayKey(appId);
    kekePayRequest.setProductType(productType);
    kekePayRequest.setReturnUrl(DEFAULT_JSAPI_URL + PAYED + "/" + request.getOrderNo());
    return kekePayRequest;
  }

  @Override
  public PaymentResponse query(PaymentRequest request) {
    String url = QUERY_ENDPOINT;
    logger.info("query POST: " + url);
    long l = System.currentTimeMillis();
    PaymentResponse response = null;
    try {

      MultiValueMap<String, String> keyPairs = this.getKeyPairs(request);
      String sign = this.signature(keyPairs, appSecret);
      keyPairs.add("sign", sign);
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
      headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
      HttpEntity<?> httpEntity = new HttpEntity<>(keyPairs, headers);
      KekePayQueryResponse kekePayQueryResponse = kekePayProxy
          .exchange(url, HttpMethod.POST, httpEntity, KekePayQueryResponse.class).getBody();

      response = toPaymentResponse(kekePayQueryResponse);
      logger.info("query result: " + kekePayQueryResponse.getResultCode() + " "
          + kekePayQueryResponse.getErrMsg() + " "
          + response.getBill().getOrderStatus() + ", took "
          + (System.currentTimeMillis() - l) + "ms");
    } catch (RestClientException e) {
      logger.info("query failed, took "
          + (System.currentTimeMillis() - l) + "ms", e);
      throw e;
    }
    return response;
  }

  @Override
  public PaymentResponse refund(PaymentRequest request) {
    return null;
  }

  private PaymentResponse toPaymentResponse(KekePayQueryResponse kekePayQueryResponse) {
    if (kekePayQueryResponse == null
        || !KekePayQueryResponse.SUCCESS.equals(kekePayQueryResponse.getResultCode())) {
      String code = kekePayQueryResponse == null ? NO_RESPONSE : kekePayQueryResponse
          .getResultCode();
      String msg = kekePayQueryResponse == null ? "No response"
          : kekePayQueryResponse.getErrMsg();
      throw new GatewayException(code, msg);
    }
    PaymentResponse response = new PaymentResponse();
    response.setCode(PaymentResponse.SUCCESS);
    Bill bill = new Bill();
    bill.setOrderNo(kekePayQueryResponse.getOutTradeNo());
    bill.setGatewayOrderNo(kekePayQueryResponse.getTrxNo());
    bill.setOrderStatus(toOrderStatus(kekePayQueryResponse.getOrderStatus()));
    response.setBill(bill);
    return response;
  }


}