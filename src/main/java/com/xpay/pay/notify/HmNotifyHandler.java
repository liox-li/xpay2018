package com.xpay.pay.notify;

import com.xpay.pay.proxy.PaymentResponse.OrderStatus;
import com.xpay.pay.proxy.hm.TranRsp;
import com.xpay.pay.util.CommonUtils;
import com.xpay.pay.util.JsonUtils;
import org.springframework.stereotype.Service;

/**
 * Created by sunjian on Date: 2018/2/9 下午3:13
 * Description:
 */
@Service
public class HmNotifyHandler extends AbstractNotifyHandler {

  @Override
  protected NotifyBody extractNotifyBody(String url, String content) {
    TranRsp tranRsp = JsonUtils.fromJson(content, TranRsp.class);
    OrderStatus status =
        "Y".equals(tranRsp.getTransStatus()) ? OrderStatus.SUCCESS
            : OrderStatus.PAYERROR;
    return new NotifyBody(tranRsp.getOrderNo(), tranRsp.getTransSeq(), status,
        CommonUtils.toInt(tranRsp.getTransAmount()),
        null);
  }

  @Override
  protected String getSuccessResponse() {
    return "success";
  }

  @Override
  protected String getFailedResponse() {
    return "error";
  }
}