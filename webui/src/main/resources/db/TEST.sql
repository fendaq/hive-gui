/*
 Navicat Premium Data Transfer

 Source Server         : oracle
 Source Server Type    : Oracle
 Source Server Version : 110200
 Source Host           : 192.168.8.118:1521
 Source Schema         : TEST

 Target Server Type    : Oracle
 Target Server Version : 110200
 File Encoding         : 65001

 Date: 24/11/2018 16:27:20
*/


-- ----------------------------
-- Table structure for ETL_JOB
-- ----------------------------
DROP TABLE ETL_JOB;
CREATE TABLE ETL_JOB (
  JOB_ID NUMBER(20) NOT NULL ,
  CREAT_TIME NUMBER(14) ,
  ETL_TYPE NVARCHAR2(200) NOT NULL ,
  JOB_NAME NVARCHAR2(200) NOT NULL ,
  ETL_COMMENT NVARCHAR2(200) ,
  CONFIG CLOB ,
  CREAT_USER NVARCHAR2(200) NOT NULL ,
  UPDATE_TYPE NVARCHAR2(200) NOT NULL ,
  PATH_NAME NVARCHAR2(200) NOT NULL ,
  TABLE_NAME NVARCHAR2(200) ,
  DATABASE_NAME NVARCHAR2(200) ,
  EXECUTE_COUNT NUMBER(20) ,
  JOB_TYPE NVARCHAR2(200) ,
  LAST_TIME_STATE NVARCHAR2(200) ,
  IS_DELETE NVARCHAR2(2) ,
  UPDATE_TIME NUMBER(14) ,
  HAS_SUCCESS VARCHAR2(2 BYTE)
)
TABLESPACE USERS
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536
  NEXT 1048576
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;
COMMENT ON COLUMN ETL_JOB.UPDATE_TIME IS '更新时间';
COMMENT ON COLUMN ETL_JOB.HAS_SUCCESS IS '是否成功执行过';

-- ----------------------------
-- Table structure for ETL_JOB_GROUP
-- ----------------------------
DROP TABLE ETL_JOB_GROUP;
CREATE TABLE ETL_JOB_GROUP (
  JOB_GROUP_ID NUMBER(20) NOT NULL ,
  SCHED_NAME NVARCHAR2(120) ,
  JOB_NAME NVARCHAR2(200) ,
  JOB_GROUP NVARCHAR2(200) ,
  CREAT_TIME NUMBER(14) ,
  JOB_GROUP_NAME NVARCHAR2(200) NOT NULL ,
  ETL_COMMENT NVARCHAR2(200) ,
  EXECUTE_COUNT NUMBER(11) ,
  EXECUTE_HZ NVARCHAR2(200) ,
  EXECUTE_HZ_CONTENT NVARCHAR2(200) NOT NULL ,
  EXECUTE_TIME_END NUMBER(14) ,
  START_CONDITION NVARCHAR2(200) ,
  START_CONDITION_MAX_WAIT NVARCHAR2(10) ,
  LAST_TIME NUMBER(14) ,
  LAST_TIME_STATE NVARCHAR2(64) ,
  RETRY_NUM NUMBER(11) NOT NULL ,
  IS_DELETE NCHAR(1) ,
  CREAT_USER NVARCHAR2(64) NOT NULL ,
  EXECUTE_TIME_START NUMBER(14) ,
  RETRY_HZ NUMBER(11) ,
  JOB_SWITCH NVARCHAR2(20)
)
TABLESPACE USERS
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536
  NEXT 1048576
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;
COMMENT ON COLUMN ETL_JOB_GROUP.JOB_GROUP_ID IS '任务组ID';
COMMENT ON COLUMN ETL_JOB_GROUP.CREAT_TIME IS '建立时间';
COMMENT ON COLUMN ETL_JOB_GROUP.JOB_GROUP_NAME IS '任务组名称';
COMMENT ON COLUMN ETL_JOB_GROUP.ETL_COMMENT IS '描述';
COMMENT ON COLUMN ETL_JOB_GROUP.EXECUTE_COUNT IS '执行次数';
COMMENT ON COLUMN ETL_JOB_GROUP.EXECUTE_HZ IS '执行频率';
COMMENT ON COLUMN ETL_JOB_GROUP.EXECUTE_HZ_CONTENT IS '执行频率条件';
COMMENT ON COLUMN ETL_JOB_GROUP.EXECUTE_TIME_END IS '任务组结束时间';
COMMENT ON COLUMN ETL_JOB_GROUP.START_CONDITION IS '开始执行条件';
COMMENT ON COLUMN ETL_JOB_GROUP.START_CONDITION_MAX_WAIT IS '执行条件最长等待时间';
COMMENT ON COLUMN ETL_JOB_GROUP.LAST_TIME IS '上次执行时间';
COMMENT ON COLUMN ETL_JOB_GROUP.LAST_TIME_STATE IS '最近执行状态';
COMMENT ON COLUMN ETL_JOB_GROUP.RETRY_NUM IS '重试次数';
COMMENT ON COLUMN ETL_JOB_GROUP.IS_DELETE IS '是否删除';
COMMENT ON COLUMN ETL_JOB_GROUP.CREAT_USER IS '建立用户';
COMMENT ON COLUMN ETL_JOB_GROUP.EXECUTE_TIME_START IS '任务开始时间';
COMMENT ON COLUMN ETL_JOB_GROUP.RETRY_HZ IS '重试频率';
COMMENT ON TABLE ETL_JOB_GROUP IS '自定义任务组表';

