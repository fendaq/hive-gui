package com.webkettle.sql.entity;

import com.aiyi.core.annotation.po.FieldName;
import com.aiyi.core.annotation.po.ID;
import com.aiyi.core.annotation.po.TableName;
import com.aiyi.core.beans.Po;

import java.util.Date;

/**
 * 数据库表格对象
 * @author gsk
 */
@TableName(name = "SYS_TABLE")
public class TableVO extends Po {

    /**
     * 表ID
     */
    @ID
    @FieldName(name = "TABLEID")
    private long tableId;

    /**
     * 表名
     */
    @FieldName(name = "TABLENAME")
    private String tableName;

    /**
     * 秒说明
     */
    @FieldName(name = "TABLECOMMENT")
    private String tableComment;

    /**
     * 所属数据库名
     */
    @FieldName(name = "DATABASENAME")
    private String databaseName;

    /**
     * 创建Id
     */
    @FieldName(name = "USERID")
    private long userId;

    /**
     * 最后修改人Id
     */
    @FieldName(name = "UPDATEID")
    private long updateId;

    /**
     * 创建时间
     */
    @FieldName(name = "CREATTIME")
    private Date createTime;

    /**
     * 所属系统Id
     */
    @FieldName(name = "SYSTEMID")
    private long systemId;

    @FieldName(name = "UPDATETIME")
    private Date updateTime;

    /**
     * 创建用户名
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUpdateTime() {
        if (null == updateTime){
            updateTime = new Date();
        }
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getUpdatetime() {
        if (null == updateTime){
            updateTime = new Date();
        }
        return updateTime;
    }

    public void setUpdatetime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(long updateId) {
        this.updateId = updateId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getSystemId() {
        return systemId;
    }

    public void setSystemId(long systemId) {
        this.systemId = systemId;
    }
}
