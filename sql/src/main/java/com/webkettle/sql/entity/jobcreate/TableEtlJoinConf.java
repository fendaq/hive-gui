package com.webkettle.sql.entity.jobcreate;

import java.util.List;

/**
 * 数据表ETL链表查询配置
 * @author gsk
 */
public class TableEtlJoinConf {

    /**
     * 联查类型:LEFT_JOIN = 左查询, RIGHT_JOIN = 右查询, JOIN = 交集, FULL_JOIN = 并集
     */
    private String type;

    /**
     * 关联的表名
     */
    private String tableName;

    /**
     * 关联表所在数据库名
     */
    private String databaseName;

    /**
     * 关联条件
     */
    private List<TableEtlJoinOnConf> on;

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<TableEtlJoinOnConf> getOn() {
        return on;
    }

    public void setOn(List<TableEtlJoinOnConf> on) {
        this.on = on;
    }
}
