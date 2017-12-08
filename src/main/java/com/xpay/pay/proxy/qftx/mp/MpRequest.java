package com.xpay.pay.proxy.qftx.mp;

/**
 * Created by sunjian on Date: 2017/12/8 下午3:15
 * Description:
 */
public class MpRequest {

  private String version;
  private String charset;
  private String sign_type;
  private String mch_id;
  private String out_trade_no;
  private String device_info;
  private String body;
  private String attach;
  private int total_fee;
  private String mch_create_ip;
  private String notify_url;
  private String time_start; //yyyyMMddHHmmss
  private String time_expire;
  private String op_user_id;
  private String goods_tag;
  private String product_id;
  private String nonce_str;
  private String limit_credit_pay;
  private String sign;

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getCharset() {
    return charset;
  }

  public void setCharset(String charset) {
    this.charset = charset;
  }

  public String getSign_type() {
    return sign_type;
  }

  public void setSign_type(String sign_type) {
    this.sign_type = sign_type;
  }

  public String getMch_id() {
    return mch_id;
  }

  public void setMch_id(String mch_id) {
    this.mch_id = mch_id;
  }

  public String getOut_trade_no() {
    return out_trade_no;
  }

  public void setOut_trade_no(String out_trade_no) {
    this.out_trade_no = out_trade_no;
  }

  public String getDevice_info() {
    return device_info;
  }

  public void setDevice_info(String device_info) {
    this.device_info = device_info;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getAttach() {
    return attach;
  }

  public void setAttach(String attach) {
    this.attach = attach;
  }

  public int getTotal_fee() {
    return total_fee;
  }

  public void setTotal_fee(int total_fee) {
    this.total_fee = total_fee;
  }

  public String getMch_create_ip() {
    return mch_create_ip;
  }

  public void setMch_create_ip(String mch_create_ip) {
    this.mch_create_ip = mch_create_ip;
  }

  public String getNotify_url() {
    return notify_url;
  }

  public void setNotify_url(String notify_url) {
    this.notify_url = notify_url;
  }

  public String getTime_start() {
    return time_start;
  }

  public void setTime_start(String time_start) {
    this.time_start = time_start;
  }

  public String getTime_expire() {
    return time_expire;
  }

  public void setTime_expire(String time_expire) {
    this.time_expire = time_expire;
  }

  public String getOp_user_id() {
    return op_user_id;
  }

  public void setOp_user_id(String op_user_id) {
    this.op_user_id = op_user_id;
  }

  public String getGoods_tag() {
    return goods_tag;
  }

  public void setGoods_tag(String goods_tag) {
    this.goods_tag = goods_tag;
  }

  public String getProduct_id() {
    return product_id;
  }

  public void setProduct_id(String product_id) {
    this.product_id = product_id;
  }

  public String getNonce_str() {
    return nonce_str;
  }

  public void setNonce_str(String nonce_str) {
    this.nonce_str = nonce_str;
  }

  public String getLimit_credit_pay() {
    return limit_credit_pay;
  }

  public void setLimit_credit_pay(String limit_credit_pay) {
    this.limit_credit_pay = limit_credit_pay;
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }
}