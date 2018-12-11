package com.webkettle.sql.entity;

import com.aiyi.core.annotation.po.FieldName;
import com.aiyi.core.annotation.po.ID;
import com.aiyi.core.annotation.po.TableName;
import com.aiyi.core.annotation.po.TempField;
import com.aiyi.core.beans.Po;

import java.util.Date;
import java.util.List;

/**
 * 数据库信息记录实体
 * @author gsk
 */
@TableName(name = "SYS_DATABASE")
public class DatabaseVO extends Po {

    /**
     * 数据库ID
     */
    @ID
    @FieldName(name = "DATABASEID")
    private long databaseId;

    /**
     * 数据库名称
     */
    @FieldName(name = "DATABASENAME")
    private String databaseName;

    /**
     * 数据库描述
     */
    @FieldName(name = "DATABASECOMMENT")
    private String databaseComment;

    /**
     * 创建人ID
     */
    @FieldName(name = "USERID")
    private long userId;

    /**
     * 创建时间
     */
    @FieldName(name = "CREATTIME")
    private Date createTime;

    /**
     * 数据库分组
     */
    @FieldName(name = "DB_GROUP")
    private String dbGroup = "SOURCE";

    @FieldName(name = "SYSTEMID")
    private long systemId = 0;

    @TempField
    private String groupKey;

    @TempField
    private String action;

    @TempField
    private String databasetype;

    @TempField
    private long id;

    @TempField
    private String name;
    @TempField
    private List<?> list;
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatetime(Date createtime){
        this.createTime = createtime;
    }

    public Date getCreatetime(){
        return this.createTime;
    }

    public String getDatabasetype() {
        if (null == databasetype){
            return dbGroup;
        }
        return databasetype;
    }

    public void setDatabasetype(String databaseType) {
        this.databasetype = databaseType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getSystemId() {
        return systemId;
    }

    public void setSystemId(long systemId) {
        this.systemId = systemId;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public String getDbGroup() {
        if (null == dbGroup){
            return databasetype;
        }
        return dbGroup;
    }

    public void setDbGroup(String dbGroup) {
        this.dbGroup = dbGroup;
    }

    public long getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(long databaseId) {
        this.databaseId = databaseId;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseComment() {
        return databaseComment;
    }

    public void setDatabaseComment(String databaseComment) {
        this.databaseComment = databaseComment;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
