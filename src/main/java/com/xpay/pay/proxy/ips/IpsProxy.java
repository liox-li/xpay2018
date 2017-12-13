package com.xpay.pay.proxy.ips;

import cn.com.ips.payat.webservice.scan.ScanService;
import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.model.Bill;
import com.xpay.pay.model.StoreChannel.IpsProps;
import com.xpay.pay.proxy.IPaymentProxy;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.proxy.ips.common.ReqHead;
import com.xpay.pay.proxy.ips.gatewayreq.Body;
import com.xpay.pay.proxy.ips.gatewayreq.GateWayReq;
import com.xpay.pay.proxy.ips.gatewayreq.Ips;
import com.xpay.pay.util.CryptoUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Component;

/**
 * Created by sunjian on Date: 2017/12/12 下午9:51
 * Description:
 */
@Component
public class IpsProxy implements IPaymentProxy {

  private static final String SUCCESS = "000000";
  protected final Logger logger = LogManager.getLogger(IpsProxy.class);
  @Autowired
  Marshaller marshaller;
  Unmarshaller unmarshaller;

  @Autowired
  private ScanService scanService;

  @Override
  public PaymentResponse unifiedOrder(PaymentRequest request) {
    String[] accountParam = request.getExtStoreId().split(",");
    try {
      //merCode, account, signature md5
      String merCode = accountParam[0];
      String account = accountParam[1];
      String md5Signature = accountParam[2];
      Ips ips = toIps(request, merCode, account, md5Signature);
      ByteArrayOutputStream os;
      os = new ByteArrayOutputStream();
      marshaller.marshal(ips, new StreamResult(os));
      String scanPayRequest = os.toString();
      logger.info("ips order request: " + scanPayRequest);
      String result = scanService.scanPay(scanPayRequest);
      StreamSource streamSource = new StreamSource(new ByteArrayInputStream(result.getBytes()));
      logger.info("ips order response: " + result);
      com.xpay.pay.proxy.ips.gatewayrsp.Ips respIps = (com.xpay.pay.proxy.ips.gatewayrsp.Ips) unmarshaller
          .unmarshal(streamSource);
      if (!SUCCESS.equals(respIps.getGateWayRsp().getHead().getRspCode())) {
        throw new GatewayException(respIps.getGateWayRsp().getHead().getRspCode(),
            respIps.getGateWayRsp().getHead().getRspMsg());
      }
      signatureValidWithThrow(merCode, md5Signature, result, respIps);
      PaymentResponse response = new PaymentResponse();
      response.setCode(PaymentResponse.SUCCESS);
      Bill bill = new Bill();
      bill.setCodeUrl(respIps.getGateWayRsp().getBody().getQrCode());
      bill.setOrderNo(request.getOrderNo());
      bill.setOrderStatus(OrderStatus.NOTPAY);
      response.setBill(bill);
      return response;
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }

    return null;
  }

  private void signatureValidWithThrow(String merCode, String md5Signature, String result,
      com.xpay.pay.proxy.ips.gatewayrsp.Ips respIps) {
    int startIndex = result.indexOf("<body>");
    int endIndex = result.indexOf("</body>");
    String signature;
    if (startIndex > -1 && endIndex > -1) {
      signature = CryptoUtils
          .md5(result.substring(startIndex, endIndex + 7) + merCode + md5Signature);
    } else {
      signature = CryptoUtils.md5(merCode + md5Signature);
    }
    if (signature == null || !signature
        .equals(respIps.getGateWayRsp().getHead().getSignature())) {
      throw new RuntimeException("bad request signature not matches: " + signature);
    }
  }

  private Ips toIps(PaymentRequest request, String merCode, String account, String md5Signature)
      throws IOException {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    String date = simpleDateFormat.format(new Date());
    Ips ips = new Ips();
    GateWayReq gateWayReq = new GateWayReq();
    Body body = new Body();
    body.setMerBillNo(request.getOrderNo());
    switch (request.getPayChannel()) {
      case ALIPAY:
        body.setGatewayType("11");
        break;
      case WECHAT:
        body.setGatewayType("10");
        break;
      default:
        throw new RuntimeException("Not Support the pay channel");
    }
    if (request.getChannelProps() != null) {
      IpsProps props = (IpsProps) request.getChannelProps();
      body.setMerType(props.getMerType());
      body.setSubMerCode(props.getSubMerCode());
    }
    body.setDate(date.substring(0, 8));
    body.setCurrencyType("156");
    NumberFormat numberFormat = new DecimalFormat("#.##");
    numberFormat.setGroupingUsed(false);
    numberFormat.setMaximumFractionDigits(2);
    numberFormat.setRoundingMode(RoundingMode.DOWN);
    body.setAmount(numberFormat.format(request.getTotalFee()));
    body.setLang("GB");
    body.setAttach(request.getAttach());
    body.setRetEncodeType("17");
    body.setServerUrl(request.getNotifyUrl());
    body.setBillEXP("2");
    body.setGoodsName(request.getSubject());
    gateWayReq.setBody(body);
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    marshaller.marshal(body, new StreamResult(os));
    String signature = CryptoUtils.md5(os.toString() + merCode + md5Signature);
    ReqHead head = new ReqHead();
    head.setMerCode(merCode);
    head.setAccount(account);
    head.setReqDate(date);
    head.setSignature(signature);
    gateWayReq.setHead(head);
    ips.setGateWayReq(gateWayReq);
    return ips;
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