package com.lefuorgn.lefu.bean;

import java.util.List;

/**
 * 刷新后新的位置轨迹
 */

public class NewTrajectoryObjDataPoint {

    private List<String> coordinates;

    public List<String> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<String> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "NewTrajectoryObjDataPoint{" +
                "coordinates=" + coordinates +
                '}';
    }
}
