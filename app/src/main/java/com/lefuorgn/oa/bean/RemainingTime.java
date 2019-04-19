package com.lefuorgn.oa.bean;

/**
 * 可以调休的剩余时间
 */

public class RemainingTime {

    private long default_time; // 调休的剩余时间

    public long getDefault_time() {
        return default_time;
    }

    public void setDefault_time(long default_time) {
        this.default_time = default_time;
    }

    @Override
    public String toString() {
        return "RemainingTime{" +
                "default_time=" + default_time +
                '}';
    }
}
