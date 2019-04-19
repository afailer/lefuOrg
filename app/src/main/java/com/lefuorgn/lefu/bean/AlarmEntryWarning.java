package com.lefuorgn.lefu.bean;

import java.io.Serializable;

/**
 * 告警信息警告详情
 */

public class AlarmEntryWarning implements Serializable {

    private long id; // 警告ID
    private long device_id; // 设备ID
    private String older_name; // 老人名称
    private String document_number; // 身份证号码
    private long time; // 告警时间
    private String address; // 地址
    private int status; // 处理状态
    private int types; // 当前告警数据类型
    private String sim_phone; // 手表安装的手机卡的电话号码
    private String latitude; // 纬度
    private String longitude; // 经度
    private int remaining_power; // 电量百分比

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDevice_id() {
        return device_id;
    }

    public void setDevice_id(long device_id) {
        this.device_id = device_id;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTypes() {
        return types;
    }

    public void setTypes(int types) {
        this.types = types;
    }

    public String getSim_phone() {
        return sim_phone;
    }

    public void setSim_phone(String sim_phone) {
        this.sim_phone = sim_phone;
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

    public int getRemaining_power() {
        return remaining_power;
    }

    public void setRemaining_power(int remaining_power) {
        this.remaining_power = remaining_power;
    }

    @Override
    public String toString() {
        return "AlarmEntryWarning{" +
                "id=" + id +
                ", device_id=" + device_id +
                ", older_name='" + older_name + '\'' +
                ", document_number='" + document_number + '\'' +
                ", time=" + time +
                ", address='" + address + '\'' +
                ", status=" + status +
                ", types=" + types +
                ", sim_phone='" + sim_phone + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", remaining_power=" + remaining_power +
                '}';
    }
}
