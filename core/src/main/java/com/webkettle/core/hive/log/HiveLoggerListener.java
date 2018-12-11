package com.webkettle.core.hive.log;

import com.webkettle.core.commons.CommAttr;
import org.apache.hive.jdbc.HivePreparedStatement;
import org.apache.hive.jdbc.HiveStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * Hive日志监听器
 * @author gsk
 */
public class HiveLoggerListener {

    protected static Logger logger = LoggerFactory.getLogger(HiveLoggerListener.class);

    /**
     * 监听某个Hive Sql执行的日志, 直到他执行完毕
     * @param statement
     *          Sql描述器
     * @param excuteId
     *          执行ID
     * @param collback
     *          回调函数
     */
    public static void listen(Statement statement, String excuteId, HiveLoggerCollback collback){
        // 非hive, 直接输出 start ... end!
        if (!(statement instanceof HiveStatement) && !(statement instanceof HivePreparedStatement)){
            try {
                while (!statement.isClosed()){
                    try {
                        Thread.sleep(500);
                    }catch (InterruptedException e){
                        e.fillInStackTrace();
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            return;
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HiveStatement hiveStatement = (HiveStatement) statement;
                    try {
                        while (!hiveStatement.isClosed() && hiveStatement.hasMoreLogs()){
                            List<String> logList = new LinkedList<>();
                            synchronized (CommAttr.LOCK.QUERY_LOG){
                                if (!hiveStatement.isClosed() && hiveStatement.hasMoreLogs()){
//                                    logList = hiveStatement.getQueryLog(true, 100);
                                }
                            }
                            for(String log: logList){
                                // 回调每行日志
                                logger.info(log);
                                collback.call(log, CommAttr.HIVE.LOG_TYPE.REDUCE,
                                        CommAttr.HIVE.LOG_STATUS.RUNING, excuteId);
                            }
                            try {
                                Thread.sleep(300);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                    }catch (SQLException e){
                        logger.info("日志获取出错", e);
                    }
                }
            }).start();
        }


    }


}
