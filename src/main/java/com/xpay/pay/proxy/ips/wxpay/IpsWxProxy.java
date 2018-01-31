package com.xpay.pay.proxy.ips.wxpay;

import static com.xpay.pay.util.TimeUtils.TimePatternTime;
import static com.xpay.pay.util.TimeUtils.TimeShortPatternTime;

import com.xpay.pay.ApplicationConstants;
import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.model.Bill;
import com.xpay.pay.model.StoreChannel.IpsProps;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.proxy.ips.AbstractIpsProxy;
import com.xpay.pay.proxy.ips.common.ReqHead;
import com.xpay.pay.proxy.ips.wxpay.payreq.Body;
import com.xpay.pay.proxy.ips.wxpay.payreq.GoodsInfo;
import com.xpay.pay.proxy.ips.wxpay.payreq.Ips;
import com.xpay.pay.proxy.ips.wxpay.payreq.WxPayReq;
import com.xpay.pay.util.CryptoUtils;
import com.xpay.pay.util.IDGenerator;
import com.xpay.pay.util.TimeUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import javax.xml.transform.stream.StreamResult;
import org.springframework.stereotype.Component;

/**
 * Created by sunjian on Date: 2018/1/30 下午10:05
 * Description:
 */
@Component
public class IpsWxProxy extends AbstractIpsProxy {

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

  public String getReqParam(PaymentRequest paymentRequest) {
    String[] accountParam = paymentRequest.getExtStoreId().split(",");
    try {
      //merCode, account, signature md5
      String merCode = accountParam[0];
      String account = accountParam[1];
      String md5Signature = accountParam[2];
      Ips ips = toIps(paymentRequest, merCode, account, md5Signature);
      ByteArrayOutputStream os;
      os = new ByteArrayOutputStream();
      marshaller.marshal(ips, new StreamResult(os));
      String quickReq = os.toString();
      logger.info("ips wxpay request: " + quickReq);
      return quickReq;
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
      throw new RuntimeException(e);
    }
  }

  private Ips toIps(PaymentRequest paymentRequest, String merCode, String account,
      String md5Signature) throws IOException {
    Ips ips = new Ips();
    Body body = new Body();
    body.setMerBillno(paymentRequest.getOrderNo());
    GoodsInfo goodsInfo = new GoodsInfo();
    goodsInfo.setGoodsName(paymentRequest.getSubject());
    goodsInfo.setGoodsCount("1");
    body.setGoodsInfo(goodsInfo);
    if (paymentRequest.getChannelProps() != null) {
      IpsProps props = (IpsProps) paymentRequest.getChannelProps();
      body.setMerType(props.getMerType());
      body.setSubMerCode(props.getSubMerCode());
    } else {
      body.setMerType("0");
    }
    DecimalFormat numberFormat = new DecimalFormat("#.##");
    numberFormat.setGroupingUsed(false);
    body.setOrdAmt(numberFormat.format(paymentRequest.getTotalFee()));
    body.setOrdTime(TimeUtils
        .formatTime(TimeUtils.parseTime(paymentRequest.getOrderTime(), TimeShortPatternTime),
            TimePatternTime));
    body.setMerchantUrl(paymentRequest.getReturnUrl());
    body.setServerUrl(paymentRequest.getNotifyUrl());
    Calendar expCalendar = Calendar.getInstance();
    expCalendar.add(Calendar.HOUR_OF_DAY, 2);
    body.setBillEXP(TimeUtils.formatTime(expCalendar.getTime(), TimePatternTime));
    body.setCurrencyType("156");
    body.setRetEncodeType("17");
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    marshaller.marshal(body, new StreamResult(os));
    String bodyStr = os.toString();
    bodyStr = bodyStr.substring(bodyStr.indexOf("<body>"));
    String signature = CryptoUtils.md5(bodyStr + merCode + md5Signature);
    ReqHead head = new ReqHead();
    head.setVersion(getVersion());
    head.setMsgId(IDGenerator.buildTimeSeriesId(3));
    head.setMerCode(merCode);
    head.setAccount(account);
    head.setReqDate(paymentRequest.getOrderTime());
    head.setSignature(signature);
    WxPayReq wxpayReq = new WxPayReq();
    wxpayReq.setBody(body);
    wxpayReq.setHead(head);
    ips.setWxPayReq(wxpayReq);
    return ips;
  }

  @Override
  protected String getReqDate(PaymentRequest request) {
    return TimeUtils.formatTime(new Date(), TimeUtils.TimeShortPatternTime);
  }

  protected String getVersion() {
    return "v1.0.1";
  }
}