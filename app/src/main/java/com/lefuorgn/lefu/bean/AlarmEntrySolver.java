package com.lefuorgn.lefu.bean;

import java.io.Serializable;

/**
 * 告警信息处理人详情
 */

public class AlarmEntrySolver implements Serializable {

    private long user_id; // 处理人user_id
    private String user_name; // 处理人用户名
    private long time; // 处理时间
    private int level; // 级别
    private String remark; // 备注

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "AlarmEntrySolver{" +
                "user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", time=" + time +
                ", level=" + level +
                ", remark='" + remark + '\'' +
                '}';
    }
}
