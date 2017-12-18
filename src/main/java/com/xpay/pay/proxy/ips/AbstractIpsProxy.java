package com.xpay.pay.proxy.ips;

import cn.com.ips.payat.webservice.orderquery.OrderQueryService;
import cn.com.ips.payat.webservice.refund.RefundService;
import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.model.Bill;
import com.xpay.pay.proxy.IPaymentProxy;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.proxy.ips.common.ReqHead;
import com.xpay.pay.proxy.ips.query.merbillno.req.OrderQueryReq;
import com.xpay.pay.proxy.ips.refund.req.RefundReq;
import com.xpay.pay.util.CryptoUtils;
import com.xpay.pay.util.IDGenerator;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.util.StringUtils;

/**
 * Created by sunjian on Date: 2017/12/15 上午10:36
 * Description:
 */
public abstract class AbstractIpsProxy implements IPaymentProxy{

  protected static final String SUCCESS = "000000";
  protected final Logger logger = LogManager.getLogger(getClass());
  @Autowired
  private OrderQueryService orderQueryService;

  @Autowired
  private RefundService refundService;

  @Qualifier("ipsMarshaller")
  @Autowired
  protected Marshaller marshaller;

  @Qualifier("queryUnmarshaller")
  @Autowired
  protected Unmarshaller queryUnmarshaller;

  @Qualifier("refundUnmarshaller")
  @Autowired
  protected Unmarshaller refundUnmarshaller;

  @Override
  public PaymentResponse query(PaymentRequest request) {
    String[] accountParam = request.getExtStoreId().split(",");
    try {
      //merCode, account, signature md5
      String merCode = accountParam[0];
      String account = accountParam[1];
      String md5Signature = accountParam[2];
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
      String date = simpleDateFormat.format(new Date());
      com.xpay.pay.proxy.ips.query.merbillno.req.Ips ips = new com.xpay.pay.proxy.ips.query.merbillno.req.Ips();
      OrderQueryReq orderQueryReq = new OrderQueryReq();
      com.xpay.pay.proxy.ips.query.merbillno.req.Body body = new com.xpay.pay.proxy.ips.query.merbillno.req.Body();
      body.setMerBillNo(request.getOrderNo());
      body.setDate(date.substring(0, 8));
      NumberFormat numberFormat = new DecimalFormat("#.##");
      numberFormat.setGroupingUsed(false);
      body.setAmount(numberFormat.format(request.getTotalFee()));
      orderQueryReq.setBody(body);
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      marshaller.marshal(body, new StreamResult(os));
      String bodyStr = os.toString();
      bodyStr = bodyStr.substring(bodyStr.indexOf("<body>"));
      String signature = CryptoUtils.md5(bodyStr + merCode + md5Signature);
      ReqHead head = new ReqHead();
      head.setMsgId(IDGenerator.buildTimeSeriesId());
      head.setMerCode(merCode);
      head.setAccount(account);
      head.setReqDate(date);
      head.setSignature(signature);
      orderQueryReq.setHead(head);
      ips.setOrderQueryReq(orderQueryReq);
      marshaller.marshal(ips, new StreamResult(os));
      String req = os.toString();
      logger.info("ips query request: " + req);
      String rsp = orderQueryService.getOrderByMerBillNo(req);
      logger.info("ips query response: " + rsp);
      StreamSource streamSource = new StreamSource(new ByteArrayInputStream(rsp.getBytes()));
      com.xpay.pay.proxy.ips.query.merbillno.rsp.Ips respIps = (com.xpay.pay.proxy.ips.query.merbillno.rsp.Ips) queryUnmarshaller
          .unmarshal(streamSource);
      if (!SUCCESS.equals(respIps.getOrderQueryRsp().getHead().getRspCode())) {
        throw new GatewayException(respIps.getOrderQueryRsp().getHead().getRspCode(),
            respIps.getOrderQueryRsp().getHead().getRspMsg());
      }
      PaymentResponse response = new PaymentResponse();
      response.setCode(PaymentResponse.SUCCESS);
      Bill bill = new Bill();
      bill.setOrderNo(respIps.getOrderQueryRsp().getBody().getMerBillNo());
      bill.setGatewayOrderNo(respIps.getOrderQueryRsp().getBody().getIpsBillNo());
      OrderStatus orderStatus = toOrderStatus(respIps.getOrderQueryRsp().getBody().getStatus());
      bill.setOrderStatus(orderStatus);
      response.setBill(bill);
      return response;
    } catch (Exception e) {
      logger.error("ToPaymentResponse error", e);
    }
    return null;
  }

