package com.gsk.bigdata.service.impl;

import com.aiyi.core.beans.Method;
import com.aiyi.core.beans.WherePrams;
import com.aiyi.core.exception.ServiceInvokeException;
import com.aiyi.core.sql.where.C;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gsk.bigdata.dao.*;
import com.gsk.bigdata.service.DbManagerService;
import com.gsk.bigdata.service.JobService;
import com.webkettle.core.commons.CommAttr;
import com.webkettle.core.hive.log.HiveLoggerCollback;
import com.webkettle.core.hive.session.SqlSessionManager;
import com.webkettle.core.utils.ContextUtil;
import com.webkettle.core.utils.UUIDUtil;
import com.webkettle.sql.*;
import com.webkettle.sql.entity.*;
import com.webkettle.sql.entity.jobcreate.JobExecuteType;
import com.webkettle.sql.entity.jobcreate.TableEtlConf;
import com.webkettle.sql.entity.jobcreate.TableExport;
import com.webkettle.sql.entity.result.EtlLogView;
import com.webkettle.sql.entity.result.PageResult;
import com.webkettle.webservice.client.UserDetailsService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.validation.ValidationException;
import java.util.*;

/**
 * 任务业务处理实现层
 * @author gsk
 */
@Service
public class JobServiceImpl implements JobService {

    @Resource
    private ETLJobGroupDao etlJobGroupDao;
    @Resource
    private EtlJobDao etlJobDao;
    @Resource
    private SqlCommonExcute sqlCommonExcute;
    @Resource
    private SqlSessionManager sqlSessionManager;
    @Resource
    private EtlLogDao etlLogDao;
    @Resource
    private EtlGroupJobDao etlGroupJobDao;
    @Resource
    private DbManagerService dbManagerService;
    @Resource
    private UserDetailsService userDetailsService;

    @Resource @Qualifier("Scheduler")
    private Scheduler scheduler;

    private Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

    /**
     * 是否正在执行一键更新
     */
    private boolean updateIng = false;
    /**
     * 一键更新当前重试次数
     */
    private int updateCount = 0;
    /**
     * 一键更新的执行ID
     */
    private String updateExecuteId = null;

    /**
     * 一键更新当前执行的任务
     */
    private EtlJobVO nowEtlJobVO;

    @Override
    public List<EtlJobVO> listTempJobs() {
//        List<EtlJobVO> etlJobVOS = etlJobDao.listAll();
//        logger.info("获取到所有任务, 数量:[{}]", etlJobVOS.size());
//
//        List<TableVO> tables = new ArrayList<>();
//        List<DatabaseVO> databaseVOS = sqlCommonExcute.listDatabase();
//        Map<String, DatabaseVO> dataBaseMap = new HashMap<>();
//        for(DatabaseVO databaseVO: databaseVOS){
//            dataBaseMap.put(databaseVO.getDatabaseName().toUpperCase(), databaseVO);
//        }
//
//        Iterator<EtlJobVO> iterator = etlJobVOS.iterator();
//        while (iterator.hasNext()) {
//            EtlJobVO jobVO = iterator.next();
//            if (jobVO.getExecuteCount() > 0 && CommAttr.JOB.STATE.SUCCESS.equalsIgnoreCase(jobVO.getLastTimeState())){
//                iterator.remove();
//                continue;
//            }
//            if (!StringUtils.isEmpty(jobVO.getDbName())){
//                DatabaseVO databaseVO = dataBaseMap.get(jobVO.getDbName().toUpperCase());
//                logger.info("检查数据库是否创建:[{}]", JSON.toJSONString(jobVO));
//                if (null == databaseVO){
//                    logger.info("未找到HIVE中对应任务的数据库:[{}]", JSON.toJSONString(jobVO));
//                    iterator.remove();
//                    continue;
//                }
//            }
//        }
        List<EtlJobVO> etlJobVOS = etlJobDao.listTempJobs();
        logger.info("获得已有临时任务:[{}]", JSON.toJSONString(etlJobVOS));
        return etlJobVOS;
    }

    @Override
    public PageResult<EtlJobVO> listJobs(String jobType, String dbName, String groupId, String tableName,
                                         String hasSuccess, Integer pageNum, Integer pageSize) {
        if (null == pageNum){
            pageNum = 1;
        }
        if (null == pageSize){
            pageSize = 10;
        }
        if (!StringUtils.isEmpty(tableName)){
            tableName = "%" + tableName + "%";
        }
        int total = etlJobDao.countSearch(jobType, dbName, groupId, tableName, hasSuccess);
        List<EtlJobVO> etlJobVOS = etlJobDao.listPage(jobType, dbName, groupId, tableName, hasSuccess,
                (pageNum - 1) * pageSize, pageSize);
        return PageResult.init(pageNum, pageSize, total, etlJobVOS);
    }

