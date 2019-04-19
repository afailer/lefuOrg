package com.lefuorgn.lefu.bean;

/**
 * 老人移动轨迹点
 */

public class Trajectory {

    private long device_id; // 设备ID
    private long older_id; // 老人ID
    private long agency_id; // 老人所在机构ID
    private String latitude; // 纬度
    private String longitude; // 经度
    private long time; // 当前经纬度刷新时间
    private String address; // 当前经纬度点描述
    private String imei; // 设备15位的设备串号
    private boolean select;

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

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    @Override
    public String toString() {
        return "Trajectory{" +
                "device_id=" + device_id +
                ", older_id=" + older_id +
                ", agency_id=" + agency_id +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", time=" + time +
                ", address='" + address + '\'' +
                ", imei='" + imei + '\'' +
                '}';
    }
}
