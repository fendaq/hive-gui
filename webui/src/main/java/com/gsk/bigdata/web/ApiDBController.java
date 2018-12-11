package com.gsk.bigdata.web;

import com.aiyi.core.beans.Method;
import com.aiyi.core.beans.ResultBean;
import com.aiyi.core.exception.RequestParamException;
import com.aiyi.core.exception.ServiceInvokeException;
import com.aiyi.core.sql.where.C;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gsk.bigdata.dao.EtlLogDao;
import com.gsk.bigdata.service.DbManagerService;
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
import com.webkettle.sql.enums.TableCreatedTypeEnum;
import com.webkettle.sql.service.TableCreateJobManagerService;
import com.webkettle.webservice.client.UserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.ValidationException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("api/db")
public class ApiDBController {

    Logger logger  = LoggerFactory.getLogger(ApiDBController.class);

    @Resource
    private DbManagerService dbManagerService;

    @Resource
    private SqlCommonExcute sqlCommonExcute;

    @Resource
    private SqlSessionManager sqlSessionManager;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private EtlLogDao etlLogDao;

    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
    }

    /**
     * 分组列出我的数据库列表
     * @return
     *          数据库分组 >> 数据库列表
     */
    @GetMapping("common")
    public ResultBean listMyDatabase(){

        List<LinkedHashMap> dbList = JSONArray.parseArray(userDetailsService.getAuthenticationList(
                ContextUtil.getToken(), null), LinkedHashMap.class);
        List<DatabaseGroupVO> listDbGroup = new LinkedList<>();

        HashMap<String, List<Object>> cacheHashMap = new HashMap<>();
        for (LinkedHashMap map: dbList){
            String groupKey = map.get("databasetype").toString();
            List<Object> objects = cacheHashMap.get(groupKey);
            if (null == objects){
                objects = new ArrayList<>();
                cacheHashMap.put(groupKey, objects);
            }
            objects.add(map);
        }

        for(DataBaseGroupEnum groupEnum: DataBaseGroupEnum.values()){
            DatabaseGroupVO groupVO = new DatabaseGroupVO();
            groupVO.setName(groupEnum.getDscp());
            groupVO.setKey(groupEnum.getValue());
            groupVO.setList(new ArrayList<>());
            List<Object> objects = cacheHashMap.get(groupEnum.getValue());
            if (null != objects){
                groupVO.setList(objects);
            }
            listDbGroup.add(groupVO);
        }

        return ResultBean.success("数据库获取成功").putResponseBody("list", listDbGroup);
    }

    /**
     * 列出我在制定数据库下的数据表列表
     * @param databaseId
     *          数据库ID
     * @return
     *          数据表列表
     */
    @GetMapping("{databaseId}/tables")
    public ResultBean listMyTablesInDb(@PathVariable("databaseId") long databaseId){
        DatabaseVO databaseVO = dbManagerService.getDatabaseById(databaseId);
        if (null == databaseVO){
            throw new RequestParamException("ID为[" + databaseId + "]的数据库不存在或您没有该库的操作权限");
        }
        return ResultBean.success("表格获取成功")
                .putResponseBody("list", JSON.parse(userDetailsService.getAuthenticationList(
                        ContextUtil.getToken(), databaseVO.getDatabaseName())));
    }

    /**
     * 列出表内指定部分数据
     * @param databaseId
     *          数据库ID
     * @param tableId
     *          表ID
     * @param pageNum
     *          第几页数据
     * @param pageSize
     *          每页多少条
     * @return
     */
    @GetMapping("{databaseId}/tables/{tableId}/datas")
    public ResultBean listTableDatas(@PathVariable("databaseId") long databaseId,
                                     @PathVariable("tableId") long tableId,
                                     Integer pageNum, Integer pageSize){
        if (null == pageNum){
            pageNum = 1;
        }
        if (null == pageSize){
            pageSize = 10;
        }
//        String cacheKey = "DATABASE_" + databaseId + "_TABLE_" + tableId + "_DATAS_" + pageNum + "-" + pageSize;
//        PageResult<Map<String, Object>> mapPageResult = CacheUtils.get(cacheKey);
//        if (null == mapPageResult){
            PageResult<Map<String, Object>> mapPageResult = dbManagerService
                    .listTableDatas(databaseId, tableId, pageNum, pageSize);
//        }
        // 延长缓存时间
//        CacheUtils.set(cacheKey, mapPageResult, 60000);
        return ResultBean.success("数据获取成功").setResponseBody(mapPageResult);
    }

    /**
     * 创建数据表
     * @param databaseId
     *          要存储在的数据库ID
     * @param createdConf
     *          建表配置信息
     * @return
     */
    @PostMapping("{databaseId}/tables")
    public ResultBean createTableInDb(@PathVariable("databaseId") long databaseId,
                                      @RequestBody TableCreatedConf createdConf){
        DatabaseVO databaseVO = dbManagerService.getDatabaseById(databaseId);
        if (null == databaseVO){
            throw new RuntimeException("目标数据库不存在或您没有操作该库的权限,请重新选择数据库");
        }

        TableCreateJobManagerService jobManagerService =
                TableCreatedTypeEnum.valueOf(createdConf.getType()).getJobManagerService();

        int jobId = jobManagerService.saveConfig(databaseVO.getDatabaseName(), createdConf);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("custJobId", jobId);
        return ResultBean.success("建表任务创建完毕").setResponseBody(responseBody);
    }

    /**
     * 获取数据表统计信息
     * @param databaseId
     *          数据库ID
     * @param tableId
     *          数据表ID
     * @return
     */
    @GetMapping("{databaseId}/tables/{tableId}/statistics")
    public ResultBean getTableStatistics(@PathVariable("databaseId") long databaseId,
                                         @PathVariable("tableId") long tableId){
        DatabaseVO databaseVO = dbManagerService.getDatabaseById(databaseId);
        if (null == databaseVO){
            throw new RuntimeException("目标数据库不存在或您没有操作该库的权限,请重新选择数据库");
        }
        TableVO tableVO = dbManagerService.getTableById(databaseId, tableId);
        if (null == databaseVO){
            throw new RuntimeException("目标数据表不存在或您没有操作该表的权限,请重新选择数据库");
        }
        List<EtlLogVO> list = etlLogDao.list(Method.where("TABLE_NAME", C.EQ, tableVO.getTableName())
                .limit(0, 1).orderBy("LOGID DESC"));

        if (list.size() > 0){
            tableVO.setUpdateTime(list.get(0).getExecuteTime());
        }else {
            tableVO.setUpdateTime(tableVO.getCreateTime());
        }
        return ResultBean.success("统计信息获取成功")
                .putResponseBody("list", list).putResponseBody("info", tableVO);
    }

    /**
     * 立即执行sql
     * @param conf
     *          Sql执行配置
     * @return
     */
    @PostMapping("execute")
    public ResultBean excuteSql(@RequestBody TableCreatedConf conf){
        Map<String, List<Map<String, Object>>> stringListMap = dbManagerService.excuteSql(conf);
        // TODO vue.js 无法针对字段中带有"."的JSON进行解析, 因此需要重新遍历一次替换字段名中的"."后期找解决方案
        Set<String> strings = stringListMap.keySet();
        for (String tabKey: strings){
            List<Map<String, Object>> maps = stringListMap.get(tabKey);
            List<Map<String, Object>> newMaps = new LinkedList<>();
            Iterator<Map<String, Object>> iterator = maps.iterator();

            while (iterator.hasNext()){
                Map<String, Object> map = iterator.next();
                Map<String, Object> newMap = new LinkedHashMap<>();
                Set<String> fieldList = map.keySet();
                for(String field: fieldList){
                    if (StringUtils.isEmpty(field)){
                        continue;
                    }
                    Object o = map.get(field);
                    if (null != o){
                        newMap.put(field.replace(".", "_"), o);
                    }
                }
                iterator.remove();
                newMaps.add(newMap);
            }
            stringListMap.put(tabKey, newMaps);
        }
        return ResultBean.success("SQL执行完毕")
                .putResponseBody("result", stringListMap);
    }

    /**
     * 从某个库中删除数据表
     * @param databaseId
     *          数据库ID
     * @param tableId
     *          数据表Id
     * @return
     */
    @PostMapping("{databaseId}/tables/{tableId}/delete")
    public ResultBean deleteTableOnDb(@PathVariable("databaseId") long databaseId,
                                      @PathVariable("tableId") long tableId){
        dbManagerService.deleteInDb(databaseId, tableId);
        return listMyTablesInDb(databaseId).setMessage("删表任务创建完毕");
    }

    /**
     * 列出表内字段列表
     * @param databaseId
     *          数据库ID
     * @param table
     *          表ID或表名
     * @return
     */
    @GetMapping("{databaseId}/tables/{tableId}/colums")
    public ResultBean listColums(@PathVariable("databaseId") long databaseId,
                                 @PathVariable("tableId") String table){
        List<TableEtlColum> tableEtlColums = new ArrayList<>();
        try {
            tableEtlColums = dbManagerService.listColums(databaseId, Long.valueOf(table));
        }catch (Exception e){
            tableEtlColums = dbManagerService.listColums(databaseId, table);
        }

        return ResultBean.success("表列列表获取成功")
                .putResponseBody("list", tableEtlColums);
    }

    /**
     * 创建数据库
     */
    @PostMapping("add")
    public ResultBean createDatabase(@RequestBody DatabaseVO databaseVO){
        // 入外部库
        if(sqlCommonExcute.dbIsExist(databaseVO.getDatabaseName())){
            throw new RuntimeException("该库名称已被占用, 请更换其他库");
        }
        sqlSessionManager.excSql(UUIDUtil.next(), new HiveLoggerCollback() {
            @Override
            public void call(String log, String logType, String logStatus, String excuteId) {
                if (logStatus.equalsIgnoreCase(CommAttr.HIVE.LOG_STATUS.SUCCESS)){
                    // 同步至关系型数据库
                    databaseVO.setDbGroup(databaseVO.getGroupKey());
                    databaseVO.setDatabasetype(databaseVO.getGroupKey());
                    if (StringUtils.isEmpty(databaseVO.getDatabaseComment())){
                        databaseVO.setDatabaseComment(databaseVO.getDatabaseName());
                    }
                    databaseVO.setUserId(ContextUtil.getUserId());
                    dbManagerService.addDatabaseToPl(databaseVO);
                }else if(logStatus.equalsIgnoreCase(CommAttr.HIVE.LOG_STATUS.FAIL)){
                    throw new ServiceInvokeException("HIVE操作失败:" + log);
                }
            }
        }, "CREATE DATABASE IF NOT EXISTS " + databaseVO.getDatabaseName());
        return ResultBean.success("数据库添加成功").setResponseBody(listMyDatabase().getResponseBody());
    }

    /**
     * 删除数据库
     * @param databaseId
     *          数据库ID
     * @return
     */
    @PostMapping("{databaseId}/delete")
    public ResultBean deleteDatebase(@PathVariable("databaseId") long databaseId){
        DatabaseVO databaseVO = dbManagerService.getDatabaseById(databaseId);
        if (null == databaseVO){
            throw new RuntimeException("数据库不存在或您的权限不足以操作该数据库");
        }
        if (!userDetailsService.checkUserAuthority(ContextUtil.getToken(),
                databaseVO.getDatabaseName(), null, "DROP")){
            throw new ValidationException(
                    "您没有[" + databaseVO.getDatabaseName() + "]库的[DROP]权限.");
        }
        sqlSessionManager.excSql(UUIDUtil.next(), new HiveLoggerCollback() {
            @Override
            public void call(String log, String logType, String logStatus, String excuteId) {
                logger.info("SQLLog:[DELETE_DATABASE]-excuteId:[{}], log:[{}]", excuteId, log);
                if (logStatus.equalsIgnoreCase(CommAttr.HIVE.LOG_STATUS.SUCCESS)){
                    // 同步至关系型数据库
                    userDetailsService.delDataBase(databaseId);
                }else if (logStatus.equalsIgnoreCase(CommAttr.HIVE.LOG_STATUS.FAIL)){
                    throw new ServiceInvokeException("HIVE操作失败:" + log);
                }
            }
        }, "DROP DATABASE IF EXISTS " + databaseVO.getDatabaseName() + " cascade");
        return ResultBean.success("数据库删除成功").setResponseBody(listMyDatabase().getResponseBody());
    }


    /**
     * 导入文件到数据库
     * @param tableImport
     *          文件导入配置
     * @return
     */
    @PostMapping("import")
    public ResultBean importToTable(@RequestBody TableImport tableImport){
        int jobId = dbManagerService.importToTable(tableImport);
        return ResultBean.success("导入任务创建成功").putResponseBody("custJobId", jobId);
    }

    /**
     * 导出表数据到hdfs
     * @param databaseId
     *          数据库库ID
     * @param tableId
     *          表ID
     * @return
     */
    @PostMapping("{databaseId}/tables/{tableId}/export")
    public ResultBean exportToHdfs(@PathVariable("databaseId") long databaseId,
                                   @PathVariable("tableId") long tableId,
                                   @RequestBody TableExport tableExport){
        if (ContextUtil.getUserId() != 1L){
            throw new ValidationException("只有最高超管才有导出权限.");
        }
        return ResultBean.success("文件导出成功")
                .putResponseBody("fileKey", dbManagerService.export(databaseId, tableId, tableExport))
                .putResponseBody("custJobId", tableExport.getJobId());
    }

    /**
     * SQL 解析
     * @param createdConf
     *          承载SQL的容器
     * @return
     */
    @PostMapping("sql/show")
    public ResultBean showCreateTableSqlColum(@RequestBody TableCreatedConf createdConf){
        if(null == createdConf || StringUtils.isEmpty(createdConf.getSql())){
            throw new RuntimeException("请上传要分析的SQL");
        }

        String sql;
        try {
            byte[] decode = Base64.getUrlDecoder().decode(createdConf.getSql());
            sql = new String(decode, "UTF-8");
        }catch (Exception e){
            throw new RuntimeException("SQL解码失败");
        }

        List<SqlAnsyResult> results = new ArrayList<>();
        DatabaseVO databaseVO = dbManagerService.getDatabaseById(createdConf.getDatabaseId());
        if (null != databaseVO){
            results = SqlUtil.ansySqls(sql, databaseVO.getDatabaseName());
        }
        return ResultBean.success("SQL解析成功").putResponseBody("list", results);
    }

    /**
     * Sql 权限校验
     * @param conf
     *          建表配置
     * @return
     */
    @PostMapping("sql/check")
    public ResultBean checkSql(@RequestBody TableCreatedConf conf){
        String sql;
        try {
            sql = new String(Base64.getDecoder().decode(conf.getSql()), Charset.forName("GBK"));
        }catch (Exception e){
            throw new ValidationException("SQL解码失败");
        }
        if (StringUtils.isEmpty(sql.trim())){
            throw new ValidationException("请输入完整的SQL语句.");
        }
        DatabaseVO databaseVO = dbManagerService.getDatabaseById(conf.getDatabaseId());
        if (null != databaseVO){
            conf.setDbName(databaseVO.getDatabaseName());
        }
        List<SqlAnsyResult> sqlAnsyResults = SqlUtil.ansySqls(sql, conf.getDbName());
        // TODO 这里二期 进行抽离, 其他执行SQL的地方重复代码太多
        for (SqlAnsyResult result: sqlAnsyResults){
            if (result.getAction().equalsIgnoreCase("CREATE")){
                result.setTableName(null);
            }
            if (!userDetailsService.checkUserAuthority(ContextUtil.getToken(),
                    result.getDbName(), result.getTableName(), result.getAction())){
                throw new ValidationException(
                        "您没有[" + result.getDbName() + "]库" + (result.getTableName() == null ? "的" : "中[" + result.getTableName()
                                + "]表的[") + result.getAction() + "]权限");
            }
        }
        return ResultBean.success("权限校验通过");
    }

}
