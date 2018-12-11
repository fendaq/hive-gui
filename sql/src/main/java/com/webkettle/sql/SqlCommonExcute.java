package com.webkettle.sql;

import com.webkettle.core.commons.CommAttr;
import com.webkettle.core.hive.log.HiveLoggerCollback;
import com.webkettle.core.hive.session.SqlSessionManager;
import com.webkettle.core.utils.UUIDUtil;
import com.webkettle.sql.entity.DatabaseVO;
import com.webkettle.sql.entity.TableVO;
import com.webkettle.sql.entity.jobcreate.TableColum;
import com.webkettle.sql.entity.jobcreate.TableEtlColum;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.*;

/**
 * SQL 公共方法执行器
 * @author gsk
 */
@Component
public class SqlCommonExcute {

    private static Logger logger = LoggerFactory.getLogger(SqlCommonExcute.class);

    @Resource
    private SqlSessionManager sqlSessionManager;

    /**
     * 创建数据表
     * @param tableName
     *          表名
     * @param tableCommont
     *          表说明
     * @param partition
     *          是否分区
     * @param delimiter
     *          分隔符
     * @param stored
     *          数据格式
     * @param colums
     *          表中列
     * @return
     */
    public boolean createTable(String dbName, String tableName, String tableCommont, boolean partition, String delimiter,
                               String stored, TableColum... colums){

        return createTable(dbName, tableName, tableCommont, partition, delimiter, stored, new HiveLoggerCollback() {
            @Override
            public void call(String log, String logType, String logStatus, String excuteId) {
                logger.info("SqlLog: ID=[{}], log=[{}]", excuteId, log);
            }
        }, colums);

    }


    /**
     * 创建表
     * @param tableName
     *          表名
     * @param tableCommont
     *          表说明
     * @param partition
     *          是否分区
     * @param delimiter
     *          分隔符
     * @param stored
     *          存储类型
     * @param collback
     *          日志回调
     * @param colums
     *          表列
     * @return
     */
    public boolean createTable(String dbName, String tableName, String tableCommont, boolean partition, String delimiter,
                               String stored, HiveLoggerCollback collback, TableColum... colums){

        String selectDbSql = "USE " + dbName;

        StringBuffer buffer = new StringBuffer();
        buffer.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append("(");
        for(int i = 0; i < colums.length; i++){
            buffer.append(colums[i].getName()).append(" ").append(colums[i].getType());
            if (i < colums.length - 1){
                buffer.append(",");
            }
        }
        buffer.append(")COMMENT '").append(tableCommont).append("' ");

        if (partition){
            buffer.append("PARTITIONED BY(" + CommAttr.TABLE.PARTITION + " STRING) ");
        }
        if (!StringUtils.isEmpty(delimiter)){
            buffer.append("ROW FORMAT DELIMITED FIELDS TERMINATED BY '").append(delimiter).append("' ");
        }
        if (!StringUtils.isEmpty(stored)){
            buffer.append("STORED AS ").append(stored);
        }
        return sqlSessionManager.excSql(UUIDUtil.next(), collback, selectDbSql, buffer.toString());
    }

    /**
     * 数据库是否存在
     * @param dbName
     *          数据库名称
     * @return
     */
    public boolean dbIsExist(String dbName){
        if (StringUtils.isEmpty(dbName)){
            throw new RuntimeException("数据库名称不能为空");
        }
        for(DatabaseVO databaseVO: listDatabase()){
            if (dbName.equalsIgnoreCase(databaseVO.getDatabaseName())){
                return true;
            }
        }
        return false;
    }

