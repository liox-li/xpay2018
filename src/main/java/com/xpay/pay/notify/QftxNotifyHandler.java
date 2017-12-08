package com.xpay.pay.notify;

import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.proxy.qftx.mp.QftxMpProxy;
import com.xpay.pay.util.CryptoUtils;
import com.xpay.pay.util.XmlUtils;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Created by sunjian on Date: 2017/12/8 下午3:56
 * Description:
 */
@Service
public class QftxNotifyHandler extends AbstractNotifyHandler {

  protected final Logger logger = LogManager.getLogger("AccessLog");

  @Override
  protected NotifyBody extractNotifyBody(String url, String content) {
    logger.info("qftx body: " + content);
    Map<String, String> params = null;
    try {
      params = XmlUtils.fromXml(content.getBytes(), "utf-8");
      if (!PaymentResponse.SUCCESS.equals(params.get("status")) || !PaymentResponse.SUCCESS
          .equals(params.get("result_code"))) {
        return null;
      }
      boolean checkSign = CryptoUtils.checkSignature(params, QftxMpProxy.appSecret, "sign", "key");
      if (!checkSign) {
        return null;
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return null;
    }
    String billNo = params.get("out_trade_no");
    String extOrderNo = params.get("transaction_id");
    OrderStatus status =
        "0".equals(params.get("pay_result")) ? OrderStatus.SUCCESS : OrderStatus.PAYERROR;
    String totalFee = params.get("total_fee");
    NotifyBody notifyBody = new NotifyBody(billNo, extOrderNo, status, totalFee, null);
    return notifyBody;
  }

  @Override
  protected String getSuccessResponse() {
    return "success";
  }

  @Override
  protected String getFailedResponse() {
    return "fail";
  }
}