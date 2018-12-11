package com.webkettle.sql.entity.result;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL语句分析结果类
 * @author gsk
 */
public class SqlAnsyTempResult {

    /**
     * 操作类型
     */
    private String action;

    /**
     * 操作表名
     */
    private List<String> tabName = new ArrayList<>();

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<String> getTabName() {
        return tabName;
    }

    public void setTabName(List<String> tabName) {
        this.tabName = tabName;
    }
}
