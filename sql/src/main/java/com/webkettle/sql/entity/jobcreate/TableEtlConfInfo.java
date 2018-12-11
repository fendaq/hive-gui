package com.webkettle.sql.entity.jobcreate;

import java.util.List;

/**
 * 数据表ETL配置详情
 * @author gsk
 */
public class TableEtlConfInfo {

    /**
     * 源表数据库
     */
    private String sourceDatabase;

    /**
     * 源数据表名
     */
    private String sourceTable;

    /**
     * 源数据表中文说明
     */
    private String sourceTableComment;

    /**
     * 数据列配置
     */
    private List<TableEtlColum> colums;

    /**
     * 链表查询配置
     */
    private List<TableEtlJoinConf> joins;

    /**
     * 分组统计配置
     */
    private TableEtlGroupConf group;

    public String getSourceDatabase() {
        return sourceDatabase;
    }

    public void setSourceDatabase(String sourceDatabase) {
        this.sourceDatabase = sourceDatabase;
    }

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public String getSourceTableComment() {
        return sourceTableComment;
    }

    public void setSourceTableComment(String sourceTableComment) {
        this.sourceTableComment = sourceTableComment;
    }

    public List<TableEtlColum> getColums() {
        return colums;
    }

    public void setColums(List<TableEtlColum> colums) {
        this.colums = colums;
    }

    public List<TableEtlJoinConf> getJoins() {
        return joins;
    }

    public void setJoins(List<TableEtlJoinConf> joins) {
        this.joins = joins;
    }

    public TableEtlGroupConf getGroup() {
        return group;
    }

    public void setGroup(TableEtlGroupConf group) {
        this.group = group;
    }
}
