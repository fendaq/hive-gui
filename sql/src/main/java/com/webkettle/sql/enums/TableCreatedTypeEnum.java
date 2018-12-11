package com.webkettle.sql.enums;

import com.webkettle.sql.SpringContextUtil;
import com.webkettle.sql.service.TableCreateJobManagerService;

/**
 * 建表类型枚举类
 * @author gsk
 */
public enum TableCreatedTypeEnum {

    SQL("SQL", "通过SQL脚本进行建表", "tableCreateSQLJobManagerService"),
    ETL("ETL", "通过ETL配置进行建表", "tableCreateETLJobManagerService");

    private String value;

    private String dscp;

    private String beanName;

    TableCreatedTypeEnum(String value, String dscp, String beanName){
        this.dscp = dscp;
        this.value = value;
        this.beanName = beanName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDscp() {
        return dscp;
    }

    public void setDscp(String dscp) {
        this.dscp = dscp;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    /**
     * 获取建表任务单元管理实例
     * @return
     */
    public TableCreateJobManagerService getJobManagerService(){
        return SpringContextUtil.getBean(getBeanName(), TableCreateJobManagerService.class);
    }
}
