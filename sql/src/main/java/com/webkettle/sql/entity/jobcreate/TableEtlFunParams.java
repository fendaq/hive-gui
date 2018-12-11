package com.webkettle.sql.entity.jobcreate;

/**
 * 数据表ETL函数列参数配置
 * @author gsk
 */
public class TableEtlFunParams {

    /**
     * 参数参考类型, field = 字段, var = 变量
     */
    private String type;

    /**
     * 参数值
     */
    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
