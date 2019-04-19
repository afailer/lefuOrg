package com.lefuorgn.lefu.bean;

/**
 * 刷新后新的位置轨迹
 */

public class NewTrajectoryObj {

    private NewTrajectoryObjData locationdata;

    public NewTrajectoryObjData getLocationdata() {
        return locationdata;
    }

    public void setLocationdata(NewTrajectoryObjData locationdata) {
        this.locationdata = locationdata;
    }

    @Override
    public String toString() {
        return "NewTrajectoryObj{" +
                "locationdata=" + locationdata +
                '}';
    }
}
