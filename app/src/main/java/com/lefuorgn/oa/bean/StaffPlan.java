package com.lefuorgn.oa.bean;

import com.lefuorgn.lefu.base.BaseGridItem;

/**
 * Created by liuting on 2017/4/19.
 */

public class StaffPlan implements BaseGridItem {
    private long oa_scheduling_id;
    private long user_id;
    private long agency_id;
    private String oa_scheduling_name;
    private String oa_scheduling_short_name="";
    private String oa_scheduling_color;//早晚班显示的颜色
    private long time;//时间
    private long id;

    public long getOa_scheduling_id() {
        return oa_scheduling_id;
    }

    public void setOa_scheduling_id(long oa_scheduling_id) {
        this.oa_scheduling_id = oa_scheduling_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(long agency_id) {
        this.agency_id = agency_id;
    }

    public String getOa_scheduling_name() {
        return oa_scheduling_name;
    }

    public void setOa_scheduling_name(String oa_scheduling_name) {
        this.oa_scheduling_name = oa_scheduling_name;
    }

    public String getOa_scheduling_short_name() {
        return oa_scheduling_short_name;
    }

    public void setOa_scheduling_short_name(String oa_scheduling_short_name) {
        this.oa_scheduling_short_name = oa_scheduling_short_name;
    }

    public String getOa_scheduling_color() {
        return oa_scheduling_color;
    }

    public void setOa_scheduling_color(String oa_scheduling_color) {
        this.oa_scheduling_color = oa_scheduling_color;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
