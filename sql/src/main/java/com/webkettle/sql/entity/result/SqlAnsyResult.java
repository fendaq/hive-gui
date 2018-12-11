package com.webkettle.sql.entity.result;

/**
 * 最终SQL分析结果
 * @author gsk
 */
public class SqlAnsyResult {

    /**
     * 操作对象
     */
    private String action;

    /**
     * 操作库名
     */
    private String dbName;

    /**
     * 操作表名
     */
    private String tableName;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        if (null != action){
            this.action = action.trim();
        }
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        if (null != dbName){
            dbName = dbName.trim().toUpperCase();
        }
        this.dbName = dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        if (null != tableName){
            tableName = tableName.trim().toUpperCase();
        }
        this.tableName = tableName;
    }
}
