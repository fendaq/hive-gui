package com.gsk.bigdata.service;

import com.webkettle.sql.entity.EtlJobDbVo;
import com.webkettle.sql.entity.EtlJobGroupVO;
import com.webkettle.sql.entity.EtlJobVO;
import com.webkettle.sql.entity.EtlLogVO;
import com.webkettle.sql.entity.result.EtlLogView;
import com.webkettle.sql.entity.result.PageResult;

import java.util.List;

/**
 * 任务业务处理
 * @author gsk
 */
public interface JobService {

    /**
     * 列出临时任务列表(未归纳到任务组中的任务)
     * @return
     */
    List<EtlJobVO> listTempJobs();

    /**
     * 列出所有任务
     * @return
     */
    PageResult<EtlJobVO> listJobs(String jobType, String dbName, String groupId, String tableName,
                                  String hasSuccess, Integer pageNum, Integer pageSize);

    /**
     * 列出所有的任务列表
     * @return
     */
    List<EtlJobDbVo> listAllJobs();

    /**
     * 删除任务
     * @param jobId
     */
    void deleteJob(int jobId);

    /**
     * 保存更改(执行一键更新)
     * @return
     */
    String applyUpdate();

    /**
     * 获取一键更新日志
     * @return
     */
    EtlLogView applyUpdateLogs();

    /**
     * 获取指定任务更新详情
     * @param executeId
     *          执行ID
     * @return
     */
    EtlLogView applyUpdateLogs(String executeId);

    /**
     * 列出当前用户的任务组
     * @return
     */
    PageResult<EtlJobGroupVO> listGroups(String groupName, Integer pageNum, Integer pageSize);

    /**
     * 创建任务组 并使其调度规则生效
     * @param groupVO
     *          任务组实体
     * @return
     */
    EtlJobGroupVO createGroup(EtlJobGroupVO groupVO);

    /**
     * 获取所有任务组列表
     * @return
     */
    List<EtlJobGroupVO> listAllGroups();

    /**
     * 更新任务组
     * @param groupVO
     *          任务组实体
     * @return
     */
    EtlJobGroupVO updateGroup(EtlJobGroupVO groupVO);

    /**
     * 获取任务组详情
     * @param groupId
     *          任务组Id
     * @return
     */
    EtlJobGroupVO getInfo(int groupId);

    /**
     * 更改开关状态
     * @param groupVO
     *          承载开关状态的实体类
     */
    void updateSwitch(EtlJobGroupVO groupVO);

    /**
     * 删除任务
     * @param groupId
     *          任务ID
     */
    void delete(int groupId);

    /**
     * 获取任务日志列表
     * @param pageNum
     *          页码
     * @param pageSize
     *          每页长度
     * @return
     */
    PageResult<EtlLogVO> jobLogs(Integer pageNum, Integer pageSize, String jobType, String dbName, String groupId);

}
