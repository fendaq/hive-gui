package com.webkettle.sql.entity.jobcreate;

public class JobExecuteType {
    private String type;
    /**
     * 频率
     */
    private int hz;
    /**
     * 频率单位
     */
    private String hzCompany;
    /**
     * 分钟
     */
    private String minute;

    /**
     * 小时
     */
    private String hour;

    /**
     * 天
     */
    private String day;

    /**
     * 月
     */
    private String month;

    /**
     * 星期
     */
    private String week;

    /**
     * 自定义表达式
     */
    private String cron;

    /**
     * 执行方案文本说明
     */
    private String planText;

    public String getPlanText() {
        return planText;
    }

    public void setPlanText(String planText) {
        this.planText = planText;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getHz() {
        return hz;
    }

    public void setHz(int hz) {
        this.hz = hz;
    }

    public String getHzCompany() {
        return hzCompany;
    }

    public void setHzCompany(String hzCompany) {
        this.hzCompany = hzCompany;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }
}
