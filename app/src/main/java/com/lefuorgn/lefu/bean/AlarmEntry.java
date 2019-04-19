package com.lefuorgn.lefu.bean;

import java.io.Serializable;

/**
 * 告警信息条目
 */

public class AlarmEntry implements Serializable {

    private long id; // 告警信息ID
    private String address; // 告警信息地址
    private String older_name; // 老人名称
    private String document_number; // 老人身份证号码
    private String sim_phone; // 手表号码
    private long time; // 报警时间
    private String latitude;
    private String longitude;
    private int types; // 当前告警数据类型
    private int status; // 当前告警数据类型状态
    private int remaining_power; // 低电量

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOlder_name() {
        return older_name;
    }

    public void setOlder_name(String older_name) {
        this.older_name = older_name;
    }

    public String getDocument_number() {
        return document_number;
    }

    public void setDocument_number(String document_number) {
        this.document_number = document_number;
    }

    public String getSim_phone() {
        return sim_phone;
    }

    public void setSim_phone(String sim_phone) {
        this.sim_phone = sim_phone;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getTypes() {
        return types;
    }

    public void setTypes(int types) {
        this.types = types;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRemaining_power() {
        return remaining_power;
    }

    public void setRemaining_power(int remaining_power) {
        this.remaining_power = remaining_power;
    }

    @Override
    public String toString() {
        return "AlarmEntry{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", older_name='" + older_name + '\'' +
                ", document_number='" + document_number + '\'' +
                ", sim_phone='" + sim_phone + '\'' +
                ", time=" + time +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", types=" + types +
                ", status=" + status +
                ", remaining_power=" + remaining_power +
                '}';
    }
}
