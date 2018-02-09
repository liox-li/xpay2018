package com.xpay.pay.proxy.hm;

import com.xpay.pay.proxy.IPaymentProxy;
import com.xpay.pay.proxy.PaymentRequest;
import com.xpay.pay.proxy.PaymentResponse;
import com.xpay.pay.util.AppConfig;
import com.xpay.pay.util.CryptoUtils;
import com.xpay.pay.util.HttpClient;
import com.xpay.pay.util.JsonUtils;
import com.xpay.pay.util.TimeUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Created by sunjian on Date: 2018/2/8 下午2:28
 * Description:
 */
@Component
public class HmProxy implements IPaymentProxy {

  private static final Logger logger = LogManager.getLogger(HmProxy.class);
  private static final AppConfig config = AppConfig.HmConfig;
  private static final String appServerId = config.getProperty("provider.server.id");
  private static final String appPageId = config.getProperty("provider.page.id");
  private static final String appDesKey = config.getProperty("provider.deskey");
  private static final String appDesIv = config.getProperty("provider.desIv");
  private static final String appDesSecurity = config.getProperty("provider.desSecurity");
  private static final String baseEndpoint = config.getProperty("provider.endpoint");

  @Override
  public PaymentResponse unifiedOrder(PaymentRequest request) {
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

  /**
   * @param name 名字
   * @param province 省份
   * @param city 城市
   * @param address 地址
   * @param certType 证件类型： 01：身份证 02：军官证 03：护照 04：户口簿 05：回乡证 06：其他
   * @param certId 证件号
   * @param mobile 手机号码
   * @param accoutId 银行账户
   * @param accountName 银行账户名称
   * @param bankName 银行名称
   * @param bankCode 银行行号
   */
  public void open(String name, String province, String city, String address, String certType,
      String certId, String mobile, String accoutId, String accountName,
      String bankName, String bankCode, String fee, String rate) {
    MerRegisterReq req = new MerRegisterReq();
    req.setMerName(name);
    req.setRealName(name);
    req.setMerState(province);
    req.setMerCity(city);
    req.setMerAddress(address);
    req.setCertType(certType);
    req.setCertId(certId);
    req.setMobile(mobile);
    req.setAccountId(accoutId);
    req.setAccountName(accountName);
    req.setBankName(bankName);
    req.setBankCode(bankCode);
    req.setOperFlag("A");
    req.setT1consFee(fee);
    req.setT1consRate(rate);
    req.setT0drawFee("0");
    req.setT0drawRate("0");
    MerRegisterRsp response = invoke("merRegister", appServerId, req, MerRegisterRsp.class);
    System.out.println(
        response.getStatusCode() + " " + response.getStatusMsg() + " " + response.getMerCode());
  }

  /**
   * @param merCode
   * @param orderNo
   * @param orderAmount
   * @param accNo
   * @param telNo
   */
  public void payWithCREDIT(String merCode, String orderNo, String orderAmount, String accNo,
      String telNo, String cvn2, String valiDate) {
    TranReq tranReq = new TranReq();
    tranReq.setAccNo(accNo);
    tranReq.setAccType("CREDIT");//CREDIT：贷记卡 DEBIT：借记卡 DEPOSIT：存折 BUZACC：对公账户
    tranReq.setMerCode(merCode);
    tranReq.setOrderNo("P" + this.appServerId + orderNo);
    tranReq.setOrderDate(TimeUtils.formatTime(new Date(), "yyyyMMdd"));
    tranReq.setOrderTime(TimeUtils.formatTime(new Date(), TimeUtils.TimeShortPatternTime));
    tranReq.setOrderAmount(orderAmount);
    tranReq.setTelNo(telNo);
    tranReq.setCvn2(CryptoUtils.encrypt3DESECB(appDesSecurity, cvn2));
    tranReq.setValiDate(CryptoUtils.encrypt3DESECB(appDesSecurity, valiDate));
    TranRsp tranRsp = invoke("fastPay", appServerId, tranReq, TranRsp.class);
    /**
     *
     * 无卡交易限额:
     * 单笔限额 100-2W,
     * 单卡5W,
     * 单日单个身份证20W,
     * 交易时间1-22点
     * 收款交易手续费：进位算法
     * 提现交易手续费：四舍五入算法
     * 精确到分位
     *
     */
  }

  public void payWithDEBIT(String merCode, String orderNo, String orderAmount, String accNo,
      String telNo) {
    TranReq tranReq = new TranReq();
    tranReq.setAccNo(accNo);
    tranReq.setAccType("DEBIT");//CREDIT：贷记卡 DEBIT：借记卡 DEPOSIT：存折 BUZACC：对公账户
    tranReq.setMerCode(merCode);
    tranReq.setOrderNo("P" + this.appPageId + orderNo);
    tranReq.setOrderDate(TimeUtils.formatTime(new Date(), "yyyyMMdd"));
    tranReq.setOrderTime(TimeUtils.formatTime(new Date(), TimeUtils.TimeShortPatternTime));
    tranReq.setOrderAmount(orderAmount);
    tranReq.setTelNo(telNo);
    TranRsp tranRsp = invoke("fastPay", appPageId, tranReq, TranRsp.class);
    /**
     *
     * 无卡交易限额:
     * 单笔限额 100-2W,
     * 单卡5W,
     * 单日单个身份证20W,
     * 交易时间1-22点
     * 收款交易手续费：进位算法
     * 提现交易手续费：四舍五入算法
     * 精确到分位
     *
     */
  }

  public void withdrawl(String merCode, String orderNo, String transAmount) {
    CashTranReq cashTranReqReq = new CashTranReq();
    cashTranReqReq.setOrderNo("T" + appServerId + orderNo);
    cashTranReqReq.setMerCode(merCode);
    cashTranReqReq.setPaymentType("2015");
    cashTranReqReq.setTransDate(TimeUtils.formatTime(new Date(), TimeUtils.TimeShortPatternTime));
    cashTranReqReq.setTransAmount(transAmount);
    CashTranRsp cashTranRsp = invoke("fastPayCash", appServerId, cashTranReqReq, CashTranRsp.class);
    System.out.println(cashTranRsp);
  }

  public void queryWithdraw(String merCode, String orderNo, String transDate, String transSeq) {
    query(merCode, "T" + appServerId, orderNo, transDate, transSeq);
  }

  public void queryTrans(String merCode, String orderNo, String transDate, String transSeq) {
    query(merCode, "P" + appServerId, orderNo, transDate, transSeq);
  }

  private void query(String merCode, String orderNoPrefix, String orderNo, String transDate,
      String transSeq) {
    QryTranReq qryTranReq = new QryTranReq();
    qryTranReq.setOrderNo(orderNoPrefix + orderNo);
    qryTranReq.setMerCode(merCode);
    qryTranReq.setTransSeq(transSeq);
    qryTranReq.setPaymentType("2015");
    qryTranReq.setTransDate(transDate);
    QryTranRsp qryTranRsp = invoke("fastPayQuery", appServerId, qryTranReq, QryTranRsp.class);
  }

  private <T> T invoke(String method, String appId, Object req, Class<T> clazz) {
    String request = JsonUtils.toJson(req);
    logger.info("hm req: " + request);
    //DES加密
    String desJson = CryptoUtils.encryptDES(appDesKey, appDesIv, request);
    logger.info("hm req des: " + desJson);
    Map<String, String> map = new HashMap<>();
    map.put("id", appId);
    map.put("data", desJson);
    String encodeResponse = HttpClient.doPost(baseEndpoint + method, map, 10000);
    String response = CryptoUtils.decryptDES(appDesKey, appDesIv, encodeResponse);
    logger.info("hm resp:" + response);
    return JsonUtils.fromJson(response, clazz);
  }
}