    @Override
    public List<EtlJobDbVo> listAllJobs() {

        Map<String, EtlJobDbVo> jobDbVoMap = new LinkedHashMap<>();
        EtlJobDbVo etlJobDbVo1 = new EtlJobDbVo();
        etlJobDbVo1.setTasks(new LinkedList<>());
        etlJobDbVo1.setEtlComment("采集任务");
        etlJobDbVo1.setType(CommAttr.JOB.TYPE.COLLECT);
        jobDbVoMap.put(CommAttr.JOB.TYPE.COLLECT, etlJobDbVo1);

        EtlJobDbVo etlJobDbVo2 = new EtlJobDbVo();
        etlJobDbVo2.setTasks(new LinkedList<>());
        etlJobDbVo2.setEtlComment("更新任务");
        etlJobDbVo2.setType(CommAttr.JOB.TYPE.UPDATE);
        jobDbVoMap.put(CommAttr.JOB.TYPE.UPDATE, etlJobDbVo2);

        EtlJobDbVo etlJobDbVo3 = new EtlJobDbVo();
        etlJobDbVo3.setTasks(new LinkedList<>());
        etlJobDbVo3.setEtlComment("导出任务");
        etlJobDbVo3.setType(CommAttr.JOB.TYPE.EXPORT);
        jobDbVoMap.put(CommAttr.JOB.TYPE.EXPORT, etlJobDbVo3);

        List<EtlJobVO> etlJobVOS = etlJobDao.listAll();
        Map<String, EtlJobVO> jobMaps = new LinkedHashMap<>();

        // 去重
        for(EtlJobVO etlJobVO: etlJobVOS){
            jobMaps.put(String.valueOf(etlJobVO.getJobId()), etlJobVO);
        }

        for(String key: jobMaps.keySet()){
            EtlJobVO etlJobVO = jobMaps.get(key);
            EtlJobDbVo etlJobDbVo = jobDbVoMap.get(etlJobVO.getJobType());
            if (null == etlJobDbVo){
                continue;
            }
            List<EtlJobVO> tasks = etlJobDbVo.getTasks();
            if (null == tasks){
                tasks = new LinkedList<>();
                etlJobDbVo.setTasks(tasks);
            }
            tasks.add(etlJobVO);
        }

        List<EtlJobDbVo> result = new LinkedList<>();
        Set<String> types = jobDbVoMap.keySet();
        for(String tp: types){
            result.add(jobDbVoMap.get(tp));
        }
        return result;
    }

    @Override
    public void deleteJob(int jobId) {
        EtlJobVO etlJobVO = etlJobDao.get(jobId);
        if (null == etlJobVO){
            throw new RuntimeException("该任务不存在或您没有该任务的操作权限");
        }
        // 删除任务
        etlJobVO.setIsDelete(CommAttr.BOOL.Y);
        etlJobDao.update(etlJobVO);
        // 解除与任务组关联
        etlGroupJobDao.del(Method.where("JOB_ID", C.EQ, jobId));
        // 是否一起删除表信息
        if(!sqlCommonExcute.tableIsExist(etlJobVO.getDbName(), etlJobVO.getTableName())){
            DatabaseVO database = dbManagerService.getDatabaseByName(etlJobVO.getDbName());
            if (null != database){
                TableVO table = dbManagerService.getTableByName(etlJobVO.getDbName(), etlJobVO.getTableName());
                if (null != table){
                    dbManagerService.deleteInDb(database.getDatabaseId(), table.getTableId());
                }
            }
        }
    }

