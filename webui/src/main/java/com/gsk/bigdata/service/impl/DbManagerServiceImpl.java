package com.gsk.bigdata.service.impl;

import com.aiyi.core.beans.Method;
import com.aiyi.core.beans.WherePrams;
import com.aiyi.core.exception.RequestParamException;
import com.aiyi.core.exception.ServiceInvokeException;
import com.aiyi.core.sql.where.C;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gsk.bigdata.dao.EtlJobDao;
import com.gsk.bigdata.dao.EtlLogDao;
import com.gsk.bigdata.service.DbManagerService;
import com.gsk.bigdata.service.HDFSService;
import com.webkettle.core.commons.CommAttr;
import com.webkettle.core.hive.log.HiveLoggerCollback;
import com.webkettle.core.hive.session.SqlSessionManager;
import com.webkettle.core.utils.ContextUtil;
import com.webkettle.core.utils.UUIDUtil;
import com.webkettle.sql.SqlCommonExcute;
import com.webkettle.sql.SqlUtil;
import com.webkettle.sql.entity.*;
import com.webkettle.sql.entity.jobcreate.TableCreatedConf;
import com.webkettle.sql.entity.jobcreate.TableEtlColum;
import com.webkettle.sql.entity.jobcreate.TableExport;
import com.webkettle.sql.entity.jobcreate.TableImport;
import com.webkettle.sql.entity.result.PageResult;
import com.webkettle.sql.entity.result.SqlAnsyResult;
import com.webkettle.sql.enums.DataBaseGroupEnum;
import com.webkettle.webservice.client.UserDetailsService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.ValidationException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 数据库业务管理实现层
 * @author gsk
 */
@Service
public class DbManagerServiceImpl implements DbManagerService {

    @Resource
    private SqlCommonExcute sqlCommonExcute;

    @Resource
    private SqlSessionManager sqlSessionManager;

    @Resource
    private EtlJobDao etlJobDao;

    @Value("${bigdata.hdfs.prefix}")
    private String hdfsPrefix;

    @Resource
    private HDFSService hdfsService;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private EtlLogDao etlLogDao;

    private Logger logger = LoggerFactory.getLogger(DbManagerService.class);

    @Override
    public List<DatabaseGroupVO> listdbs() {

        List<DatabaseVO> databaseVOS = sqlCommonExcute.listDatabase();

        String dbListStr = userDetailsService.getAuthenticationList(ContextUtil.getToken(), null);
        List<DatabaseVO> list = JSON.parseArray(dbListStr, DatabaseVO.class);
//        List<DatabaseVO> list = userDBRoleService.listPossessionDatabase();

        if (null == list){
            list = new ArrayList<>();
        }
        Map<String, DatabaseVO> ruledbsMap = new HashMap<>();

        for(DatabaseVO databaseVO: databaseVOS){
            ruledbsMap.put(databaseVO.getDatabaseName().toUpperCase(), databaseVO);
        }


        Map<String, DatabaseGroupVO> dataGroupMap = new LinkedHashMap<>();
        for (DataBaseGroupEnum groupEnum: DataBaseGroupEnum.values()){
	        DatabaseGroupVO databaseGroupVO1 = new DatabaseGroupVO();
	        databaseGroupVO1.setKey(groupEnum.getValue());
	        databaseGroupVO1.setName(groupEnum.getDscp());
	        dataGroupMap.put(groupEnum.getValue(), databaseGroupVO1);
        }

        // 结果Map封装
        for(DatabaseVO databaseVO: list){
            DatabaseGroupVO databaseGroupVO = dataGroupMap.get(databaseVO.getDbGroup());
            if (null == databaseGroupVO){
                databaseGroupVO = new DatabaseGroupVO();
                DataBaseGroupEnum dataBaseGroupEnum = DataBaseGroupEnum.valueOf(databaseVO.getDbGroup());
                databaseGroupVO.setName(dataBaseGroupEnum.getDscp());
                databaseGroupVO.setKey(dataBaseGroupEnum.getValue());
                databaseGroupVO.setList(new ArrayList<>());
                dataGroupMap.put(databaseVO.getDbGroup(), databaseGroupVO);
            }
            DatabaseVO ruleDb = ruledbsMap.get(databaseVO.getDatabaseName().toUpperCase());
            if (null != ruleDb){
                databaseGroupVO.getList().add(databaseVO);
            }
        }

        // 转换为List结果
        List<DatabaseGroupVO> result = new ArrayList<>();
        Set<String> groupKeys = dataGroupMap.keySet();
        for(String key: groupKeys){
            result.add(dataGroupMap.get(key));
        }

        return result;
    }

