package com.webkettle.sql.entity;

import com.aiyi.core.annotation.po.*;
import com.aiyi.core.beans.Po;
import com.alibaba.fastjson.JSON;
import com.webkettle.core.commons.CommAttr;
import com.webkettle.sql.entity.jobcreate.JobExecuteType;

import java.util.Date;
import java.util.List;

/**
 * 任务组实体
 * @author gsk
 */
@TableName(name = "ETL_JOB_GROUP")
public class EtlJobGroupVO extends Po {

    @ID
    @FieldName(name = "JOB_GROUP_ID")
    private int jobGroupId;

    /**
     * 任务框架所需参数
     */
    @FieldName(name = "SCHED_NAME")
    private String schedName;
    /**
     * 任务框架所需参数
     */
    @FieldName(name = "JOB_NAME")
    private String jobName;

    /**
     * 任务框架所需参数
     */
    @FieldName(name = "JOB_GROUP")
    private String jobGroup;

    /**
     * 创建时间
     */
    @FieldName(name = "CREAT_TIME")
    @DateTime
    private Date creatTime;

    /**
     * 任务组名称
     */
    @FieldName(name = "JOB_GROUP_NAME")
    private String jobGroupName;

    /**
     * 任务组描述
     */
    @FieldName(name = "ETL_COMMENT")
    private String etlComment;

    /**
     * 该组任务已执行次数
     */
    @FieldName(name = "EXECUTE_COUNT")
    private int executeCount;

    /**
     * 执行频率配置(频率)
     */
    @FieldName(name = "EXECUTE_HZ")
    private String executeHz;

    /**
     * 执行规则高级规则配置
     */
    @FieldName(name = "EXECUTE_HZ_CONTENT")
    private String executeHzContent;

    /**
     * 执行高级规则配置实体
     */
    @TempField
    private JobExecuteType executeHzContentEntity;

    /**
     * 任务组结束时间
     */
    @FieldName(name = "EXECUTE_TIME_END")
    @DateTime
    private Date executeTimeEnd;

    /**
     * 任务开始时间
     */
    @FieldName(name = "EXECUTE_TIME_START")
    @DateTime
    private Date executeTimeStart;

    /**
     * 任务执行前置条件
     */
    @FieldName(name = "START_CONDITION")
    private String startCondition;

    /**
     * 未满足条件时最长等待时间
     */
    @FieldName(name = "START_CONDITION_MAX_WAIT")
    private String startConditionMaxWait;

    /**
     * 上次执行时间
     */
    @FieldName(name = "LAST_TIME")
    @DateTime
    private Date lastTime;

    /**
     * 上次执行状态
     */
    @FieldName(name = "LAST_TIME_STATE")
    private String lastTimeState;

    /**
     * 任务失败时重试次数
     */
    @FieldName(name = "RETRY_NUM")
    private int retryNum;

    @FieldName(name = "RETRY_HZ")
    private int retryHz;

    /**
     * 是否删除
     */
    @FieldName(name = "IS_DELETE")
    private String isDelete;

    /**
     * 任务创建人
     */
    @FieldName(name = "CREAT_USER")
    private String creatUser;

    /**
     * 任务开关
     */
    @FieldName(name = "JOB_SWITCH")
    private String jobSwitch = CommAttr.JOB.SWITCH.RUN;

    /**
     * 任务列表(临时字段, 详情或创建或修改任务时有)
     */
    @TempField
    private List<EtlJobVO> jobs;

    public String getJobSwitch() {
        return jobSwitch;
    }

    public void setJobSwitch(String jobSwitch) {
        this.jobSwitch = jobSwitch;
    }

    public int getRetryHz() {
        return retryHz;
    }

    public void setRetryHz(int retryHz) {
        this.retryHz = retryHz;
    }

    public JobExecuteType getExecuteHzContentEntity() {
        if (null == executeHzContentEntity && null != executeHzContent && !"".equals(executeHzContent.trim())){
            executeHzContentEntity = JSON.parseObject(executeHzContent, JobExecuteType.class);
        }
        return executeHzContentEntity;
    }

    public void setExecuteHzContentEntity(JobExecuteType executeHzContentEntity) {
        this.executeHzContentEntity = executeHzContentEntity;
    }

    public Date getExecuteTimeStart() {
        return executeTimeStart;
    }

    public void setExecuteTimeStart(Date executeTimeStart) {
        this.executeTimeStart = executeTimeStart;
    }

    public int getJobGroupId() {
        return jobGroupId;
    }

    public void setJobGroupId(int jobGroupId) {
        this.jobGroupId = jobGroupId;
    }

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public String getJobGroupName() {
        return jobGroupName;
    }

    public void setJobGroupName(String jobGroupName) {
        this.jobGroupName = jobGroupName;
    }

    public String getEtlComment() {
        return etlComment;
    }

    public void setEtlComment(String etlComment) {
        this.etlComment = etlComment;
    }

    public int getExecuteCount() {
        return executeCount;
    }

    public void setExecuteCount(int executeCount) {
        this.executeCount = executeCount;
    }

    public String getExecuteHz() {
        return executeHz;
    }

    public void setExecuteHz(String executeHz) {
        this.executeHz = executeHz;
    }

    public String getExecuteHzContent() {
        return executeHzContent;
    }

    public void setExecuteHzContent(String executeHzContent) {
        this.executeHzContent = executeHzContent;
    }

    public Date getExecuteTimeEnd() {
        return executeTimeEnd;
    }

    public void setExecuteTimeEnd(Date executeTimeEnd) {
        this.executeTimeEnd = executeTimeEnd;
    }

    public String getStartCondition() {
        return startCondition;
    }

    public void setStartCondition(String startCondition) {
        this.startCondition = startCondition;
    }

    public String getStartConditionMaxWait() {
        return startConditionMaxWait;
    }

    public void setStartConditionMaxWait(String startConditionMaxWait) {
        this.startConditionMaxWait = startConditionMaxWait;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public String getLastTimeState() {
        return lastTimeState;
    }

    public void setLastTimeState(String lastTimeState) {
        this.lastTimeState = lastTimeState;
    }

    public int getRetryNum() {
        return retryNum;
    }

    public void setRetryNum(int retryNum) {
        this.retryNum = retryNum;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreatUser() {
        return creatUser;
    }

    public void setCreatUser(String creatUser) {
        this.creatUser = creatUser;
    }

    public List<EtlJobVO> getJobs() {
        return jobs;
    }

    public void setJobs(List<EtlJobVO> jobs) {
        this.jobs = jobs;
    }
}
