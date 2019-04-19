package com.lefuorgn.gov.bean;

/**
 * 辖区数据信息
 */

public class AllDataItem {

    private String type; // 条目类型名称
    private String value; // 当前类型信息值
    private int icon; // 当前条目类型图片资源ID
    private long time; // 最近更新时间
    private String countName=""; // 条目分类类型
    private int countIcon; // 条目分类类型图片资源ID
    private Class clazz; // 当前条目要跳转的类字节码

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getCountName() {
        return countName;
    }

    public void setCountName(String countName) {
        this.countName = countName;
    }

    public int getCountIcon() {
        return countIcon;
    }

    public void setCountIcon(int countIcon) {
        this.countIcon = countIcon;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