    @Override
    public List<TableVO> listTablesInDb(long dbId) {
        DatabaseVO databaseVO = getDatabaseById(dbId);
        if (null == databaseVO){
            throw new RuntimeException("数据库不存在");
        }
        List<TableVO> tableVOS = JSON.parseArray(userDetailsService.getAuthenticationList(
                ContextUtil.getToken(), databaseVO.getDatabaseName()), TableVO.class);
        return tableVOS;
    }

    @Override
    public DatabaseVO getDatabaseById(long databaseId) {
//        userDetailsService.getAuthenticationList(
//                ContextUtil.getToken(), null);
        List<DatabaseVO> list = JSONArray.parseArray(userDetailsService.getAuthenticationList(
                ContextUtil.getToken(), null), DatabaseVO.class);

        for (DatabaseVO databaseVO: list){
            if (databaseVO.getDatabaseId() == databaseId){
                return databaseVO;
            }
        }
        return null;
    }

    @Override
    public TableVO getTableById(long databaseId, long tableId) {
        DatabaseVO databaseVO = getDatabaseById(databaseId);
        if (null == databaseVO){
            throw new RequestParamException("ID为[" + databaseId + "]的数据库不存在或您没有该库的操作权限.");
        }
        List<TableVO> tableVOList = JSON.parseArray(userDetailsService.getAuthenticationList(
                ContextUtil.getToken(), databaseVO.getDatabaseName()), TableVO.class);
        for (TableVO tableVO: tableVOList){
            if (tableId == tableVO.getTableId()){
                return tableVO;
            }
        }
        return null;
    }

    @Override
    public TableVO getTableByName(String databaseName, String tableName) {
        List<TableVO> tableVOList = JSON.parseArray(userDetailsService.getAuthenticationList(
                ContextUtil.getToken(), databaseName), TableVO.class);
        for (TableVO tableVO: tableVOList){
            if (tableName.equalsIgnoreCase(tableVO.getTableName())){
                return tableVO;
            }
        }
        return null;
    }

    @Override
    public DatabaseVO getDatabaseByName(String dbName) {
        List<DatabaseVO> list = JSONArray.parseArray(userDetailsService.getAuthenticationList(
                    ContextUtil.getToken(), null), DatabaseVO.class);

        for (DatabaseVO databaseVO: list){
            if (dbName.equals(databaseVO.getDatabaseName())){
                return databaseVO;
            }
        }
        return null;
    }

    @Override
    public PageResult<Map<String, Object>> listTableDatas(long databaseId, long tableId, int pageNum, int pageSize) {
        DatabaseVO databaseVO = getDatabaseById(databaseId);
        if (null == databaseVO){
            throw new RuntimeException("数据库不存在");
        }
        TableVO tableVO = getTableById(databaseId, tableId);
        if (null == tableVO){
            throw new RuntimeException("数据表不存在");
        }
        Map<String, List<Map<String, Object>>> stringListMap = null;
        try {
            stringListMap = sqlSessionManager.excQuerySql(UUIDUtil.next(), new HiveLoggerCollback() {},
                    "SELECT * FROM " + databaseVO.getDatabaseName() + "." + tableVO.getTableName() + " LIMIT " + (100));
        }catch (Exception e){
            if (e.getMessage().contains("Table not found")){
                throw new RuntimeException("Hive中尚未建立该库或表, 请尝试在Hive中初始数据.");
            }
            throw e;
        }

        List<Map<String, Object>> list = stringListMap.get(CommAttr.SQL.SCRIPT_RESULT_MAP + 1);

        int start = (pageNum - 1) * pageSize;
        int end = pageNum * pageSize - 1;

        // 不足当前页
        if (list.size() < start){
            return PageResult.init(pageNum, pageSize, list.size(), new ArrayList<>());
        }

        List<Map<String, Object>> pageResult = new ArrayList<>();
        for(int i = start; i <= end; i++){
            if (i < list.size()){
                Map<String, Object> stringObjectMap = list.get(i);
                Set<String> keys = stringObjectMap.keySet();
                Map<String, Object> resultMap = new LinkedHashMap<>();
                for(String key: keys){
                    resultMap.put(key.substring(tableVO.getTableName().length() + 1), stringObjectMap.get(key));
                }
                pageResult.add(resultMap);
            }else{
                break;
            }
        }
        return PageResult.init(pageNum, pageSize, list.size(), pageResult);
    }