    @Override
    public String applyUpdate() {
        synchronized (CommAttr.LOCK.APPLY_UPDATE){
            if (updateIng){
                throw new RuntimeException("一键更新正在执行中, 请勿重复点击");
            }
            updateIng = true;
        }
        updateExecuteId = UUIDUtil.next();
        final String excId = updateExecuteId;
        final SysUserVO loginUser = ContextUtil.getUserEntity();
        final String token = ContextUtil.getToken();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContextUtil.setUserEntity(loginUser);
                ContextUtil.setToken(token);
                applyUpdate(excId);
            }
        }).start();
        return updateExecuteId;
    }

    @Override
    public EtlLogView applyUpdateLogs() {
        if (StringUtils.isEmpty(updateExecuteId)){
            updateExecuteId = "df8575ed02154fbbbd4f65a26dadb678";
        }
        return applyUpdateLogs(updateExecuteId);
    }

    @Override
    public EtlLogView applyUpdateLogs(String executeId) {
        List<EtlLogVO> logs = etlLogDao.list(Method.where("UUID", C.EQ, executeId));
        StringBuffer logsBuffer = new StringBuffer();
        String state = "";
        for(EtlLogVO log: logs){
            state = log.getExecuteState();
            logsBuffer.append(log.getEtlLog()).append("\n");
        }
        EtlLogView view = new EtlLogView();
        view.setLastTimeState(state);
        view.setLogs(logsBuffer.toString());
        return view;
    }

    /**
     * 执行一键更新操作
     * @param excuteId
     * @return
     */
    private String applyUpdate(String excuteId){
        // 获取临时任务
        List<EtlJobVO> etlJobVOS = listTempJobs();
        if (null == etlJobVOS || etlJobVOS.size() < 1){
            updateIng = false;
        }
        etlJobVOS.sort((a, b) ->  a.getJobId() - b.getJobId());
        for(EtlJobVO etlJobVO: etlJobVOS){
            try {
                etlJobVO.setLastTimeState(CommAttr.JOB.STATE.RUNING);
                etlJobVO.setUpdateTime(new Date());
                etlJobVO.setExecuteCount(etlJobVO.getExecuteCount() + 1);
                etlJobDao.update(etlJobVO);
                executeJob(etlJobVO, excuteId);
                etlJobVO.setHasSuccess(CommAttr.BOOL.Y);
                etlJobVO.setLastTimeState(CommAttr.JOB.STATE.SUCCESS);
                etlJobDao.update(etlJobVO);
            }catch (Exception e){
                updateIng = false;
                setLog("一键更新任务执行完毕", 0,0, CommAttr.JOB.TYPE.UPDATE,
                        CommAttr.JOB.TYPE.UPDATE, CommAttr.JOB.STATE.FAIL, excuteId,
                        etlJobVO.getTableName(), etlJobVO.getGroupName(), etlJobVO.getEtlComment());
                etlJobVO.setLastTimeState(CommAttr.JOB.STATE.FAIL);
                return excuteId;
            }finally {
                etlJobDao.update(etlJobVO);
            }
        }
        updateIng = false;
        setLog("一键更新任务执行完毕", 0,
                0, CommAttr.JOB.TYPE.UPDATE, CommAttr.JOB.TYPE.UPDATE, "SUCCESS", excuteId,
                null, null, null);
        return excuteId;
    }


    /**
     * 执行单个任务
     * @param etlJobVO
     *          待执行的任务实体
     * @param excuteId
     */
    private void executeJob(EtlJobVO etlJobVO, String excuteId){
        // 前置条件判断, 是否可执行
        if (StringUtils.isEmpty(etlJobVO.getJobType())){
            if (StringUtils.isEmpty(etlJobVO.getEtlType())){
                return;
            }
            if (CommAttr.BOOL.Y.equalsIgnoreCase(etlJobVO.getIsDelete())){
                return;
            }
            if(etlJobVO.getEtlType().equals(CommAttr.ETL.TYPE.SQL)){
                etlJobVO.setJobType(CommAttr.JOB.TYPE.UPDATE);
            }
        }

        // 载入任务对应用户信息
        try {
            JSONObject jsonObject = JSON.parseObject(userDetailsService.loadUserByUsername(etlJobVO.getCreateUser()))
                    .getJSONObject("sysUser");
            ContextUtil.setUserId(jsonObject.getLong("userId"));
        }catch (Exception e){
            throw new ServiceInvokeException("远程服务调用失败", e);
        }

        // 日志记录开始
        setLog("任务:" + etlJobVO.getEtlComment() + "开始执行...", etlJobVO.getGroupId(),
                etlJobVO.getJobId(), etlJobVO.getUpdateType(), etlJobVO.getEtlType(), CommAttr.JOB.STATE.RUNING,
                excuteId, etlJobVO.getTableName(), etlJobVO.getGroupName(), etlJobVO.getEtlComment());
        // 根据不同的任务进行不同的处理
        nowEtlJobVO = etlJobVO;
        if (etlJobVO.getJobType().equalsIgnoreCase(CommAttr.JOB.TYPE.EXPORT)){
            TableExport tableExport = JSON.parseObject(etlJobVO.getConfig(), TableExport.class);
            sqlCommonExcute.exportToHdfs(excuteId, etlJobVO.getDbName(), etlJobVO.getTableName(),
                    etlJobVO.getPathName(), tableExport.getSplitChar(), new HiveLoggerCollback() {
                        @Override
                        public void call(String log, String logType, String logStatus, String excuteId) {
                            setLog(log, etlJobVO.getGroupId(), etlJobVO.getJobId(), etlJobVO.getUpdateType(),
                                    etlJobVO.getEtlType(), CommAttr.JOB.STATE.RUNING, excuteId,
                                    etlJobVO.getTableName(), etlJobVO.getGroupName(), etlJobVO.getEtlComment());
                            if (logStatus.equalsIgnoreCase(CommAttr.HIVE.LOG_STATUS.FAIL)){
                                setLog("任务执行失败:" + log, etlJobVO.getGroupId(), etlJobVO.getJobId(), etlJobVO.getUpdateType(),
                                        etlJobVO.getEtlType(), CommAttr.JOB.STATE.FAIL, excuteId,
                                        etlJobVO.getTableName(), etlJobVO.getGroupName(), etlJobVO.getEtlComment());
                                throw new RuntimeException("Hive操作失败:" + log);
                            }
                        }
                    });
        }else if(etlJobVO.getJobType().equalsIgnoreCase(CommAttr.JOB.TYPE.UPDATE)){
            if (etlJobVO.getEtlType().equalsIgnoreCase(CommAttr.ETL.TYPE.SQL)){
                // 自定义SQL
                if (!StringUtils.isEmpty(etlJobVO.getDbName())){
                    excuteSql("USE " + etlJobVO.getDbName() + ";" + etlJobVO.getConfig(), etlJobVO.getGroupId(),
                            etlJobVO.getJobId(),etlJobVO.getUpdateType(),etlJobVO.getEtlType(), excuteId,
                            etlJobVO.getTableName(), etlJobVO.getGroupName(), etlJobVO.getEtlComment());
                }
            }else if(etlJobVO.getEtlType().equalsIgnoreCase(CommAttr.ETL.TYPE.ETL)){
                // ETL
                List<String> sqlList = ETLUtil.conf2Sql(JSON.parseObject(etlJobVO.getConfig(), TableEtlConf.class));
                for(String sql: sqlList){
                    excuteSql(sql, etlJobVO.getGroupId(), etlJobVO.getJobId(), etlJobVO.getUpdateType(),
                            etlJobVO.getEtlType(), excuteId, etlJobVO.getTableName(), etlJobVO.getGroupName(),
                            etlJobVO.getEtlComment());
                }
            }

        }else if(etlJobVO.getJobType().equalsIgnoreCase(CommAttr.JOB.TYPE.COLLECT)){
            // 内部采集任务 TODO 这里不做实现, 由内部任务管理器完成采集工作.
        }
        setLog("任务:" + etlJobVO.getEtlComment() + "执行完毕", etlJobVO.getGroupId(),
                etlJobVO.getJobId(), etlJobVO.getUpdateType(), etlJobVO.getEtlType(),
                CommAttr.JOB.STATE.SUCCESS, excuteId,etlJobVO.getTableName(), etlJobVO.getGroupName(),
                etlJobVO.getEtlComment());
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
    private void excuteSql(String sql, int groupId, int jobId, String updateType, String etlType, String excuteId,
                           String tableName, String groupName, String displayName){
        if (sql == null || sql.trim().equalsIgnoreCase("")){
            return;
        }
        TableVO collbackTableVo = new TableVO();
        sqlSessionManager.excSql(excuteId, new HiveLoggerCollback() {
            @Override
            public void call(String log, String logType, String logStatus, String excuteId) {
                // TODO 这里二期 进行抽离, 其他执行SQL的地方重复代码太多
                if (logStatus.equalsIgnoreCase(CommAttr.HIVE.LOG_STATUS.FAIL)){
                    setLog("Hive执行失败:" + log, groupId, jobId, updateType, etlType, CommAttr.JOB.STATE.FAIL, excuteId,
                            tableName, groupName, displayName);
                    throw new ValidationException("Hive执行失败:" + log);
                }else {
                    if (logStatus.equalsIgnoreCase(CommAttr.HIVE.LOG_STATUS.START) ||
                            logStatus.equalsIgnoreCase(CommAttr.HIVE.LOG_STATUS.RUNING)){
                        setLog(log, groupId, jobId, updateType, etlType, CommAttr.JOB.STATE.RUNING, excuteId,
                                tableName, groupName, displayName);
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
                    }
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

    /**
     * 保存Log到数据库
     * @param log
     *          日志内容
     * @param groupId
     *          任务组ID
     * @param jobId
     *          任务ID
     * @param updateType
     *          更新类型
     * @param etlType
     *          任务类型
     * @param state
     *          任务状态
     * @param excuteId
     *          执行ID
     */
    private void setLog(String log, int groupId, int jobId, String updateType, String etlType, String state,
                        String excuteId, String tableName, String groupName, String jobDisplayName){
        log = log.replace("'", "\"");
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
            etlLog.setTableName(tableName);
            etlLog.setGroupName(groupName);
            etlLog.setJobDisplayName(jobDisplayName);
            etlLogDao.add(etlLog);
        }else{
            etlLog.setEtlLog((etlLog.getEtlLog().replace("'", "\"")) + "\r\n\"" + log.replace("'", "\"") + "\"\r\n");
            etlLog.setExecuteState(state);
            etlLog.setUpdateType(updateType);
            etlLog.setEtlType(etlType);
            etlLog.setEndTime(new Date());
            etlLog.setJobDisplayName(jobDisplayName);
            etlLogDao.update(etlLog);
        }
    }

    @Override
    public PageResult<EtlJobGroupVO> listGroups(String groupName, Integer pageNum, Integer pageSize) {
        pageNum = null == pageNum ? 1 : pageNum;
        pageSize = null == pageSize ? 10 : pageSize;

        WherePrams where = Method.createDefault().and("IS_DELETE", C.NE, CommAttr.BOOL.Y)
                .and("CREAT_USER", C.EQ, ContextUtil.getUserName());
        if (!StringUtils.isEmpty(groupName)){
            where.and("JOB_GROUP_NAME", C.LIKE, groupName);
        }

        int total = (int)etlJobGroupDao.count(where);
        int startNum = (pageNum - 1) * pageSize;
        return PageResult.init(pageNum, pageSize, total,
                etlJobGroupDao.list(where.limit(startNum, pageSize).orderBy("JOB_GROUP_ID DESC")));
    }

    @Override
    @Transactional
    public EtlJobGroupVO createGroup(EtlJobGroupVO groupVO) {
        String jobName = UUIDUtil.next();
        groupVO.setCreatUser(ContextUtil.getUserName());
        groupVO.setIsDelete("N");
        groupVO.setJobGroup(Scheduler.DEFAULT_GROUP);
        groupVO.setJobName(jobName);
        groupVO.setExecuteHzContent(JSON.toJSONString(groupVO.getExecuteHzContentEntity()));
        groupVO.setCreatTime(new Date());
        etlJobGroupDao.add(groupVO);
        EtlJobGroupVO newJob = etlJobGroupDao.get(Method.where("JOB_NAME", C.EQ, jobName));
        groupVO.setJobGroupId(newJob.getJobGroupId());
        newJob.setJobs(groupVO.getJobs());

        //配置任务执行周期
        JobExecuteType type = JSON.parseObject(groupVO.getExecuteHzContent(), JobExecuteType.class);
        // 获得调度表达式
        String cron = JobCronUtils.getByBean(type);

        // 任务组与任务关联
        if (null == groupVO.getJobs() || groupVO.getJobs().size() == 0){
            throw new RuntimeException("任务组中至少要有一个任务");
        }
        addGroupRelation(groupVO);

        // 只执行一次的情况下, 是否立即执行 若立即执行则直接执行, 否则生成一次性调度
        if (CommAttr.JOB.JOB_EXE_TYPE_ONE.equalsIgnoreCase(type.getType())){
            if (groupVO.getExecuteTimeStart() != null){
                if (groupVO.getExecuteTimeStart().getTime() < System.currentTimeMillis()){
                    executeJobGroups(newJob);
                    return newJob;
                }else{
                    cron = JobCronUtils.date2Cron(groupVO.getExecuteTimeStart());
                }
            }else{
                executeJobGroups(newJob);
                return newJob;
            }
        }

        // 创建任务调度
        try{
            //配置计划任务的工作类名，这个类需要实现Job接口，在execute方法中实现所需要做的工作
            JobDataMap jobDataMap = new JobDataMap();
            JobDetail jobDetail = JobBuilder.newJob(TableCreateSQLJobManagerService.class)
                    .withIdentity(jobName, Scheduler.DEFAULT_GROUP).setJobData(jobDataMap).build();

            // 传入调度表达式
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);

            CronTrigger trigger = TriggerBuilder.newTrigger().withDescription(newJob.getJobGroupName()).withIdentity(jobName, Scheduler.DEFAULT_GROUP)
                    .withSchedule(scheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, trigger);

            // 入库
            groupVO.setJobGroup(Scheduler.DEFAULT_GROUP);
            groupVO.setSchedName(scheduler.getSchedulerName());
            etlJobGroupDao.update(groupVO);

            // 启动任务
            if (!scheduler.isStarted()){
                scheduler.start();
            }

            return newJob;

        }catch (SchedulerException e){
            throw new RuntimeException("任务组创建失败", e);
        }
    }


    /**
     * 执行任务组
     * @param newJob
     *          任务组对象
     */
    private void executeJobGroups(EtlJobGroupVO newJob){
        List<EtlJobVO> jobs = newJob.getJobs();
        String next = UUIDUtil.next();
        newJob.setLastTimeState(CommAttr.JOB.STATE.RUNING);
        newJob.setLastTime(new Date());
        newJob.setExecuteCount(newJob.getExecuteCount() + 1);
        etlJobGroupDao.update(newJob);
        try {
            for(EtlJobVO jobVO: jobs){
                EtlJobVO etlJobVO = etlJobDao.get(jobVO.getJobId());
                if (null == etlJobVO){continue;}
                etlJobVO.setLastTimeState(CommAttr.JOB.STATE.RUNING);
                etlJobVO.setUpdateTime(new Date());
                etlJobVO.setExecuteCount(etlJobVO.getExecuteCount() + 1);
                etlJobVO.setGroupName(newJob.getJobGroupName());
                etlJobVO.setGroupId(newJob.getJobGroupId());
                etlJobDao.update(etlJobVO);
                executeJob(etlJobVO, next);
                etlJobVO.setLastTimeState(CommAttr.JOB.STATE.SUCCESS);
                etlJobVO.setHasSuccess(CommAttr.BOOL.Y);
                etlJobDao.update(etlJobVO);
            }
            newJob.setLastTimeState(CommAttr.JOB.STATE.SUCCESS);
        }catch (Exception e){
            newJob.setLastTimeState(CommAttr.JOB.STATE.FAIL);
        }finally {
            etlJobGroupDao.update(newJob);
        }
    }

    @Override
    public List<EtlJobGroupVO> listAllGroups() {
        return etlJobGroupDao.list(Method.where("CREAT_USER", C.EQ, ContextUtil.getUserName())
                .and("IS_DELETE", C.EQ, CommAttr.BOOL.N));
    }

    @Override
    public EtlJobGroupVO updateGroup(EtlJobGroupVO groupVO) {
        EtlJobGroupVO dbGroupVo = etlJobGroupDao.get(groupVO.getJobGroupId());
        if (CommAttr.JOB.STATE.RUNING.equalsIgnoreCase(dbGroupVo.getLastTimeState())){
            throw new ValidationException("该任务正在调度周期中, 请稍后再试");
        }
        if (groupVO.getExecuteTimeStart() != null){
            dbGroupVo.setExecuteTimeStart(groupVO.getExecuteTimeStart());
        }
        if (groupVO.getStartConditionMaxWait() != null){
            dbGroupVo.setStartConditionMaxWait(groupVO.getStartConditionMaxWait());
        }
        if (groupVO.getStartCondition() != null){
            dbGroupVo.setStartCondition(groupVO.getStartCondition());
        }
        if (groupVO.getJobGroupName() != null){
            dbGroupVo.setJobGroupName(groupVO.getJobGroupName());
        }
        if (groupVO.getExecuteTimeEnd() != null){
            dbGroupVo.setExecuteTimeEnd(groupVO.getExecuteTimeEnd());
        }
        if (groupVO.getEtlComment() != null){
            dbGroupVo.setEtlComment(groupVO.getEtlComment());
        }
        dbGroupVo.setRetryHz(groupVO.getRetryHz());
        dbGroupVo.setRetryNum(groupVO.getRetryNum());

        if (null != groupVO.getExecuteHzContentEntity()){
            dbGroupVo.setExecuteHzContentEntity(groupVO.getExecuteHzContentEntity());
            dbGroupVo.setExecuteHzContent(JSON.toJSONString(groupVO.getExecuteHzContentEntity()));
        }

        // 任务组与任务关联
        if (null == groupVO.getJobs() || groupVO.getJobs().size() == 0){
            throw new RuntimeException("任务组中至少要有一个任务");
        }
        addGroupRelation(groupVO);
        dbGroupVo.setJobs(groupVO.getJobs());

        //配置任务执行周期
        JobExecuteType type = dbGroupVo.getExecuteHzContentEntity();
        // 获得调度表达式
        String cron = JobCronUtils.getByBean(type);

        // 只执行一次的情况下, 是否立即执行 若立即执行则直接执行, 否则生成一次性调度
        if (CommAttr.JOB.JOB_EXE_TYPE_ONE.equalsIgnoreCase(type.getType())){
            if (groupVO.getExecuteTimeStart() != null){
                if (groupVO.getExecuteTimeStart().getTime() < System.currentTimeMillis()){
                    deleteJobInScheduler(dbGroupVo);
                    executeJobGroups(dbGroupVo);
                    return dbGroupVo;
                }else{
                    cron = JobCronUtils.date2Cron(dbGroupVo.getExecuteTimeStart());
                }
            }else{
                deleteJobInScheduler(dbGroupVo);
                executeJobGroups(dbGroupVo);
                return dbGroupVo;
            }
        }

        // 传入调度表达式
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        try{
            Trigger trigger = scheduler.getTrigger(TriggerKey.triggerKey(
                    dbGroupVo.getJobName(), dbGroupVo.getJobGroup()));
            CronTrigger c = (CronTrigger) trigger;
            if (c == null){
                c = TriggerBuilder.newTrigger().withDescription(dbGroupVo.getJobGroupName())
                        .withIdentity(dbGroupVo.getJobName(), Scheduler.DEFAULT_GROUP)
                        .withSchedule(scheduleBuilder).build();
                scheduler.rescheduleJob(TriggerKey.triggerKey(
                        dbGroupVo.getJobName(), dbGroupVo.getJobGroup()), c);
            }else{
                String oldTime = c.getCronExpression();
                if (!oldTime.equalsIgnoreCase(cron)) {
                    if(scheduler.isShutdown()){
                        scheduler.start();
                    }
                    CronTrigger newTrigger = TriggerBuilder.newTrigger()
                            .withDescription(dbGroupVo.getJobGroupName())
                            .withIdentity(dbGroupVo.getJobName(), dbGroupVo.getJobGroup())
                            .withSchedule(scheduleBuilder).build();
                    scheduler.rescheduleJob(TriggerKey.triggerKey(
                            dbGroupVo.getJobName(), dbGroupVo.getJobGroup()), newTrigger);
                }
            }
        }catch (SchedulerException e){
            throw new RuntimeException("任务调度时间更改失败", e);
        }
        etlJobGroupDao.update(dbGroupVo);
        return dbGroupVo;
    }

    /**
     * 从任务调度中删除任务
     * @param dbGroup
     *          任务组
     * @return
     */
    private EtlJobGroupVO deleteJobInScheduler(EtlJobGroupVO dbGroup){
        try {
            if (!StringUtils.isEmpty(dbGroup.getJobName()) && !StringUtils.isEmpty(dbGroup.getJobGroup())){
                scheduler.deleteJob(new JobKey(dbGroup.getJobName(), dbGroup.getJobGroup()));
            }
            return dbGroup;
        }catch (SchedulerException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加任务组与任务的关联
     * @param groupVO
     *          任务组实体
     */
    private void addGroupRelation(EtlJobGroupVO groupVO){
        int groupId = groupVO.getJobGroupId();
        // 干掉旧的关联
        etlGroupJobDao.del(Method.where("JOB_GROUP_ID", C.EQ, groupId));
        int sort = 1;
        // 添加新的
        for(EtlJobVO jobVO: groupVO.getJobs()){
            if (null != jobVO){

                if (etlGroupJobDao.isExist(Method.where("JOB_ID", C.EQ, jobVO.getJobId())
                        .and("JOB_GROUP_ID", C.NE, groupId))){
                    throw new RuntimeException("任务[" + jobVO.getEtlComment() + "]已加入其它任务组, 无法重复添加");
                }
                EtlGroupJobVO groupJobVO = new EtlGroupJobVO();
                groupJobVO.setEtlJobNum(sort);
                groupJobVO.setJobGroupId(groupId);
                groupJobVO.setJobId(jobVO.getJobId());
                etlGroupJobDao.add(groupJobVO);
            }
            sort ++;
        }
    }

    @Override
    public EtlJobGroupVO getInfo(int groupId) {
        EtlJobGroupVO etlJobGroupVO = etlJobGroupDao.get(groupId);
        List<EtlGroupJobVO> groupJobVo = etlGroupJobDao.list(Method.where("JOB_GROUP_ID", C.EQ, groupId).orderBy("ETL_JOB_NUM ASC"));
        List<EtlJobVO> tableVos = new LinkedList<>();
        for(EtlGroupJobVO etlGroupJobVO: groupJobVo){
            EtlJobVO etlJobVO = etlJobDao.get(etlGroupJobVO.getJobId());
            if(null != etlJobVO){
                tableVos.add(etlJobVO);
            }
        }
        if (null != etlJobGroupVO){
            etlJobGroupVO.setJobs(tableVos);
        }
        etlJobGroupVO.setExecuteHzContentEntity(JSON.parseObject(etlJobGroupVO.getExecuteHzContent(), JobExecuteType.class));
        return etlJobGroupVO;
    }

    @Override
    public void updateSwitch(EtlJobGroupVO groupVO) {
        EtlJobGroupVO etlJobGroupVO = etlJobGroupDao.get(groupVO.getJobGroupId());

        if (null == etlJobGroupVO){
            throw new RuntimeException("任务组未找到");
        }

        if (CommAttr.JOB.STATE.RUNING.equalsIgnoreCase(etlJobGroupVO.getLastTimeState())){
            throw new RuntimeException("正在执行中的任务不能更改状态");
        }

        etlJobGroupVO.setJobSwitch(groupVO.getJobSwitch());

        etlJobGroupDao.update(etlJobGroupVO);
    }

    @Override
    public void delete(int groupId) {
        EtlJobGroupVO etlJobGroupVO = etlJobGroupDao.get(groupId);

        if (null == etlJobGroupVO){
            throw new RuntimeException("任务不存在");
        }

        if(CommAttr.JOB.STATE.RUNING.equalsIgnoreCase(etlJobGroupVO.getLastTimeState())){
            throw new RuntimeException("任务正在执行中, 请稍后再试");
        }

        etlJobGroupVO.setIsDelete(CommAttr.BOOL.Y);
        etlJobGroupVO.setJobSwitch(CommAttr.JOB.SWITCH.STOP);
        etlJobGroupDao.update(etlJobGroupVO);

        // 解除任务组与任务关联
        etlGroupJobDao.del(Method.where("JOB_GROUP_ID", C.EQ, etlJobGroupVO.getJobGroupId()));

        // 删除内存中执行的任务
        deleteJobInScheduler(etlJobGroupVO);
    }

    @Override
    public PageResult<EtlLogVO> jobLogs(Integer pageNum, Integer pageSize, String jobType,
                                        String dbName, String groupId) {
        pageNum = null == pageNum ? 1 : pageNum;
        pageSize = null == pageSize ? 10 : pageSize;

        WherePrams where = Method.where("JOB_ID", C.NE, 0);
        if (!StringUtils.isEmpty(dbName)){
            where.and("TABLE_NAME", C.EQ, dbName);
        }
        if (!StringUtils.isEmpty(groupId)){
            where.and("JOB_GROUP_ID", C.EQ, groupId);
        }
        if (!StringUtils.isEmpty(jobType)){
            if (jobType.equalsIgnoreCase("UPDATE")){
                where.and("ETL_TYPE", C.EQ, "SQL").or("ETL_TYPE", C.EQ, "ETL");
            }else{
                where.and("ETL_TYPE", C.EQ, jobType);
            }
        }

        long allCount = etlLogDao.count(where);

        List<EtlLogVO> list = etlLogDao.list(where
                .limit((pageNum - 1) * pageSize, pageSize).orderBy("LOGID DESC"));

        for(EtlLogVO etlLogVO: list){
            double executeTaking = 0;
            Date startTime = etlLogVO.getStartTime();
            Date endTime = etlLogVO.getEndTime();
            if (null != startTime && null != endTime){
                executeTaking = endTime.getTime() - startTime.getTime();
                executeTaking = executeTaking / 1000.00D;
            }
            etlLogVO.setExecuteTaking(executeTaking);
        }

        return PageResult.init(pageNum, pageSize, allCount, list);
    }
}
