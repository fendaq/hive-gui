package com.gsk.bigdata.service.impl;

import com.aiyi.core.beans.Method;
import com.aiyi.core.beans.WherePrams;
import com.aiyi.core.exception.ServiceInvokeException;
import com.aiyi.core.sql.where.C;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gsk.bigdata.dao.*;
import com.gsk.bigdata.service.DbManagerService;
import com.webkettle.core.commons.CommAttr;
import com.webkettle.core.hive.log.HiveLoggerCollback;
import com.webkettle.core.hive.session.SqlSessionManager;
import com.webkettle.core.utils.ContextUtil;
import com.webkettle.core.utils.UUIDUtil;
import com.webkettle.sql.ETLUtil;
import com.webkettle.sql.SpringContextUtil;
import com.webkettle.sql.SqlCommonExcute;
import com.webkettle.sql.SqlUtil;
import com.webkettle.sql.entity.*;
import com.webkettle.sql.entity.jobcreate.JobExecuteType;
import com.webkettle.sql.entity.jobcreate.TableCreatedConf;
import com.webkettle.sql.entity.jobcreate.TableEtlConf;
import com.webkettle.sql.entity.jobcreate.TableExport;
import com.webkettle.sql.entity.result.SqlAnsyResult;
import com.webkettle.sql.enums.TableCreatedTypeEnum;
import com.webkettle.sql.service.TableCreateJobManagerService;
import com.webkettle.webservice.client.UserDetailsService;
import org.apache.http.client.utils.DateUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.validation.ValidationException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Sql 任务单元管理类
 * @author gsk
 */
@Service("tableCreateSQLJobManagerService")
public class TableCreateSQLJobManagerService implements TableCreateJobManagerService {

    @Resource
    private EtlJobDao etlJobDao;
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private DbManagerService dbManagerService;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public int saveConfig(String dbName, TableCreatedConf config) {
        // 解码sql
        String sql = null;
        try {
            sql = new String(Base64.getDecoder().decode(config.getSql()), Charset.forName("GBK"));
        }catch (Exception e){
            throw new RuntimeException("SQL脚本解码失败");
        }
        List<SqlAnsyResult> sqlAnsyResults = SqlUtil.ansySqls(sql, dbName);
        String tableName = "";
        // TODO 这里二期 进行抽离, 其他执行SQL的地方重复代码太多
        for (SqlAnsyResult sqlAnsyResult: sqlAnsyResults){
            String tableNam = sqlAnsyResult.getTableName();
            if (sqlAnsyResult.getAction().equalsIgnoreCase("DROP")){
                throw new ValidationException("DROP 操作不允许保存为任务");
            }
            if (sqlAnsyResult.getAction().equalsIgnoreCase("CREATE")){
                tableNam = null;
            }
            if (!userDetailsService.checkUserAuthority(ContextUtil.getToken(), sqlAnsyResult.getDbName(),
                    tableNam, sqlAnsyResult.getAction())){
                throw new ValidationException("您没有[" + sqlAnsyResult.getDbName() + "]库" + (null == tableNam ? "的": "中[" +
                        tableNam + "]表的") + "[" + sqlAnsyResult.getAction() + "]权限.");
            }
            // TODO 先不建库, 但保留代码, 可能有用
//            if (sqlAnsyResult.getAction().toUpperCase().equals("CREATE") &&
//                    !StringUtils.isEmpty(sqlAnsyResult.getTableName())){
//                TableVO tableVO = new TableVO();
//                tableVO.setTableName(sqlAnsyResult.getTableName());
//                tableVO.setCreateTime(new Date());
//                tableVO.setDatabaseName(sqlAnsyResult.getDbName());
//                tableVO.setSystemId(0);
//                tableVO.setUpdateId(ContextUtil.getUserId());
//                tableVO.setTableComment(sqlAnsyResult.getTableName());
//                tableVO.setUserId(ContextUtil.getUserId());
//                dbManagerService.addTableToPl(tableVO);
//                userDetailsService.addSysTable(JSON.toJSONString(tableVO));
//            }
        }

        if (StringUtils.isEmpty(tableName)){
            for(SqlAnsyResult result: sqlAnsyResults){
                if (!StringUtils.isEmpty(result.getTableName())){
                    tableName = result.getTableName();
                    break;
                }
            }
        }
        if (StringUtils.isEmpty(tableName)){
            tableName = "自定义SQL任务[" + DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss]");
        }

