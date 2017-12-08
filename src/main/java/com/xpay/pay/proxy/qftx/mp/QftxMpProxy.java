package com.xpay.pay.proxy.qftx.mp;

import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.model.Bill;
import com.xpay.pay.proxy.IPaymentProxy;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.util.AppConfig;
import com.xpay.pay.util.CryptoUtils;
import com.xpay.pay.util.HttpClient;
import com.xpay.pay.util.IDGenerator;
import com.xpay.pay.util.XmlUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Created by sunjian on Date: 2017/12/8 下午3:02
 * Description:
 */
@Component
public class QftxMpProxy implements IPaymentProxy {

  private static final AppConfig config = AppConfig.QftxMpConfig;
  private static final String baseEndpoint = config.getProperty("provider.endpoint");
  private static final String pathMpPay = config.getProperty("provider.mp.pay");
  private static final String pathQuery = config.getProperty("provider.query");
  protected final Logger logger = LogManager.getLogger("AccessLog");

  @Override
  public PaymentResponse unifiedOrder(PaymentRequest request) {
    String[] appKeys = getAppKeys(request.getExtStoreId());
    MpRequest mpRequest = this.toMpRequest(request, appKeys[0]);
    String sign = signature(mpRequest, appKeys[1]);
    mpRequest.setSign(sign);
    List<KeyValuePair> keyPairs = this.getKeyPairs(mpRequest);
    String xmlBody = XmlUtils.toXml(keyPairs);
    String url = baseEndpoint + pathMpPay;
    String result = HttpClient.doPost(url, xmlBody, DEFAULT_TIMEOUT);
    PaymentResponse paymentResponse = null;
    try {
      paymentResponse = toPaymentResponse(request, result);
    } catch (Exception e) {
      logger.error("ToPaymentResponse error", e);
    }
    return paymentResponse;
  }

  private String[] getAppKeys(String extStoreNo) {
    return extStoreNo.split(",");
  }

  private PaymentResponse toPaymentResponse(PaymentRequest request, String result)
      throws Exception {
    logger.info("response:" + result);
    String[] appKeys = getAppKeys(request.getExtStoreId());
    Map<String, String> params = XmlUtils.fromXml(result.getBytes(), "utf-8");
    if (!PaymentResponse.SUCCESS.equals(params.get("status"))) {
      String code = params.get("status");
      String msg = params.get("message");
      msg = StringUtils.isBlank(msg) ? params.get("message") : msg;
      throw new GatewayException(code, msg);
    }
    boolean checkSign = CryptoUtils.checkSignature(params, appKeys[1], "sign", "key");

    if (!checkSign || !PaymentResponse.SUCCESS.equals(params.get("result_code"))) {
      String code = params.get("result_code");
      String msg = params.get("message");
      msg = StringUtils.isBlank(msg) ? params.get("message") : msg;
      throw new GatewayException(code, msg);
    }
    PaymentResponse response = new PaymentResponse();
    response.setCode(PaymentResponse.SUCCESS);
    Bill bill = new Bill();
    bill.setCodeUrl(params.get("redirect_url"));
    bill.setOrderNo(request.getOrderNo());
    bill.setOrderStatus(OrderStatus.NOTPAY);
    response.setBill(bill);
    return response;
  }

  private MpRequest toMpRequest(PaymentRequest paymentRequest, String appId) {
    MpRequest mpRequest = new MpRequest();
    mpRequest.setMch_id(appId);
    mpRequest.setOut_trade_no(paymentRequest.getOrderNo());
    mpRequest.setDevice_info(paymentRequest.getDeviceId());
    mpRequest.setBody(paymentRequest.getSubject());
    mpRequest.setAttach(paymentRequest.getAttach());
    if (paymentRequest.getTotalFee() != null) {
      mpRequest.setTotal_fee((int) (paymentRequest.getTotalFee() * 100));
    }
    mpRequest.setMch_create_ip(paymentRequest.getServerIp());
    mpRequest.setNotify_url(paymentRequest.getNotifyUrl());
    mpRequest.setNonce_str(IDGenerator.buildKey(10));
    return mpRequest;
  }

  private String signature(MpRequest request, String appSecret) {
    List<KeyValuePair> keyPairs = getKeyPairs(request);

    UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
    for (KeyValuePair pair : keyPairs) {
      builder.queryParam(pair.getKey(), pair.getValue());
    }
    builder.queryParam("key", appSecret);
    String params = builder.build().toString().substring(1);
    logger.debug("sorted params: " + params);
    String md5 = CryptoUtils.md5(params);
    logger.debug("md5 upper: " + md5.toUpperCase());
    return md5 == null ? null : md5.toUpperCase();
  }