-- ----------------------------
-- Table structure for ETL_JOB_JOBGROUP
-- ----------------------------
DROP TABLE ETL_JOB_JOBGROUP;
CREATE TABLE ETL_JOB_JOBGROUP (
  JOB_JOBGROUP_ID NUMBER(20) NOT NULL ,
  JOB_GROUP_ID NUMBER(20) ,
  JOB_ID NUMBER(20) ,
  ETL_JOB_NUM NUMBER(11)
)
TABLESPACE USERS
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536
  NEXT 1048576
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;
COMMENT ON COLUMN ETL_JOB_JOBGROUP.JOB_JOBGROUP_ID IS '主键ID';
COMMENT ON COLUMN ETL_JOB_JOBGROUP.JOB_GROUP_ID IS '任务组ID';
COMMENT ON COLUMN ETL_JOB_JOBGROUP.JOB_ID IS '主键ID';
COMMENT ON COLUMN ETL_JOB_JOBGROUP.ETL_JOB_NUM IS 'ETL任务序号';

-- ----------------------------
-- Table structure for ETL_LOG
-- ----------------------------
DROP TABLE ETL_LOG;
CREATE TABLE ETL_LOG (
  LOGID NUMBER(20) NOT NULL ,
  UUID NVARCHAR2(120) NOT NULL ,
  ETL_TYPE NVARCHAR2(120) ,
  JOB_ID NUMBER(20) ,
  JOB_GROUP_ID NUMBER(20) ,
  START_TIME NUMBER(14) ,
  END_TIME NUMBER(14) ,
  EXECUTE_TIME NUMBER(14) ,
  EXECUTE_STATE NVARCHAR2(120) NOT NULL ,
  UPDATE_TYPE NVARCHAR2(120) ,
  PARTITION_TAB NVARCHAR2(120) ,
  FILE_NAME NVARCHAR2(200) ,
  ETL_LOG NCLOB ,
  ETL_GROUP_NAME NVARCHAR2(255) ,
  TABLE_NAME NVARCHAR2(255) ,
  IS_DELETE NVARCHAR2(2) ,
  JOB_DISPLAY_NAME NVARCHAR2(255)
)
TABLESPACE USERS
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536
  NEXT 1048576
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;
COMMENT ON COLUMN ETL_LOG.LOGID IS 'LOGID -自增';
COMMENT ON COLUMN ETL_LOG.UUID IS '任务组标识';
COMMENT ON COLUMN ETL_LOG.ETL_TYPE IS '类型';
COMMENT ON COLUMN ETL_LOG.JOB_ID IS '任务ID';
COMMENT ON COLUMN ETL_LOG.JOB_GROUP_ID IS '任务组ID';
COMMENT ON COLUMN ETL_LOG.START_TIME IS '开始执行时间';
COMMENT ON COLUMN ETL_LOG.END_TIME IS '结束时间';
COMMENT ON COLUMN ETL_LOG.EXECUTE_TIME IS '执行时长';
COMMENT ON COLUMN ETL_LOG.EXECUTE_STATE IS '执行状态';
COMMENT ON COLUMN ETL_LOG.UPDATE_TYPE IS '更新方式';
COMMENT ON COLUMN ETL_LOG.PARTITION_TAB IS '分区记录';
COMMENT ON COLUMN ETL_LOG.FILE_NAME IS '采集文件名';
COMMENT ON COLUMN ETL_LOG.ETL_LOG IS '日志';
COMMENT ON COLUMN ETL_LOG.ETL_GROUP_NAME IS '所属组';
COMMENT ON COLUMN ETL_LOG.TABLE_NAME IS '表名';
COMMENT ON COLUMN ETL_LOG.IS_DELETE IS '是否删除';
COMMENT ON COLUMN ETL_LOG.JOB_DISPLAY_NAME IS '任务展示名称';
COMMENT ON TABLE ETL_LOG IS '日志表';

-- ----------------------------
-- Table structure for QRTZ_BLOB_TRIGGERS
-- ----------------------------
DROP TABLE QRTZ_BLOB_TRIGGERS;
CREATE TABLE QRTZ_BLOB_TRIGGERS (
  SCHED_NAME NVARCHAR2(120) NOT NULL ,
  TRIGGER_NAME NVARCHAR2(200) NOT NULL ,
  TRIGGER_GROUP NVARCHAR2(200) NOT NULL ,
  BLOB_DATA BLOB
)
TABLESPACE USERS
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;

-- ----------------------------
-- Table structure for QRTZ_CALENDARS
-- ----------------------------
DROP TABLE QRTZ_CALENDARS;
CREATE TABLE QRTZ_CALENDARS (
  SCHED_NAME NVARCHAR2(120) NOT NULL ,
  CALENDAR_NAME NVARCHAR2(200) NOT NULL ,
  CALENDAR BLOB NOT NULL
)
TABLESPACE USERS
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;

-- ----------------------------
-- Table structure for QRTZ_CRON_TRIGGERS
-- ----------------------------
DROP TABLE QRTZ_CRON_TRIGGERS;
CREATE TABLE QRTZ_CRON_TRIGGERS (
  SCHED_NAME NVARCHAR2(120) NOT NULL ,
  TRIGGER_NAME NVARCHAR2(200) NOT NULL ,
  TRIGGER_GROUP NVARCHAR2(200) NOT NULL ,
  CRON_EXPRESSION NVARCHAR2(120) NOT NULL ,
  TIME_ZONE_ID NVARCHAR2(80)
)
TABLESPACE USERS
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536
  NEXT 1048576
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;

