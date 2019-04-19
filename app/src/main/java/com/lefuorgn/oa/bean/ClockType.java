package com.lefuorgn.oa.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 请假类型
 */

public class ClockType implements Serializable{

    private long id; // 请假类型条目ID
    private int type; // 请假类型ID
    private String name; // 请假类型名称
    private int status; // 1：启用 2：停用
    private int min_type; // 0：不限制 1：小时 2：天
    private int date_type; // 1：工作日 2：自然日
    private long min_time; // 最小申请值
    private long max_time; // 最大申请值
    private long default_time; // 假期默认长度, 根据min_type来显示
    private List<ClockType> clockTypes; // 请假申请内容

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMin_type() {
        return min_type;
    }

    public void setMin_type(int min_type) {
        this.min_type = min_type;
    }

    public int getDate_type() {
        return date_type;
    }

    public void setDate_type(int date_type) {
        this.date_type = date_type;
    }

    public long getMin_time() {
        return min_time;
    }

    public void setMin_time(long min_time) {
        this.min_time = min_time;
    }

    public long getMax_time() {
        return max_time;
    }

    public void setMax_time(long max_time) {
        this.max_time = max_time;
    }

    public long getDefault_time() {
        return default_time;
    }

    public void setDefault_time(long default_time) {
        this.default_time = default_time;
    }

    public List<ClockType> getClockTypes() {
        return clockTypes;
    }

    public void setClockTypes(List<ClockType> clockTypes) {
        this.clockTypes = clockTypes;
    }

    @Override
    public String toString() {
        return "ClockType{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", min_type=" + min_type +
                ", date_type=" + date_type +
                ", min_time=" + min_time +
                ", max_time=" + max_time +
                ", default_time=" + default_time +
                ", clockTypes=" + clockTypes +
                '}';
    }
}