    @Override
    public void deleteInDb(long databaseId, long tableId) {
        DatabaseVO databaseVO = getDatabaseById(databaseId);
        if (null == databaseVO){
            throw new RuntimeException("目标数据库不存在或您没有操作该库的权限,请重新选择数据库");
        }
        TableVO tableVO = getTableById(databaseId, tableId);
        if (null == tableVO){
            throw new RuntimeException("目标表不存在或您没有操作该表的权限,请重新选择表");
        }

        if (!userDetailsService.checkUserAuthority(ContextUtil.getToken(), databaseVO.getDatabaseName(),
                tableVO.getTableName(), "DROP")){
            throw new RuntimeException("您没有[" + databaseVO.getDatabaseName() + "]库中[" +
                    tableVO.getTableName() + "]表的[DROP]权限.");
        }

        // 执行删表SQL
        sqlSessionManager.excSql(UUIDUtil.next(), new HiveLoggerCollback() {
            @Override
            public void call(String log, String logType, String logStatus, String excuteId) {
                if (logStatus.equalsIgnoreCase(CommAttr.HIVE.LOG_STATUS.SUCCESS)){
                    // 同步至关系型数据库
                    userDetailsService.delSysTable(tableId);
                }else if (logStatus.equalsIgnoreCase(CommAttr.HIVE.LOG_STATUS.FAIL)){
                    throw new ServiceInvokeException("HIVE操作失败:" + log);
                }
            }
        }, "DROP TABLE IF EXISTS " + databaseVO.getDatabaseName() + "." + tableVO.getTableName());
    }

    @Override
    public List<TableEtlColum> listColums(long databaseId, long tableId) {
        DatabaseVO databaseVO = getDatabaseById(databaseId);
        if (null == databaseVO){
            throw new RuntimeException("目标数据库不存在或您没有操作该库的权限,请重新选择数据库");
        }
        TableVO tableVO = getTableById(databaseId, tableId);
        if (null == tableVO){
            throw new RuntimeException("目标表不存在或您没有操作该表的权限,请重新选择表");
        }
        return sqlCommonExcute.listTableColums(UUIDUtil.next(), databaseVO.getDatabaseName(), tableVO.getTableName(),
                new HiveLoggerCollback() {});
    }

    @Override
    public List<TableEtlColum> listColums(long databaseId, String tableName) {
        DatabaseVO databaseVO = getDatabaseById(databaseId);
        if (null == databaseVO){
            throw new RuntimeException("目标数据库不存在或您没有操作该库的权限,请重新选择数据库");
        }
        return sqlCommonExcute.listTableColums(UUIDUtil.next(), databaseVO.getDatabaseName(), tableName,
            new HiveLoggerCollback() {});
    }

    @Override
    @Transactional
    public int importToTable(TableImport tableImport) {
        // 是否有建表权限
        if (!userDetailsService.checkUserAuthority(ContextUtil.getToken(),
                tableImport.getDbName().toUpperCase(), null, "CREATE")){
            throw new ValidationException("您没有[" + tableImport.getDbName() + "]库的[CREATE]权限");
        }

        // 得到文件真实路径
        String filePath = tableImport.getFileKey();
        if (CommAttr.HDFS.LOCATION_TYPE_FILE_KEY.equals(tableImport.getLocationType())){
            String prefix = hdfsPrefix + ContextUtil.getUserName();
            if (prefix.charAt(0) != '/'){
                prefix = "/" + prefix;
            }
            if (prefix.charAt(prefix.length() - 1) != '/'){
                prefix += "/";
            }
            if (filePath.charAt(0) == '/'){
                filePath = filePath.substring(1);
            }
            filePath = prefix + filePath;
        }

        try {
            sqlCommonExcute.importTableByFile(tableImport.getDbName(), tableImport.getTableName(), tableImport.getColums(),
                    filePath, UUIDUtil.next(),new HiveLoggerCollback() {
                        @Override
                        public void call(String log, String logType, String logStatus, String excuteId) {
                            if (logStatus.equalsIgnoreCase(CommAttr.HIVE.LOG_STATUS.SUCCESS)){
                                // 同步到关系型数据库
                                TableVO tableVO = new TableVO();
                                tableVO.setTableComment(tableImport.getTableName());
                                tableVO.setTableName(tableImport.getTableName());
                                tableVO.setUpdateId(Long.valueOf("" + ContextUtil.getUserId()));
                                tableVO.setSystemId(1);
                                tableVO.setDatabaseName(tableImport.getDbName());
                                tableVO.setUserId(Long.valueOf("" + ContextUtil.getUserId()));
                                addTableToPl(tableVO);
                            }else if (logStatus.equalsIgnoreCase(CommAttr.HIVE.LOG_STATUS.FAIL)){
                                throw new ServiceInvokeException("Hive操作失败:" + log);
                            }
                        }
                    }, tableImport.getLoadType(), tableImport.getSplitChar(), tableImport.getCharset());
        }catch (Exception e){
            hdfsService.delete(hdfsPrefix, tableImport.getFileKey());
            throw new RuntimeException(e);
        }
        return 1;
    }

