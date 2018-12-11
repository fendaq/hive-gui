package com.webkettle.sql.entity.jobcreate;

/**
 * 表格导出参数
 */
public class TableExport {

    /**
     * 任务名, 可空
     */
    private String jobName;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 分隔符
     */
    private String splitChar;

    /**
     * 后缀名
     */
    private String suffix;

    private int jobId;

    /**
     * 是否建立任务
     */
    private String newJob;

    public String getNewJob() {
        return newJob;
    }

    public void setNewJob(String newJob) {
        this.newJob = newJob;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSplitChar() {
        return splitChar;
    }

    public void setSplitChar(String splitChar) {
        this.splitChar = splitChar;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
