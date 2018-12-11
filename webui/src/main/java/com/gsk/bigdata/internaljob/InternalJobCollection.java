package com.gsk.bigdata.internaljob;

import com.aiyi.core.beans.Method;
import com.aiyi.core.sql.where.C;
import com.gsk.bigdata.dao.EtlJobDao;
import com.gsk.bigdata.service.DbManagerService;
import com.webkettle.core.commons.CommAttr;
import com.webkettle.core.entity.CollectionProp;
import com.webkettle.core.hive.log.HiveLoggerCollback;
import com.webkettle.core.utils.CollectionUtil;
import com.webkettle.core.utils.UUIDUtil;
import com.webkettle.sql.SqlCommonExcute;
import com.webkettle.sql.entity.DatabaseVO;
import com.webkettle.sql.entity.EtlJobVO;
import com.webkettle.sql.entity.TableVO;
import com.webkettle.sql.enums.DataBaseGroupEnum;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 内部任务调度
 * @author gsk
 */
@Component
public class InternalJobCollection {

    protected Logger logger = LoggerFactory.getLogger(InternalJobCollection.class);

    private boolean working = false;

    @Value("${bigdata.hdfs.collection-path}")
    private String collPath;
    @Resource
    private FileSystem fileSystem;
    @Resource
    private SqlCommonExcute sqlCommonExcute;
    @Resource
    private EtlJobDao etlJobDao;
    @Resource
    private DbManagerService dbManagerService;

    @Scheduled(cron = "0/10 * * * * ?")
    public void collectionPath(){
        if (working){
            return;
        }
        working = true;
        try {
            Path path = new Path(collPath);

            if (!fileSystem.exists(path)){
                return;
            }
            FileStatus[] fileStatuses = fileSystem.listStatus(path);

            for(FileStatus fileStatus: fileStatuses){
                String fileName = fileStatus.getPath().getName();

                // 后缀是_COPYING_时, 表示正在上传中, 先不做处理
                int hz = fileName.lastIndexOf("_COPYING_");
                if (hz != -1 && fileName.substring(hz).equals("_COPYING_")){
                    continue;
                }

                String pre = "";
                fileName = fileStatus.getPath().getName();
                if (fileName.charAt(0) == 'V' || fileName.charAt(1) == '_'){
                    fileName = fileName.substring(2);
                    pre = "V_";
                }
                if (!fileName.contains("_")){
                    continue;
                }
                String time = fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf("."));
//                if (Integer.valueOf(time).intValue() < Integer.valueOf(DateUtil.formatDate(new Date(), "yyyyMMdd")).intValue()){
//                    continue;
//                }
                String tableName = fileName.substring(0, fileName.lastIndexOf("_"));

                CollectionProp prop = CollectionUtil.hasProp(tableName);
                if (null == prop){
                    continue;
                }
                logger.info("开始采集[{}]", tableName);
                String loadType = prop.getType().equalsIgnoreCase(CommAttr.SQL.SQL_LOAD_APPEND) ?
                        CommAttr.SQL.SQL_LOAD_APPEND: CommAttr.SQL.SQL_LOAD_COVER;


                EtlJobVO etlJobVO = etlJobDao.get(Method.where("UPDATE_TYPE", C.EQ, loadType)
                        .and("JOB_TYPE", C.EQ, CommAttr.JOB.TYPE.COLLECT)
                        .and("IS_DELETE", C.NE, CommAttr.BOOL.Y)
                        .and("TABLE_NAME", C.EQ, prop.getTableName())
                        .and("DATABASE_NAME", C.EQ, prop.getDbName()));

                if (null == etlJobVO){
                    etlJobVO = new EtlJobVO();
                    etlJobVO.setIsDelete(CommAttr.BOOL.N);
                    etlJobVO.setTableName(prop.getTableName());
                    etlJobVO.setDbName(prop.getDbName());
                    etlJobVO.setJobType(CommAttr.JOB.TYPE.COLLECT);
                    etlJobVO.setPathName(collPath + fileName);
                    etlJobVO.setEtlType(CommAttr.JOB.TYPE.COLLECT);
                    etlJobVO.setJobName("internal");
                    etlJobVO.setUpdateType(loadType);
                    etlJobVO.setEtlComment("内部-采集任务");
                    etlJobVO.setCreateUser("admin");
                    etlJobVO.setConfig(null);
                    etlJobVO.setCreateTime(new Date());
                    etlJobDao.add(etlJobVO);
                    etlJobVO = etlJobDao.get(Method.where("UPDATE_TYPE", C.EQ, loadType)
                            .and("JOB_TYPE", C.EQ, CommAttr.JOB.TYPE.COLLECT)
                            .and("IS_DELETE", C.NE, CommAttr.BOOL.Y)
                            .and("TABLE_NAME", C.EQ, prop.getTableName())
                            .and("DATABASE_NAME", C.EQ, prop.getDbName()));
                }

                DatabaseVO databaseVO = new DatabaseVO();
                databaseVO.setDatabaseComment(prop.getDbName());
                databaseVO.setCreateTime(new Date());
                databaseVO.setUserId(1L);
                databaseVO.setDatabaseName(prop.getDbName());
                databaseVO.setDbGroup(DataBaseGroupEnum.SOURCE.getValue());
                databaseVO.setGroupKey(DataBaseGroupEnum.SOURCE.getValue());
                databaseVO.setDatabasetype(DataBaseGroupEnum.SOURCE.getValue());
                dbManagerService.addDatabaseToPl(databaseVO);

                TableVO tableVO = new TableVO();
                tableVO.setTableName(prop.getTableName());
                tableVO.setDatabaseName(prop.getDbName());
                tableVO.setUserId(1L);
                tableVO.setTableComment(prop.getTableName());
                tableVO.setCreateTime(new Date());
                dbManagerService.addTableToPl(tableVO);

                etlJobVO.setLastTimeState(CommAttr.JOB.STATE.RUNING);
                etlJobVO.setExecuteCount(etlJobVO.getExecuteCount() + 1);
                etlJobDao.update(etlJobVO);
                try {
                    sqlCommonExcute.collection(collPath + pre + fileName, prop.getDbName(), prop.getTableName(),
                            new HiveLoggerCollback() {}, loadType, null, UUIDUtil.next());
                }catch (Exception e){
                    etlJobVO.setLastTimeState(CommAttr.JOB.STATE.FAIL);
                    etlJobDao.update(etlJobVO);
                    e.printStackTrace();
                }
                etlJobVO.setLastTimeState(CommAttr.JOB.STATE.SUCCESS);
                etlJobVO.setHasSuccess(CommAttr.BOOL.Y);
                etlJobDao.update(etlJobVO);
            }
        }catch (Exception e){
            logger.info("数据采集异常", e);
        }finally {
            working = false;
        }
    }
}
