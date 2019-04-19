package com.lefuorgn.lefu.bean;

/**
 * 体征图形化展示信息
 */

public class SignGraphicData {

    private long entry_dt; // 录入日期
    private String entry_staff_name; // 录入人
    private long inspect_dt; // 测量日期
    private String inspect_staff_name; // 测量人
    private String val1; // 值1
    private String val2; // 值2

    public long getEntry_dt() {
        return entry_dt;
    }

    public void setEntry_dt(long entry_dt) {
        this.entry_dt = entry_dt;
    }

    public String getEntry_staff_name() {
        return entry_staff_name;
    }

    public void setEntry_staff_name(String entry_staff_name) {
        this.entry_staff_name = entry_staff_name;
    }

    public long getInspect_dt() {
        return inspect_dt;
    }

    public void setInspect_dt(long inspect_dt) {
        this.inspect_dt = inspect_dt;
    }

    public String getInspect_staff_name() {
        return inspect_staff_name;
    }

    public void setInspect_staff_name(String inspect_staff_name) {
        this.inspect_staff_name = inspect_staff_name;
    }

    public String getVal1() {
        return val1;
    }

    public void setVal1(String val1) {
        this.val1 = val1;
    }

    public String getVal2() {
        return val2;
    }

    public void setVal2(String val2) {
        this.val2 = val2;
    }

    @Override
    public String toString() {
        return "SignGraphicData{" +
                "entry_dt=" + entry_dt +
                ", entry_staff_name='" + entry_staff_name + '\'' +
                ", inspect_dt=" + inspect_dt +
                ", inspect_staff_name='" + inspect_staff_name + '\'' +
                ", val1='" + val1 + '\'' +
                ", val2='" + val2 + '\'' +
                '}';
    }
}
