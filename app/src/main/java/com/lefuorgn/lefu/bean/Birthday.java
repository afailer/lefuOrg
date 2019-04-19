package com.lefuorgn.lefu.bean;

import java.io.Serializable;

/**
 * 生日页面数据
 */

public class Birthday implements Serializable {

    private String MONTH; // 月份
    private int COUNT; // 当前月份生日提醒人数

    public String getMONTH() {
        return MONTH;
    }

    public void setMONTH(String MONTH) {
        this.MONTH = MONTH;
    }

    public int getCOUNT() {
        return COUNT;
    }

    public void setCOUNT(int COUNT) {
        this.COUNT = COUNT;
    }

    @Override
    public String toString() {
        return "Birthday{" +
                "MONTH='" + MONTH + '\'' +
                ", COUNT=" + COUNT +
                '}';
    }
}