    @Override
    public String export(long databaseId, long tableId, TableExport tableExport) {
        DatabaseVO databaseVO = getDatabaseById(databaseId);
        if (null == databaseVO){
            throw new RuntimeException("数据库不存在或您没有操作该库的权限");
        }
        TableVO tableVO = getTableById(databaseId, tableId);
        if (null == tableVO || !tableVO.getDatabaseName().equalsIgnoreCase(databaseVO.getDatabaseName())){
            throw new RuntimeException("ID为" + databaseId + "的数据库中不存在ID为" + tableId + "的表");
        }

        // 生成路径
        if (StringUtils.isEmpty(tableExport.getFileName())){
            throw new RuntimeException("请填写文件名");
        }
        String filePath = DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd");
        filePath += "/" + UUIDUtil.next() + "/" + tableExport.getFileName() + "." + tableExport.getSuffix();
        String prefix = hdfsPrefix + ContextUtil.getUserName();
        if (prefix.charAt(0) != '/'){
            prefix = "/" + prefix;
        }
        if (prefix.charAt(prefix.length() - 1) != '/'){
            prefix += "/";
        }

        // 创建目录
        hdfsService.createPath(prefix + filePath);

        if (StringUtils.isEmpty(tableExport.getJobName()) || StringUtils.isEmpty(tableExport.getNewJob())
            || tableExport.getNewJob().equalsIgnoreCase(CommAttr.BOOL.N)){
            // 立即导出
            sqlCommonExcute.exportToHdfs(UUIDUtil.next(), databaseVO.getDatabaseName(), tableVO.getTableName(),
                    prefix + filePath, tableExport.getSplitChar(), new HiveLoggerCollback() {});
        }else{
            // 创建为任务
            EtlJobVO jobVO = new EtlJobVO();
            jobVO.setConfig(JSON.toJSONString(tableExport));
            jobVO.setCreateUser(ContextUtil.getUserName());
            jobVO.setEtlComment("导出任务");
            jobVO.setUpdateType(CommAttr.SQL.SQL_LOAD_COVER);
            jobVO.setEtlType("EXPORT");
            jobVO.setPathName(prefix + filePath);
            jobVO.setCreateTime(new Date());
            jobVO.setJobName(tableExport.getJobName());
            jobVO.setJobType(CommAttr.JOB.TYPE.EXPORT);
            jobVO.setDbName(databaseVO.getDatabaseName());
            jobVO.setTableName(tableVO.getTableName());
            etlJobDao.add(jobVO);
            tableExport.setJobId(etlJobDao.get(Method.where("PATH_NAME", C.EQ, prefix + filePath)).getJobId());
        }
        return filePath;
    }

