package com.webkettle.core.entity;

/**
 * 采集表配置实体
 * @author gsk
 */
public class CollectionProp {

    private String dbName;

    private String tableName;

    private String type;

    private String typeDscp;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
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

    public String getTypeDscp() {
        return typeDscp;
    }

    public void setTypeDscp(String typeDscp) {
        this.typeDscp = typeDscp;
    }
}
