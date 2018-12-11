package com.webkettle.core.commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 公共属性类
 * @author gsk
 */
public class CommAttr {


    /**
     * 逻辑常量
     */
    public static final class BOOL{
        /**
         * 是
         */
        public static final String Y = "Y";
        /**
         * 否
         */
        public static final String N = "N";
    }

    /**
     * 锁
     */
    public static final class LOCK{
        /**
         * 一键更新锁
         */
        public static Object APPLY_UPDATE = new Object();

        /**
         * 日志查询锁
         */
        public static Object QUERY_LOG = new Object();
    }

    /**
     * 认证相关常量
     */
    public static final class AUTH{
        /**
         * 项目私钥Resource目录
         */
        public static final String PRIVATE_LEY_PATH = "rsa/private.key";
        /**
         * 项目公钥Resource目录
         */
        public static final String PUBLIC_LEY_PATH = "rsa/public.key";

        /**
         * 用户认证Cookie Key
         */
        public static final String AUTH_COOKIE_KEY = "_cache_auth";
    }

    /**
     * SQL 相关常量
     */
    public static final class SQL{

        /**
         * SQL语句分隔符
         */
        public static final String SCRIPT_SPLIT = ";";

        /**
         * SQL查询结果集前缀
         */
        public static final String SCRIPT_RESULT_MAP = "result";

        /**
         * 数据导入方式: 增量
         */
        public static final String SQL_LOAD_APPEND = "APPEND";

        /**
         * 数据导入方式: 覆盖
         */
        public static final String SQL_LOAD_COVER = "COVER";


        /**
         * 字段类型
         * @author gsk
         */
        public static final class FIELD_TYPE{

            public static final FieldType TINYINT = createType("TINYINT", "有符号超短整数");

            public static final FieldType SMALLINT = createType("SMALLINT", "有符号短整数");

            public static final FieldType INT = createType("INT", "有符号整数");

            public static final FieldType BIGINT = createType("BIGINT", "有符号长整数");

            public static final FieldType FLOAT = createType("FLOAT", "单精度浮点数");

            public static final FieldType DOUBLE = createType("DOUBLE", "双精度浮点数");

            public static final FieldType BOOLEAN = createType("BOOLEAN", "逻辑型");

            public static final FieldType STRING = createType("STRING", "字符串");

            public static final FieldType BINARY = createType("BINARY", "二进制类型,从Hive0.8.0开始支持");

            public static final FieldType VARCHAR = createType("VARCHAR", "字符串,从Hive0.12.0开始支持");

            public static final FieldType CHAR = createType("CHAR", "字符串,从Hive0.13.0开始支持");

            public static final FieldType DECIMAL = createType("DECIMAL", "十进制数");

            public static final FieldType TIMESTAMP = createType("TIMESTAMP", "时间类型");

            public static final FieldType DATE = createType("DATE", "日期类型");

            /**
             * 本项目该版本支持的字段类型列表
             * @return
             *          本项目该版本支持的字段类型列表
             */
            public static final List<FieldType> TYPE_LIST(){
                return new ArrayList<FieldType>(Arrays.asList(SMALLINT, INT, BIGINT, FLOAT, DOUBLE, BOOLEAN, STRING, VARCHAR, DECIMAL, TIMESTAMP, DATE));
            }

            private static FieldType createType(String type, String dscp){
                return new CommAttr.SQL.FIELD_TYPE.FieldType(type, dscp);
            }
            public static class FieldType{
                public final String TYPE;

                public final String DSCP;

                private FieldType(String type, String dscp){
                    this.DSCP = dscp;
                    this.TYPE = type;
                }
            }

        }

    }

    /**
     * ETL相关
     */
    public static final class ETL{

        /**
         * ETL列类型: 字段列
         */
        public static final String COLUM_TYPE_FIELD = "field";

        /**
         * ETL列类型: 值列
         */
        public static final String COLUM_TYPE_VAR = "var";

        /**
         * ETL TYPE
         */
        public static final class TYPE{

            /**
             * 自定义SQL类型
             */
            public static final String SQL = "SQL";

            /**
             * ETL配置类型
             */
            public static final String ETL = "ETL";
        }

    }

    /**
     * Hive 相关常量
     */
    public static final class HIVE{
        /**
         * Hive日志类型相关常量
         */
        public static final class LOG_TYPE{
            /**
             * SQL 脚本
             */
            public static final String SQL = "SQL";
            /**
             * MapReduce日志
             */
            public static final String REDUCE = "REDUCE";
        }

        /**
         * Hive SQL 执行状态
         */
        public static final class LOG_STATUS{
            /**
             * SQL执行前
             */
            public static final String START = "PRE";

            /**
             * SQL执行中
             */
            public static final String RUNING = "RUNING";
            /**
             * SQL执行成功
             */
            public static final String SUCCESS = "SUCCESS";
            /**
             * SQL执行失败
             */
            public static final String FAIL = "FAIL";
        }
    }

    /**
     * HDFS 相关常量
     */
    public static final class HDFS{

        /**
         * 用户自有文件标识
         */
        public static final String LOCATION_TYPE_FILE_KEY = "FILE_KEY";

        /**
         * HDFS中的任意一个路径
         */
        public static final String LOCATION_TYPE_ABSOLUTE_PATH = "ABSOLUTE_PATH";
    }

    /**
     * 任务相关
     */
    public static final class JOB{

        /**
         * 只执行一次
         */
        public static final String JOB_EXE_TYPE_ONE = "ONE";

        /**
         * 简单按频率
         */
        public static final String JOB_EXE_TYPE_REPACT = "REPACT";

        /**
         * 复杂执行
         */
        public static final String JOB_EXE_TYPE_INFO = "INFO";

        /**
         * 自定义表达式
         */
        public static final String JOB_EXE_TYPE_CRON = "CRON";

        public static final class TYPE{

            /**
             * 数据采集任务
             */
            public static final String COLLECT = "COLLECT";

            /**
             * 导出任务
             */
            public static final String EXPORT = "EXPORT";

            /**
             * 更新任务
             */
            public static final String UPDATE = "UPDATE";
        }

        /**
         * 任务状态
         */
        public static final class STATE{

            /**
             * 等待执行
             */
            public static final String WAITING = "WAITING";

            /**
             * 执行中
             */
            public static final String RUNING = "RUNING";

            /**
             * 执行成功
             */
            public static final String SUCCESS = "SUCCESS";

            /**
             * 执行失败
             */
            public static final String FAIL = "FAIL";

        }

        /**
         * 任务组开关
         */
        public static final class SWITCH{

            /**
             * 运行
             */
            public static final String RUN = "RUN";
            /**
             * 停止
             */
            public static final String STOP = "STOP";
        }
    }

    /**
     * WEBService相关常量
     */
    public static final class WEB_SERVICE{

        /**
         * 连接地址
         */
//        public static final String ADDR = "192.168.8.108:8080";
        public static final String ADDR = "103.160.113.51:9080";
//        public static final String ADDR = "103.160.113.48:9080";
    }

    /**
     * 数据表相关常量
     */
    public static final class TABLE{

        /**
         * 分区字段
         */
        public static final String PARTITION = "partition_day";
    }

}
