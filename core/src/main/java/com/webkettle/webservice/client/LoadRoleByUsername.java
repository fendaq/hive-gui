
package com.webkettle.webservice.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for loadRoleByUsername complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="loadRoleByUsername">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="userAccount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "loadRoleByUsername", propOrder = {
    "userAccount"
})
public class LoadRoleByUsername {

    protected String userAccount;

    /**
     * Gets the value of the userAccount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserAccount() {
        return userAccount;
    }

    /**
     * Sets the value of the userAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserAccount(String value) {
        this.userAccount = value;
    }

}