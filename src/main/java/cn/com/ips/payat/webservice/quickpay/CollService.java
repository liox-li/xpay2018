
package cn.com.ips.payat.webservice.quickpay;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "CollService", targetNamespace = "http://payat.ips.com.cn/WebService/CollTrade")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface CollService {


    /**
     * 
     * @param sign
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "http://payat.ips.com.cn/WebService/CollTrade/acquireSms")
    @WebResult(name = "SmsResult", partName = "SmsResult")
    public String acquireSms(
        @WebParam(name = "sign", partName = "sign")
        String sign);

    /**
     * 
     * @param tradeSms
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "http://payat.ips.com.cn/WebService/CollTrade/acquireTradeSms")
    @WebResult(name = "tradeSmsResult", partName = "tradeSmsResult")
    public String acquireTradeSms(
        @WebParam(name = "tradeSms", partName = "tradeSms")
        String tradeSms);

    /**
     * 
     * @param collTrade
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "http://payat.ips.com.cn/WebService/CollTrade/collConsume")
    @WebResult(name = "CollTradeResult", partName = "CollTradeResult")
    public String collConsume(
        @WebParam(name = "collTrade", partName = "collTrade")
        String collTrade);

    /**
     * 
     * @param disengage
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "http://payat.ips.com.cn/WebService/CollTrade/disengage")
    @WebResult(name = "SignDisengageResult", partName = "SignDisengageResult")
    public String disengage(
        @WebParam(name = "disengage", partName = "disengage")
        String disengage);

    /**
     * 
     * @param bankCard
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "http://payat.ips.com.cn/WebService/CollTrade/getBankList")
    @WebResult(name = "BankResultResult", partName = "BankResultResult")
    public String getBankList(
        @WebParam(name = "bankCard", partName = "bankCard")
        String bankCard);

    /**
     * 
     * @param signConfirm
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "http://payat.ips.com.cn/WebService/CollTrade/signConfirm")
    @WebResult(name = "SignConfirmResult", partName = "SignConfirmResult")
    public String signConfirm(
        @WebParam(name = "signConfirm", partName = "signConfirm")
        String signConfirm);

    /**
     * 
     * @param cardInfo
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "http://payat.ips.com.cn/WebService/CollTrade/signApply")
    @WebResult(name = "CardResult", partName = "CardResult")
    public String signApply(
        @WebParam(name = "cardInfo", partName = "cardInfo")
        String cardInfo);

    /**
     * 
     * @param accountCheck
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "http://payat.ips.com.cn/WebService/CollTrade/collAccountCheck")
    @WebResult(name = "CollAccountCheckResult", partName = "CollAccountCheckResult")
    public String collAccountCheck(
        @WebParam(name = "accountCheck", partName = "accountCheck")
        String accountCheck);

    /**
     * 
     * @param needIdAuth
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "http://payat.ips.com.cn/WebService/CollTrade/needIdAuth")
    @WebResult(name = "NeedIdAuthResult", partName = "NeedIdAuthResult")
    public String needIdAuth(
        @WebParam(name = "needIdAuth", partName = "needIdAuth")
        String needIdAuth);

    /**
     * 
     * @param signQuery
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "http://payat.ips.com.cn/WebService/CollTrade/signQuery")
    @WebResult(name = "SignQueryResult", partName = "SignQueryResult")
    public String signQuery(
        @WebParam(name = "signQuery", partName = "signQuery")
        String signQuery);

}
