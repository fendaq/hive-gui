package com.webkettle.sql.entity.jobcreate;

import java.util.List;

/**
 * 数据表ETL配置列信息
 * @author gsk
 */
public class TableEtlColum {

    /**
     * 源表或关联表中选中的字段名
     */
    private String columName;

    /**
     * 源表或关联表中选中字段的中文说明
     */
    private String columComment;

    /**
     * 源表或关联表的表名
     */
    private String tableName;

    /**
     * 源表或关联表所在数据库名
     */
    private String databaseName;
    /**
     * 字段类型, field = "普通字段字段", function(xxx) = "xxx"方法的函数字段
     */
    private String type;

    /**
     * 函数列参数配置, 当type = field时, 此字段参与业务
     */
    private List<TableEtlFunParams> params;

    /**
     * 质量处理
     */
    private List<TableEtlQualityConf> quality;

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public List<TableEtlQualityConf>  getQuality() {
        return quality;
    }

    public void setQuality(List<TableEtlQualityConf>  quality) {
        this.quality = quality;
    }

    public String getColumName() {
        return columName;
    }

    public void setColumName(String columName) {
        this.columName = columName;
    }

    public String getColumComment() {
        return columComment;
    }

    public void setColumComment(String columComment) {
        this.columComment = columComment;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<TableEtlFunParams> getParams() {
        return params;
    }

    public void setParams(List<TableEtlFunParams> params) {
        this.params = params;
    }
}
