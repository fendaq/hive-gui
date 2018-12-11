package com.webkettle.core.hive.log;

import com.webkettle.core.commons.CommAttr;

/**
 * Hive Sql 日志回调接口
 * @author gsk
 */
public interface HiveLoggerCollback {


    /**
     * Hive Sql 回调通知方法
     * @param log
     *          回调日志内容
     * @param logType
     *          日志类型: SQL = 执行SQL, REDUCE = REDUCE执行日志
     * @param excuteSuatus
     *          执行状态: PRE = 执行SQL前通知, SUCCESS = 执行成功通知, FAIL = 执行失败通知
     * @param excuteId
     *          执行ID
     */
    default void call(String log, String logType, String excuteSuatus, String excuteId){
        if (excuteSuatus.equalsIgnoreCase(CommAttr.HIVE.LOG_STATUS.FAIL)){
            throw new RuntimeException("Hive操作失败:" + log);
        }
    }

}
