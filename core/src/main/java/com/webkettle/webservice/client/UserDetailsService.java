
package com.webkettle.webservice.client;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAXWS SI.
 * JAX-WS RI 2.0_02-b08-fcs
 * Generated source version: 2.0
 * 
 */
@WebService(name = "UserDetailsService", targetNamespace = "http://impl.webservice.platform.haxx.com/")
public interface UserDetailsService {


    /**
     * 
     * @param userAccount
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "loadOrgsByUsername", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.LoadOrgsByUsername")
    @ResponseWrapper(localName = "loadOrgsByUsernameResponse", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.LoadOrgsByUsernameResponse")
    public String loadOrgsByUsername(
        @WebParam(name = "userAccount", targetNamespace = "")
        String userAccount);

    /**
     * 
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getRoleList", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.GetRoleList")
    @ResponseWrapper(localName = "getRoleListResponse", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.GetRoleListResponse")
    public String getRoleList();

    /**
     * 
     * @param orgId
     * @param loginId
     * @param userId
     * @param roleId
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getUserAuthorityList", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.GetUserAuthorityList")
    @ResponseWrapper(localName = "getUserAuthorityListResponse", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.GetUserAuthorityListResponse")
    public String getUserAuthorityList(
        @WebParam(name = "userId", targetNamespace = "")
        Long userId,
        @WebParam(name = "roleId", targetNamespace = "")
        Long roleId,
        @WebParam(name = "orgId", targetNamespace = "")
        Long orgId,
        @WebParam(name = "loginId", targetNamespace = "")
        Long loginId);

    /**
     * 
     * @param dbName
     * @param token
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAuthenticationList", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.GetAuthenticationList")
    @ResponseWrapper(localName = "getAuthenticationListResponse", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.GetAuthenticationListResponse")
    public String getAuthenticationList(
        @WebParam(name = "token", targetNamespace = "")
        String token,
        @WebParam(name = "dbName", targetNamespace = "")
        String dbName);

    /**
     * 
     * @param userId
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getCurrentUserSkin", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.GetCurrentUserSkin")
    @ResponseWrapper(localName = "getCurrentUserSkinResponse", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.GetCurrentUserSkinResponse")
    public String getCurrentUserSkin(
        @WebParam(name = "userId", targetNamespace = "")
        Long userId);

    /**
     * 
     * @param token
     * @param systemId
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "loadUserLogin", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.LoadUserLogin")
    @ResponseWrapper(localName = "loadUserLoginResponse", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.LoadUserLoginResponse")
    public String loadUserLogin(
        @WebParam(name = "systemId", targetNamespace = "")
        String systemId,
        @WebParam(name = "token", targetNamespace = "")
        String token);

    /**
     * 
     * @param jsonStr
     */
    @WebMethod
    @RequestWrapper(localName = "addSysTable", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.AddSysTable")
    @ResponseWrapper(localName = "addSysTableResponse", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.AddSysTableResponse")
    public void addSysTable(
        @WebParam(name = "jsonStr", targetNamespace = "")
        String jsonStr);

    /**
     * 
     * @param userAccount
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "loadRoleByUsername", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.LoadRoleByUsername")
    @ResponseWrapper(localName = "loadRoleByUsernameResponse", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.LoadRoleByUsernameResponse")
    public String loadRoleByUsername(
        @WebParam(name = "userAccount", targetNamespace = "")
        String userAccount);

    /**
     * 
     * @param jsonStr
     */
    @WebMethod
    @RequestWrapper(localName = "addDataBase", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.AddDataBase")
    @ResponseWrapper(localName = "addDataBaseResponse", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.AddDataBaseResponse")
    public void addDataBase(
        @WebParam(name = "jsonStr", targetNamespace = "")
        String jsonStr);

    /**
     * 
     * @param orgId
     * @param userAccount
     */
    @WebMethod
    @RequestWrapper(localName = "setCurOrg", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.SetCurOrg")
    @ResponseWrapper(localName = "setCurOrgResponse", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.SetCurOrgResponse")
    public void setCurOrg(
        @WebParam(name = "userAccount", targetNamespace = "")
        String userAccount,
        @WebParam(name = "orgId", targetNamespace = "")
        Long orgId);

    /**
     * 
     * @param account
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getUserAndOrgList", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.GetUserAndOrgList")
    @ResponseWrapper(localName = "getUserAndOrgListResponse", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.GetUserAndOrgListResponse")
    public String getUserAndOrgList(
        @WebParam(name = "account", targetNamespace = "")
        String account);

    /**
     * 
     * @param dbName
     * @param token
     * @param action
     * @param tabName
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "checkUserAuthority", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.CheckUserAuthority")
    @ResponseWrapper(localName = "checkUserAuthorityResponse", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.CheckUserAuthorityResponse")
    public boolean checkUserAuthority(
        @WebParam(name = "token", targetNamespace = "")
        String token,
        @WebParam(name = "dbName", targetNamespace = "")
        String dbName,
        @WebParam(name = "tabName", targetNamespace = "")
        String tabName,
        @WebParam(name = "action", targetNamespace = "")
        String action);

    /**
     * 
     * @param userAccount
     * @return
     *     returns java.lang.String
     * @throws Exception_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "loadUserByUsername", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.LoadUserByUsername")
    @ResponseWrapper(localName = "loadUserByUsernameResponse", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.LoadUserByUsernameResponse")
    public String loadUserByUsername(
        @WebParam(name = "userAccount", targetNamespace = "")
        String userAccount)
        throws Exception_Exception
    ;

    /**
     * 
     * @param account
     * @param password
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "longin", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.Longin")
    @ResponseWrapper(localName = "longinResponse", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.LonginResponse")
    public String longin(
        @WebParam(name = "account", targetNamespace = "")
        String account,
        @WebParam(name = "password", targetNamespace = "")
        String password);

    /**
     * 
     * @param tableId
     */
    @WebMethod
    @RequestWrapper(localName = "delSysTable", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.DelSysTable")
    @ResponseWrapper(localName = "delSysTableResponse", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.DelSysTableResponse")
    public void delSysTable(
        @WebParam(name = "tableId", targetNamespace = "")
        Long tableId);

    /**
     * 
     * @param databaseId
     */
    @WebMethod
    @RequestWrapper(localName = "delDataBase", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.DelDataBase")
    @ResponseWrapper(localName = "delDataBaseResponse", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.DelDataBaseResponse")
    public void delDataBase(
        @WebParam(name = "databaseId", targetNamespace = "")
        Long databaseId);

    /**
     * 
     * @param userAccount
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "loadCurOrgByUsername", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.LoadCurOrgByUsername")
    @ResponseWrapper(localName = "loadCurOrgByUsernameResponse", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.LoadCurOrgByUsernameResponse")
    public String loadCurOrgByUsername(
        @WebParam(name = "userAccount", targetNamespace = "")
        String userAccount);

    /**
     * 
     * @param jsonStr
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "updateAuthority", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.UpdateAuthority")
    @ResponseWrapper(localName = "updateAuthorityResponse", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.UpdateAuthorityResponse")
    public String updateAuthority(
        @WebParam(name = "jsonStr", targetNamespace = "")
        String jsonStr);

    /**
     * 
     * @param account
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "loadUserByAccountMobile", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.LoadUserByAccountMobile")
    @ResponseWrapper(localName = "loadUserByAccountMobileResponse", targetNamespace = "http://impl.webservice.platform.haxx.com/", className = "com.webkettle.webservice.client.LoadUserByAccountMobileResponse")
    public String loadUserByAccountMobile(
        @WebParam(name = "account", targetNamespace = "")
        String account);

}