    @Override
    public Map<String, List<Map<String, Object>>> excuteSql(TableCreatedConf conf) {
        // 获得选定数据库
        DatabaseVO databaseVO = getDatabaseById(conf.getDatabaseId());
        if (null == databaseVO){
            throw new ValidationException("请先选定数据库");
        }
        // 解析sql
        String sql = "USE " + databaseVO.getDatabaseName() + ";" + new String(Base64.getDecoder().decode(conf.getSql())
                , Charset.forName("GBK"));
        // 剔除注释
        sql = SqlUtil.removeNotes(sql);
        // 权限检测
        // TODO 这里二期 进行抽离, 其他执行SQL的地方重复代码太多
        List<SqlAnsyResult> sqlAnsyResults = SqlUtil.ansySqls(sql, databaseVO.getDatabaseName());
        for (SqlAnsyResult sqlAnsyResult: sqlAnsyResults){
            if (sqlAnsyResult.getAction().equalsIgnoreCase("CREATE")){
                sqlAnsyResult.setTableName(null);
            }
            if (!userDetailsService.checkUserAuthority(ContextUtil.getToken(),
                    sqlAnsyResult.getDbName(), sqlAnsyResult.getTableName(), sqlAnsyResult.getAction())){
                String msg = "您没有[" + sqlAnsyResult.getDbName() + "]库";
                if (!StringUtils.isEmpty(sqlAnsyResult.getTableName())){
                    msg += "中[" + sqlAnsyResult.getTableName() + "]表";
                }
                msg += "的[" + sqlAnsyResult.getAction() + "]权限";
                throw new ValidationException(msg);
            }
        }
        // 分割SQL
        List<String> sqlList = SqlUtil.splitSqls(sql);
        // 执行SQL
        String excuetId = UUIDUtil.next();
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        Date startTime = new Date();
        try {
            result = sqlSessionManager.excQuerySql(excuetId, new HiveLoggerCollback() {
                @Override
                public void call(String log, String logType, String logStatus, String excuteId) {
                    // TODO 这里二期 进行抽离, 其他执行SQL的地方重复代码太多
                    if (logStatus.equalsIgnoreCase(CommAttr.HIVE.LOG_STATUS.START) ||
                            logStatus.equalsIgnoreCase(CommAttr.HIVE.LOG_STATUS.RUNING)){
                        addLog(excuetId, log, null, CommAttr.JOB.STATE.RUNING, startTime);
                    }

                    if (logStatus.equalsIgnoreCase(CommAttr.HIVE.LOG_STATUS.SUCCESS)){
                        if (logType.equalsIgnoreCase(CommAttr.HIVE.LOG_TYPE.SQL)) {
                            String thisSql = log.trim().toUpperCase().replace("IF NOT EXISTS", "")
                                    .replace("(", " (");;
                            if (thisSql.indexOf("USE") == 0) {
                                databaseVO.setDatabaseName(thisSql.substring(3).trim().toUpperCase()
                                        .split(" ")[0].replace(";", ""));
                                addLog(excuetId, "当前数据库分析:[" + databaseVO.getDatabaseName() + "]", null,
                                        CommAttr.JOB.STATE.RUNING, startTime);
                            }
                            if (thisSql.indexOf("CREATE DATABASE") == 0) {
                                String dbName = thisSql.substring("CREATE DATABASE".length())
                                        .trim().toUpperCase().split(" ")[0].trim().replace(";", "");
                                addLog(excuetId, "创建数据库:[" + dbName + "]", null,
                                        CommAttr.JOB.STATE.RUNING, startTime);
                                createDb(dbName, "APPLICATION", ContextUtil.getUserId());
                            }
                            if (thisSql.indexOf("DROP DATABASE") == 0){
                                String dbName = thisSql.substring("DROP DATABASE".length())
                                        .trim().toUpperCase().split(" ")[0].trim().replace(";", "");
                                addLog(excuetId, "准备删除数据库:[" + dbName + "]", null,
                                        CommAttr.JOB.STATE.RUNING, startTime);
                                DatabaseVO database = getDatabaseByName(dbName);
                                if (null != database){
                                    userDetailsService.delDataBase(database.getDatabaseId());
                                    addLog(excuetId, "完成删除数据库:[" + dbName + "]", null,
                                            CommAttr.JOB.STATE.RUNING, startTime);
                                }
                            }
                            if (thisSql.indexOf("CREATE TABLE") == 0) {
                                String tableName = thisSql.substring("CREATE TABLE".length())
                                        .trim().toUpperCase().split(" ")[0];
                                addLog(excuetId, "创建数据表:[" + tableName + "]", tableName,
                                        CommAttr.JOB.STATE.RUNING, startTime);
                                int dbspindex = tableName.indexOf(".");
                                if (dbspindex != -1) {
                                    databaseVO.setDatabaseName(tableName.substring(0, dbspindex));
                                }
                                createTable(databaseVO.getDatabaseName(), tableName.substring(tableName.indexOf(".") + 1), ContextUtil.getUserId());
                            }
                            if (thisSql.indexOf("DROP TABLE") == 0){
                                String tableName = thisSql.substring("DROP TABLE".length())
                                        .trim().toUpperCase().split(" ")[0];
                                addLog(excuetId, "准备删除数据表:[" + tableName + "]", tableName,
                                        CommAttr.JOB.STATE.RUNING, startTime);
                                TableVO tableVO = null;
                                int dbspindex = tableName.indexOf(".");
                                if (dbspindex != -1) {
                                    tableVO = getTableByName(tableName.substring(0, dbspindex), tableName.substring(dbspindex + 1));
                                }else{
                                    tableVO = getTableByName(databaseVO.getDatabaseName(), tableName.substring(dbspindex + 1));
                                }
                                if (null != tableVO){
                                    userDetailsService.delSysTable(tableVO.getTableId());
                                    addLog(excuetId, "完成删除数据表:[" + tableName + "]", tableName,
                                            CommAttr.JOB.STATE.RUNING, startTime);
                                }
                            }
                            // 最后分析出表
                            List<SqlAnsyResult> sqlAnsyResults1 = SqlUtil.ansySqls(thisSql, databaseVO.getDatabaseName());
                            for (SqlAnsyResult sqlAnsyResult: sqlAnsyResults1){
                                if (!StringUtils.isEmpty(sqlAnsyResult.getTableName())){
                                    addLog(excuetId, "表[" + sqlAnsyResult.getTableName() + "]更新完毕",
                                            sqlAnsyResult.getTableName(), CommAttr.JOB.STATE.SUCCESS, startTime);
                                }
                            }
                        }
                    }else if (logStatus.equalsIgnoreCase(CommAttr.HIVE.LOG_STATUS.FAIL)){
                        addLog(excuetId, "HIVE执行失败:" + log,null, CommAttr.JOB.STATE.SUCCESS,
                                startTime);
                        throw new ServiceInvokeException("HIVE执行失败:" + log);
                    }
                }
            }, sqlList.toArray(new String[]{}));
        }catch (Exception e){
            addLog(excuetId, "临时任务执行完毕", null, CommAttr.JOB.STATE.FAIL, startTime);
            throw new RuntimeException(e);
        }
        addLog(excuetId, "临时任务执行完毕", null, CommAttr.JOB.STATE.SUCCESS, startTime);
        return result;
    }


