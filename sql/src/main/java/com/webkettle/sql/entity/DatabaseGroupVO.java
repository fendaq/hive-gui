package com.webkettle.sql.entity;

import java.util.LinkedList;
import java.util.List;

/**
 * 数据库分组实体
 */
public class DatabaseGroupVO {

    /**
     * 分组名
     */
    private String name;

    /**
     * 分组标识
     */
    private String key;

    /**
     * 分组内数据库列表
     */
    private List<Object> list = new LinkedList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
