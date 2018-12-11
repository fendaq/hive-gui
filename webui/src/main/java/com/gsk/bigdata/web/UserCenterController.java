package com.gsk.bigdata.web;

import com.aiyi.core.beans.Method;
import com.aiyi.core.beans.ResultBean;
import com.aiyi.core.sql.where.C;
import com.alibaba.fastjson.JSON;
import com.gsk.bigdata.dao.SysUserConfDao;
import com.webkettle.core.utils.ContextUtil;
import com.webkettle.sql.entity.SysUserConfVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.ValidationException;

@RestController
@RequestMapping("api/user")
public class UserCenterController {

    @Resource
    private SysUserConfDao sysUserConfDao;

    /**
     * 用户自定义配置信息保存
     * @return
     */
    @PutMapping("conf")
    public ResultBean setUserConfig(@RequestBody SysUserConfVO confVO){
        SysUserConfVO userConf = sysUserConfDao.get(Method.where("USER_ID", C.EQ, confVO.getUserId()));
        if (null == userConf){
            confVO.setConf(JSON.toJSONString(confVO.getConfEntity()));
            sysUserConfDao.add(confVO);
        }else {
            userConf.setConf(JSON.toJSONString(confVO.getConfEntity()));
            sysUserConfDao.update(userConf);
        }
        return ResultBean.success("配置保存成功");
    }

    /**
     * 获取用户配置信息
     * @param userId
     * @return
     */
    @GetMapping("conf/{userId}")
    public ResultBean getUserConf(@PathVariable("userId") long userId){
        SysUserConfVO userConf = sysUserConfDao.get(Method.where("USER_ID", C.EQ, userId));
        if (null == userConf){
            throw new ValidationException("用户没有任何配置信息");
        }
        return ResultBean.success("配置获取成功").setResponseBody(userConf);
    }

    @GetMapping("current")
    public ResultBean getCurrentLoginUser(){
        return ResultBean.success("当前的登录用户获取成功")
                .putResponseBody("user", ContextUtil.getUserEntity());
    }

}
