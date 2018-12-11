
package com.webkettle.webservice.client;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAXWS SI.
 * JAX-WS RI 2.0_02-b08-fcs
 * Generated source version: 2.0
 * 
 */
@WebFault(name = "Exception", targetNamespace = "http://impl.webservice.platform.haxx.com/")
public class Exception_Exception
    extends java.lang.Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private com.webkettle.webservice.client.Exception faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public Exception_Exception(String message, com.webkettle.webservice.client.Exception faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public Exception_Exception(String message, com.webkettle.webservice.client.Exception faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: com.webkettle.webservice.client.Exception
     */
    public com.webkettle.webservice.client.Exception getFaultInfo() {
        return faultInfo;
    }

}