-- ----------------------------
-- Table structure for QRTZ_FIRED_TRIGGERS
-- ----------------------------
DROP TABLE QRTZ_FIRED_TRIGGERS;
CREATE TABLE QRTZ_FIRED_TRIGGERS (
  SCHED_NAME NVARCHAR2(120) NOT NULL ,
  ENTRY_ID NVARCHAR2(95) NOT NULL ,
  TRIGGER_NAME NVARCHAR2(200) NOT NULL ,
  TRIGGER_GROUP NVARCHAR2(200) NOT NULL ,
  INSTANCE_NAME NVARCHAR2(200) NOT NULL ,
  FIRED_TIME NUMBER(20) NOT NULL ,
  SCHED_TIME NUMBER(20) NOT NULL ,
  PRIORITY NUMBER(11) NOT NULL ,
  STATE NVARCHAR2(16) NOT NULL ,
  JOB_NAME NVARCHAR2(200) ,
  JOB_GROUP NVARCHAR2(200) ,
  IS_NONCONCURRENT NVARCHAR2(1) ,
  REQUESTS_RECOVERY NVARCHAR2(1)
)
TABLESPACE USERS
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536
  NEXT 1048576
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;

-- ----------------------------
-- Table structure for QRTZ_JOB_DETAILS
-- ----------------------------
DROP TABLE QRTZ_JOB_DETAILS;
CREATE TABLE QRTZ_JOB_DETAILS (
  SCHED_NAME NVARCHAR2(120) NOT NULL ,
  JOB_NAME NVARCHAR2(200) NOT NULL ,
  JOB_GROUP NVARCHAR2(200) NOT NULL ,
  DESCRIPTION NVARCHAR2(250) ,
  JOB_CLASS_NAME NVARCHAR2(250) NOT NULL ,
  IS_DURABLE NVARCHAR2(1) NOT NULL ,
  IS_NONCONCURRENT NVARCHAR2(1) NOT NULL ,
  IS_UPDATE_DATA NVARCHAR2(1) NOT NULL ,
  REQUESTS_RECOVERY NVARCHAR2(1) NOT NULL ,
  JOB_DATA BLOB
)
TABLESPACE USERS
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536
  NEXT 1048576
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;

-- ----------------------------
-- Table structure for QRTZ_LOCKS
-- ----------------------------
DROP TABLE QRTZ_LOCKS;
CREATE TABLE QRTZ_LOCKS (
  SCHED_NAME NVARCHAR2(120) NOT NULL ,
  LOCK_NAME NVARCHAR2(40) NOT NULL
)
TABLESPACE USERS
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;

-- ----------------------------
-- Table structure for QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
DROP TABLE QRTZ_PAUSED_TRIGGER_GRPS;
CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS (
  SCHED_NAME NVARCHAR2(120) NOT NULL ,
  TRIGGER_GROUP NVARCHAR2(200) NOT NULL
)
TABLESPACE USERS
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;

-- ----------------------------
-- Table structure for QRTZ_SCHEDULER_STATE
-- ----------------------------
DROP TABLE QRTZ_SCHEDULER_STATE;
CREATE TABLE QRTZ_SCHEDULER_STATE (
  SCHED_NAME NVARCHAR2(120) NOT NULL ,
  INSTANCE_NAME NVARCHAR2(200) NOT NULL ,
  LAST_CHECKIN_TIME NUMBER(20) NOT NULL ,
  CHECKIN_INTERVAL NUMBER(20) NOT NULL
)
TABLESPACE USERS
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;

-- ----------------------------
-- Table structure for QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
DROP TABLE QRTZ_SIMPLE_TRIGGERS;
CREATE TABLE QRTZ_SIMPLE_TRIGGERS (
  SCHED_NAME NVARCHAR2(120) NOT NULL ,
  TRIGGER_NAME NVARCHAR2(200) NOT NULL ,
  TRIGGER_GROUP NVARCHAR2(200) NOT NULL ,
  REPEAT_COUNT NUMBER(20) NOT NULL ,
  REPEAT_INTERVAL NUMBER(20) NOT NULL ,
  TIMES_TRIGGERED NUMBER(20) NOT NULL
)
TABLESPACE USERS
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;

-- ----------------------------
-- Table structure for QRTZ_SIMPROP_TRIGGERS
-- ----------------------------
DROP TABLE QRTZ_SIMPROP_TRIGGERS;
CREATE TABLE QRTZ_SIMPROP_TRIGGERS (
  SCHED_NAME NVARCHAR2(120) NOT NULL ,
  TRIGGER_NAME NVARCHAR2(200) NOT NULL ,
  TRIGGER_GROUP NVARCHAR2(200) NOT NULL ,
  STR_PROP_1 NVARCHAR2(512) ,
  STR_PROP_2 NVARCHAR2(512) ,
  STR_PROP_3 NVARCHAR2(512) ,
  INT_PROP_1 NUMBER(11) ,
  INT_PROP_2 NUMBER(11) ,
  LONG_PROP_1 NUMBER(20) ,
  LONG_PROP_2 NUMBER(20) ,
  DEC_PROP_1 NUMBER ,
  DEC_PROP_2 NUMBER ,
  BOOL_PROP_1 NVARCHAR2(1) ,
  BOOL_PROP_2 NVARCHAR2(1)
)
TABLESPACE USERS
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;

-- ----------------------------
-- Table structure for QRTZ_TRIGGERS
-- ----------------------------
DROP TABLE QRTZ_TRIGGERS;
CREATE TABLE QRTZ_TRIGGERS (
  SCHED_NAME NVARCHAR2(120) NOT NULL ,
  TRIGGER_NAME NVARCHAR2(200) NOT NULL ,
  TRIGGER_GROUP NVARCHAR2(200) NOT NULL ,
  JOB_NAME NVARCHAR2(200) NOT NULL ,
  JOB_GROUP NVARCHAR2(200) NOT NULL ,
  DESCRIPTION NVARCHAR2(250) ,
  NEXT_FIRE_TIME NUMBER(20) ,
  PREV_FIRE_TIME NUMBER(20) ,
  PRIORITY NUMBER(11) ,
  TRIGGER_STATE NVARCHAR2(16) NOT NULL ,
  TRIGGER_TYPE NVARCHAR2(8) NOT NULL ,
  START_TIME NUMBER(20) NOT NULL ,
  END_TIME NUMBER(20) ,
  CALENDAR_NAME NVARCHAR2(200) ,
  MISFIRE_INSTR NUMBER(6) ,
  JOB_DATA BLOB
)
TABLESPACE USERS
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536
  NEXT 1048576
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;

