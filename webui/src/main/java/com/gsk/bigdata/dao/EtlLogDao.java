package com.gsk.bigdata.dao;

import com.aiyi.core.beans.Method;
import com.aiyi.core.beans.WherePrams;
import com.aiyi.core.dao.impl.DaoImpl;
import com.aiyi.core.sql.where.C;
import com.webkettle.sql.entity.EtlLogVO;
import org.springframework.stereotype.Repository;

@Repository
public class EtlLogDao extends DaoImpl<EtlLogVO, Integer> {

    /**
     * 精确获取某日志
     * @param excuteId
     *          执行ID
     * @param groupId
     *          任务组ID
     * @param jobId
     *          任务ID
     * @return
     */
    public EtlLogVO addEtlLog(String excuteId, int groupId, int jobId){
        WherePrams where = Method.where("UUID", C.EQ, excuteId).and("JOB_GROUP_ID", C.EQ, groupId)
                .and("JOB_ID", C.EQ, jobId);

        EtlLogVO etlLogVO = get(where);
        if (null == etlLogVO){
            etlLogVO = new EtlLogVO();
            etlLogVO.setJobGroupId(groupId);
            etlLogVO.setJobId(jobId);
            etlLogVO.setUuid(excuteId);
            add(etlLogVO);
            return get(where);
        }

        return etlLogVO;
    }

}
