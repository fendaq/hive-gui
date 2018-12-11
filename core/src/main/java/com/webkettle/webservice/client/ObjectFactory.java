
package com.webkettle.webservice.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.webkettle.webservice.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DelDataBaseResponse_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "delDataBaseResponse");
    private final static QName _GetUserAuthorityList_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "getUserAuthorityList");
    private final static QName _Longin_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "longin");
    private final static QName _LoadOrgsByUsername_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "loadOrgsByUsername");
    private final static QName _CheckUserAuthority_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "checkUserAuthority");
    private final static QName _LoadUserLoginResponse_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "loadUserLoginResponse");
    private final static QName _SetCurOrgResponse_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "setCurOrgResponse");
    private final static QName _LonginResponse_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "longinResponse");
    private final static QName _AddDataBaseResponse_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "addDataBaseResponse");
    private final static QName _LoadCurOrgByUsernameResponse_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "loadCurOrgByUsernameResponse");
    private final static QName _UpdateAuthorityResponse_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "updateAuthorityResponse");
    private final static QName _GetRoleListResponse_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "getRoleListResponse");
    private final static QName _GetUserAndOrgList_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "getUserAndOrgList");
    private final static QName _UpdateAuthority_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "updateAuthority");
    private final static QName _AddSysTable_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "addSysTable");
    private final static QName _GetCurrentUserSkinResponse_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "getCurrentUserSkinResponse");
    private final static QName _LoadUserLogin_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "loadUserLogin");
    private final static QName _AddDataBase_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "addDataBase");
    private final static QName _DelSysTableResponse_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "delSysTableResponse");
    private final static QName _LoadUserByUsernameResponse_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "loadUserByUsernameResponse");
    private final static QName _LoadRoleByUsername_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "loadRoleByUsername");
    private final static QName _GetRoleList_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "getRoleList");
    private final static QName _Exception_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "Exception");
    private final static QName _GetUserAuthorityListResponse_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "getUserAuthorityListResponse");
    private final static QName _SetCurOrg_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "setCurOrg");
    private final static QName _LoadUserByAccountMobile_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "loadUserByAccountMobile");
    private final static QName _GetAuthenticationList_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "getAuthenticationList");
    private final static QName _AddSysTableResponse_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "addSysTableResponse");
    private final static QName _LoadOrgsByUsernameResponse_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "loadOrgsByUsernameResponse");
    private final static QName _GetUserAndOrgListResponse_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "getUserAndOrgListResponse");
    private final static QName _LoadRoleByUsernameResponse_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "loadRoleByUsernameResponse");
    private final static QName _LoadUserByAccountMobileResponse_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "loadUserByAccountMobileResponse");
    private final static QName _CheckUserAuthorityResponse_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "checkUserAuthorityResponse");
    private final static QName _GetCurrentUserSkin_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "getCurrentUserSkin");
    private final static QName _GetAuthenticationListResponse_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "getAuthenticationListResponse");
    private final static QName _LoadUserByUsername_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "loadUserByUsername");
    private final static QName _LoadCurOrgByUsername_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "loadCurOrgByUsername");
    private final static QName _DelSysTable_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "delSysTable");
    private final static QName _DelDataBase_QNAME = new QName("http://impl.webservice.platform.haxx.com/", "delDataBase");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.webkettle.webservice.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UpdateAuthority }
     * 
     */
    public UpdateAuthority createUpdateAuthority() {
        return new UpdateAuthority();
    }

    /**
     * Create an instance of {@link GetCurrentUserSkin }
     * 
     */
    public GetCurrentUserSkin createGetCurrentUserSkin() {
        return new GetCurrentUserSkin();
    }

    /**
     * Create an instance of {@link GetUserAuthorityListResponse }
     * 
     */
    public GetUserAuthorityListResponse createGetUserAuthorityListResponse() {
        return new GetUserAuthorityListResponse();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link AddSysTable }
     * 
     */
    public AddSysTable createAddSysTable() {
        return new AddSysTable();
    }

    /**
     * Create an instance of {@link LoadOrgsByUsernameResponse }
     * 
     */
    public LoadOrgsByUsernameResponse createLoadOrgsByUsernameResponse() {
        return new LoadOrgsByUsernameResponse();
    }

    /**
     * Create an instance of {@link LoadUserByAccountMobileResponse }
     * 
     */
    public LoadUserByAccountMobileResponse createLoadUserByAccountMobileResponse() {
        return new LoadUserByAccountMobileResponse();
    }

    /**
     * Create an instance of {@link SetCurOrgResponse }
     * 
     */
    public SetCurOrgResponse createSetCurOrgResponse() {
        return new SetCurOrgResponse();
    }

    /**
     * Create an instance of {@link GetUserAndOrgList }
     * 
     */
    public GetUserAndOrgList createGetUserAndOrgList() {
        return new GetUserAndOrgList();
    }

    /**
     * Create an instance of {@link GetAuthenticationListResponse }
     * 
     */
    public GetAuthenticationListResponse createGetAuthenticationListResponse() {
        return new GetAuthenticationListResponse();
    }

    /**
     * Create an instance of {@link GetRoleList }
     * 
     */
    public GetRoleList createGetRoleList() {
        return new GetRoleList();
    }

    /**
     * Create an instance of {@link AddSysTableResponse }
     * 
     */
    public AddSysTableResponse createAddSysTableResponse() {
        return new AddSysTableResponse();
    }

    /**
     * Create an instance of {@link LoadUserLogin }
     * 
     */
    public LoadUserLogin createLoadUserLogin() {
        return new LoadUserLogin();
    }

    /**
     * Create an instance of {@link GetAuthenticationList }
     * 
     */
    public GetAuthenticationList createGetAuthenticationList() {
        return new GetAuthenticationList();
    }

    /**
     * Create an instance of {@link GetUserAndOrgListResponse }
     * 
     */
    public GetUserAndOrgListResponse createGetUserAndOrgListResponse() {
        return new GetUserAndOrgListResponse();
    }

    /**
     * Create an instance of {@link Longin }
     * 
     */
    public Longin createLongin() {
        return new Longin();
    }

    /**
     * Create an instance of {@link DelSysTableResponse }
     * 
     */
    public DelSysTableResponse createDelSysTableResponse() {
        return new DelSysTableResponse();
    }

    /**
     * Create an instance of {@link CheckUserAuthorityResponse }
     * 
     */
    public CheckUserAuthorityResponse createCheckUserAuthorityResponse() {
        return new CheckUserAuthorityResponse();
    }

    /**
     * Create an instance of {@link GetCurrentUserSkinResponse }
     * 
     */
    public GetCurrentUserSkinResponse createGetCurrentUserSkinResponse() {
        return new GetCurrentUserSkinResponse();
    }

    /**
     * Create an instance of {@link LoadUserByUsername }
     * 
     */
    public LoadUserByUsername createLoadUserByUsername() {
        return new LoadUserByUsername();
    }

    /**
     * Create an instance of {@link CheckUserAuthority }
     * 
     */
    public CheckUserAuthority createCheckUserAuthority() {
        return new CheckUserAuthority();
    }

    /**
     * Create an instance of {@link LoadUserByUsernameResponse }
     * 
     */
    public LoadUserByUsernameResponse createLoadUserByUsernameResponse() {
        return new LoadUserByUsernameResponse();
    }

    /**
     * Create an instance of {@link LoadRoleByUsernameResponse }
     * 
     */
    public LoadRoleByUsernameResponse createLoadRoleByUsernameResponse() {
        return new LoadRoleByUsernameResponse();
    }

    /**
     * Create an instance of {@link LoadRoleByUsername }
     * 
     */
    public LoadRoleByUsername createLoadRoleByUsername() {
        return new LoadRoleByUsername();
    }

    /**
     * Create an instance of {@link LoadUserByAccountMobile }
     * 
     */
    public LoadUserByAccountMobile createLoadUserByAccountMobile() {
        return new LoadUserByAccountMobile();
    }

    /**
     * Create an instance of {@link DelDataBaseResponse }
     * 
     */
    public DelDataBaseResponse createDelDataBaseResponse() {
        return new DelDataBaseResponse();
    }

    /**
     * Create an instance of {@link UpdateAuthorityResponse }
     * 
     */
    public UpdateAuthorityResponse createUpdateAuthorityResponse() {
        return new UpdateAuthorityResponse();
    }

    /**
     * Create an instance of {@link AddDataBase }
     * 
     */
    public AddDataBase createAddDataBase() {
        return new AddDataBase();
    }

    /**
     * Create an instance of {@link GetRoleListResponse }
     * 
     */
    public GetRoleListResponse createGetRoleListResponse() {
        return new GetRoleListResponse();
    }

    /**
     * Create an instance of {@link LoadCurOrgByUsername }
     * 
     */
    public LoadCurOrgByUsername createLoadCurOrgByUsername() {
        return new LoadCurOrgByUsername();
    }

    /**
     * Create an instance of {@link SetCurOrg }
     * 
     */
    public SetCurOrg createSetCurOrg() {
        return new SetCurOrg();
    }

    /**
     * Create an instance of {@link LoadCurOrgByUsernameResponse }
     * 
     */
    public LoadCurOrgByUsernameResponse createLoadCurOrgByUsernameResponse() {
        return new LoadCurOrgByUsernameResponse();
    }

    /**
     * Create an instance of {@link LoadUserLoginResponse }
     * 
     */
    public LoadUserLoginResponse createLoadUserLoginResponse() {
        return new LoadUserLoginResponse();
    }

    /**
     * Create an instance of {@link LonginResponse }
     * 
     */
    public LonginResponse createLonginResponse() {
        return new LonginResponse();
    }

    /**
     * Create an instance of {@link GetUserAuthorityList }
     * 
     */
    public GetUserAuthorityList createGetUserAuthorityList() {
        return new GetUserAuthorityList();
    }

    /**
     * Create an instance of {@link DelSysTable }
     * 
     */
    public DelSysTable createDelSysTable() {
        return new DelSysTable();
    }

    /**
     * Create an instance of {@link AddDataBaseResponse }
     * 
     */
    public AddDataBaseResponse createAddDataBaseResponse() {
        return new AddDataBaseResponse();
    }

    /**
     * Create an instance of {@link LoadOrgsByUsername }
     * 
     */
    public LoadOrgsByUsername createLoadOrgsByUsername() {
        return new LoadOrgsByUsername();
    }

    /**
     * Create an instance of {@link DelDataBase }
     * 
     */
    public DelDataBase createDelDataBase() {
        return new DelDataBase();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DelDataBaseResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "delDataBaseResponse")
    public JAXBElement<DelDataBaseResponse> createDelDataBaseResponse(DelDataBaseResponse value) {
        return new JAXBElement<DelDataBaseResponse>(_DelDataBaseResponse_QNAME, DelDataBaseResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserAuthorityList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "getUserAuthorityList")
    public JAXBElement<GetUserAuthorityList> createGetUserAuthorityList(GetUserAuthorityList value) {
        return new JAXBElement<GetUserAuthorityList>(_GetUserAuthorityList_QNAME, GetUserAuthorityList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Longin }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "longin")
    public JAXBElement<Longin> createLongin(Longin value) {
        return new JAXBElement<Longin>(_Longin_QNAME, Longin.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadOrgsByUsername }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "loadOrgsByUsername")
    public JAXBElement<LoadOrgsByUsername> createLoadOrgsByUsername(LoadOrgsByUsername value) {
        return new JAXBElement<LoadOrgsByUsername>(_LoadOrgsByUsername_QNAME, LoadOrgsByUsername.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckUserAuthority }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "checkUserAuthority")
    public JAXBElement<CheckUserAuthority> createCheckUserAuthority(CheckUserAuthority value) {
        return new JAXBElement<CheckUserAuthority>(_CheckUserAuthority_QNAME, CheckUserAuthority.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadUserLoginResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "loadUserLoginResponse")
    public JAXBElement<LoadUserLoginResponse> createLoadUserLoginResponse(LoadUserLoginResponse value) {
        return new JAXBElement<LoadUserLoginResponse>(_LoadUserLoginResponse_QNAME, LoadUserLoginResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetCurOrgResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "setCurOrgResponse")
    public JAXBElement<SetCurOrgResponse> createSetCurOrgResponse(SetCurOrgResponse value) {
        return new JAXBElement<SetCurOrgResponse>(_SetCurOrgResponse_QNAME, SetCurOrgResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LonginResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "longinResponse")
    public JAXBElement<LonginResponse> createLonginResponse(LonginResponse value) {
        return new JAXBElement<LonginResponse>(_LonginResponse_QNAME, LonginResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddDataBaseResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "addDataBaseResponse")
    public JAXBElement<AddDataBaseResponse> createAddDataBaseResponse(AddDataBaseResponse value) {
        return new JAXBElement<AddDataBaseResponse>(_AddDataBaseResponse_QNAME, AddDataBaseResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadCurOrgByUsernameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "loadCurOrgByUsernameResponse")
    public JAXBElement<LoadCurOrgByUsernameResponse> createLoadCurOrgByUsernameResponse(LoadCurOrgByUsernameResponse value) {
        return new JAXBElement<LoadCurOrgByUsernameResponse>(_LoadCurOrgByUsernameResponse_QNAME, LoadCurOrgByUsernameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateAuthorityResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "updateAuthorityResponse")
    public JAXBElement<UpdateAuthorityResponse> createUpdateAuthorityResponse(UpdateAuthorityResponse value) {
        return new JAXBElement<UpdateAuthorityResponse>(_UpdateAuthorityResponse_QNAME, UpdateAuthorityResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRoleListResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "getRoleListResponse")
    public JAXBElement<GetRoleListResponse> createGetRoleListResponse(GetRoleListResponse value) {
        return new JAXBElement<GetRoleListResponse>(_GetRoleListResponse_QNAME, GetRoleListResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserAndOrgList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "getUserAndOrgList")
    public JAXBElement<GetUserAndOrgList> createGetUserAndOrgList(GetUserAndOrgList value) {
        return new JAXBElement<GetUserAndOrgList>(_GetUserAndOrgList_QNAME, GetUserAndOrgList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateAuthority }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "updateAuthority")
    public JAXBElement<UpdateAuthority> createUpdateAuthority(UpdateAuthority value) {
        return new JAXBElement<UpdateAuthority>(_UpdateAuthority_QNAME, UpdateAuthority.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddSysTable }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "addSysTable")
    public JAXBElement<AddSysTable> createAddSysTable(AddSysTable value) {
        return new JAXBElement<AddSysTable>(_AddSysTable_QNAME, AddSysTable.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCurrentUserSkinResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "getCurrentUserSkinResponse")
    public JAXBElement<GetCurrentUserSkinResponse> createGetCurrentUserSkinResponse(GetCurrentUserSkinResponse value) {
        return new JAXBElement<GetCurrentUserSkinResponse>(_GetCurrentUserSkinResponse_QNAME, GetCurrentUserSkinResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadUserLogin }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "loadUserLogin")
    public JAXBElement<LoadUserLogin> createLoadUserLogin(LoadUserLogin value) {
        return new JAXBElement<LoadUserLogin>(_LoadUserLogin_QNAME, LoadUserLogin.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddDataBase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "addDataBase")
    public JAXBElement<AddDataBase> createAddDataBase(AddDataBase value) {
        return new JAXBElement<AddDataBase>(_AddDataBase_QNAME, AddDataBase.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DelSysTableResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "delSysTableResponse")
    public JAXBElement<DelSysTableResponse> createDelSysTableResponse(DelSysTableResponse value) {
        return new JAXBElement<DelSysTableResponse>(_DelSysTableResponse_QNAME, DelSysTableResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadUserByUsernameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "loadUserByUsernameResponse")
    public JAXBElement<LoadUserByUsernameResponse> createLoadUserByUsernameResponse(LoadUserByUsernameResponse value) {
        return new JAXBElement<LoadUserByUsernameResponse>(_LoadUserByUsernameResponse_QNAME, LoadUserByUsernameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadRoleByUsername }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "loadRoleByUsername")
    public JAXBElement<LoadRoleByUsername> createLoadRoleByUsername(LoadRoleByUsername value) {
        return new JAXBElement<LoadRoleByUsername>(_LoadRoleByUsername_QNAME, LoadRoleByUsername.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRoleList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "getRoleList")
    public JAXBElement<GetRoleList> createGetRoleList(GetRoleList value) {
        return new JAXBElement<GetRoleList>(_GetRoleList_QNAME, GetRoleList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserAuthorityListResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "getUserAuthorityListResponse")
    public JAXBElement<GetUserAuthorityListResponse> createGetUserAuthorityListResponse(GetUserAuthorityListResponse value) {
        return new JAXBElement<GetUserAuthorityListResponse>(_GetUserAuthorityListResponse_QNAME, GetUserAuthorityListResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetCurOrg }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "setCurOrg")
    public JAXBElement<SetCurOrg> createSetCurOrg(SetCurOrg value) {
        return new JAXBElement<SetCurOrg>(_SetCurOrg_QNAME, SetCurOrg.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadUserByAccountMobile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "loadUserByAccountMobile")
    public JAXBElement<LoadUserByAccountMobile> createLoadUserByAccountMobile(LoadUserByAccountMobile value) {
        return new JAXBElement<LoadUserByAccountMobile>(_LoadUserByAccountMobile_QNAME, LoadUserByAccountMobile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAuthenticationList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "getAuthenticationList")
    public JAXBElement<GetAuthenticationList> createGetAuthenticationList(GetAuthenticationList value) {
        return new JAXBElement<GetAuthenticationList>(_GetAuthenticationList_QNAME, GetAuthenticationList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddSysTableResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "addSysTableResponse")
    public JAXBElement<AddSysTableResponse> createAddSysTableResponse(AddSysTableResponse value) {
        return new JAXBElement<AddSysTableResponse>(_AddSysTableResponse_QNAME, AddSysTableResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadOrgsByUsernameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "loadOrgsByUsernameResponse")
    public JAXBElement<LoadOrgsByUsernameResponse> createLoadOrgsByUsernameResponse(LoadOrgsByUsernameResponse value) {
        return new JAXBElement<LoadOrgsByUsernameResponse>(_LoadOrgsByUsernameResponse_QNAME, LoadOrgsByUsernameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserAndOrgListResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "getUserAndOrgListResponse")
    public JAXBElement<GetUserAndOrgListResponse> createGetUserAndOrgListResponse(GetUserAndOrgListResponse value) {
        return new JAXBElement<GetUserAndOrgListResponse>(_GetUserAndOrgListResponse_QNAME, GetUserAndOrgListResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadRoleByUsernameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "loadRoleByUsernameResponse")
    public JAXBElement<LoadRoleByUsernameResponse> createLoadRoleByUsernameResponse(LoadRoleByUsernameResponse value) {
        return new JAXBElement<LoadRoleByUsernameResponse>(_LoadRoleByUsernameResponse_QNAME, LoadRoleByUsernameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadUserByAccountMobileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "loadUserByAccountMobileResponse")
    public JAXBElement<LoadUserByAccountMobileResponse> createLoadUserByAccountMobileResponse(LoadUserByAccountMobileResponse value) {
        return new JAXBElement<LoadUserByAccountMobileResponse>(_LoadUserByAccountMobileResponse_QNAME, LoadUserByAccountMobileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckUserAuthorityResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "checkUserAuthorityResponse")
    public JAXBElement<CheckUserAuthorityResponse> createCheckUserAuthorityResponse(CheckUserAuthorityResponse value) {
        return new JAXBElement<CheckUserAuthorityResponse>(_CheckUserAuthorityResponse_QNAME, CheckUserAuthorityResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCurrentUserSkin }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "getCurrentUserSkin")
    public JAXBElement<GetCurrentUserSkin> createGetCurrentUserSkin(GetCurrentUserSkin value) {
        return new JAXBElement<GetCurrentUserSkin>(_GetCurrentUserSkin_QNAME, GetCurrentUserSkin.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAuthenticationListResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "getAuthenticationListResponse")
    public JAXBElement<GetAuthenticationListResponse> createGetAuthenticationListResponse(GetAuthenticationListResponse value) {
        return new JAXBElement<GetAuthenticationListResponse>(_GetAuthenticationListResponse_QNAME, GetAuthenticationListResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadUserByUsername }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "loadUserByUsername")
    public JAXBElement<LoadUserByUsername> createLoadUserByUsername(LoadUserByUsername value) {
        return new JAXBElement<LoadUserByUsername>(_LoadUserByUsername_QNAME, LoadUserByUsername.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoadCurOrgByUsername }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "loadCurOrgByUsername")
    public JAXBElement<LoadCurOrgByUsername> createLoadCurOrgByUsername(LoadCurOrgByUsername value) {
        return new JAXBElement<LoadCurOrgByUsername>(_LoadCurOrgByUsername_QNAME, LoadCurOrgByUsername.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DelSysTable }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "delSysTable")
    public JAXBElement<DelSysTable> createDelSysTable(DelSysTable value) {
        return new JAXBElement<DelSysTable>(_DelSysTable_QNAME, DelSysTable.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DelDataBase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.platform.haxx.com/", name = "delDataBase")
    public JAXBElement<DelDataBase> createDelDataBase(DelDataBase value) {
        return new JAXBElement<DelDataBase>(_DelDataBase_QNAME, DelDataBase.class, null, value);
    }

}
