package com.webkettle.sql.service;


import com.webkettle.sql.entity.jobcreate.TableCreatedConf;
import org.quartz.Job;

/**
 * 建表相关接口层
 * @author gsk
 */
public interface TableCreateJobManagerService extends Job {

    /**
     * 保存建表任务
     * @param config
     *          建表配置文本
     */
    int saveConfig(String dbName, TableCreatedConf config);

    /**
     * 执行建表任务
     * @param config
     */
    void doCreate(String config);

    /**
     * 获取任务单元类型
     * @return
     */
    String getJobItemType();

}
