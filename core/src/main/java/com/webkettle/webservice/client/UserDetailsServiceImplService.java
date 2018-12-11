
package com.webkettle.webservice.client;

import com.webkettle.core.commons.CommAttr;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;


/**
 * This class was generated by the JAXWS SI.
 * JAX-WS RI 2.0_02-b08-fcs
 * Generated source version: 2.0
 * 
 */
@WebServiceClient(name = "UserDetailsServiceImplService", targetNamespace = "http://impl.webservice.platform.haxx.com/", wsdlLocation = "http://" + CommAttr.WEB_SERVICE.ADDR + "/bpm/service/UserDetailsService?wsdl")
public class UserDetailsServiceImplService
    extends Service
{

    private final static URL USERDETAILSSERVICEIMPLSERVICE_WSDL_LOCATION;

    static {
        URL url = null;
        try {
            url = new URL("http://" +CommAttr.WEB_SERVICE.ADDR + "/bpm/service/UserDetailsService?wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        USERDETAILSSERVICEIMPLSERVICE_WSDL_LOCATION = url;
    }

    public UserDetailsServiceImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public UserDetailsServiceImplService() {
        super(USERDETAILSSERVICEIMPLSERVICE_WSDL_LOCATION, new QName("http://impl.webservice.platform.haxx.com/", "UserDetailsServiceImplService"));
    }

    /**
     * 
     * @return
     *     returns UserDetailsService
     */
    @WebEndpoint(name = "UserDetailsServiceImplPort")
    public UserDetailsService getUserDetailsServiceImplPort() {
        return (UserDetailsService)super.getPort(new QName("http://impl.webservice.platform.haxx.com/", "UserDetailsServiceImplPort"), UserDetailsService.class);
    }

}
