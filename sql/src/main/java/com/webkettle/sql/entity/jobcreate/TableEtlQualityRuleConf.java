package com.webkettle.sql.entity.jobcreate;

/**
 * 数据表ETL质量处理比较规则配置
 * @author gsk
 */
public class TableEtlQualityRuleConf {

    /**
     * 规则ID
     */
    private String id;

    /**
     * 质量处理的列
     */
    private TableEtlColum colum;

    /**
     * 比较符
     */
    private String calc;

    /**
     * 比较值
     */
    private String refer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TableEtlColum getColum() {
        return colum;
    }

    public void setColum(TableEtlColum colum) {
        this.colum = colum;
    }

    public String getCalc() {
        return calc;
    }

    public void setCalc(String calc) {
        this.calc = calc;
    }

    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }
}
