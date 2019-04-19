package com.lefuorgn.lefu.bean;

import java.util.List;

/**
 * 设备的安全区域
 */

public class ElectronicFence {

    private long device_id; // 设备ID
    private String older_name; // 老人名称
    private String document_number; // 老人证件号
    private String imei; // 设备15位的设备串号
    private String coordinate; // 安全区域
    private List<Trajectory> list; // 移动轨迹点集合

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

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public List<Trajectory> getList() {
        return list;
    }

    public void setList(List<Trajectory> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ElectronicFence{" +
                "device_id=" + device_id +
                ", older_name='" + older_name + '\'' +
                ", document_number='" + document_number + '\'' +
                ", imei='" + imei + '\'' +
                ", coordinate='" + coordinate + '\'' +
                '}';
    }
}
