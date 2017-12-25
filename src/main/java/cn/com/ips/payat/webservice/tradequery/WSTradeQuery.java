
package cn.com.ips.payat.webservice.tradequery;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "WSTradeQuery", targetNamespace = "http://payat.ips.com.cn/WebService/TradeQuery", wsdlLocation = "https://newpay.ips.com.cn/psfp-entry/services/trade?wsdl")
public class WSTradeQuery
    extends Service
{

    private final static URL WSTRADEQUERY_WSDL_LOCATION;
    private final static WebServiceException WSTRADEQUERY_EXCEPTION;
    private final static QName WSTRADEQUERY_QNAME = new QName("http://payat.ips.com.cn/WebService/TradeQuery", "WSTradeQuery");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("https://newpay.ips.com.cn/psfp-entry/services/trade?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        WSTRADEQUERY_WSDL_LOCATION = url;
        WSTRADEQUERY_EXCEPTION = e;
    }

    public WSTradeQuery() {
        super(__getWsdlLocation(), WSTRADEQUERY_QNAME);
    }

    public WSTradeQuery(WebServiceFeature... features) {
        super(__getWsdlLocation(), WSTRADEQUERY_QNAME, features);
    }

    public WSTradeQuery(URL wsdlLocation) {
        super(wsdlLocation, WSTRADEQUERY_QNAME);
    }

    public WSTradeQuery(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, WSTRADEQUERY_QNAME, features);
    }

    public WSTradeQuery(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public WSTradeQuery(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns TradeQueryService
     */
    @WebEndpoint(name = "WSTradeQuerySoap")
    public TradeQueryService getWSTradeQuerySoap() {
        return super.getPort(new QName("http://payat.ips.com.cn/WebService/TradeQuery", "WSTradeQuerySoap"), TradeQueryService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns TradeQueryService
     */
    @WebEndpoint(name = "WSTradeQuerySoap")
    public TradeQueryService getWSTradeQuerySoap(WebServiceFeature... features) {
        return super.getPort(new QName("http://payat.ips.com.cn/WebService/TradeQuery", "WSTradeQuerySoap"), TradeQueryService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (WSTRADEQUERY_EXCEPTION!= null) {
            throw WSTRADEQUERY_EXCEPTION;
        }
        return WSTRADEQUERY_WSDL_LOCATION;
    }

}