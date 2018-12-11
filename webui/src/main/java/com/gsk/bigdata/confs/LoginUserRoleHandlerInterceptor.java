package com.gsk.bigdata.confs;

import com.aiyi.core.exception.AccessOAuthException;
import com.aiyi.core.exception.ServiceInvokeException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gsk.bigdata.service.impl.webservicemock.UserDetailsServiceImpl;
import com.webkettle.core.utils.ContextUtil;
import com.webkettle.core.utils.UUIDUtil;
import com.webkettle.sql.entity.SysUserVO;
import com.webkettle.webservice.client.UserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Spring Security 登录配置
 * @author gsk
 */
@Component
public class LoginUserRoleHandlerInterceptor implements HandlerInterceptor {

    protected static final Logger logger = LoggerFactory.getLogger(LoginUserRoleHandlerInterceptor.class);

    @Resource
    private UserDetailsService userDetailsService;
    @Value("${auth.login-path}")
    private String loginPath;
    @Value("${auth.system-id}")
    private String systemId;

    @Value("${spring.profiles.active}")
    private String profiles;
    @Value("${auth.test-token}")
    private String testToken;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String reqUrl = request.getRequestURI();
        if (reqUrl.contains(".") &&
                reqUrl.substring(reqUrl.lastIndexOf(".")).equalsIgnoreCase(".html")){
            return true;
        }
        if (reqUrl.contains("uc/login") || reqUrl.contains("uc/logout") || reqUrl.contains("uc/load-mobile")){
            return true;
        }
        if (reqUrl.indexOf("/api") != 0){
            return true;
        }
        String token = request.getHeader("auth_token");
        if (null == token){
            token = request.getParameter("token");
        }
        if(null == token){
            throw new AccessOAuthException("请先登录");
        }

        String jsonStr = null;
        if (profiles.equalsIgnoreCase("dev")
                && testToken.equalsIgnoreCase("__test_admin_token")){
            jsonStr = UserDetailsServiceImpl.mockSysUserJson;
        }else{
            try {
                jsonStr = userDetailsService.loadUserLogin(systemId, token);
            }catch (Exception e){
                throw new ServiceInvokeException("权限认证失败", e);
            }
        }

        if (null == jsonStr){
            throw new AccessOAuthException();
        }

        SysUserVO SysUserVORes = JSONObject.parseObject(jsonStr, SysUserVO.class);

//        SysUserVO SysUserVORes = new SysUserVO();
//        SysUserVORes.setAccount("admin");
//        SysUserVORes.setEmail("719348277@qq.com");
//        SysUserVORes.setFullname("admin");
//        SysUserVORes.setIsDel("N");
//        SysUserVORes.setId(1L);
//        SysUserVORes.setUserId(1L);
//        SysUserVORes.setMobile("17744416451");
//        SysUserVORes.setName("admin");



        //initContext(request, SysUserVORes, response);
        //return true;
        // 注调登录认证, 先调试接口
        SysUserVO user = new SysUserVO();
        user.setAccount(SysUserVORes.getAccount());
        user.setEmail(SysUserVORes.getEmail());
        user.setFullname(SysUserVORes.getFullname());
        user.setMobile(SysUserVORes.getMobile());
        user.setOrgName(SysUserVORes.getOrgName());
        user.setStatus(SysUserVORes.getStatus());
        user.setIsDel(SysUserVORes.getIsDel());
        user.setIsExpired(SysUserVORes.getIsExpired());
        user.setIsLock(SysUserVORes.getIsLock());
        user.setUserId(SysUserVORes.getUserId());

        initContext(request, user, response);
        ContextUtil.setToken(token);
        return true;

    }

    /**
     * 调到登录页
     * @param response
     */
    private void toLoginPage(HttpServletResponse response)
    {
        try
        {
            response.sendRedirect(loginPath + "?subid=" + systemId);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化本次请求上下文
     * @param request
     *          请求方法
     * @param user
     *          当前用户
     */

    private void initContext(HttpServletRequest request, SysUserVO user, HttpServletResponse response)
    {
        HttpSession session = request.getSession();
        session.setAttribute("ctx", request.getContextPath());

        ContextUtil.setUserEntity(user);
        ContextUtil.setUserName(user.getAccount());
        ContextUtil.setUserId(user.getUserId());

        String requestId = request.getHeader("requestId");
        if (StringUtils.isEmpty(requestId))
        {
            requestId = UUIDUtil.next();
        }
        ContextUtil.setRequestId(requestId);
        // 跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        //跨域 Header
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,XFILENAME,XFILECATEGORY,XFILESIZE");
        logger.info("requestURI:[{}], requestID:[{}], requestUser:[{}], cuskAddr:[{}]", request.getRequestURI(),
                requestId, user.getAccount(), request.getRemoteAddr());
    }
}
