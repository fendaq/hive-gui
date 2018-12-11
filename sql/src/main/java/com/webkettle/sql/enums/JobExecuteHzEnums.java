package com.webkettle.sql.enums;

/**
 * 执行频率单位枚举
 * @author gsk
 */
public enum JobExecuteHzEnums {

    MINUTE("MINUTE", "分钟", "0 0/{MINUTE} * * * ?"), HOUR("HOUR", "小时", "0 0 0/{HOUR} * * ?"),
    DAY("DAY", "天", "0 0 1 0/{DAY} * ?"), MONTH("MONTH", "月", "0 0 1 1 0/{MONTH} ?"),
    WEEK("WEEK", "星期", "0 0 1 ? * 0/{WEEK}");
    private String value;

    private String dscp;

    private String template;

    JobExecuteHzEnums(String value, String dscp, String template){
        this.dscp = dscp;
        this.value = value;
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
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
}
