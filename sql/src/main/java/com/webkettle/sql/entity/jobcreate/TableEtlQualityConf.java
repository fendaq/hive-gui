package com.webkettle.sql.entity.jobcreate;

import java.util.List;

/**
 * 数据表ETL质量处理配置
 * @author gsk
 */
public class TableEtlQualityConf {

    /**
     * 质量处理规则
     */
    private List<TableEtlQualityRuleConf> rules;

    /**
     * 质量处理关联表达式
     */
    private String rulesRel;

    /**
     * 质量处理最终值
     */
    private String fullValue;

    public List<TableEtlQualityRuleConf> getRules() {
        return rules;
    }

    public void setRules(List<TableEtlQualityRuleConf> rules) {
        this.rules = rules;
    }

    public String getRulesRel() {
        return rulesRel;
    }

    public void setRulesRel(String rulesRel) {
        this.rulesRel = rulesRel;
    }

    public String getFullValue() {
        return fullValue;
    }

    public void setFullValue(String fullValue) {
        this.fullValue = fullValue;
    }
}
