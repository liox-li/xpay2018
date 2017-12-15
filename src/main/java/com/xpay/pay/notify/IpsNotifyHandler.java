package com.xpay.pay.notify;

import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.proxy.ips.notify.Ips;
import java.io.ByteArrayInputStream;
import java.net.URLDecoder;
import javax.xml.transform.stream.StreamSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

/**
 * Created by sunjian on Date: 2017/12/13 下午1:35
 * Description:
 */
@Service
public class IpsNotifyHandler extends AbstractNotifyHandler {

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
      logger.error("IpsNotifyHandler extractNotifyBody " + content, e);
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
    if (notify == null || notify.getGateWayRsp() == null || notify.getGateWayRsp().getHead() == null
        || notify.getGateWayRsp().getBody() == null || !"000000"
        .equals(notify.getGateWayRsp().getHead().getRspCode())) {
      return null;
    }
    String billNo = notify.getGateWayRsp().getBody().getMerBillNo();
    OrderStatus status =
        "Y".equals(notify.getGateWayRsp().getBody().getStatus()) ? OrderStatus.SUCCESS
            : OrderStatus.PAYERROR;
    String totalFee = String
        .valueOf((int) (Float.valueOf(notify.getGateWayRsp().getBody().getAmount()) * 100));
    return new NotifyBody(billNo, notify.getGateWayRsp().getBody().getIpsBillNo(), status, totalFee,
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