-- ----------------------------
-- Table structure for SYS_DATABASE
-- ----------------------------
-- DROP TABLE SYS_DATABASE;
-- CREATE TABLE SYS_DATABASE (
--   DATABASEID NUMBER(20) NOT NULL ,
--   DATABASENAME NVARCHAR2(64) NOT NULL ,
--   DATABASECOMMENT NVARCHAR2(64) NOT NULL ,
--   USERID NUMBER(20) NOT NULL ,
--   CREATTIME DATE ,
--   SYSTEMID NUMBER(20) NOT NULL ,
--   DB_GROUP NVARCHAR2(16)
-- )
-- TABLESPACE USERS
-- LOGGING
-- NOCOMPRESS
-- PCTFREE 10
-- INITRANS 1
-- STORAGE (
--   INITIAL 65536
--   NEXT 1048576
--   MINEXTENTS 1
--   MAXEXTENTS 2147483645
--   BUFFER_POOL DEFAULT
-- )
-- PARALLEL 1
-- NOCACHE
-- DISABLE ROW MOVEMENT
-- ;
-- COMMENT ON COLUMN SYS_DATABASE.DATABASEID IS '库ID';
-- COMMENT ON COLUMN SYS_DATABASE.DATABASENAME IS '库名';
-- COMMENT ON COLUMN SYS_DATABASE.DATABASECOMMENT IS '库别名';
-- COMMENT ON COLUMN SYS_DATABASE.USERID IS '建立用户id';
-- COMMENT ON COLUMN SYS_DATABASE.CREATTIME IS '建立时间';
-- COMMENT ON COLUMN SYS_DATABASE.SYSTEMID IS '系统ID';
-- COMMENT ON TABLE SYS_DATABASE IS 'hive库信息表';

-- ----------------------------
-- Table structure for SYS_DATABASE_TABLE
-- ----------------------------
-- DROP TABLE SYS_DATABASE_TABLE;
-- CREATE TABLE SYS_DATABASE_TABLE (
--   DATABASETABLEID NUMBER(6) NOT NULL ,
--   TABLEID NUMBER(6) ,
--   DATABASEID NUMBER(6)
-- )
-- TABLESPACE USERS
-- LOGGING
-- NOCOMPRESS
-- PCTFREE 10
-- INITRANS 1
-- STORAGE (
--   BUFFER_POOL DEFAULT
-- )
-- PARALLEL 1
-- NOCACHE
-- DISABLE ROW MOVEMENT
-- ;
-- COMMENT ON COLUMN SYS_DATABASE_TABLE.DATABASETABLEID IS '主键ID';
-- COMMENT ON COLUMN SYS_DATABASE_TABLE.TABLEID IS '表ID';
-- COMMENT ON COLUMN SYS_DATABASE_TABLE.DATABASEID IS '库ID';
-- COMMENT ON TABLE SYS_DATABASE_TABLE IS '表库关系';

-- ----------------------------
-- Table structure for SYS_DATAROLE_DATABASE
-- ----------------------------
-- DROP TABLE SYS_DATAROLE_DATABASE;
-- CREATE TABLE SYS_DATAROLE_DATABASE (
--   DATAROLEDATABASEID NUMBER(6) NOT NULL ,
--   DATABASEID NUMBER(6) ,
--   DATAROLEID NUMBER(6)
-- )
-- TABLESPACE USERS
-- LOGGING
-- NOCOMPRESS
-- PCTFREE 10
-- INITRANS 1
-- STORAGE (
--   BUFFER_POOL DEFAULT
-- )
-- PARALLEL 1
-- NOCACHE
-- DISABLE ROW MOVEMENT
-- ;
-- COMMENT ON COLUMN SYS_DATAROLE_DATABASE.DATAROLEDATABASEID IS '主键ID';
-- COMMENT ON COLUMN SYS_DATAROLE_DATABASE.DATABASEID IS '库ID';
-- COMMENT ON COLUMN SYS_DATAROLE_DATABASE.DATAROLEID IS '角色ID';
-- COMMENT ON TABLE SYS_DATAROLE_DATABASE IS '角色与库关系表';

-- ----------------------------
-- Table structure for SYS_DATAROLE_TABLE
-- ----------------------------
-- DROP TABLE SYS_DATAROLE_TABLE;
-- CREATE TABLE SYS_DATAROLE_TABLE (
--   DATAROLETABLEID NUMBER(6) NOT NULL ,
--   DATAROLEID NUMBER(6) ,
--   TABLEID NUMBER(6)
-- )
-- TABLESPACE USERS
-- LOGGING
-- NOCOMPRESS
-- PCTFREE 10
-- INITRANS 1
-- STORAGE (
--   BUFFER_POOL DEFAULT
-- )
-- PARALLEL 1
-- NOCACHE
-- DISABLE ROW MOVEMENT
-- ;
-- COMMENT ON COLUMN SYS_DATAROLE_TABLE.DATAROLETABLEID IS '主键ID';
-- COMMENT ON COLUMN SYS_DATAROLE_TABLE.DATAROLEID IS '角色ID';
-- COMMENT ON COLUMN SYS_DATAROLE_TABLE.TABLEID IS '表ID';
-- COMMENT ON TABLE SYS_DATAROLE_TABLE IS '角色与表关系表';