    private void addLog(String executeId, String log, String tableName, String state, Date startTime){
        WherePrams where = Method.where("UUID", C.EQ, executeId);
        EtlLogVO logVO = etlLogDao.get(where);
        if (null == logVO){
            logVO = new EtlLogVO();
            logVO.setJobDisplayName("立即执行任务");
            logVO.setGroupName("临时任务");
            logVO.setTableName(tableName);
            logVO.setExecuteTime(startTime);
            logVO.setStartTime(startTime);
            logVO.setEtlType(CommAttr.ETL.TYPE.SQL);
            logVO.setUpdateType(CommAttr.SQL.SQL_LOAD_APPEND);
            logVO.setExecuteState(state);
            logVO.setEtlLog(log);
            logVO.setUuid(executeId);
            logVO.setEndTime(new Date());
            logVO.setJobId(-1);
            etlLogDao.add(logVO);
        }else{
            logVO.setEndTime(new Date());
            logVO.setExecuteState(state);
            logVO.setEtlLog(logVO.getEtlLog() + "\r\n" + log);
            if (null != tableName){
                logVO.setTableName(tableName);
            }
            etlLogDao.update(logVO);
        }
    }


    @Override
    public void addDatabaseToPl(DatabaseVO databaseVO) {
        createDb(databaseVO.getDatabaseName(), databaseVO.getDbGroup(), databaseVO.getUserId());
    }

    @Override
    public void addTableToPl(TableVO tableVO) {
        createTable(tableVO.getDatabaseName(), tableVO.getTableName(), tableVO.getUserId());
    }

    /**
     * 在Oracle中创建数据库
     * @param name
     *          数据库名
     * @param group
     *          数据库组
     */
    private void createDb(String name, String group, long userId){
        DatabaseVO insertDb = new DatabaseVO();
        insertDb.setDatabasetype(group);
        insertDb.setGroupKey(group);
        insertDb.setDbGroup(group);
        insertDb.setDatabaseName(name.toUpperCase());
        insertDb.setDatabaseComment(name.toUpperCase());
        insertDb.setName(name);
        insertDb.setUserId(userId);
        userDetailsService.addDataBase(JSON.toJSONString(insertDb));
    }

    /**
     * 在Oracle中创建数据表
     * @param dbName
     *          库名
     * @param tableName
     *          表名
     */
    private void createTable(String dbName, String tableName, long userId){
        TableVO tableVO = new TableVO();
        tableVO.setTableComment(tableName.toUpperCase());
        tableVO.setDatabaseName(dbName.toUpperCase());
        tableVO.setTableName(tableName.toUpperCase());
        tableVO.setUpdateId(userId);
        tableVO.setUserId(userId);
        userDetailsService.addSysTable(JSON.toJSONString(tableVO));
    }


}
