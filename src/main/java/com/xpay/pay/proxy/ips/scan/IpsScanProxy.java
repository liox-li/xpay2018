package com.xpay.pay.proxy.ips.scan;

import cn.com.ips.payat.webservice.scan.ScanService;
import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.model.Bill;
import com.xpay.pay.model.StoreChannel.IpsProps;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.proxy.ips.AbstractIpsProxy;
import com.xpay.pay.proxy.ips.common.ReqHead;
import com.xpay.pay.proxy.ips.scan.gatewayreq.Body;
import com.xpay.pay.proxy.ips.scan.gatewayreq.GateWayReq;
import com.xpay.pay.proxy.ips.scan.gatewayreq.Ips;
import com.xpay.pay.util.CryptoUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sunjian on Date: 2017/12/12 下午9:51
 * Description:
 */
@Component
public class IpsScanProxy extends AbstractIpsProxy {

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
      logger.info("ips order response: " + result);
      StreamSource streamSource = new StreamSource(new ByteArrayInputStream(result.getBytes()));
      com.xpay.pay.proxy.ips.scan.gatewayrsp.Ips respIps = (com.xpay.pay.proxy.ips.scan.gatewayrsp.Ips) unmarshaller
          .unmarshal(streamSource);
      if (!SUCCESS.equals(respIps.getGateWayRsp().getHead().getRspCode())) {
        throw new GatewayException(respIps.getGateWayRsp().getHead().getRspCode(),
            respIps.getGateWayRsp().getHead().getRspMsg());
      }
      signatureValidWithThrow(merCode, md5Signature, result,
          respIps.getGateWayRsp().getHead().getSignature());
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

  private Ips toIps(PaymentRequest request, String merCode, String account, String md5Signature)
      throws IOException {
    String date = request.getOrderTime();
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
  protected String getReqDate(PaymentRequest request) {
    return request.getOrderTime();
  }
}