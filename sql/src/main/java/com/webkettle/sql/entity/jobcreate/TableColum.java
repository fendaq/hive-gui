package com.webkettle.sql.entity.jobcreate;

/**
 * 表列实体
 * @author gsk
 */
public class TableColum {

    /**
     * 列名称
     */
    private String name;
    /**
     * 列类型
     */
    private String type;
    /**
     * 列值
     */
    private Object value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
