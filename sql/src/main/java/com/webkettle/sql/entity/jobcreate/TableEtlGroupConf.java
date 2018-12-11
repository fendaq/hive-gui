package com.webkettle.sql.entity.jobcreate;

import java.util.List;

/**
 * 数据表ETL分组统计配置
 * @author gsk
 */
public class TableEtlGroupConf {

    /**
     * 列分组配置
     */
    private List<TableEtlColum> group;

    /**
     * 列汇总配置
     */
    private List<TableEtlColum> summary;

    public List<TableEtlColum> getGroup() {
        return group;
    }

    public void setGroup(List<TableEtlColum> group) {
        this.group = group;
    }

    public List<TableEtlColum> getSummary() {
        return summary;
    }

    public void setSummary(List<TableEtlColum> summary) {
        this.summary = summary;
    }
}
