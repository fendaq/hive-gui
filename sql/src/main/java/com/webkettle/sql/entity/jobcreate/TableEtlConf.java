package com.webkettle.sql.entity.jobcreate;

/**
 * 数据表ETL操作配置
 * @author gsk
 */
public class TableEtlConf {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 所在库
     */
    private String databaseName;

    /**
     * 追加还是覆盖
     */
    private String loadType;

    /**
     * etl规则配置详情
     */
    private TableEtlConfInfo info;

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getLoadType() {
        return loadType;
    }

    public void setLoadType(String loadType) {
        this.loadType = loadType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public TableEtlConfInfo getInfo() {
        return info;
    }

    public void setInfo(TableEtlConfInfo info) {
        this.info = info;
    }
}
