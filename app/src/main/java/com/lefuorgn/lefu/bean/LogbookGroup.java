package com.lefuorgn.lefu.bean;

import java.io.Serializable;

/**
 * 交班记录分组
 */

public class LogbookGroup implements Serializable{

    private long id; // 分组ID
    private long agency_id; // 机构ID
    private long create_time; // 创建时间
    private long update_time; // 更新时间
    private String group_name; // 分组名称

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(long agency_id) {
        this.agency_id = agency_id;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    @Override
    public String toString() {
        return "LogbookGroup{" +
                "id=" + id +
                ", agency_id=" + agency_id +
                ", create_time=" + create_time +
                ", update_time=" + update_time +
                ", group_name='" + group_name + '\'' +
                '}';
    }
}
