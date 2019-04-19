package com.lefuorgn.lefu.bean;

import java.util.List;

/**
 * 告警信息条目详情
 */

public class AlarmEntryDetails {

    private String coordinate; // 围栏信息
    private AlarmEntryWarning sosWarningForm; // 警告信息对象
    private List<AlarmEntrySolver> sosWarningUserMaps; // 处理人对象

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public AlarmEntryWarning getSosWarningForm() {
        return sosWarningForm;
    }

    public void setSosWarningForm(AlarmEntryWarning sosWarningForm) {
        this.sosWarningForm = sosWarningForm;
    }

    public List<AlarmEntrySolver> getSosWarningUserMaps() {
        return sosWarningUserMaps;
    }

    public void setSosWarningUserMaps(List<AlarmEntrySolver> sosWarningUserMaps) {
        this.sosWarningUserMaps = sosWarningUserMaps;
    }

    @Override
    public String toString() {
        return "AlarmEntryDetails{" +
                "coordinate='" + coordinate + '\'' +
                ", sosWarningForm=" + sosWarningForm +
                ", sosWarningUserMaps=" + sosWarningUserMaps +
                '}';
    }
}
