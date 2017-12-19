package com.xpay.pay.notify;

import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.proxy.supay.Notify;
import com.xpay.pay.proxy.supay.SUPayProxy;
import com.xpay.pay.proxy.supay.SUPayResponse;
import com.xpay.pay.util.JsonUtils;
import com.xpay.pay.util.RSASignature;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Created by sunjian on Date: 2017/12/15 上午12:14
 * Description:
 */
@Service
public class SUPayNotifyHandler extends AbstractNotifyHandler {

  @Override
  protected NotifyBody extractNotifyBody(String url, String content) {
    String orderNo = "";
    String extOrderNo = "";
    String status = "";
    String targetOrderNo = "";
    String totalFee = "";
    try {
      SUPayResponse suPayResponse = JsonUtils.fromJson(content, SUPayResponse.class);

      if (!RSASignature.doCheck(
          suPayResponse.getCode() + suPayResponse.getMsg() + suPayResponse.getDate() + suPayResponse
              .getResult(), suPayResponse.getSign(), SUPayProxy.publicKey)) {
        return null;
      }

      if (!"100".equals(suPayResponse.getCode())) {
        return null;
      }
      Notify notify = JsonUtils.fromJson(suPayResponse.getResult(), Notify.class);
      if (notify != null) {
        orderNo = notify.getMerOrderId();
        extOrderNo = notify.getPlatformOrderId();
        status = notify.getStatus();
        totalFee = notify.getAmount().toString();
      }
    } catch (Exception e) {
    }
    OrderStatus orderStatus = OrderStatus.USERPAYING;
    switch (status) {
      case "0":
        orderStatus = OrderStatus.SUCCESS;
        break;
      case "1":
        orderStatus = OrderStatus.PAYERROR;
        break;
    }
    return StringUtils.isBlank(orderNo) ? null
        : new NotifyBody(orderNo, extOrderNo, orderStatus, totalFee, targetOrderNo);

  }

  @Override
  protected String getSuccessResponse() {
    return "success";
  }

  @Override
  protected String getFailedResponse() {
    return "failed";
  }
}