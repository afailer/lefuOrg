package com.lefuorgn.lefu.bean;

/**
 * 告警信息条目
 */

public class AlarmInformationDetails {

    private String name; // 当前条目的名称
    private Class<?> cls; // 当前条目要跳转到相应activity类的字节码
    private int drawableRes; // 当前条目图片资源
    private int infoNum; // 提示未读信息条数
    private String skipInfo; // 条目跳转信息

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getCls() {
        return cls;
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }

    public int getDrawableRes() {
        return drawableRes;
    }

    public void setDrawableRes(int drawableRes) {
        this.drawableRes = drawableRes;
    }

    public int getInfoNum() {
        return infoNum;
    }

    public void setInfoNum(int infoNum) {
        this.infoNum = infoNum;
    }

    public String getSkipInfo() {
        return skipInfo;
    }

    public void setSkipInfo(String skipInfo) {
        this.skipInfo = skipInfo;
    }
}
