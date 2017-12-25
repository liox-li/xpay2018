package com.xpay.pay.proxy.ips;

import com.xpay.pay.exception.GatewayException;
import com.xpay.pay.proxy.ips.useropen.IpsRequest;
import com.xpay.pay.proxy.ips.useropen.IpsResponse;
import com.xpay.pay.proxy.ips.useropen.req.Head;
import com.xpay.pay.proxy.ips.useropen.req.OpenUserReqXml;
import com.xpay.pay.proxy.ips.useropen.rsp.Body;
import com.xpay.pay.proxy.ips.useropen.rsp.OpenUserRespXml;
import com.xpay.pay.util.CryptoUtils;
import com.xpay.pay.util.TimeUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by sunjian on Date: 2017/12/25 下午5:04
 * Description:
 */
@Service
public class IpsProxy {

  private static final String OPEN_URL = "https://ebp.ips.com.cn/fpms-access/action/user/open";
  private static final String MD5_SIGNATURE = "wmm7x3LwJdEPgmOQxcpBugQOmQewxXjwQD5ASRbKcFq7QTK6kOolYMpG9Ov5qt8RfNwaCoXX5edKt9HWbyxL5Qr1Z32oZE85Env04BUq4bX7RCHdxhT8u0rEX5BqCDsg";
  private static final String DES_IV = "2BBNEs28";
  private static final String DES_KEY = "oov94m6YhhFxAySqF99MoLRV";

  protected final Logger logger = LogManager.getLogger(getClass());

  @Qualifier("userOpenUnmarshaller")
  @Autowired
  protected Unmarshaller unmarshaller;

  @Qualifier("ipsMarshaller")
  @Autowired
  protected Marshaller marshaller;

  @Autowired
  RestTemplate restTemplate;

  public Body open(String reqIp, String merCode, String userType, String customerCode,
      String identityType,
      String identityNo, String userName, String legalName, String legalCardNo, String mobiePhoneNo,
      String telPhoneNo, String email, String contactAddress, String remark, String pageUrl,
      String s2sUrl, String directSell, String stmsAcctNo, String ipsUserName) throws IOException {
    com.xpay.pay.proxy.ips.useropen.req.Body body = new com.xpay.pay.proxy.ips.useropen.req.Body();
    body.setMerAcctNo(merCode);
    body.setUserType(userType);
    body.setCustomerCode(customerCode);
    body.setIdentityType(identityType);
    body.setIdentityNo(identityNo);
    body.setUserName(userName);
    body.setLegalName(legalName);
    body.setLegalCardNo(legalCardNo);
    body.setMobiePhoneNo(mobiePhoneNo);
    body.setTelPhoneNo(telPhoneNo);
    body.setEmail(email);
    body.setContactAddress(contactAddress);
    body.setRemark(remark);
    body.setPageUrl(pageUrl);
    body.setS2sUrl(s2sUrl);
    body.setDirectSell(directSell);
    body.setStmsAcctNo(stmsAcctNo);
    body.setIpsUserName(ipsUserName);

    ByteArrayOutputStream os = new ByteArrayOutputStream();
    marshaller.marshal(body, new StreamResult(os));
    String bodyStr = os.toString();
    bodyStr = bodyStr.substring(bodyStr.indexOf("<body>"));
    logger.info("signature body: " + bodyStr + MD5_SIGNATURE);
    String signature = CryptoUtils.md5(bodyStr + MD5_SIGNATURE);
    Head head = new Head();
    head.setVersion("V1.0.1");
    head.setReqIp(reqIp);
    head.setReqDate(TimeUtils.formatTime(new Date(), "yyyy-MM-dd HH:mm:ss"));
    head.setSignature(signature);
    OpenUserReqXml openUserReqXml = new OpenUserReqXml();
    openUserReqXml.setBody(body);
    openUserReqXml.setHead(head);

    IpsRequest ipsRequest = new IpsRequest();
    ipsRequest.setArgMerCode(merCode);
    os = new ByteArrayOutputStream();
    marshaller.marshal(openUserReqXml, new StreamResult(os));
    String arg3DesXmlPara = CryptoUtils.encryptDESede(DES_KEY, DES_IV, os.toString());
    logger.info("open account des xml[3des]:" + arg3DesXmlPara);
    ipsRequest.setArg3DesXmlPara(arg3DesXmlPara);
    os = new ByteArrayOutputStream();
    marshaller.marshal(ipsRequest, new StreamResult(os));
    String request = os.toString();
    logger.info("open account request: " + request);
    MultiValueMap<String, String> keyPairs = new LinkedMultiValueMap<>();
    keyPairs.add("ipsRequest", request);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.set("Accept", MediaType.ALL_VALUE);
    HttpEntity<?> httpEntity = new HttpEntity<>(keyPairs, headers);
    String response = restTemplate.exchange(OPEN_URL, HttpMethod.POST, httpEntity, String.class)
        .getBody();
    logger.info("open account response: " + response);
    StreamSource streamSource = new StreamSource(
        new ByteArrayInputStream(response.getBytes("UTF-8")));
    IpsResponse ipsResponse = (IpsResponse) unmarshaller.unmarshal(streamSource);
    if (!"M000000".equals(ipsResponse.getRspCode())) {
      throw new GatewayException(ipsResponse.getRspCode(), ipsResponse.getRspMsg());
    }
    String xml = CryptoUtils.decryptDESede(DES_KEY, DES_IV, ipsResponse.getP3DesXmlPara());
    streamSource = new StreamSource(new ByteArrayInputStream(xml.getBytes("UTF-8")));
    OpenUserRespXml openUserRespXml = (OpenUserRespXml) unmarshaller.unmarshal(streamSource);
    return openUserRespXml.getBody();
  }
}