-- ----------------------------
-- Table structure for SYS_ORG_DATABASE
-- ----------------------------
-- DROP TABLE SYS_ORG_DATABASE;
-- CREATE TABLE SYS_ORG_DATABASE (
--   ORGDATABASEID NUMBER(6) NOT NULL ,
--   DATABASEID NUMBER(6) ,
--   ORGID NUMBER(6) ,
--   ALLOWSELECT NUMBER(6) ,
--   ALLOWINSERT NUMBER(6) ,
--   ALLOWALTER NUMBER(6) ,
--   ALLOWEXPORT NUMBER(6) ,
--   ALLOWDROPTABLE NUMBER(6) ,
--   ALLOWDROPDATABASE NUMBER(6)
-- )
-- TABLESPACE USERS
-- LOGGING
-- NOCOMPRESS
-- PCTFREE 10
-- INITRANS 1
-- STORAGE (
--   BUFFER_POOL DEFAULT
-- )
-- PARALLEL 1
-- NOCACHE
-- DISABLE ROW MOVEMENT
-- ;
-- COMMENT ON COLUMN SYS_ORG_DATABASE.ORGDATABASEID IS '主键ID';
-- COMMENT ON COLUMN SYS_ORG_DATABASE.DATABASEID IS '库ID';
-- COMMENT ON COLUMN SYS_ORG_DATABASE.ORGID IS '组织ID';
-- COMMENT ON COLUMN SYS_ORG_DATABASE.ALLOWSELECT IS '允许查询';
-- COMMENT ON COLUMN SYS_ORG_DATABASE.ALLOWINSERT IS '允许写入';
-- COMMENT ON COLUMN SYS_ORG_DATABASE.ALLOWALTER IS '允许修改';
-- COMMENT ON COLUMN SYS_ORG_DATABASE.ALLOWEXPORT IS '允许导出';
-- COMMENT ON COLUMN SYS_ORG_DATABASE.ALLOWDROPTABLE IS '允许删表';
-- COMMENT ON COLUMN SYS_ORG_DATABASE.ALLOWDROPDATABASE IS '允许删库';
-- COMMENT ON TABLE SYS_ORG_DATABASE IS '用户组与库的关系';

-- ----------------------------
-- Table structure for SYS_TABLE
-- ----------------------------
-- DROP TABLE SYS_TABLE;
-- CREATE TABLE SYS_TABLE (
--   TABLEID NUMBER(20) NOT NULL ,
--   TABLENAME NVARCHAR2(64) NOT NULL ,
--   TABLECOMMENT NVARCHAR2(64) NOT NULL ,
--   COLUMNNAME NVARCHAR2(64) ,
--   COLUMNCOMMENT NVARCHAR2(64) ,
--   DATABASENAME NVARCHAR2(64) NOT NULL ,
--   USERID NUMBER(6) NOT NULL ,
--   CREATTIME DATE ,
--   UPDATEID NUMBER(6) ,
--   UPDATETIME DATE NOT NULL ,
--   SYSTEMID NUMBER(6) NOT NULL
-- )
-- TABLESPACE USERS
-- LOGGING
-- NOCOMPRESS
-- PCTFREE 10
-- INITRANS 1
-- STORAGE (
--   INITIAL 65536
--   NEXT 1048576
--   MINEXTENTS 1
--   MAXEXTENTS 2147483645
--   BUFFER_POOL DEFAULT
-- )
-- PARALLEL 1
-- NOCACHE
-- DISABLE ROW MOVEMENT
-- ;
-- COMMENT ON COLUMN SYS_TABLE.TABLEID IS '表ID';
-- COMMENT ON COLUMN SYS_TABLE.TABLENAME IS '表名';
-- COMMENT ON COLUMN SYS_TABLE.TABLECOMMENT IS '表别名';
-- COMMENT ON COLUMN SYS_TABLE.COLUMNNAME IS '列名';
-- COMMENT ON COLUMN SYS_TABLE.COLUMNCOMMENT IS '列别名';
-- COMMENT ON COLUMN SYS_TABLE.DATABASENAME IS '所属库';
-- COMMENT ON COLUMN SYS_TABLE.USERID IS '建立用户id';
-- COMMENT ON COLUMN SYS_TABLE.CREATTIME IS '建立时间';
-- COMMENT ON COLUMN SYS_TABLE.UPDATEID IS '修改用户id';
-- COMMENT ON COLUMN SYS_TABLE.UPDATETIME IS '修改时间';
-- COMMENT ON COLUMN SYS_TABLE.SYSTEMID IS '系统ID';
-- COMMENT ON TABLE SYS_TABLE IS 'hive表的信息';

-- ----------------------------
-- Table structure for SYS_USER_CONF
-- ----------------------------
DROP TABLE SYS_USER_CONF;
CREATE TABLE SYS_USER_CONF (
  CONF_ID NUMBER NOT NULL ,
  USER_ID NUMBER ,
  CONF NCLOB
)
TABLESPACE USERS
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536
  NEXT 1048576
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;