  private List<KeyValuePair> getKeyPairs(MpRequest paymentRequest) {
    if (paymentRequest == null) {
      return null;
    }
    List<KeyValuePair> keyPairs = new ArrayList<KeyValuePair>();

    if (StringUtils.isNotBlank(paymentRequest.getMch_id())) {
      keyPairs.add(new KeyValuePair("mch_id", paymentRequest.getMch_id()));
    }
    if (StringUtils.isNotBlank(paymentRequest.getOut_trade_no())) {
      keyPairs.add(new KeyValuePair("out_trade_no", paymentRequest.getOut_trade_no()));
    }
    if (StringUtils.isNotBlank(paymentRequest.getDevice_info())) {
      keyPairs.add(new KeyValuePair("device_info", paymentRequest.getDevice_info()));
    }
    if (StringUtils.isNotBlank(paymentRequest.getBody())) {
      keyPairs.add(new KeyValuePair("body", paymentRequest.getBody()));
    }
    if (StringUtils.isNotBlank(paymentRequest.getAttach())) {
      keyPairs.add(new KeyValuePair("attach", paymentRequest.getAttach()));
    }
    if (paymentRequest.getTotal_fee() > 0) {
      keyPairs.add(new KeyValuePair("total_fee", String.valueOf(paymentRequest.getTotal_fee())));
    }
    if (StringUtils.isNotBlank(paymentRequest.getMch_create_ip())) {
      keyPairs.add(new KeyValuePair("mch_create_ip", paymentRequest.getMch_create_ip()));
    }
    if (StringUtils.isNotBlank(paymentRequest.getNotify_url())) {
      keyPairs.add(new KeyValuePair("notify_url", paymentRequest.getNotify_url()));
    }
    keyPairs.add(new KeyValuePair("nonce_str", paymentRequest.getNonce_str()));
    if (StringUtils.isNotBlank(paymentRequest.getSign())) {
      keyPairs.add(new KeyValuePair("sign", paymentRequest.getSign()));
    }
    keyPairs.sort((x1, x2) -> {
      return x1.getKey().compareTo(x2.getKey());
    });
    return keyPairs;
  }

  @Override
  public PaymentResponse query(PaymentRequest request) {
    String[] appKeys = getAppKeys(request.getExtStoreId());
    MpRequest mpRequest = this.toMpRequest(request, appKeys[0]);
    String sign = signature(mpRequest, appKeys[1]);
    mpRequest.setSign(sign);
    List<KeyValuePair> keyPairs = this.getKeyPairs(mpRequest);
    String xmlBody = XmlUtils.toXml(keyPairs);

    String url = baseEndpoint + pathQuery;
    String result = HttpClient.doPost(url, xmlBody, DEFAULT_TIMEOUT);
    PaymentResponse paymentResponse = null;
    try {
      paymentResponse = toPaymentResponse(result, appKeys[1]);
    } catch (Exception e) {
      logger.error("ToPaymentResponse error", e);
    }
    return paymentResponse;
  }

  private PaymentResponse toPaymentResponse(String result, String appSecret) throws Exception {
    Map<String, String> params = XmlUtils.fromXml(result.getBytes(), "utf-8");
    if (!PaymentResponse.SUCCESS.equals(params.get("status"))) {
      String code = params.get("status");
      String msg = params.get("message");
      msg = StringUtils.isBlank(msg) ? params.get("message") : msg;
      throw new GatewayException(code, msg);
    }
    boolean checkSign = CryptoUtils.checkSignature(params, appSecret, "sign", "key");
    if (!checkSign || !PaymentResponse.SUCCESS.equals(params.get("result_code"))) {
      String code = params.get("err_code");
      String msg = params.get("err_msg");
      throw new GatewayException(code, msg);
    }
    PaymentResponse response = new PaymentResponse();
    response.setCode(PaymentResponse.SUCCESS);
    Bill bill = new Bill();
    bill.setOrderNo(params.get("out_trade_no"));
    bill.setGatewayOrderNo(params.get("transaction_id"));
    String tradeStatus = params.get("trade_state");
    OrderStatus orderStatus =
        StringUtils.isBlank(tradeStatus) ? OrderStatus.NOTPAY : OrderStatus.valueOf(tradeStatus);
    bill.setOrderStatus(orderStatus);
    response.setBill(bill);
    return response;
  }

  OrderStatus toOrderStatus(String tradeStatus) {
    switch (tradeStatus) {
      case "SUCCESS":
      case "NOTPAY":
      case "PAYERROR":
        return OrderStatus.valueOf(tradeStatus);
      case "CLOSE":
        return OrderStatus.REVOKED;
      case "FREEZED":
        return OrderStatus.USERPAYING;
      default:
        return OrderStatus.NOTPAY;
    }
  }

  @Override
  public PaymentResponse refund(PaymentRequest request) {
    return null;
  }
}