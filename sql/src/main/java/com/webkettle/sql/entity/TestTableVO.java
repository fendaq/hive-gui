package com.webkettle.sql.entity;

import com.aiyi.core.annotation.po.TableName;
import com.aiyi.core.beans.Po;

import java.util.Date;

@TableName(name = "test_table")
public class TestTableVO extends Po {

    private int id;

    private Date testDate;

    private boolean testBool;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    public boolean isTestBool() {
        return testBool;
    }

    public void setTestBool(boolean testBool) {
        this.testBool = testBool;
    }
}
