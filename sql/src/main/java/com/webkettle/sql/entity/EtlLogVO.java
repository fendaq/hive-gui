package com.webkettle.sql.entity;

import com.aiyi.core.annotation.po.*;
import com.aiyi.core.beans.Po;
import com.aiyi.core.enums.BigFieldType;

import java.util.Date;

/**
 * ETL 执行日志
 */
@TableName(name = "ETL_LOG")
public class EtlLogVO extends Po {

    /**
     * 主键
     */
    @ID
    @FieldName(name = "LOGID")
    private int logId;

    /**
     * 执行ID
     */
    @FieldName(name = "UUID")
    private String uuid;

    /**
     * 任务类型?
     */
    @FieldName(name = "ETL_TYPE")
    private String etlType;

    /**
     * 任务ID
     */
    @FieldName(name = "JOB_ID")
    private int jobId;

    /**
     * 任务组ID
     */
    @FieldName(name = "JOB_GROUP_ID")
    private int jobGroupId;

    /**
     * 开始时间
     */
    @FieldName(name = "START_TIME")
    @DateTime
    private Date startTime;

    /**
     * 结束时间
     */
    @FieldName(name = "END_TIME")
    @DateTime
    private Date endTime;

    /**
     * 执行时间?
     */
    @FieldName(name = "EXECUTE_TIME")
    @DateTime
    private Date executeTime;

    /**
     * 执行状态
     */
    @FieldName(name = "EXECUTE_STATE")
    private String executeState;

    /**
     * 任务耗时
     */
    @TempField
    private double executeTaking;

    /**
     * 更新类型
     */
    @FieldName(name = "UPDATE_TYPE")
    private String updateType;

    /**
     * 分区记录
     */
    @FieldName(name = "PARTITION_TAB")
    private String partitionTab;

    /**
     * 采集文件名
     */
    @FieldName(name = "FILE_NAME")
    private String fileName;

    /**
     * ETL日志
     */
    @BigField(BigFieldType.NCLOB)
    @FieldName(name = "ETL_LOG")
    private String etlLog;

    /**
     * 所属任务组名
     */
    @FieldName(name = "ETL_GROUP_NAME")
    private String groupName;

    @FieldName(name = "TABLE_NAME")
    private String tableName;

    @FieldName(name = "JOB_DISPLAY_NAME")
    private String jobDisplayName;

    public String getJobDisplayName() {
        return jobDisplayName;
    }

    public void setJobDisplayName(String jobDisplayName) {
        this.jobDisplayName = jobDisplayName;
    }

    public double getExecuteTaking() {
        return executeTaking;
    }

    public void setExecuteTaking(double executeTaking) {
        this.executeTaking = executeTaking;
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

    public int isLOGID(){
        return logId;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEtlType() {
        return etlType;
    }

    public void setEtlType(String etlType) {
        this.etlType = etlType;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getJobGroupId() {
        return jobGroupId;
    }

    public void setJobGroupId(int jobGroupId) {
        this.jobGroupId = jobGroupId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public String getExecuteState() {
        return executeState;
    }

    public void setExecuteState(String executeState) {
        this.executeState = executeState;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getPartitionTab() {
        return partitionTab;
    }

    public void setPartitionTab(String partitionTab) {
        this.partitionTab = partitionTab;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getEtlLog() {
        return etlLog;
    }

    public void setEtlLog(String etlLog) {
        this.etlLog = etlLog;
    }
}
