package com.xpay.pay.notify;

import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.proxy.ips.wxpay.notify.Ips;
import java.io.ByteArrayInputStream;
import java.net.URLDecoder;
import javax.xml.transform.stream.StreamSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

/**
 * Created by sunjian on Date: 2018/1/30 下午11:00
 * Description:
 */
@Service
public class IpsWxNotifyHandler extends AbstractNotifyHandler {

  @Qualifier("ipsWxNotifyUnmarshaller")
  @Autowired
  Unmarshaller unmarshaller;

  @Override
  protected NotifyBody extractNotifyBody(String url, String content) {
    logger.info("ips body: " + content);
    String paymentResult = null;
    try {
      String decoded = URLDecoder.decode(content, "utf-8");
      String[] params = decoded.split("&");
      for (String param : params) {
        String[] pair = param.split("=");
        String key = pair[0];
        if ("paymentResult".equals(key)) {
          paymentResult = pair[1];
          break;
        }
      }
    } catch (Exception e) {
      logger.error("IpsWxNotifyHandler extractNotifyBody " + content, e);
      return null;
    }
    Ips notify = null;
    try {
      notify = (Ips) unmarshaller
          .unmarshal(new StreamSource(new ByteArrayInputStream(paymentResult.getBytes("utf-8"))));
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return null;
    }
    if (notify == null || notify.getWxPayRsp() == null || notify.getWxPayRsp().getHead() == null
        || notify.getWxPayRsp().getBody() == null || !"000000"
        .equals(notify.getWxPayRsp().getHead().getRspCode())) {
      return null;
    }
    String billNo = notify.getWxPayRsp().getBody().getMerBillNo();
    OrderStatus status =
        "Y".equals(notify.getWxPayRsp().getBody().getStatus()) ? OrderStatus.SUCCESS
            : OrderStatus.PAYERROR;
    String totalFee = String
        .valueOf((int) (Float.valueOf(notify.getWxPayRsp().getBody().getOrdAmt()) * 100));
    return new NotifyBody(billNo, notify.getWxPayRsp().getBody().getIpsBillNo(), status, totalFee,
        null);
  }

  @Override
  protected String getSuccessResponse() {
    return "ipscheckok";
  }

  @Override
  protected String getFailedResponse() {
    return "error";
  }
}