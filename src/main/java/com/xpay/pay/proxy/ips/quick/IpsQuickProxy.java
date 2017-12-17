package com.xpay.pay.proxy.ips.quick;

import com.xpay.pay.ApplicationConstants;
import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.model.Bill;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.proxy.ips.AbstractIpsProxy;
import com.xpay.pay.proxy.ips.common.ReqHead;
import com.xpay.pay.proxy.ips.quick.req.Body;
import com.xpay.pay.proxy.ips.quick.req.GateWayReq;
import com.xpay.pay.proxy.ips.quick.req.Ips;
import com.xpay.pay.util.CryptoUtils;
import com.xpay.pay.util.TimeUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import javax.xml.transform.stream.StreamResult;
import org.springframework.stereotype.Component;

/**
 * Created by sunjian on Date: 2017/12/15 上午10:42
 * Description:
 */
@Component
public class IpsQuickProxy extends AbstractIpsProxy {

  @Override
  public PaymentResponse unifiedOrder(PaymentRequest request) {
    if (request.getTotalFeeAsFloat() <= 0f) {
      throw new GatewayException(ApplicationConstants.CODE_COMMON,
          "Total fee must be more than 0.");
    }
    String url = DEFAULT_H5API_URL + request.getOrderNo();
    PaymentResponse response = new PaymentResponse();
    response.setCode(PaymentResponse.SUCCESS);
    Bill bill = new Bill();
    bill.setCodeUrl(url);
    bill.setOrderStatus(OrderStatus.NOTPAY);
    response.setBill(bill);
    return response;
  }

  public String getReqParam(PaymentRequest request) {
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
      String quickReq = os.toString();
      logger.info("ips order request: " + quickReq);
      return quickReq;
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
      throw new RuntimeException(e);
    }
  }

  private Ips toIps(PaymentRequest request, String merCode, String account, String md5Signature)
      throws IOException {
    String date = request.getOrderTime();
    Ips ips = new Ips();
    GateWayReq gateWayReq = new GateWayReq();
    Body body = new Body();
    body.setMerBillNo(request.getOrderNo());
    body.setGatewayType("01");
    body.setDate(date.substring(0, 8));
    body.setCurrencyType("156");
    NumberFormat numberFormat = new DecimalFormat("#.##");
    numberFormat.setGroupingUsed(false);
    body.setAmount(numberFormat.format(request.getTotalFee()));
    body.setLang("GB");
    body.setMerchanturl(request.getReturnUrl()+"?success");
    body.setFailUrl(request.getReturnUrl()+"?error");
    body.setAttach(request.getAttach());
    body.setOrderEncodeType("5");
    body.setRetEncodeType("17");
    body.setRetType("1");
    body.setServerUrl(request.getNotifyUrl());
    body.setBillEXP("2");
    body.setGoodsName(request.getSubject());
    gateWayReq.setBody(body);
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    marshaller.marshal(body, new StreamResult(os));
    String bodyStr = os.toString();
    bodyStr = bodyStr.substring(bodyStr.indexOf("<body>"));
    String signature = CryptoUtils.md5(bodyStr + merCode + md5Signature);
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
    return TimeUtils.formatTime(new Date(), TimeUtils.TimeShortPatternTime);
  }
}