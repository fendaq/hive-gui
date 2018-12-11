package com.webkettle.sql.entity.jobcreate;

/**
 * 数据表链表查询条件配置
 * @author gsk
 */
public class TableEtlJoinOnConf {

    /**
     * 源表字段名
     */
    private String sourceColumName;

    /**
     * 关联表字段名
     */
    private String joinColumName;

    /**
     * 新的结果列
     */
    private String newColumName;

    public String getSourceColumName() {
        return sourceColumName;
    }

    public void setSourceColumName(String sourceColumName) {
        this.sourceColumName = sourceColumName;
    }

    public String getJoinColumName() {
        return joinColumName;
    }

    public void setJoinColumName(String joinColumName) {
        this.joinColumName = joinColumName;
    }

    public String getNewColumName() {
        return newColumName;
    }

    public void setNewColumName(String newColumName) {
        this.newColumName = newColumName;
    }
}
