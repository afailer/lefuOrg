package com.lefuorgn.lefu.bean;

/**
 * 刷新后新的位置轨迹
 */

public class NewTrajectoryObjData {

    private String address;
    private NewTrajectoryObjDataPoint point;
    private NewTrajectoryObjDataTime time_begin;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public NewTrajectoryObjDataPoint getPoint() {
        return point;
    }

    public void setPoint(NewTrajectoryObjDataPoint point) {
        this.point = point;
    }

    public NewTrajectoryObjDataTime getTime_begin() {
        return time_begin;
    }

    public void setTime_begin(NewTrajectoryObjDataTime time_begin) {
        this.time_begin = time_begin;
    }

    @Override
    public String toString() {
        return "NewTrajectoryObjData{" +
                "address='" + address + '\'' +
                ", point=" + point +
                ", time_begin=" + time_begin +
                '}';
    }
}
