package com.webkettle.sql.entity.jobcreate;

import java.util.List;

/**
 * 从文件导入到表相关配置
 * @author gsk
 */
public class TableImport {

    /**
     * 动作名称
     */
    private String jobName;

    /**
     * 路径类型
     */
    private String locationType;

    /**
     * 文件标识
     */
    private String fileKey;

    /**
     * 列分割符
     */
    private String splitChar;

    /**
     * 封闭符
     */
    private String wrapChar;

    /**
     * 字符编码
     */
    private String charset;

    /**
     * 是否存回车
     */
    private boolean isSaveEnter;

    /**
     * 是否存表头
     */
    private boolean isSaveHead;

    /**
     * 数据库名称
     */
    private String dbName;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 是否覆盖
     */
    private String loadType;

    /**
     * 表列
     */
    private List<TableColum> colums;



    public List<TableColum> getColums() {
        return colums;
    }

    public void setColums(List<TableColum> colums) {
        this.colums = colums;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getSplitChar() {
        return splitChar;
    }

    public void setSplitChar(String splitChar) {
        this.splitChar = splitChar;
    }

    public String getWrapChar() {
        return wrapChar;
    }

    public void setWrapChar(String wrapChar) {
        this.wrapChar = wrapChar;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public boolean isSaveEnter() {
        return isSaveEnter;
    }

    public void setSaveEnter(boolean saveEnter) {
        isSaveEnter = saveEnter;
    }

    public boolean isSaveHead() {
        return isSaveHead;
    }

    public void setSaveHead(boolean saveHead) {
        isSaveHead = saveHead;
    }

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

    public String getLoadType() {
        return loadType;
    }

    public void setLoadType(String loadType) {
        this.loadType = loadType;
    }
}
