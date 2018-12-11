package com.gsk.bigdata.service;

import com.webkettle.sql.entity.DatabaseGroupVO;
import com.webkettle.sql.entity.DatabaseVO;
import com.webkettle.sql.entity.TableVO;
import com.webkettle.sql.entity.jobcreate.*;
import com.webkettle.sql.entity.result.PageResult;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库管理业务类
 * @author gsk
 */
public interface DbManagerService {

    /**
     * 列出当前用户所持有的数据库
     * @return
     */
    List<DatabaseGroupVO> listdbs();

    /**
     * 列出当前用户在指定数据库下所持有的数据表列表
     * @param dbId
     *          数据库ID
     * @return
     *          数据表列表
     */
    List<TableVO> listTablesInDb(long dbId);

    /**
     * 通过ID获取数据库实体
     * @param databaseId
     *          数据库ID
     * @return
     */
    DatabaseVO getDatabaseById(long databaseId);

    /**
     * 通过ID获取数据表实体
     * @param tableId
     *          数据表ID
     * @return
     */
    TableVO getTableById(long databaseId, long tableId);

    /**
     * 通过名称查找数据表实体
     * @param databaseName
     *          数据库名称
     * @param tableName
     *          数据表名称
     * @return
     */
    TableVO getTableByName(String databaseName, String tableName);

    /**
     * 通过数据库名获取数据库详情
     * @param dbName
     *          数据库名
     * @return
     */
    DatabaseVO getDatabaseByName(String dbName);


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
    PageResult<Map<String, Object>> listTableDatas(long databaseId, long tableId, int pageNum, int pageSize);

    /**
     * 从某个库中删除数据表
     * @param databaseId
     *          数据库ID
     * @param tableId
     *          数据表ID
     */
    void deleteInDb(long databaseId, long tableId);

    /**
     * 列出表内字段列表
     * @param databaseId
     *          数据库ID
     * @param tableId
     *          表ID
     * @return
     */
    List<TableEtlColum> listColums(long databaseId, long tableId);

    /**
     * 通过表名列出表内字段列表
     * @param databaseId
     *          数据库ID
     * @param tableName
     *          表名
     * @return
     */
    List<TableEtlColum> listColums(long databaseId, String tableName);

    /**
     * 导入文件到数据库
     * @param tableImport
     *          数据库导入配置
     * @return
     */
    int importToTable(TableImport tableImport);

    /**
     * 从数据库中导出文件到HDFS
     * @param databaseId
     *          数据库ID
     * @param tableId
     *          表ID
     * @return
     *          导出到HDFS的文件标识
     */
    String export(long databaseId, long tableId, TableExport tableExport);

    /**
     * 立即执行SQL
     * @param conf
     *          SQL执行配置
     * @return
     *          {
     *              "result1":{
     *                  "conumList": [],
     *                  "datas":[]
     *              },
     *              "result2"{
     *                  "conumList": [],
     *                  "datas":[]
     *              }
     *          }
     */
    Map<String, List<Map<String, Object>>> excuteSql(TableCreatedConf conf);

    /**
     * 创建数据库到统一开发平台
     * @param databaseVO
     *          数据库实体
     */
    void addDatabaseToPl(DatabaseVO databaseVO);

    /**
     * 创建数据表到统一开发平台
     * @param tableVO
     *          数据表实体
     */
    void addTableToPl(TableVO tableVO);
}
