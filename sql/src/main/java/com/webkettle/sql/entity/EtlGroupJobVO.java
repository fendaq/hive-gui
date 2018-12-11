package com.webkettle.sql.entity;

import com.aiyi.core.annotation.po.FieldName;
import com.aiyi.core.annotation.po.ID;
import com.aiyi.core.annotation.po.TableName;
import com.aiyi.core.beans.Po;

/**
 * ETL任务组与任务关联实体
 */
@TableName(name = "ETL_JOB_JOBGROUP")
public class EtlGroupJobVO extends Po {

    /**
     * 关联ID
     */
    @ID
    @FieldName(name = "JOB_JOBGROUP_ID")
    private int jobJobGroupId;

    /**
     * 任务组ID
     */
    @FieldName(name = "JOB_GROUP_ID")
    private int jobGroupId;

    /**
     * 任务ID
     */
    @FieldName(name = "JOB_ID")
    private int jobId;

    /**
     * 关联任务序号(排序及执行顺序)
     */
    @FieldName(name = "ETL_JOB_NUM")
    private int etlJobNum;

    public int getJobJobGroupId() {
        return jobJobGroupId;
    }

    public void setJobJobGroupId(int jobJobGroupId) {
        this.jobJobGroupId = jobJobGroupId;
    }

    public int getJobGroupId() {
        return jobGroupId;
    }

    public void setJobGroupId(int jobGroupId) {
        this.jobGroupId = jobGroupId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getEtlJobNum() {
        return etlJobNum;
    }

    public void setEtlJobNum(int etlJobNum) {
        this.etlJobNum = etlJobNum;
    }
}
