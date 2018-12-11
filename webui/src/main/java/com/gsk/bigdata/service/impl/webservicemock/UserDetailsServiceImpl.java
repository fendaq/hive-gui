package com.gsk.bigdata.service.impl.webservicemock;

import com.aiyi.core.annotation.po.vali.Validation;
import com.aiyi.core.beans.Method;
import com.aiyi.core.sql.where.C;
import com.alibaba.fastjson.JSON;
import com.webkettle.core.utils.ContextUtil;
import com.webkettle.core.utils.UUIDUtil;
import com.webkettle.sql.SpringContextUtil;
import com.webkettle.sql.entity.DatabaseVO;
import com.webkettle.sql.entity.TableVO;
import com.webkettle.webservice.client.Exception_Exception;
import com.webkettle.webservice.client.UserDetailsService;
import org.springframework.util.StringUtils;

import javax.validation.ValidationException;
import java.util.List;

/**
 * 用户操作本地实现类(测试类, 测试数据)
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    public static String mockSysUserJson = "{\"account\":\"admin\",\"accountNonExpired\":false,\"accountNonLocked\":false,\"credentialsNonExpired\":false,\"email\":\"719348277@qq.com\",\"enabled\":false,\"fromType\":0,\"fullname\":\"admin\",\"isDel\":\"N\",\"mobile\":\"17744416451\",\"userId\":1}";

    private String testToken = UUIDUtil.next();

    /**
     * 通过用户名获用户数据
     * @param userAccount
     * @return
     */
    @Override
    public String loadCurOrgByUsername(String userAccount) {
        return mockSysUserJson;
    }

    /**
     * 更新用户权限
     * @param jsonStr
     * @return
     */
    @Override
    public String updateAuthority(String jsonStr) {
        return null;
    }

    /**
     * 通过用户手机号获取用户信息
     * @param account
     * @return
     */
    @Override
    public String loadUserByAccountMobile(String account) {
        return mockSysUserJson;
    }

    @Override
    public String getCurrentUserSkin(Long userId) {
        return "mockUserSkin";
    }

    @Override
    public String loadUserLogin(String systemId, String token) {
        return mockSysUserJson;
    }

    @Override
    public void addSysTable(String jsonStr) {
        TableVO tableVO = JSON.parseObject(jsonStr, TableVO.class);
    }

    @Override
    public String loadOrgsByUsername(String userAccount) {
        return null;
    }

    @Override
    public String getRoleList() {
        return null;
    }

    @Override
    public String getUserAuthorityList(Long userId, Long roleId, Long orgId, Long loginId) {
        return null;
    }

    @Override
    public String getAuthenticationList(String token, String dbName) {
        return "";
    }

    @Override
    public String loadUserByUsername(String userAccount) {
        return mockSysUserJson;
    }

    @Override
    public String longin(String account, String password) {
        return testToken;
    }

    @Override
    public void delSysTable(Long tableId) {

    }

    @Override
    public void delDataBase(Long databaseId) {
    }
    @Override
    public void setCurOrg(String userAccount, Long orgId) {

    }

    @Override
    public String getUserAndOrgList(String account) {
        return null;
    }

    @Override
    public boolean checkUserAuthority(String token, String dbName, String tabName, String action) {
        if (null == dbName){
            throw new ValidationException("请选定数据库");
        }
        char[] chars = dbName.toCharArray();
        int res = 0;
        for (char c: chars){
            res += c;
        }
        if (res % 2 != 0){
            return false;
        }
        if (!StringUtils.isEmpty(tabName)){
            res = 0;
            chars = tabName.toCharArray();
            for (char c: chars){
                res += c;
            }
            if (res % 2 != 0){
                return false;
            }
        }
        return true;
    }

    @Override
    public String loadRoleByUsername(String userAccount) {
        return mockSysUserJson;
    }

    @Override
    public void addDataBase(String jsonStr) {
    }
}
