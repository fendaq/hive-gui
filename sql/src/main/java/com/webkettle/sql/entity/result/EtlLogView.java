package com.webkettle.sql.entity.result;

/**
 * 日志
 * @author gsk
 */
public class EtlLogView {

    private String lastTimeState;

    private String logs;

    public String getLastTimeState() {
        return lastTimeState;
    }

    public void setLastTimeState(String lastTimeState) {
        this.lastTimeState = lastTimeState;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }
}
