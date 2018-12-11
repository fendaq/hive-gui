package com.gsk.bigdata.service.impl;

import com.aiyi.core.beans.Method;
import com.aiyi.core.sql.where.C;
import com.alibaba.fastjson.JSON;
import com.gsk.bigdata.dao.EtlJobDao;
import com.gsk.bigdata.service.DbManagerService;
import com.webkettle.core.commons.CommAttr;
import com.webkettle.core.utils.ContextUtil;
import com.webkettle.core.utils.UUIDUtil;
import com.webkettle.sql.ETLUtil;
import com.webkettle.sql.entity.EtlJobVO;
import com.webkettle.sql.entity.TableVO;
import com.webkettle.sql.entity.jobcreate.TableCreatedConf;
import com.webkettle.sql.entity.jobcreate.TableEtlConf;
import com.webkettle.sql.service.TableCreateJobManagerService;
import com.webkettle.webservice.client.UserDetailsService;
import org.apache.http.client.utils.DateUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

import javax.annotation.Resource;
import javax.validation.ValidationException;

/**
 * ETL建表任务处理类
 * @author gsk
 */
@Service("tableCreateETLJobManagerService")
public class TableCreateETLJobManagerService implements TableCreateJobManagerService {

    @Resource
    private EtlJobDao etlJobDao;

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private DbManagerService dbManagerService;

    @Override
    public int saveConfig(String dbName, TableCreatedConf config) {
        TableEtlConf etl = config.getEtl();
        if (null == etl){
            throw new RuntimeException("请提交ETL配置信息");
        }
        try {
            ETLUtil.conf2Sql(etl);
        }catch (Exception e){
            if (e instanceof ValidationException){
                throw e;
            }
            throw new RuntimeException("ETL校验错误, 请合理配置ETL", e);
        }

        // 表入库
        String tableName = etl.getTableName();
        TableVO tableVO = new TableVO();
        tableVO.setTableName(tableName);
        tableVO.setCreateTime(new Date());
        tableVO.setDatabaseName(dbName);
        tableVO.setSystemId(0);
        tableVO.setUpdateId((int) ContextUtil.getUserId());
        tableVO.setTableComment(tableName);
        dbManagerService.addTableToPl(tableVO);
//        userDetailsService.addSysTable(JSON.toJSONString(tableVO));

        // 建表信息入库
        String jobName = UUIDUtil.next();
        String jobDisplayName = config.getJobDisplayName();
        if (StringUtils.isEmpty(jobDisplayName)){
            jobDisplayName = "ETL任务[" + DateUtils.formatDate(new Date(), "yyyyMMddHHmmss") + "]";
        }
        EtlJobVO etlJobVO = new EtlJobVO();
        etlJobVO.setConfig(JSON.toJSONString(etl));
        etlJobVO.setCreateTime(new Date());
        etlJobVO.setCreateUser(ContextUtil.getUserName());
        etlJobVO.setEtlComment(jobDisplayName);
        etlJobVO.setJobName(jobName);
        etlJobVO.setUpdateType(CommAttr.SQL.SQL_LOAD_APPEND);
        etlJobVO.setEtlType("ETL");
        etlJobVO.setPathName("/");
        etlJobVO.setJobType(CommAttr.JOB.TYPE.UPDATE);
        etlJobVO.setIsDelete(CommAttr.BOOL.N);
        etlJobVO.setTableName(tableName);
        etlJobVO.setDbName(dbName);
        etlJobDao.add(etlJobVO);

        return etlJobDao.get(Method.where("JOB_NAME", C.EQ, jobName)).getJobId();
    }

    @Override
    public void doCreate(String config) {

    }

    @Override
    public String getJobItemType() {
        return null;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}
