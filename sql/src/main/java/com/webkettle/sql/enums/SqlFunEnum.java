package com.webkettle.sql.enums;

/**
 * SQL函数相关枚举
 * @author gsk
 */
public enum SqlFunEnum {
    ROUND("ROUND", "取整"),RAND("RAND", "取随机数"), ABS("ABS", "取绝对值"), FROM_UNIXTIME("FROM_UNIXTIME", "UNIX时间戳转日期"),
    UNIX_TIMESTAMP("UNIX_TIMESTAMP", "取当前UNIX时间戳"), YEAR("YEAR", "日期转年"), MONTH("MONTH", "日期转月"),
    DAY("DAY", "日期转天"), WEEKOFYEAR("WEEKOFYEAR", "日期转周"), DATE_ADD("DATE_ADD", "日期增加"),
    DATE_SUB("DATE_SUB", "日期减少"), IF("IF", "IF函数"), COALESCE("COALESCE", "非空查找"), CONCAT("CONCAT", "字符串连接"),
    SUBSTR("SUBSTR", "字符串截取"), COUNT("COUNT", "数量统计"), SUM("SUM", "求和"), AVG("AVG", "求平均值"), MIN("MIN", "求最小值"),
    MAX("MAX", "求最大值");

    private String value;

    private String dscp;

    private Entity entity;

    SqlFunEnum(String value, String dscp){
        this.dscp = dscp;
        this.value = value;
        entity = new Entity();
        entity.dscp = dscp;
        entity.value = value;
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

    public Entity getEntity(){
        return entity;
    }

    public class Entity{
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
    }
}
