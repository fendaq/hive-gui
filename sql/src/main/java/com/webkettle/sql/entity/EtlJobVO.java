package com.webkettle.sql.entity;

import com.aiyi.core.annotation.po.*;
import com.aiyi.core.beans.Po;
import com.webkettle.core.commons.CommAttr;

import java.util.Date;

/**
 * 任务单元实体
 * @author gsk
 */
@TableName(name = "ETL_JOB")
public class EtlJobVO extends Po {

    /**
     * 任务ID
     */
    @FieldName(name = "JOB_ID")
    @ID
    private int jobId;

    /**
     * 创建时间
     */
    @FieldName(name = "CREAT_TIME")
    @DateTime
    private Date createTime;

    /**
     * 任务类型, SQL = SQL脚本, ETL = ETL配置
     */
    @FieldName(name = "ETL_TYPE")
    private String etlType;

    /**
     * 任务名称
     */
    @FieldName(name = "JOB_NAME")
    private String jobName;

    /**
     * 任务说明
     */
    @FieldName(name = "ETL_COMMENT")
    private String etlComment;

    /**
     * 任务配置
     */
    @FieldName(name = "CONFIG")
    @BigField
    private String config;

    /**
     * 创建用户(用户名)
     */
    @FieldName(name = "CREAT_USER")
    private String createUser;

    /**
     * 更新方式, ALL = 全量, APPEND = 增量
     */
    @FieldName(name = "UPDATE_TYPE")
    private String updateType;

    /**
     * 采集路径
     */
    @FieldName(name = "PATH_NAME")
    private String pathName;

    /**
     * 对应数据表名
     */
    @FieldName(name = "TABLE_NAME")
    private String tableName;

    /**
     * 对应数据库名
     */
    @FieldName(name = "DATABASE_NAME")
    private String dbName;

    /**
     * 执行次数
     */
    @FieldName(name = "EXECUTE_COUNT")
    private int executeCount;

    @TempField
    private String groupName;

    @TempField
    private int groupId;

    @FieldName(name = "JOB_TYPE")
    private String jobType;

    /**
     * 上次执行状态
     */
    @FieldName(name = "LAST_TIME_STATE")
    private String lastTimeState;

    /**
     * 是否删除
     */
    @FieldName(name = "IS_DELETE")
    private String isDelete = CommAttr.BOOL.N;

    /**
     * 更新时间
     */
    @FieldName(name = "UPDATE_TIME")
    @DateTime
    private Date updateTime;

    /**
     * 是否成功执行过
     */
    @FieldName(name = "HAS_SUCCESS")
    private String hasSuccess = "N";

    public String getHasSuccess() {
        return hasSuccess;
    }

    public void setHasSuccess(String hasSuccess) {
        this.hasSuccess = hasSuccess;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getLastTimeState() {
        return lastTimeState;
    }

    public void setLastTimeState(String lastTimeState) {
        this.lastTimeState = lastTimeState;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public int getExecuteCount() {
        return executeCount;
    }

    public void setExecuteCount(int executeCount) {
        this.executeCount = executeCount;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getEtlType() {
        return etlType;
    }

    public void setEtlType(String etlType) {
        this.etlType = etlType;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getEtlComment() {
        return etlComment;
    }

    public void setEtlComment(String etlComment) {
        this.etlComment = etlComment;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getStrJobId(){
        return String.valueOf(jobId);
    }
}
