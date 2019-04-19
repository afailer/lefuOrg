package com.lefuorgn.lefu.bean;

import java.io.Serializable;

/**
 * 院方活动
 */

public class Event implements Serializable{

    private long id; // 活动ID
    private String theme; // 活动主题
    private String reserved; // 活动详情
    private String agency_name; // 当前活动机构名称
    private String holdTimeView; // 当前活动创建时间, 格式"2016-05-08"
    private String pic; // 活动图片

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public String getAgency_name() {
        return agency_name;
    }

    public void setAgency_name(String agency_name) {
        this.agency_name = agency_name;
    }

    public String getHoldTimeView() {
        return holdTimeView;
    }

    public void setHoldTimeView(String holdTimeView) {
        this.holdTimeView = holdTimeView;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", theme='" + theme + '\'' +
                ", reserved='" + reserved + '\'' +
                ", agency_name='" + agency_name + '\'' +
                ", holdTimeView='" + holdTimeView + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }
}