-- ----------------------------
-- Table structure for SYS_USER_DATABASE
-- ----------------------------
-- DROP TABLE SYS_USER_DATABASE;
-- CREATE TABLE SYS_USER_DATABASE (
--   USERDATABASEID NUMBER(6) NOT NULL ,
--   USERID NUMBER(6) ,
--   DATABASEID NUMBER(6) ,
--   ALLOWSELECT NUMBER(6) ,
--   ALLOWINSERT NUMBER(6) ,
--   ALLOWALTER NUMBER(6) ,
--   ALLOWEXPORT NUMBER(6) ,
--   ALLOWDROPTABLE NUMBER(6) ,
--   ALLOWDROPDATABASE NUMBER(6)
-- )
-- TABLESPACE USERS
-- LOGGING
-- NOCOMPRESS
-- PCTFREE 10
-- INITRANS 1
-- STORAGE (
--   INITIAL 65536
--   NEXT 1048576
--   MINEXTENTS 1
--   MAXEXTENTS 2147483645
--   BUFFER_POOL DEFAULT
-- )
-- PARALLEL 1
-- NOCACHE
-- DISABLE ROW MOVEMENT
-- ;
-- COMMENT ON COLUMN SYS_USER_DATABASE.USERDATABASEID IS '主键ID';
-- COMMENT ON COLUMN SYS_USER_DATABASE.USERID IS '用户ID';
-- COMMENT ON COLUMN SYS_USER_DATABASE.DATABASEID IS '库ID';
-- COMMENT ON COLUMN SYS_USER_DATABASE.ALLOWSELECT IS '允许查询';
-- COMMENT ON COLUMN SYS_USER_DATABASE.ALLOWINSERT IS '允许写入';
-- COMMENT ON COLUMN SYS_USER_DATABASE.ALLOWALTER IS '允许修改';
-- COMMENT ON COLUMN SYS_USER_DATABASE.ALLOWEXPORT IS '允许导出';
-- COMMENT ON COLUMN SYS_USER_DATABASE.ALLOWDROPTABLE IS '允许删表';
-- COMMENT ON COLUMN SYS_USER_DATABASE.ALLOWDROPDATABASE IS '允许删库';
-- COMMENT ON TABLE SYS_USER_DATABASE IS '用户与库的关系';

-- -- ----------------------------
-- -- Table structure for SYS_USER_TABLE
-- -- ----------------------------
-- DROP TABLE SYS_USER_TABLE;
-- CREATE TABLE SYS_USER_TABLE (
--   USERTABLEID NUMBER(6) NOT NULL ,
--   USERID NUMBER(6) ,
--   TABLEID NUMBER(6) ,
--   ALLOWSELECT NUMBER(6) ,
--   ALLOWINSERT NUMBER(6) ,
--   ALLOWALTER NUMBER(6) ,
--   ALLOWEXPORT NUMBER(6) ,
--   ALLOWDROPTABLE NUMBER(6)
-- )
-- TABLESPACE USERS
-- LOGGING
-- NOCOMPRESS
-- PCTFREE 10
-- INITRANS 1
-- STORAGE (
--   INITIAL 65536
--   NEXT 1048576
--   MINEXTENTS 1
--   MAXEXTENTS 2147483645
--   BUFFER_POOL DEFAULT
-- )
-- PARALLEL 1
-- NOCACHE
-- DISABLE ROW MOVEMENT
-- ;
-- COMMENT ON COLUMN SYS_USER_TABLE.USERTABLEID IS '主键ID';
-- COMMENT ON COLUMN SYS_USER_TABLE.USERID IS '用户ID';
-- COMMENT ON COLUMN SYS_USER_TABLE.TABLEID IS '表ID';
-- COMMENT ON COLUMN SYS_USER_TABLE.ALLOWSELECT IS '允许查询';
-- COMMENT ON COLUMN SYS_USER_TABLE.ALLOWINSERT IS '允许写入';
-- COMMENT ON COLUMN SYS_USER_TABLE.ALLOWALTER IS '允许修改';
-- COMMENT ON COLUMN SYS_USER_TABLE.ALLOWEXPORT IS '允许导出';
-- COMMENT ON COLUMN SYS_USER_TABLE.ALLOWDROPTABLE IS '允许删表';
-- COMMENT ON TABLE SYS_USER_TABLE IS '用户与表的关系';

-- -- ----------------------------
-- -- Table structure for TASK_EXECUTION_LOG
-- -- ----------------------------
-- DROP TABLE TASK_EXECUTION_LOG;
-- CREATE TABLE TASK_EXECUTION_LOG (
--   fireId NUMBER(20) NOT NULL ,
--   jobName NVARCHAR2(500) ,
--   startTime DATE NOT NULL ,
--   endTime DATE NOT NULL ,
--   execMethod NVARCHAR2(100) ,
--   status NVARCHAR2(50) ,
--   executionConfiguration NCLOB ,
--   executionLog NCLOB ,
--   type NVARCHAR2(20)
-- )
-- TABLESPACE USERS
-- LOGGING
-- NOCOMPRESS
-- PCTFREE 10
-- INITRANS 1
-- STORAGE (
--   BUFFER_POOL DEFAULT
-- )
-- PARALLEL 1
-- NOCACHE
-- DISABLE ROW MOVEMENT
-- ;