        String jobName = UUIDUtil.next();
        String jobDisplayName = config.getJobDisplayName();
        if (StringUtils.isEmpty(jobDisplayName)){
            jobDisplayName = "自定义SQL任务[" + DateUtils.formatDate(new Date(), "yyyyMMddHHmmss") + "]";
        }
        // 建表信息入库
        EtlJobVO etlJobVO = new EtlJobVO();
        etlJobVO.setConfig(sql);
        etlJobVO.setCreateTime(new Date());
        etlJobVO.setCreateUser(ContextUtil.getUserName());
        etlJobVO.setEtlComment(jobDisplayName);
        etlJobVO.setJobName(jobName);
        etlJobVO.setUpdateType(CommAttr.SQL.SQL_LOAD_APPEND);
        etlJobVO.setEtlType("SQL");
        etlJobVO.setPathName("/");
        etlJobVO.setTableName(tableName);
        etlJobVO.setDbName(dbName);
        etlJobVO.setJobType(CommAttr.JOB.TYPE.UPDATE);
        etlJobDao.add(etlJobVO);

        return etlJobDao.get(Method.where("JOB_NAME", C.EQ, jobName)).getJobId();
    }

    @Override
    public void doCreate(String config) {

    }

    @Override
    public String getJobItemType() {
        return TableCreatedTypeEnum.ETL.getValue();
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String jobName = jobExecutionContext.getJobDetail().getKey().getName();
        // 获取需要操作的类
        if (null == etlJobDao){
            etlJobDao = SpringContextUtil.getBean(EtlJobDao.class);
        }
        if (null == userDetailsService){
            userDetailsService = SpringContextUtil.getBean(UserDetailsService.class);
        }
        if (null == dbManagerService){
            dbManagerService = SpringContextUtil.getBean(DbManagerService.class);
        }
        ETLJobGroupDao etlJobGroupDao = SpringContextUtil.getBean(ETLJobGroupDao.class);
        EtlGroupJobDao etlGroupJobDao = SpringContextUtil.getBean(EtlGroupJobDao.class);
        EtlJobGroupVO jobGroup = etlJobGroupDao.get(Method.where("JOB_NAME", C.EQ, jobName));

        // 是否可执行
        if (null == jobGroup || CommAttr.BOOL.Y.equalsIgnoreCase(jobGroup.getIsDelete())){
            return;
        }
        if (CommAttr.JOB.SWITCH.STOP.equalsIgnoreCase(jobGroup.getJobSwitch())){
            return;
        }
        if (null != jobGroup.getExecuteTimeStart()){
            long time = jobGroup.getExecuteTimeStart().getTime();
            if (time > System.currentTimeMillis()){
                return;
            }
        }
        if (null != jobGroup.getExecuteTimeEnd()){
            long time = jobGroup.getExecuteTimeEnd().getTime();
            if (time < System.currentTimeMillis()){
                return;
            }
        }
        if (null == jobGroup.getExecuteHzContent()){
            return;
        }
        JobExecuteType jobType = JSON.parseObject(jobGroup.getExecuteHzContent(), JobExecuteType.class);
        logger.info("开始执行任务组{}", JSON.toJSONString(jobGroup));
        // 设置当前任务组状态
        jobGroup.setLastTimeState(CommAttr.JOB.STATE.RUNING);
        jobGroup.setLastTime(new Date());
        jobGroup.setExecuteCount(jobGroup.getExecuteCount() + 1);
        etlJobGroupDao.update(jobGroup);

        //获取任务列表
        List<Map<String, Object>> fieldList = etlGroupJobDao.listBySql("SELECT JOB_ID FROM ETL_JOB_JOBGROUP WHERE JOB_GROUP_ID = '"
                + jobGroup.getJobGroupId() + "' ORDER BY ETL_JOB_NUM ASC");
        logger.info("获得组内任务列表{}", JSON.toJSONString(fieldList));

        // 根据ID查出实体
        List<EtlJobVO> etlJobVOS = new LinkedList<>();
        //序列化任务列表ID
        for (int i = 0; i < fieldList.size(); i ++){
            Serializable jobId = (Serializable)fieldList.get(i).get("JOB_ID");
            EtlJobVO jobVO = etlJobDao.get(Method.where("JOB_ID", C.EQ, jobId));
            if (null != jobVO){
                etlJobVOS.add(jobVO);
            }
        }
        String executeId = UUIDUtil.next();
        try {
            excuteThread(executeId, etlJobVOS, jobGroup.getJobGroupName(), jobGroup.getJobGroupId(),
                    jobGroup.getRetryNum(), jobGroup.getRetryHz());
            logger.info("任务组执行完毕, executeId = ", executeId);
            jobGroup.setLastTimeState(CommAttr.JOB.STATE.SUCCESS);
            setLog("任务组:" + jobGroup.getJobGroupName() + "执行完毕!", jobGroup.getJobGroupId(), 0,
                    jobGroup.getJobGroupName(), "", "", "UPDATE",
                    CommAttr.JOB.STATE.SUCCESS, executeId,null);
        }catch (Exception e){
            jobGroup.setLastTimeState(CommAttr.JOB.STATE.FAIL);
            logger.info("任务组执行失败, executeId = ", executeId, e);
            setLog("任务组:" + jobGroup.getJobGroupName() + "执行失败!", jobGroup.getJobGroupId(), 0,
                    jobGroup.getJobGroupName(), "", "", "UPDATE",
                    CommAttr.JOB.STATE.FAIL, executeId, null);
            e.printStackTrace();
        }
        if (CommAttr.JOB.JOB_EXE_TYPE_ONE.equalsIgnoreCase(jobType.getType())){
            jobGroup.setJobSwitch(CommAttr.JOB.SWITCH.STOP);
            try {
                jobExecutionContext.getScheduler().deleteJob(jobExecutionContext.getJobDetail().getKey());
            }catch (SchedulerException e){
                e.printStackTrace();
            }
        }
        etlJobGroupDao.update(jobGroup);
    }

    private String excuteThread(String excuteId, List<EtlJobVO> etlJobVOS, String groupName, int groupId, int maxUpdateCount, int maxWattingTime){
        for(EtlJobVO etlJobVO: etlJobVOS){
            if (CommAttr.BOOL.Y.equalsIgnoreCase(etlJobVO.getIsDelete())){
                continue;
            }
            logger.info("开始执行任务, excuteId={},详情={}", excuteId, JSON.toJSONString(etlJobVO));
            etlJobVO.setUpdateTime(new Date());
            etlJobVO.setExecuteCount(etlJobVO.getExecuteCount() + 1);
            etlJobDao.update(etlJobVO);
            try {
                excuteJobItem(excuteId, etlJobVO, groupName, groupId, maxUpdateCount, maxWattingTime, 0);
            }catch (Exception e){
                etlJobVO.setLastTimeState(CommAttr.JOB.STATE.FAIL);
                etlJobDao.update(etlJobVO);
                throw new RuntimeException("任务[" + etlJobVO.getEtlComment() + "]执行失败", e);
            }
            etlJobVO.setHasSuccess(CommAttr.BOOL.Y);
            etlJobVO.setLastTimeState(CommAttr.JOB.STATE.SUCCESS);
            etlJobDao.update(etlJobVO);

        }
        return excuteId;

    }

    /**
     * 更新单个任务
     * @param etlJobVO
     */
    private void excuteJobItem(String excuteId, EtlJobVO etlJobVO, String gropuName, int groupId, int maxUpdateCount,
                               int maxWattingTime, int updateCount){
        SqlCommonExcute sqlCommonExcute = SpringContextUtil.getBean(SqlCommonExcute.class);
        if (StringUtils.isEmpty(etlJobVO.getJobType())){
            return;
        }

        // 载入任务对应用户信息
        try {
            JSONObject jsonObject = JSON.parseObject(userDetailsService.loadUserByUsername(etlJobVO.getCreateUser()))
                    .getJSONObject("sysUser");
            ContextUtil.setUserId(jsonObject.getLong("userId"));
        }catch (Exception e){
            throw new ServiceInvokeException("远程服务调用失败", e);
        }

        // 导出
        setLog("任务:" + etlJobVO.getEtlComment() + "开始执行", groupId, etlJobVO.getJobId(),
                gropuName, etlJobVO.getTableName(), etlJobVO.getUpdateType(), etlJobVO.getEtlType(),
                CommAttr.JOB.STATE.RUNING, excuteId, etlJobVO.getEtlComment());
        if (etlJobVO.getJobType().equalsIgnoreCase(CommAttr.JOB.TYPE.EXPORT)){
            TableExport tableExport = JSON.parseObject(etlJobVO.getConfig(), TableExport.class);
            sqlCommonExcute.exportToHdfs(excuteId, etlJobVO.getDbName(), etlJobVO.getTableName(),
                    etlJobVO.getPathName(), tableExport.getSplitChar(), new HiveLoggerCollback() {
                        @Override
                        public void call(String log, String logType, String logStatus, String excuteId) {
                            if (logStatus.equalsIgnoreCase(CommAttr.HIVE.LOG_STATUS.FAIL)){
                                setLog("任务执行失败:" + log, groupId, etlJobVO.getJobId(), gropuName,
                                        etlJobVO.getTableName(),  etlJobVO.getUpdateType(), etlJobVO.getEtlType(),
                                        CommAttr.JOB.STATE.FAIL, excuteId, etlJobVO.getEtlComment());
                                throw new ServiceInvokeException("Hive操作失败:" + log);
                            }else{
                                setLog(log, groupId, etlJobVO.getJobId(), gropuName,
                                        etlJobVO.getTableName(),  etlJobVO.getUpdateType(), etlJobVO.getEtlType(),
                                        CommAttr.JOB.STATE.RUNING, excuteId, etlJobVO.getEtlComment());
                            }

                        }
                    });
        }else if(etlJobVO.getJobType().equalsIgnoreCase(CommAttr.JOB.TYPE.UPDATE)){
            if (etlJobVO.getEtlType().equalsIgnoreCase(CommAttr.ETL.TYPE.SQL)){
                // 自定义SQL
                if (!StringUtils.isEmpty(etlJobVO.getDbName())){
                    excuteSql("USE " + etlJobVO.getDbName() + ";\n" + etlJobVO.getConfig(), groupId,
                            etlJobVO.getJobId(), gropuName, etlJobVO.getTableName(), etlJobVO.getUpdateType(),
                            etlJobVO.getEtlType(), excuteId, etlJobVO.getEtlComment());
                }
            }else if(etlJobVO.getEtlType().equalsIgnoreCase(CommAttr.ETL.TYPE.ETL)){
                // ETL
                List<String> sqlList = ETLUtil.conf2Sql(JSON.parseObject(etlJobVO.getConfig(), TableEtlConf.class));
                for(String sql: sqlList){
                    excuteSql(sql, groupId, etlJobVO.getJobId(), etlJobVO.getUpdateType(),gropuName,
                            etlJobVO.getTableName(), etlJobVO.getEtlType(), excuteId, etlJobVO.getEtlComment());
                }
            }

        }else if(etlJobVO.getJobType().equalsIgnoreCase(CommAttr.JOB.TYPE.COLLECT)){
            // 内部采集任务 TODO 稍后
        }

        setLog("任务:" + etlJobVO.getEtlComment() + "执行完毕", groupId, etlJobVO.getJobId(), gropuName,
                etlJobVO.getTableName(), etlJobVO.getUpdateType(), etlJobVO.getEtlType(),
                CommAttr.JOB.STATE.SUCCESS, excuteId, etlJobVO.getEtlComment());
    }

    /**
     * 执行SQL并记录日志
     * @param sql
     *          SQL脚本
     * @param groupId
     *          任务组ID
     * @param jobId
     *          任务ID
     * @param updateType
     *          更新方式
     * @param etlType
     *          任务类型
     * @param excuteId
     *          执行ID
     */
    private void excuteSql(String sql, int groupId, int jobId, String groupName, String tableName, String updateType,
                           String etlType, String excuteId, String displayName){
        SqlSessionManager sqlSessionManager = SpringContextUtil.getBean(SqlSessionManager.class);
        TableVO collbackTableVo = new TableVO();
        sqlSessionManager.excSql(excuteId, new HiveLoggerCollback() {
            @Override
            public void call(String log, String logType, String logStatus, String excuteId) {
                // TODO 这里二期 进行抽离, 其他执行SQL的地方重复代码太多

                if (logStatus.equalsIgnoreCase(CommAttr.HIVE.LOG_STATUS.START) ||
                        logStatus.equalsIgnoreCase(CommAttr.HIVE.LOG_STATUS.RUNING)){
                    setLog(log, groupId, jobId, groupName, tableName, updateType, etlType,
                            CommAttr.JOB.STATE.RUNING, excuteId,displayName);

                }
                if (logStatus.equalsIgnoreCase(CommAttr.HIVE.LOG_STATUS.SUCCESS)){
                    if (logType.equalsIgnoreCase(CommAttr.HIVE.LOG_TYPE.SQL)) {
                        String thisSql = log.trim().toUpperCase().replace("IF NOT EXISTS", "")
                                .replace("(", " (");
                        if (thisSql.indexOf("USE") == 0) {
                            collbackTableVo.setDatabaseName(thisSql.substring(3).trim().toUpperCase()
                                    .split(" ")[0].replace(";", ""));
                        }
                        if (thisSql.indexOf("CREATE DATABASE") == 0) {
                            String localDbName = thisSql.substring("CREATE DATABASE".length())
                                    .trim().toUpperCase().split(" ")[0].trim().replace(";", "");
                            createDb(localDbName, "APPLICATION", ContextUtil.getUserId());
                        }
                        if (thisSql.indexOf("DROP DATABASE") == 0){
                            String localDbName = thisSql.substring("DROP DATABASE".length())
                                    .trim().toUpperCase().split(" ")[0].trim().replace(";", "");
                            DatabaseVO database = dbManagerService.getDatabaseByName(localDbName);
                            if (null != database){
                                userDetailsService.delDataBase(database.getDatabaseId());
                            }
                        }
                        if (thisSql.indexOf("CREATE TABLE") == 0) {
                            String localTableName = thisSql.substring("CREATE TABLE".length())
                                    .trim().toUpperCase().split(" ")[0];
                            int dbspindex = localTableName.indexOf(".");
                            String localDbName = collbackTableVo.getDatabaseName();
                            if (dbspindex != -1) {
                                localDbName = localTableName.substring(0, dbspindex);
                                localTableName = localTableName.substring(dbspindex + 1);
                            }
                            createTable(localDbName, localTableName, ContextUtil.getUserId());
                        }
                        if (thisSql.indexOf("DROP TABLE") == 0){
                            String localTableName = thisSql.substring("DROP TABLE".length())
                                    .trim().toUpperCase().split(" ")[0];
                            TableVO tableVO = null;
                            String localDbName = collbackTableVo.getDatabaseName();
                            int dbspindex = localTableName.indexOf(".");
                            if (dbspindex != -1) {
                                localDbName = localTableName.substring(0, dbspindex);
                                localTableName = localTableName.substring(dbspindex + 1);
                            }
                            tableVO = dbManagerService.getTableByName(localDbName, localTableName);
                            if (null != tableVO){
                                userDetailsService.delSysTable(tableVO.getTableId());
                            }
                        }
                    }
                }else if (logStatus.equalsIgnoreCase(CommAttr.HIVE.LOG_STATUS.FAIL)){
                    setLog("任务执行失败:" + log, groupId, jobId, groupName, tableName, updateType, etlType,
                            CommAttr.JOB.STATE.FAIL, excuteId,displayName);
                    throw new ServiceInvokeException("任务执行失败:" + log);
                }
            }
        }, sql);
    }

    /**
     * 建库信息同步到关系型数据库
     * @param dbName
     *          数据库名
     * @param groupKey
     *          数据库组
     * @param userId
     *          用户ID
     */
    private void createDb (String dbName, String groupKey, long userId){
        DatabaseVO databaseVO = new DatabaseVO();
        databaseVO.setDatabaseName(dbName);
        databaseVO.setGroupKey(groupKey);
        databaseVO.setUserId(userId);
        dbManagerService.addDatabaseToPl(databaseVO);
    }

    /**
     * 建表信息同步至关系型数据库
     * @param dbName
     *          数据库名
     * @param tableName
     *          数据表名
     * @param userId
     *          用户ID
     */
    private void createTable(String dbName, String tableName, long userId){
        TableVO tableVO = new TableVO();
        tableVO.setTableName(tableName);
        tableVO.setTableComment(tableName);
        tableVO.setDatabaseName(dbName);
        tableVO.setUserId(userId);
        tableVO.setUpdateId(userId);
        dbManagerService.addTableToPl(tableVO);
    }


    // TODO 这里二期 进行抽离, 其他执行SQL的地方重复代码太多
    private void setLog(String log, int groupId, int jobId, String groupName, String tableName,
                        String updateType, String etlType, String state, String excuteId, String displayName){

        EtlLogDao etlLogDao = SpringContextUtil.getBean(EtlLogDao.class);
        log = log.replace("'", "\\'");
        WherePrams where = Method.where("UUID", C.EQ, excuteId).and("JOB_GROUP_ID", C.EQ, groupId)
                .and("JOB_ID", C.EQ, jobId);
        EtlLogVO etlLog = etlLogDao.get(where);
        if (null == etlLog){
            etlLog = new EtlLogVO();
            etlLog.setJobGroupId(groupId);
            etlLog.setJobId(jobId);
            etlLog.setUuid(excuteId);
            etlLog.setEtlLog(log);
            etlLog.setExecuteState(state);
            etlLog.setUpdateType(updateType);
            etlLog.setEtlType(etlType);
            etlLog.setStartTime(new Date());
            etlLog.setExecuteTime(new Date());
            etlLog.setGroupName(groupName);
            etlLog.setTableName(tableName);
            etlLog.setJobDisplayName(displayName);
            etlLogDao.add(etlLog);
        }else{
            etlLog.setEtlLog((etlLog.getEtlLog().replace("'", "\\'")) + "\n" + log);
            etlLog.setExecuteState(state);
            etlLog.setUpdateType(updateType);
            etlLog.setEtlType(etlType);
            etlLog.setEndTime(new Date());
            etlLog.setGroupName(groupName);
            etlLog.setTableName(tableName);
            etlLog.setJobDisplayName(displayName);
            etlLogDao.update(etlLog);
        }
    }
}
