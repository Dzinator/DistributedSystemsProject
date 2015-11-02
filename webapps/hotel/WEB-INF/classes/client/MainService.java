
package client;

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
 * JAX-WS RI 2.2.10
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "MainService", targetNamespace = "http://ws.middleRM/", wsdlLocation = "http://lab9-25:1413/middle/service?wsdl")
public class MainService
    extends Service
{

    private final static URL MAINSERVICE_WSDL_LOCATION;
    private final static WebServiceException MAINSERVICE_EXCEPTION;
    private final static QName MAINSERVICE_QNAME = new QName("http://ws.middleRM/", "MainService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://lab9-25:1413/middle/service?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        MAINSERVICE_WSDL_LOCATION = url;
        MAINSERVICE_EXCEPTION = e;
    }

    public MainService() {
        super(__getWsdlLocation(), MAINSERVICE_QNAME);
    }

    public MainService(WebServiceFeature... features) {
        super(__getWsdlLocation(), MAINSERVICE_QNAME, features);
    }

    public MainService(URL wsdlLocation) {
        super(wsdlLocation, MAINSERVICE_QNAME);
    }

    public MainService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, MAINSERVICE_QNAME, features);
    }

    public MainService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public MainService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns Main
     */
    @WebEndpoint(name = "MainPort")
    public Main getMainPort() {
        return super.getPort(new QName("http://ws.middleRM/", "MainPort"), Main.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns Main
     */
    @WebEndpoint(name = "MainPort")
    public Main getMainPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://ws.middleRM/", "MainPort"), Main.class, features);
    }

    private static URL __getWsdlLocation() {
        if (MAINSERVICE_EXCEPTION!= null) {
            throw MAINSERVICE_EXCEPTION;
        }
        return MAINSERVICE_WSDL_LOCATION;
    }

}
