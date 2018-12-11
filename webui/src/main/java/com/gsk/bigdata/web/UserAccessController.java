package com.gsk.bigdata.web;

import com.aiyi.core.beans.ResultBean;
import com.aiyi.core.exception.ServiceInvokeException;
import com.aiyi.core.util.cache.CacheUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.webkettle.core.utils.UUIDUtil;
import com.webkettle.sql.entity.SysUserVO;
import com.webkettle.webservice.client.UserDetailsService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.ValidationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("uc")
public class UserAccessController {

    @Resource
    private UserDetailsService userDetailsService;

    @PostMapping("login")
    public ResultBean loginPage(@RequestBody SysUserVO sysUserVO){
        if (StringUtils.isEmpty(sysUserVO.getAccount())){
            throw new ValidationException("请输入用户账号");
        }
        if (StringUtils.isEmpty(sysUserVO.getPassword())){
            throw new ValidationException("用户密码不能为空");
        }


        String token = userDetailsService.longin(sysUserVO.getAccount(), sysUserVO.getPassword());

        // 强制登录画像 TODO 后期改
        if (null != sysUserVO.getSystemId() && sysUserVO.getSystemId().longValue() == 2L){
            // 画像
            HttpClient client = HttpClients.createDefault();

            HttpPost postreq = new HttpPost();
            StringEntity entity = null;
            Map<String, String> requestParams = new HashMap<>();
            requestParams.put("username", sysUserVO.getAccount());
            requestParams.put("token", token);
            try {
                entity = new StringEntity(JSON.toJSONString(requestParams));
            } catch (UnsupportedEncodingException e) {
            }
            postreq.setEntity(entity);
            postreq.setHeader("Content-Type", "application/json");
            try {
                // TODO 这里抽离到配置文件中
//                postreq.setURI(new URI("http://192.168.8.103:8080/system-web/api/entrance/apiDoLogin"));
                postreq.setURI(new URI("http://103.160.113.41:9080/kr/api/entrance/apiDoLogin"));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            try {
                HttpResponse execute = client.execute(postreq);
                if (execute.getStatusLine().getStatusCode() != 200){
                    throw new ServiceInvokeException("远程服务异常, 状态码:" + execute.getStatusLine().getStatusCode());
                }
                String jsonStr = EntityUtils.toString(execute.getEntity());
                JSONObject result = JSON.parseObject(jsonStr);

                if ("200".equals(result.getString("statusCode"))){
                    return ResultBean.success("用户登录成功").putResponseBody("accessToken", token);
                }else{
                    throw new ServiceInvokeException(result.getString("msg"));
                }
            } catch (IOException e) {
                throw new ServiceInvokeException("远程服务通信异常", e);
            }
        }

        return ResultBean.success("用户登录成功").putResponseBody("accessToken",
                userDetailsService.longin(sysUserVO.getAccount(), sysUserVO.getPassword()));
    }


    /**
     * 登出
     * @return
     */
    @PostMapping("logout")
    public ResultBean logout(){
        return null;
    }

    /**
     * 传入用户账号, 列出与该账号手机号相同手机号的用户列表
     * @param account
     *          参考用户账号
     * @return
     */
    @GetMapping("load-mobile/{account}")
    public Object listUserByMobile(@PathVariable String account){
        return ResultBean.success("用户获取成功")
                .putResponseBody("list", JSON.parseArray(userDetailsService.loadUserByAccountMobile(account)));
    }


}
