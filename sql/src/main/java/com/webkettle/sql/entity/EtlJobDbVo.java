package com.webkettle.sql.entity;

import java.util.List;

/**
 * 纯显示用的任务分级列表
 */
public class EtlJobDbVo {

    /**
     * 任务类型
     */
    private String type;

    /**
     * 数据库名
     */
    private String etlComment;

    /**
     * 表任务列表
     */
    private List<EtlJobVO> tasks;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<EtlJobVO> getTasks() {
        return tasks;
    }

    public void setTasks(List<EtlJobVO> tasks) {
        this.tasks = tasks;
    }

    public String getEtlComment() {
        return etlComment;
    }

    public void setEtlComment(String etlComment) {
        this.etlComment = etlComment;
    }
}
