package com.gsk.bigdata.dao;

import com.aiyi.core.dao.impl.DaoImpl;
import com.aiyi.core.util.SqlUtil;
import com.webkettle.core.utils.ContextUtil;
import com.webkettle.sql.entity.EtlJobVO;
import com.webkettle.sql.entity.SysUserVO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * 任务单元持久层
 * @author gsk
 */
@Repository
public class EtlJobDao extends DaoImpl<EtlJobVO, Integer> {

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * 获取所有任务列表
     * @return
     */
    public List<EtlJobVO> listAll(){
        HashMap<String, String> params = new HashMap<>();
        SysUserVO userEntity = ContextUtil.getUserEntity();
        params.put("userName", userEntity.getAccount());
        List<HashMap> list = sqlSessionTemplate.selectList("listAllJobs", params);
        LinkedList<EtlJobVO> result = new LinkedList<>();
        for (HashMap<String, Object> map: list){
            result.addLast(handleResult(map, EtlJobVO.class));
        }
        return result;
    }

    /**
     * 查询临时任务列表
     * @return
     */
    public List<EtlJobVO> listTempJobs(){
        HashMap<String, String> params = new HashMap<>();
        SysUserVO userEntity = ContextUtil.getUserEntity();
        params.put("userName", userEntity.getAccount());
        List<HashMap> listPage = sqlSessionTemplate.selectList("listTempJobs", params);
        LinkedList<EtlJobVO> result = new LinkedList<>();
        for (HashMap<String, Object> map: listPage){
            result.addLast(handleResult(map, EtlJobVO.class));
        }
        return result;
    }

    /**
     * 获取Clob字段
     * @return
     *          插入后的ID
     */
    public long insertJob(EtlJobVO etlJobVO){
        Connection conn = sqlSessionTemplate.getConnection();
        try {
            // 禁止自动提交
            conn.setAutoCommit(false);

            // 插入记录
            String sql1 = "insert into ETL_JOB(" +
                    "       JOB_ID,CREAT_TIME,ETL_TYPE,JOB_NAME," +
                    "       ETL_COMMENT,CONFIG,CREAT_USER,UPDATE_TYPE," +
                    "       PATH_NAME,TABLE_NAME,DATABASE_NAME,EXECUTE_COUNT," +
                    "       JOB_TYPE,LAST_TIME_STATE,IS_DELETE,UPDATE_TIME" +
                    ") values (" +
                    "       SEQ_ID.NEXTVAL,?,?,?,?,EMPTY_CLOB(),?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setDate(1, new Date(System.currentTimeMillis()));
            ps1.setString(2, etlJobVO.getEtlType());
            ps1.setString(3, etlJobVO.getJobName());
            ps1.setString(4, etlJobVO.getEtlComment());
            ps1.setString(5, etlJobVO.getCreateUser());
            ps1.setString(6, etlJobVO.getUpdateType());
            ps1.setString(7, etlJobVO.getPathName());
            ps1.setString(8, etlJobVO.getTableName());
            ps1.setString(9, etlJobVO.getDbName());
            ps1.setLong(10, etlJobVO.getExecuteCount());
            ps1.setString(11, etlJobVO.getJobType());
            ps1.setString(12, etlJobVO.getLastTimeState());
            ps1.setString(13, etlJobVO.getIsDelete());
            ps1.setDate(14, null);
            ps1.executeUpdate();
            ps1.close();

            // 插入大字段
            String sql2 = "SELECT JOB_ID, CONFIG FROM ETL_JOB WHERE JOB_NAME = ?";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()){
//                etlJobVO.setJobId(rs2.getInt(1));
//                oracle.sql.CLOB clob = (oracle.sql.CLOB)rs2.getClob(2);
//                BufferedWriter out = new BufferedWriter(clob.getCharacterOutputStream());
//                String content = etlJobVO.getConfig();
//                out.write(content,0,content.length());
//                out.close();
            }
            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException("大字段存储失败", e);
        }
        return etlJobVO.getJobId();
    }

    /**
     * 条件搜索任务列表
     * @param jobType
     *          任务类型
     * @param dbName
     *          任务名称
     * @param groupId
     *          任务组ID
     * @param tableName
     *          表名
     * @param startNm
     *          开始长度
     * @param length
     *          每页条数
     * @return
     */
    public List<EtlJobVO> listPage(String jobType, String dbName, String groupId, String tableName,
                                   String hasSuccess, Integer startNm, Integer length){
        Map<String, Object> params = new HashMap<>();
        SysUserVO userEntity = ContextUtil.getUserEntity();
        params.put("jobType", jobType);
        params.put("dbName", dbName);
        params.put("groupId", groupId);
        params.put("tableName", tableName);
        params.put("hasSuccess", hasSuccess);
        params.put("startNm", startNm);
        params.put("length", length);
        params.put("endNum", startNm + length);
        params.put("userName", userEntity.getAccount());
        List<HashMap> listPage = sqlSessionTemplate.selectList("listPage", params);
        LinkedList<EtlJobVO> result = new LinkedList<>();
        for (HashMap<String, Object> map: listPage){
            result.addLast(handleResult(map, EtlJobVO.class));
        }
        return result;
    }

    /**
     * 获得符合搜索条件的总条数
     * @param jobType
     *          任务类型
     * @param dbName
     *          任务名称
     * @param groupId
     *          任务组ID
     * @param tableName
     *          表名
     * @return
     */
    public int countSearch(String jobType, String dbName, String groupId, String tableName, String hasSuccess){
        Map<String, Object> params = new HashMap<>();
        SysUserVO userEntity = ContextUtil.getUserEntity();
        params.put("jobType", jobType);
        params.put("dbName", dbName);
        params.put("groupId", groupId);
        params.put("tableName", tableName);
        params.put("userName", userEntity.getAccount());
        params.put("hasSuccess", hasSuccess);
        return sqlSessionTemplate.selectOne("countSearch", params);
    }

}
