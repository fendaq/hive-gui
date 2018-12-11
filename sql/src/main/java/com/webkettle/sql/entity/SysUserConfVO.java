package com.webkettle.sql.entity;

import com.aiyi.core.annotation.po.*;
import com.aiyi.core.beans.Po;
import com.aiyi.core.enums.BigFieldType;
import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * 用户配置信息实体
 */
@TableName(name = "SYS_USER_CONF")
public class SysUserConfVO extends Po {

    /**
     * 配置ID
     */
    @ID
    @FieldName(name = "CONF_ID")
    private long confId;

    /**
     * 用户ID
     */
    @FieldName(name = "USER_ID")
    private long userId;

    /**
     * 配置信息
     */
    @BigField(BigFieldType.NCLOB)
    @FieldName(name = "CONF")
    private String conf = "{}";

    @TempField
    private Map<String, ? extends Object> confEntity;

    public long getConfId() {
        return confId;
    }

    public void setConfId(long confId) {
        this.confId = confId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }

    public Map<String, ? extends Object> getConfEntity() {
        if (confEntity == null){
            confEntity = JSON.parseObject(conf, Map.class);
        }
        return confEntity;
    }

    public void setConfEntity(Map<String, ? extends Object> confEntity) {
        conf = JSON.toJSONString(confEntity);
        this.confEntity = confEntity;
    }
}
