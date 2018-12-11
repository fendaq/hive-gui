package com.gsk.bigdata.web;

import com.aiyi.core.beans.Method;
import com.aiyi.core.sql.where.C;
import com.gsk.bigdata.dao.SysUserConfDao;
import com.webkettle.core.utils.ContextUtil;
import com.webkettle.sql.entity.SysUserConfVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/")
public class RdpController {

    @Resource
    private SysUserConfDao sysUserConfDao;

    /**
     * 远程桌面页面
     * @return
     */
    @RequestMapping("rdp")
    public String connectionPage(Model model){
        SysUserConfVO userConfVO = sysUserConfDao.get(Method.where("USER_ID", C.EQ, ContextUtil.getUserId()));
        if (null == userConfVO){
            return "rdp/401";
        }
        Map<String, ? extends Object> confEntity = userConfVO.getConfEntity();
        if (StringUtils.isEmpty(confEntity.get("location")) || StringUtils.isEmpty(confEntity.get("domain"))
                || StringUtils.isEmpty(confEntity.get("port")) || StringUtils.isEmpty(confEntity.get("username"))
                || StringUtils.isEmpty(confEntity.get("password"))){
            return "rdp/401";
        }
        model.addAttribute("conf", confEntity);
        return "rdp/conn";
    }

}
