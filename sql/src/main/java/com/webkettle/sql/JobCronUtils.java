package com.webkettle.sql;

import com.webkettle.core.commons.CommAttr;
import com.webkettle.sql.entity.jobcreate.JobExecuteType;
import com.webkettle.sql.enums.JobExecuteHzEnums;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 任务调度表达式相关工具类
 * @author gsk
 */
public class JobCronUtils {

    /**
     * 通过Job配置对象类实体获得调度表达式
     * @param type
     * @return
     */
    public static String getByBean(JobExecuteType type){
        if (null == type){
            throw new RuntimeException("调度规则配置对象为空");
        }
        if (type.getType().equalsIgnoreCase(CommAttr.JOB.JOB_EXE_TYPE_ONE)){
            return "";
        }
        int hz = type.getHz();
        if (hz > 0){
            // 简单频率执行
            JobExecuteHzEnums enums = JobExecuteHzEnums.valueOf(type.getHzCompany());
            return enums.getTemplate().replace("{" + enums.getValue() + "}", "" + hz);
        }

        if (!StringUtils.isEmpty(type.getCron())){
            // 自定义表达式执行
            return type.getCron();
        }

        // 精确频率执行

        StringBuffer cron = new StringBuffer("0");
        if (StringUtils.isEmpty(type.getMinute())){
            cron.append(" ").append("0");
        }else{
            cron.append(" ").append(type.getMinute());
        }

        if (StringUtils.isEmpty(type.getHour())){
            cron.append(" ").append("0");
        }else{
            cron.append(" ").append(type.getHour());
        }

        if (StringUtils.isEmpty(type.getDay())){
            if (StringUtils.isEmpty(type.getWeek())){
                cron.append(" ").append("*");
            }else{
                cron.append(" ").append("?");
            }
        }else{
            cron.append(" ").append(type.getDay());
        }

        if (StringUtils.isEmpty(type.getMonth())){
            cron.append(" ").append("*");
        }else{
            cron.append(" ").append(type.getMonth());
        }

        if (StringUtils.isEmpty(type.getWeek())){
            cron.append(" ").append("?");
        }else{
            cron.append(" ").append(type.getWeek());
        }

        return cron.toString();
    }

    /**
     * 固定日期转调度表达式
     * @param date
     * @return
     */
    public static String date2Cron(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("ss mm HH dd MM ? yyyy");
        String formatTimeStr = "";
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }


}
