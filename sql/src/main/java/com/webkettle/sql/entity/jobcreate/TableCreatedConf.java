package com.webkettle.sql.entity.jobcreate;

/**
 * 数据表创建配置传输实体
 * @author gsk
 */
public class TableCreatedConf {

    private long databaseId;

    /**
     * 建表脚本类型
     * 添加方式(SQL = 通过sql添加, ETL = 通过数据引擎添加)
     */
    private String type;

    /**
     * 建表SQL脚本
     */
    private String sql;

    /**
     * 建表ETL配置
     */
    private TableEtlConf etl;

    /**
     * 数据库地址
     */
    private String dbName;

    /**
     * 任务显示名称
     */
    private String jobDisplayName;

    public String getJobDisplayName() {
        return jobDisplayName;
    }

    public void setJobDisplayName(String jobDisplayName) {
        this.jobDisplayName = jobDisplayName;
    }

    public long getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(long databaseId) {
        this.databaseId = databaseId;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public TableEtlConf getEtl() {
        return etl;
    }

    public void setEtl(TableEtlConf etl) {
        this.etl = etl;
    }
}
