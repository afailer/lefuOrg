package com.lefuorgn.lefu.bean;

/**
 * 刷新后新的位置轨迹
 */

public class NewTrajectory {

    private NewTrajectoryObj obj;

    public NewTrajectoryObj getObj() {
        return obj;
    }

    public void setObj(NewTrajectoryObj obj) {
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "NewTrajectory{" +
                "obj=" + obj +
                '}';
    }
}
