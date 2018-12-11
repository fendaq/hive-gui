package com.webkettle.sql.enums;

/**
 * 数据库分组枚举类
 * @author gsk
 */
public enum DataBaseGroupEnum {

    SOURCE("SOURCE", "贴源层数据库"),
    THEME("THEME", "主题层"),
    APPLICATION("APPLICATION", "应用层");

    private String value;

    private String dscp;



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
    DataBaseGroupEnum(String value, String dscp){
        this.value = value;
        this.dscp = dscp;
    }
}
