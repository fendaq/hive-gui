package com.webkettle.core.hive.session;

import com.webkettle.core.commons.CommAttr;
import com.webkettle.core.hive.connection.ConnectionTools;
import com.webkettle.core.hive.log.HiveLoggerCollback;
import com.webkettle.core.hive.log.HiveLoggerListener;
import com.webkettle.core.utils.ExcuteSqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.*;
import java.util.*;

/**
 * Sql通信管理器
 * @author gsk
 */
@Component
public class SqlSessionManager {

    protected Logger logger = LoggerFactory.getLogger(SqlSessionManager.class);

    @Resource
    private ConnectionTools connectionTools;


    /**
     * 执行SQL脚本
     * @param sql
     *          sql脚本
     * @param collback
     *          日志回调内容
     * @return
     *          是否成功
     */
    public boolean excSql(String excuteId, HiveLoggerCollback collback, String sql){
        return excSql(excuteId, collback, sql.split(CommAttr.SQL.SCRIPT_SPLIT));
    }

    /**
     * 执行查询SQL脚本
     * @param sql
     *          SQL脚本
     * @param collback
     *          日志记录回调函数
     * @return
     *          List<Map<String, Object>>
     */
    public Map<String, List<Map<String, Object>>> excQuerySql(String excuteId, HiveLoggerCollback collback, String sql){
        return excQuerySql(excuteId, collback, sql.split(CommAttr.SQL.SCRIPT_SPLIT));
    }

    /**
     * 批量执行SQL
     * @param collback
     *          日志回调函数
     * @param sql
     *          sql脚本...
     * @param excuteId
     *          本次任务唯一标识(可以理解为类似订单回调的订单号)
     * @return
     *          是否成功, 一个失败则失败
     */
    public boolean excSql(String excuteId, HiveLoggerCollback collback, String... sql){
        Connection connection = connectionTools.getConnection();
        try {
            for(String sqlScript: sql){
                if (null == sqlScript || sqlScript.trim().equalsIgnoreCase("")){
                    continue;
                }
                logger.info("\n---------------HIVE:SCRIPT---------------\n{}", sqlScript);
                // 执行前SQL回调
                collback.call(sqlScript.trim(), CommAttr.HIVE.LOG_TYPE.SQL, CommAttr.HIVE.LOG_STATUS.START, excuteId);
                PreparedStatement statement = connection.prepareStatement(sqlScript);
                // 注册本次SQL日志监听
                HiveLoggerListener.listen(statement, excuteId, collback);
                //执行SQL
                statement.execute();
                // 执行成功SQL回调
                collback.call(sqlScript.trim(), CommAttr.HIVE.LOG_TYPE.SQL, CommAttr.HIVE.LOG_STATUS.SUCCESS, excuteId);
            }
        }catch (Exception e){
            // 执行失败SQL回调
            collback.call(e.getMessage(), CommAttr.HIVE.LOG_TYPE.SQL, CommAttr.HIVE.LOG_STATUS.FAIL, excuteId);
            return false;
        }finally {
            connectionTools.returnConnection(connection);
        }
        return true;
    }

