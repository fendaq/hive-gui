package com.gsk.bigdata.web;

import com.aiyi.core.beans.ResultBean;
import com.gsk.bigdata.service.JobService;
import com.webkettle.sql.entity.EtlJobGroupVO;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/job")
public class ApiJobController {

    @Resource
    private JobService jobService;

    @InitBinder
    public void intDate(WebDataBinder dataBinder){
        dataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 应用更新, 执行非自定义任务组内的所有任务操作.
     * @return
     */
    @PostMapping("apply_update")
    public ResultBean applyUpdate(){
        return ResultBean.success("一键更新已启动").putResponseBody("executeId", jobService.applyUpdate());
    }

    /**
     * 获取任务执行详情
     * @return
     */
    @GetMapping("apply_update")
    public ResultBean getApplyUpdateInfo(){
        return ResultBean.success("任务详情获取成功").setResponseBody(jobService.applyUpdateLogs());
    }

    /**
     * 获取任务执行详情
     * @return
     */
    @GetMapping("apply_update/{executeId}")
    public ResultBean getApplyUpdateInfo(@PathVariable("executeId") String executeId){
        return ResultBean.success("任务详情获取成功").setResponseBody(jobService.applyUpdateLogs(executeId));
    }

    /**
     * 列出所有任务列表
     * @return
     */
    @GetMapping("jobs/search")
    public ResultBean searchJobs(String jobType, String dbName, String groupId, String tableName,
                                String hasSuccess, Integer pageNum, Integer pageSize){
        return ResultBean.success("任务列表获取成功").putResponseBody("list", jobService
                .listJobs(jobType, dbName, groupId, tableName, hasSuccess, pageNum, pageSize));
    }

    /**
     * 获取所有的任务列表
     * @return
     */
    @GetMapping("jobs")
    public ResultBean jobs(){
        return ResultBean.success("任务列表获取成功").putResponseBody("list", jobService.listAllJobs());
    }

    /**
     * 删除任务
     * @return
     */
    @PostMapping("jobs/{jobId}/delete")
    public ResultBean delete(@PathVariable("jobId") Integer jobId){
        jobService.deleteJob(jobId);
        return ResultBean.success("任务删除成功");
    }

    /**
     * 分页条件查询任务组列表
     * @return
     */
    @GetMapping("groups")
    public ResultBean listGroup(String groupName, Integer pageNum, Integer pageSize){
        return ResultBean.success("任务组列表获取成功")
                .putResponseBody("list", jobService.listGroups(groupName, pageNum, pageSize));
    }

    /**
     * 列出所有任务组列表
     * @return
     */
    @GetMapping("groups/all")
    public ResultBean listAllGroups(){
        return ResultBean.success("任务组列表获取成功")
                .putResponseBody("list", jobService.listAllGroups());
    }

    /**
     * 添加任务组
     * @param jobGroupVO
     *          任务组实体
     * @return
     */
    @PostMapping("groups")
    public ResultBean createGroup(@RequestBody EtlJobGroupVO jobGroupVO){
        return ResultBean.success("任务组创建成功").setResponseBody(jobService.createGroup(jobGroupVO));
    }

    /**
     * 编辑任务组
     * @param jobGroupVO
     *          任务组实体
     * @return
     */
    @PutMapping("groups")
    public ResultBean updateGroup(@RequestBody EtlJobGroupVO jobGroupVO){
        return ResultBean.success("任务组修改成功").setResponseBody(jobService.updateGroup(jobGroupVO));
    }

    /**
     * 获取任务组详情
     * @param groupId
     *          任务组ID
     * @return
     */
    @GetMapping("groups/{groupId}")
    public ResultBean getGroup(@PathVariable int groupId){
        return ResultBean.success("任务详情后去成功").setResponseBody(jobService.getInfo(groupId));
    }

    /**
     * 更改任务组开关状态
     * @param groupId
     * @param jobGroupVO
     * @return
     */
    @PostMapping("groups/{groupId}/switch")
    public ResultBean updateSwitch(@PathVariable("groupId") int groupId,
                                   @RequestBody EtlJobGroupVO jobGroupVO){
        jobGroupVO.setJobGroupId(groupId);
        jobService.updateSwitch(jobGroupVO);
        return ResultBean.success("开关状态更改成功");
    }


    /**
     * 删除任务
     * @param groupId
     *          任务ID
     * @return
     */
    @PostMapping("groups/{groupId}/delete")
    public ResultBean delete(@PathVariable("groupId") int groupId){
        jobService.delete(groupId);
        return ResultBean.success("任务删除成功");
    }

    /**
     * 获取任务日志列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("jobs/logs")
    public ResultBean joblogs(Integer pageNum, Integer pageSize,
                              String jobType, String dbName, String groupId){
        return ResultBean.success("任务日志获取成功")
                .putResponseBody("list", jobService.jobLogs(pageNum, pageSize, jobType, dbName, groupId));
    }

}