  private OrderStatus toOrderStatus(String status) {
    if (StringUtils.isEmpty(status)) {
      return OrderStatus.NOTPAY;
    }
    switch (status) {
      case "Y":
        return OrderStatus.SUCCESS;
      case "N":
        return OrderStatus.PAYERROR;
      case "P":
        return OrderStatus.USERPAYING;
      default:
        return OrderStatus.NOTPAY;
    }
  }

  @Override
  public PaymentResponse refund(PaymentRequest request) {
    long l = System.currentTimeMillis();
    String[] accountParam = request.getExtStoreId().split(",");
    try {
      //merCode, account, signature md5
      String merCode = accountParam[0];
      String account = accountParam[1];
      String md5Signature = accountParam[2];
      String date = request.getOrderTime();
      com.xpay.pay.proxy.ips.refund.req.Ips ips = new com.xpay.pay.proxy.ips.refund.req.Ips();
      RefundReq refundReq = new RefundReq();
      com.xpay.pay.proxy.ips.refund.req.Body body = new com.xpay.pay.proxy.ips.refund.req.Body();
      body.setMerBillNo(IDGenerator.buildKey(30));
      body.setOrgMerBillNo(request.getOrderNo());
      body.setOrgMerTime(date.substring(0, 8));
      NumberFormat numberFormat = new DecimalFormat("#.##");
      numberFormat.setGroupingUsed(false);
      body.setBillAmount(numberFormat.format(request.getTotalFee()));
      body.setRefundAmount(numberFormat.format(request.getTotalFee()));
      refundReq.setBody(body);
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      marshaller.marshal(body, new StreamResult(os));
      String bodyStr = os.toString();
      bodyStr = bodyStr.substring(bodyStr.indexOf("<body>"));
      String signature = CryptoUtils.md5(bodyStr + merCode + md5Signature);
      ReqHead head = new ReqHead();
      head.setMsgId(IDGenerator.buildTimeSeriesId());
      head.setMerCode(merCode);
      head.setAccount(account);
      head.setReqDate(getReqDate(request));
      head.setSignature(signature);
      refundReq.setHead(head);
      ips.setRefundReq(refundReq);
      marshaller.marshal(ips, new StreamResult(os));
      String req = os.toString();
      logger.info("ips refund request: " + req);
      String rsp = refundService.refund(req);
      logger.info("ips refund response: " + rsp);
      StreamSource streamSource = new StreamSource(new ByteArrayInputStream(rsp.getBytes()));
      com.xpay.pay.proxy.ips.refund.rsp.Ips respIps = (com.xpay.pay.proxy.ips.refund.rsp.Ips) refundUnmarshaller
          .unmarshal(streamSource);
      if (!SUCCESS.equals(respIps.getRefundRsp().getHead().getRspCode())) {
        throw new GatewayException(respIps.getRefundRsp().getHead().getRspCode(),
            respIps.getRefundRsp().getHead().getRspMsg());
      }
      PaymentResponse response = new PaymentResponse();
      response.setCode(PaymentResponse.SUCCESS);
      Bill bill = new Bill();
      bill.setOrderNo(request.getOrderNo());
      bill.setGatewayOrderNo(request.getGatewayOrderNo());
      OrderStatus orderStatus = OrderStatus.SUCCESS;
      switch (respIps.getRefundRsp().getBody().getStatus()) {
        case "Y":
          orderStatus = OrderStatus.REFUND;
          break;
      }
      bill.setOrderStatus(orderStatus);
      response.setBill(bill);
      return response;
    } catch (IOException e) {
      logger.info("refund failed, took " + (System.currentTimeMillis() - l) + "ms", e);
      throw new RuntimeException(e);
    }
  }

  protected abstract String getReqDate(PaymentRequest request);


  protected void signatureValidWithThrow(String merCode, String md5Signature, String result,
      String rspSignature) {
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
        .equals(rspSignature)) {
      throw new RuntimeException("bad request signature not matches: " + signature);
    }
  }
}