-- ----------------------------
-- Checks structure for table ETL_JOB
-- ----------------------------
ALTER TABLE ETL_JOB ADD CONSTRAINT SYS_C0011370 CHECK (JOB_ID IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
ALTER TABLE ETL_JOB ADD CONSTRAINT SYS_C0011371 CHECK (ETL_TYPE IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
ALTER TABLE ETL_JOB ADD CONSTRAINT SYS_C0011372 CHECK (JOB_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
ALTER TABLE ETL_JOB ADD CONSTRAINT SYS_C0011373 CHECK (CREAT_USER IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
ALTER TABLE ETL_JOB ADD CONSTRAINT SYS_C0011374 CHECK (UPDATE_TYPE IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
ALTER TABLE ETL_JOB ADD CONSTRAINT SYS_C0011375 CHECK (PATH_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table ETL_JOB_GROUP
-- ----------------------------
ALTER TABLE ETL_JOB_GROUP ADD CONSTRAINT SYS_C0011101 CHECK (JOB_GROUP_ID IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
ALTER TABLE ETL_JOB_GROUP ADD CONSTRAINT SYS_C0011102 CHECK (JOB_GROUP_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
ALTER TABLE ETL_JOB_GROUP ADD CONSTRAINT SYS_C0011103 CHECK (EXECUTE_HZ_CONTENT IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
ALTER TABLE ETL_JOB_GROUP ADD CONSTRAINT SYS_C0011104 CHECK (RETRY_NUM IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
ALTER TABLE ETL_JOB_GROUP ADD CONSTRAINT SYS_C0011105 CHECK (CREAT_USER IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table ETL_JOB_JOBGROUP
-- ----------------------------
ALTER TABLE ETL_JOB_JOBGROUP ADD CONSTRAINT SYS_C0011106 CHECK (JOB_JOBGROUP_ID IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table ETL_LOG
-- ----------------------------
ALTER TABLE ETL_LOG ADD CONSTRAINT SYS_C0011107 CHECK (LOGID IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
ALTER TABLE ETL_LOG ADD CONSTRAINT SYS_C0011108 CHECK (UUID IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
ALTER TABLE ETL_LOG ADD CONSTRAINT SYS_C0011110 CHECK (EXECUTE_STATE IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table QRTZ_BLOB_TRIGGERS
-- ----------------------------
-- ALTER TABLE QRTZ_BLOB_TRIGGERS ADD CONSTRAINT SYS_C0011112 CHECK (SCHED_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_BLOB_TRIGGERS ADD CONSTRAINT SYS_C0011113 CHECK (TRIGGER_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_BLOB_TRIGGERS ADD CONSTRAINT SYS_C0011114 CHECK (TRIGGER_GROUP IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table QRTZ_CALENDARS
-- ----------------------------
-- ALTER TABLE QRTZ_CALENDARS ADD CONSTRAINT SYS_C0011115 CHECK (SCHED_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_CALENDARS ADD CONSTRAINT SYS_C0011116 CHECK (CALENDAR_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_CALENDARS ADD CONSTRAINT SYS_C0011117 CHECK (CALENDAR IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table QRTZ_CRON_TRIGGERS
-- ----------------------------
-- ALTER TABLE QRTZ_CRON_TRIGGERS ADD CONSTRAINT SYS_C0011118 CHECK (SCHED_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_CRON_TRIGGERS ADD CONSTRAINT SYS_C0011119 CHECK (TRIGGER_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_CRON_TRIGGERS ADD CONSTRAINT SYS_C0011120 CHECK (TRIGGER_GROUP IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_CRON_TRIGGERS ADD CONSTRAINT SYS_C0011121 CHECK (CRON_EXPRESSION IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table QRTZ_FIRED_TRIGGERS
-- ----------------------------
-- ALTER TABLE QRTZ_FIRED_TRIGGERS ADD CONSTRAINT SYS_C0011122 CHECK (SCHED_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_FIRED_TRIGGERS ADD CONSTRAINT SYS_C0011123 CHECK (ENTRY_ID IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_FIRED_TRIGGERS ADD CONSTRAINT SYS_C0011124 CHECK (TRIGGER_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_FIRED_TRIGGERS ADD CONSTRAINT SYS_C0011125 CHECK (TRIGGER_GROUP IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_FIRED_TRIGGERS ADD CONSTRAINT SYS_C0011126 CHECK (INSTANCE_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_FIRED_TRIGGERS ADD CONSTRAINT SYS_C0011127 CHECK (FIRED_TIME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_FIRED_TRIGGERS ADD CONSTRAINT SYS_C0011128 CHECK (SCHED_TIME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_FIRED_TRIGGERS ADD CONSTRAINT SYS_C0011129 CHECK (PRIORITY IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_FIRED_TRIGGERS ADD CONSTRAINT SYS_C0011130 CHECK (STATE IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table QRTZ_JOB_DETAILS
-- ----------------------------
-- ALTER TABLE QRTZ_JOB_DETAILS ADD CONSTRAINT SYS_C0011131 CHECK (SCHED_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_JOB_DETAILS ADD CONSTRAINT SYS_C0011132 CHECK (JOB_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_JOB_DETAILS ADD CONSTRAINT SYS_C0011133 CHECK (JOB_GROUP IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_JOB_DETAILS ADD CONSTRAINT SYS_C0011134 CHECK (JOB_CLASS_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_JOB_DETAILS ADD CONSTRAINT SYS_C0011135 CHECK (IS_DURABLE IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_JOB_DETAILS ADD CONSTRAINT SYS_C0011136 CHECK (IS_NONCONCURRENT IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_JOB_DETAILS ADD CONSTRAINT SYS_C0011137 CHECK (IS_UPDATE_DATA IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_JOB_DETAILS ADD CONSTRAINT SYS_C0011138 CHECK (REQUESTS_RECOVERY IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table QRTZ_LOCKS
-- ----------------------------
-- ALTER TABLE QRTZ_LOCKS ADD CONSTRAINT SYS_C0011139 CHECK (SCHED_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_LOCKS ADD CONSTRAINT SYS_C0011140 CHECK (LOCK_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
-- ALTER TABLE QRTZ_PAUSED_TRIGGER_GRPS ADD CONSTRAINT SYS_C0011141 CHECK (SCHED_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_PAUSED_TRIGGER_GRPS ADD CONSTRAINT SYS_C0011142 CHECK (TRIGGER_GROUP IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table QRTZ_SCHEDULER_STATE
-- ----------------------------
-- ALTER TABLE QRTZ_SCHEDULER_STATE ADD CONSTRAINT SYS_C0011143 CHECK (SCHED_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_SCHEDULER_STATE ADD CONSTRAINT SYS_C0011144 CHECK (INSTANCE_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_SCHEDULER_STATE ADD CONSTRAINT SYS_C0011145 CHECK (LAST_CHECKIN_TIME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_SCHEDULER_STATE ADD CONSTRAINT SYS_C0011146 CHECK (CHECKIN_INTERVAL IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
-- ALTER TABLE QRTZ_SIMPLE_TRIGGERS ADD CONSTRAINT SYS_C0011147 CHECK (SCHED_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_SIMPLE_TRIGGERS ADD CONSTRAINT SYS_C0011148 CHECK (TRIGGER_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_SIMPLE_TRIGGERS ADD CONSTRAINT SYS_C0011149 CHECK (TRIGGER_GROUP IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_SIMPLE_TRIGGERS ADD CONSTRAINT SYS_C0011150 CHECK (REPEAT_COUNT IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_SIMPLE_TRIGGERS ADD CONSTRAINT SYS_C0011151 CHECK (REPEAT_INTERVAL IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_SIMPLE_TRIGGERS ADD CONSTRAINT SYS_C0011152 CHECK (TIMES_TRIGGERED IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table QRTZ_SIMPROP_TRIGGERS
-- ----------------------------
-- ALTER TABLE QRTZ_SIMPROP_TRIGGERS ADD CONSTRAINT SYS_C0011153 CHECK (SCHED_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_SIMPROP_TRIGGERS ADD CONSTRAINT SYS_C0011154 CHECK (TRIGGER_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_SIMPROP_TRIGGERS ADD CONSTRAINT SYS_C0011155 CHECK (TRIGGER_GROUP IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table QRTZ_TRIGGERS
-- ----------------------------
-- ALTER TABLE QRTZ_TRIGGERS ADD CONSTRAINT SYS_C0011156 CHECK (SCHED_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_TRIGGERS ADD CONSTRAINT SYS_C0011157 CHECK (TRIGGER_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_TRIGGERS ADD CONSTRAINT SYS_C0011158 CHECK (TRIGGER_GROUP IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_TRIGGERS ADD CONSTRAINT SYS_C0011159 CHECK (JOB_NAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_TRIGGERS ADD CONSTRAINT SYS_C0011160 CHECK (JOB_GROUP IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_TRIGGERS ADD CONSTRAINT SYS_C0011161 CHECK (TRIGGER_STATE IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_TRIGGERS ADD CONSTRAINT SYS_C0011162 CHECK (TRIGGER_TYPE IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE QRTZ_TRIGGERS ADD CONSTRAINT SYS_C0011163 CHECK (START_TIME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table SYS_DATABASE
-- ----------------------------
-- ALTER TABLE SYS_DATABASE ADD CONSTRAINT SYS_C0011164 CHECK (DATABASEID IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE SYS_DATABASE ADD CONSTRAINT SYS_C0011165 CHECK (DATABASENAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE SYS_DATABASE ADD CONSTRAINT SYS_C0011166 CHECK (DATABASECOMMENT IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE SYS_DATABASE ADD CONSTRAINT SYS_C0011167 CHECK (USERID IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE SYS_DATABASE ADD CONSTRAINT SYS_C0011168 CHECK (SYSTEMID IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table SYS_DATABASE_TABLE
-- ----------------------------
-- ALTER TABLE SYS_DATABASE_TABLE ADD CONSTRAINT SYS_C0011169 CHECK (DATABASETABLEID IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table SYS_DATAROLE_DATABASE
-- ----------------------------
-- ALTER TABLE SYS_DATAROLE_DATABASE ADD CONSTRAINT SYS_C0011170 CHECK (DATAROLEDATABASEID IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table SYS_DATAROLE_TABLE
-- ----------------------------
-- ALTER TABLE SYS_DATAROLE_TABLE ADD CONSTRAINT SYS_C0011171 CHECK (DATAROLETABLEID IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table SYS_ORG_DATABASE
-- ----------------------------
-- ALTER TABLE SYS_ORG_DATABASE ADD CONSTRAINT SYS_C0011172 CHECK (ORGDATABASEID IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table SYS_TABLE
-- ----------------------------
-- ALTER TABLE SYS_TABLE ADD CONSTRAINT SYS_C0011173 CHECK (TABLEID IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE SYS_TABLE ADD CONSTRAINT SYS_C0011174 CHECK (TABLENAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE SYS_TABLE ADD CONSTRAINT SYS_C0011175 CHECK (TABLECOMMENT IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE SYS_TABLE ADD CONSTRAINT SYS_C0011176 CHECK (DATABASENAME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE SYS_TABLE ADD CONSTRAINT SYS_C0011177 CHECK (USERID IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE SYS_TABLE ADD CONSTRAINT SYS_C0011178 CHECK (UPDATETIME IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE SYS_TABLE ADD CONSTRAINT SYS_C0011179 CHECK (SYSTEMID IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Primary Key structure for table SYS_USER_CONF
-- ----------------------------
ALTER TABLE SYS_USER_CONF ADD CONSTRAINT SYS_C0011330 PRIMARY KEY (CONF_ID);

-- ----------------------------
-- Checks structure for table SYS_USER_CONF
-- ----------------------------
ALTER TABLE SYS_USER_CONF ADD CONSTRAINT SYS_C0011329 CHECK (CONF_ID IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table SYS_USER_DATABASE
-- ----------------------------
-- ALTER TABLE SYS_USER_DATABASE ADD CONSTRAINT SYS_C0011180 CHECK (USERDATABASEID IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table SYS_USER_TABLE
-- ----------------------------
-- ALTER TABLE SYS_USER_TABLE ADD CONSTRAINT SYS_C0011181 CHECK (USERTABLEID IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Checks structure for table TASK_EXECUTION_LOG
-- ----------------------------
-- ALTER TABLE TASK_EXECUTION_LOG ADD CONSTRAINT SYS_C0011182 CHECK (fireId IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE TASK_EXECUTION_LOG ADD CONSTRAINT SYS_C0011183 CHECK (startTime IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
-- ALTER TABLE TASK_EXECUTION_LOG ADD CONSTRAINT SYS_C0011184 CHECK (endTime IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