    /**
     * 执行SQL并绝对返回结果信息
     * @param excuteId
     *          执行ID
     * @param collback
     *          日志回调方法
     * @param sql
     *          执行的SQL脚本
     * @return
     */
    public Map<String, List<Map<String, Object>>> excQuerySql(String excuteId, HiveLoggerCollback collback, String... sql){
        Connection connection = connectionTools.getConnection();
        Map<String, List<Map<String, Object>>> r = new LinkedHashMap<>();
        int resLc = 0;
        try {
            for (String sqlScript: sql){
                if (sqlScript == null || sqlScript.trim().equalsIgnoreCase("")){
                    continue;
                }
                logger.info("\n---------------HIVE:SCRIPT---------------\n{}", sqlScript);
                // 执行前
                collback.call(sqlScript.trim(), CommAttr.HIVE.LOG_TYPE.SQL, CommAttr.HIVE.LOG_STATUS.START, excuteId);
                //执行SQL
                if (ExcuteSqlUtil.isSelect(sqlScript) || ExcuteSqlUtil.isShow(sqlScript)){
                    // 如果是查询或查看类SQL, 则限制条数, 防止资源占用超限
                    sqlScript = ExcuteSqlUtil.limitQuerySql(sqlScript.trim());

                    PreparedStatement statement = connection.prepareStatement(sqlScript);
                    // 注册本次SQL日志监听
                    HiveLoggerListener.listen(statement, excuteId, collback);
                    ResultSet resultSet = statement.executeQuery();
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    //封装结果集
                    List<Map<String, Object>> resList = new LinkedList<>();
                    while (resultSet.next()){
                        Map<String, Object> result = new LinkedHashMap<>();
                        for(int i = 1; i <= metaData.getColumnCount(); i++){
                            result.put(metaData.getColumnName(i), resultSet.getObject(i));
                        }
                        resList.add(result);
                    }
                    r.put(CommAttr.SQL.SCRIPT_RESULT_MAP + (++resLc), resList);
                }else{
                    PreparedStatement statement = connection.prepareStatement(sqlScript);
                    // 注册本次SQL日志监听
                    HiveLoggerListener.listen(statement, excuteId, collback);
                    int resultCount = statement.executeUpdate(sqlScript);
    //                    long resultCount = statement.executeLargeUpdate(sqlScript);
                    Map<String, Object> result = new LinkedHashMap<>();
                    result.put("受影响的行数", resultCount);
                    List<Map<String, Object>> resList = new LinkedList<>();
                    resList.add(result);
                    r.put(CommAttr.SQL.SCRIPT_RESULT_MAP + (++resLc), resList);
                }
                collback.call(sqlScript.trim(), CommAttr.HIVE.LOG_TYPE.SQL, CommAttr.HIVE.LOG_STATUS.SUCCESS, excuteId);
            }
        }catch (Exception e){
            collback.call(e.getMessage(), CommAttr.HIVE.LOG_TYPE.SQL, CommAttr.HIVE.LOG_STATUS.FAIL, excuteId);
        }finally {
            connectionTools.returnConnection(connection);
        }
        return r;
    }

    /**
     * 执行查询SQL, 并且不一定返回结果
     * @param excuteId
     *          执行ID
     * @param collback
     *          日志回调
     * @param sql
     *          执行的SQL脚本
     * @return
     */
    public List<Object> excuteQueryTempFun(String excuteId, HiveLoggerCollback collback, String... sql){
        Connection connection = connectionTools.getConnection();
        List<Object> result = new ArrayList<>();
        try {
            for (String sqlScript: sql){
                logger.info("\n---------------HIVE:SCRIPT---------------\n{}", sqlScript);
                collback.call(sqlScript.trim(), CommAttr.HIVE.LOG_TYPE.SQL, CommAttr.HIVE.LOG_STATUS.START, excuteId);
                result.clear();
                PreparedStatement statement = connection.prepareStatement(sqlScript);
                // 注册本次SQL日志监听
                HiveLoggerListener.listen(statement, excuteId, collback);
                //执行SQL
                try {
                    ResultSet resultSet = statement.executeQuery();
                    //封装结果集
                    List<Map<String, Object>> resList = new LinkedList<>();
                    while (resultSet.next()){
                        result.add(resultSet.getObject(1));
                    }
                }catch (SQLException e){
                    if ("The query did not generate a result set!".equals(e.getMessage())){
                        statement.execute();
                    }else {
                        throw e;
                    }
                }
                collback.call(sqlScript.trim(), CommAttr.HIVE.LOG_TYPE.SQL, CommAttr.HIVE.LOG_STATUS.SUCCESS, excuteId);
            }
        }catch (Exception e){
            collback.call(e.getMessage(), CommAttr.HIVE.LOG_TYPE.SQL, CommAttr.HIVE.LOG_STATUS.FAIL, excuteId);
        }finally {
            connectionTools.returnConnection(connection);
        }
        return result;
    }
}