    /**
     * 数据表是否存在
     * @param dbName
     *          所属数据库名称
     * @param tableName
     *          数据表名
     * @return
     */
    public boolean tableIsExist(String dbName, String tableName){
        if (dbIsExist(dbName)){
            List<TableVO> tableVOS = listTablesInDb(dbName);
            for(TableVO tableVO: tableVOS){
                if (tableName.equalsIgnoreCase(tableVO.getTableName())){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取数据库列表
     * @return
     *          数据库列表
     */
    public List<DatabaseVO> listDatabase(){
        List<Object> results = sqlSessionManager.excuteQueryTempFun(UUIDUtil.next(), new HiveLoggerCollback() {
            @Override
            public void call(String log, String logType, String logStatus, String excuteId) {
                logger.info("SqlLog: ID=[{}], log=[{}]", excuteId, log);
            }
        }, "show databases");
        List<DatabaseVO> resList = new ArrayList<>();
        if (results.isEmpty()){
            return resList;
        }
        for(Object dbName: results){
            DatabaseVO databaseVO = new DatabaseVO();
            databaseVO.setDatabaseName((String) dbName);
            resList.add(databaseVO);
        }
        return resList;
    }

    /**
     * 通过自定义Sql创建一个数据表
     * @param dbName
     *          所在数据库名
     * @param tableName
     *          要创建的数据表名
     * @param sql
     *          建表语句
     * @param excuteId
     *          执行唯一标识
     * @param collback
     *          日志回调方法
     * @return
     */
    public boolean createTableBySql(String dbName, String tableName, String sql, String excuteId,
                                    HiveLoggerCollback collback){
        if (!dbIsExist(dbName)){
            throw new RuntimeException("数据库不存在, 请先创建[" + dbName + "]数据库");
        }
        return sqlSessionManager.excSql(excuteId, collback, new String[]{sql});
    }

    /**
     * 从文件导入数据表
     * @param dbName
     *          数据库名称
     * @param tableName
     *          表名称
     * @param path
     *          文件路径
     * @param excuteId
     *          执行ID
     * @param collback
     *          回调方法
     * @param loadType
     *          导入方式
     * @param splitChar
     *          分隔符
     * @return
     */
    public boolean importTableByFile(String dbName, String tableName, List<TableColum> colums, String path, String excuteId,
                                     HiveLoggerCollback collback, String loadType, String splitChar, String charSet){
        List<String> sqls = new ArrayList<>();

        StringBuffer buffer = new StringBuffer();

        // 假如没有, 先创建表, 避免出错
        try {
            buffer.setLength(0);
            buffer.append("CREATE TABLE IF NOT EXISTS ").append(dbName).append(".").append(tableName).append("(");
            StringBuffer columsBuffer = new StringBuffer();
            for(int i = 0; i < colums.size(); i++){
                columsBuffer.append(colums.get(i).getName()).append(" ").append(colums.get(i).getType());
                if (i < colums.size() - 1){
                    columsBuffer.append(",");
                }
            }
            buffer.append(columsBuffer);
            buffer.append(")PARTITIONED BY(" + CommAttr.TABLE.PARTITION + " string)");
            buffer.append(" STORED AS orc");
            sqls.add(buffer.toString());

            // 创建临时表
            buffer.setLength(0);
            buffer.append("CREATE TABLE IF NOT EXISTS ").append(dbName).append(".temp_").append(tableName).append("(");
            buffer.append(columsBuffer);

            buffer.append(")PARTITIONED BY(" + CommAttr.TABLE.PARTITION + " string)");
            buffer.append(" row format  serde 'org.apache.hadoop.hive.contrib.serde2.MultiDelimitSerDe' with" +
                    "  serdeproperties (\"field.delim\"=\"").append(splitChar).append("\")");
            buffer.append(" STORED AS textfile");
            sqls.add(buffer.toString());

            // 导入SQL到临时表
            buffer.setLength(0);
            buffer.append("LOAD DATA INPATH '").append(path).append("' INTO TABLE ");
            buffer.append(dbName).append(".temp_").append(tableName).append(" PARTITION(" + CommAttr.TABLE.PARTITION + "='")
                    .append(DateFormatUtils.format(new Date(), "yyyyMMdd")).append("')");
            sqls.add(buffer.toString());

            // 临时表到正式表
            String insertInto = " INSERT OVERWRITE TABLE ";
            if (loadType.equals(CommAttr.SQL.SQL_LOAD_APPEND)){
                insertInto = " INSERT INTO TABLE ";
            }
            buffer.setLength(0);
            buffer.append(insertInto).append(dbName).append(".").append(tableName).append(" PARTITION(" + CommAttr.TABLE.PARTITION + "='")
                    .append(DateFormatUtils.format(new Date(), "yyyyMMdd")).append("')");
            buffer.append(" SELECT ");
            columsBuffer.setLength(0);
            for(int i = 0; i < colums.size(); i++){
                columsBuffer.append(colums.get(i).getName());
                if (i < colums.size() - 1){
                    columsBuffer.append(",");
                }
            }
            buffer.append(columsBuffer);
            buffer.append(" FROM ").append(dbName).append(".temp_").append(tableName);
            sqls.add(buffer.toString());

            // 删除临时表
            buffer.setLength(0);
            buffer.append("DROP TABLE ").append(dbName).append(".temp_").append(tableName);
            sqls.add(buffer.toString());
            // 删除多余分区
            if (!loadType.equals(CommAttr.SQL.SQL_LOAD_APPEND)){
                buffer.setLength(0);
                buffer.append("alter table ").append(dbName).append(".").append(tableName).append(" drop partition (")
                        .append(CommAttr.TABLE.PARTITION).append(" <>'").append(DateFormatUtils.format(new Date(), "yyyyMMdd")).append("')");
                sqls.add(buffer.toString());
            }
            // 执行SQL
            return sqlSessionManager.excSql(excuteId, collback, sqls.toArray(new String[]{}));
        }finally {
            sqlSessionManager.excSql(excuteId, collback, new String[]{"DROP TABLE IF EXISTS " + dbName + ".temp_" + tableName});
        }
    }

    /**
     * 采集
     * @param path
     *          采集路径
     * @param dbName
     *          数据库名
     * @param tableName
     *          数据表名
     * @param collback
     *          日志回调
     * @param loadType
     *          加载类型
     * @param splitChar
     *          分隔符
     * @param excuteId
     *          执行ID
     * @return
     */
    public boolean collection(String path, String dbName, String tableName, HiveLoggerCollback collback,
                              String loadType, String splitChar, String excuteId){

        List<String> sqlList = new ArrayList<>();
        StringBuffer buffer = new StringBuffer("LOAD DATA INPATH '").append(path);
        if (loadType.equals(CommAttr.SQL.SQL_LOAD_APPEND)){
            buffer.append("' INTO");
        }else {
            buffer.append("' OVERWRITE INTO");
        }
        buffer.append(" TABLE ");
        buffer.append(dbName).append(".").append(tableName).append(" PARTITION(" + CommAttr.TABLE.PARTITION + "='")
                .append(DateFormatUtils.format(new Date(), "yyyyMMdd")).append("')");
        sqlList.add(buffer.toString());

        // 覆盖的话, 要删除非当前分区
        buffer.setLength(0);
        if (!loadType.equals(CommAttr.SQL.SQL_LOAD_APPEND)){
            buffer.append("alter table ").append(dbName).append(".").append(tableName).append(" drop partition (")
                    .append(CommAttr.TABLE.PARTITION).append("<>'").append(DateFormatUtils.format(new Date(), "yyyyMMdd")).append("')");
            sqlList.add(buffer.toString());
        }
        return sqlSessionManager.excSql(excuteId, collback, sqlList.toArray(new String[]{}));
    }


    /**
     * 列出数据库下的所有表
     * @param dbName
     *          数据库名
     * @return
     */
    public List<TableVO> listTablesInDb(String dbName) {
        if (!dbIsExist(dbName)){
            throw new RuntimeException("数据库不存在");
        }
        List<Object> results = sqlSessionManager.excuteQueryTempFun(UUIDUtil.next(), new HiveLoggerCollback() {
            @Override
            public void call(String log, String logType, String logStatus, String excuteId) {
                logger.info("SqlLog: ID=[{}], log=[{}]", excuteId, log);
            }
        }, "use " + dbName, "show tables");
        List<TableVO> resList = new ArrayList<>();
        if (null == results || results.isEmpty()){
            return resList;
        }
        for(Object tableName: results){
            TableVO tableVO = new TableVO();
            tableVO.setTableName((String) tableName);
            tableVO.setDatabaseName(dbName);
            resList.add(tableVO);
        }
        return resList;
    }

    /**
     * 导出数据表到指定的hdfs目录中
     * @param dbName
     *          数据库名
     * @param tableName
     *          表名
     * @param path
     *          存放导出数据的路径
     * @return
     */
    public boolean exportToHdfs(String excuteId, String dbName, String tableName, String path, String splitChar, HiveLoggerCollback collback){
        if (!dbIsExist(dbName)){
            throw new RuntimeException("数据库不存在");
        }
        // 获取所有列
        List<TableEtlColum> tableEtlColums = listTableColums(excuteId, dbName, tableName, collback);

        StringBuffer buffer = new StringBuffer("INSERT OVERWRITE DIRECTORY '");
        buffer.append(path).append("'").append(" ROW FORMAT DELIMITED FIELDS TERMINATED BY '").append(splitChar);
        buffer.append("' NULL DEFINED AS '' SELECT ");
        // 按列导出, 去掉分区
        String colums = "";
        for (int i = 0; i < tableEtlColums.size(); i ++){
            TableEtlColum colum = tableEtlColums.get(i);
            if (colum.getColumName().equalsIgnoreCase(CommAttr.TABLE.PARTITION)){
                continue;
            }
            colums += colum.getColumName() + ",";
        }
        colums = colums.substring(0, colums.length() - 1);
        buffer.append(colums);

        buffer.append(" FROM ").append(dbName).append(".").append(tableName);
        logger.info(buffer.toString());
        return sqlSessionManager.excSql(excuteId, collback, buffer.toString());
    }

    /**
     * 列出某表的字段列表
     * @param excuteId
     *          执行ID
     * @param dbName
     *          数据库名称
     * @param tableName
     *          表名
     * @param collback
     *          日志回调
     * @return
     */
    public List<TableEtlColum> listTableColums(String excuteId, String dbName, String tableName, HiveLoggerCollback collback){
        StringBuffer buffer = new StringBuffer("DESC ").append(dbName).append(".").append(tableName);
        logger.info(buffer.toString());
        Map<String, List<Map<String, Object>>> stringListMap =
                sqlSessionManager.excQuerySql(excuteId, collback, buffer.toString());

        if (stringListMap.isEmpty()){
            return new ArrayList<>();
        }
        Set<String> results = stringListMap.keySet();
        List<TableEtlColum> result = new LinkedList<>();
        for(String resultKey: results){
            List<Map<String, Object>> maps = stringListMap.get(resultKey);
            for (Map<String, Object> map: maps){
                String colName = (String)map.get("col_name");
                if (StringUtils.isEmpty(colName) || colName.trim().charAt(0) == '#'
                        || colName.trim().equalsIgnoreCase(CommAttr.TABLE.PARTITION)){
                    continue;
                }
                TableEtlColum colum = new TableEtlColum();
                colum.setColumName(colName);
                colum.setColumComment((String) map.get("comment"));
                colum.setType((String) map.get("data_type"));
                colum.setTableName(tableName);
                result.add(colum);
            }
        }
        return result;
    }
}
