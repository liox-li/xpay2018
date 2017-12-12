package com.xpay.pay.proxy.ips;

import cn.com.ips.payat.webservice.scan.ScanService;
import com.xpay.pay.model.StoreChannel.IpsProps;
import com.xpay.pay.proxy.IPaymentProxy;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.ips.common.Head;
import com.xpay.pay.proxy.ips.gatewayreq.Body;
import com.xpay.pay.proxy.ips.gatewayreq.GateWayReq;
import com.xpay.pay.proxy.ips.gatewayreq.Ips;
import com.xpay.pay.util.CryptoUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.transform.stream.StreamResult;
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

  protected final Logger logger = LogManager.getLogger(IpsProxy.class);

  @Autowired
  Marshaller marshaller;
  Unmarshaller unmarshaller;

  @Autowired
  private ScanService scanService;

  @Override
  public PaymentResponse unifiedOrder(PaymentRequest request) {
    try {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
      String date = simpleDateFormat.format(new Date());
      //merCode, account, signature md5
      String[] accountParam = request.getExtStoreId().split(",");
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
      String signature = CryptoUtils.md5(os.toString() + accountParam[0] + accountParam[2]);
      Head head = new Head();
      head.setMerCode(accountParam[0]);
      head.setAccount(accountParam[1]);
      head.setReqDate(date);
      head.setSignature(signature);
      gateWayReq.setHead(head);
      ips.setGateWayReq(gateWayReq);
      os = new ByteArrayOutputStream();
      marshaller.marshal(ips, new StreamResult(os));
      String scanPayRequest = os.toString();
      logger.info("ips order request: " + scanPayRequest);
      String result = scanService.scanPay(scanPayRequest);
      logger.info("ips order response: " + result);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }

    return null;
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