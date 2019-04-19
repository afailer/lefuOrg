package com.lefuorgn.lefu.bean;

import com.lefuorgn.interf.Pinyinable;

/**
 * 拥有手表设备的老人
 */

public class DeviceElderly implements Pinyinable {

    private long device_id; // 设备ID
    private long older_id; // 老人ID
    private long agency_id; // 老人所在机构ID
    private long safe_area_id; // 设备安全区域ID
    private String device_type_name; // 设备类型名称
    private String older_name; // 老人名称
    private long grant_time; // 设备发放时间
    private String latitude; // 纬度
    private String longitude; // 经度
    private long time; // 当前经纬度刷新时间
    private String address; // 当前经纬度点描述
    private String sim_phone; // 设备sim号码
    private String imei; // 设备15位的设备串号
    private String document_number; // 老人身份证号码
    private String sortLetters; // 做通讯录时的字母索引
    private String fullPinYin; // 老人姓名的全拼
    private String initial; // 老人姓名首字母全拼

    public long getDevice_id() {
        return device_id;
    }

    public void setDevice_id(long device_id) {
        this.device_id = device_id;
    }

    public long getOlder_id() {
        return older_id;
    }

    public void setOlder_id(long older_id) {
        this.older_id = older_id;
    }

    public long getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(long agency_id) {
        this.agency_id = agency_id;
    }

    public long getSafe_area_id() {
        return safe_area_id;
    }

    public void setSafe_area_id(long safe_area_id) {
        this.safe_area_id = safe_area_id;
    }

    public String getDevice_type_name() {
        return device_type_name;
    }

    public void setDevice_type_name(String device_type_name) {
        this.device_type_name = device_type_name;
    }

    public String getOlder_name() {
        return older_name;
    }

    public void setOlder_name(String older_name) {
        this.older_name = older_name;
    }

    public long getGrant_time() {
        return grant_time;
    }

    public void setGrant_time(long grant_time) {
        this.grant_time = grant_time;
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

    public String getSim_phone() {
        return sim_phone;
    }

    public void setSim_phone(String sim_phone) {
        this.sim_phone = sim_phone;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getDocument_number() {
        return document_number;
    }

    public void setDocument_number(String document_number) {
        this.document_number = document_number;
    }

    @Override
    public String getCharacters() {
        return older_name;
    }

    @Override
    public String getSortLetters() {
        return sortLetters;
    }

    @Override
    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    @Override
    public String getFullPinYin() {
        return fullPinYin;
    }

    @Override
    public void setFullPinYin(String fullPinYin) {
        this.fullPinYin = fullPinYin;
    }

    @Override
    public String getInitial() {
        return initial;
    }

    @Override
    public void setInitial(String initial) {
        this.initial = initial;
    }

    @Override
    public String toString() {
        return "DeviceElderly{" +
                "device_id=" + device_id +
                ", older_id=" + older_id +
                ", agency_id=" + agency_id +
                ", safe_area_id=" + safe_area_id +
                ", device_type_name='" + device_type_name + '\'' +
                ", older_name='" + older_name + '\'' +
                ", grant_time=" + grant_time +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", time=" + time +
                ", address='" + address + '\'' +
                ", sim_phone='" + sim_phone + '\'' +
                ", imei='" + imei + '\'' +
                ", document_number='" + document_number + '\'' +
                ", sortLetters='" + sortLetters + '\'' +
                ", fullPinYin='" + fullPinYin + '\'' +
                ", initial='" + initial + '\'' +
                '}';
    }
}
