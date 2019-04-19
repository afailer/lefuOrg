package com.lefuorgn.gov.bean;

/**
 * Created by liuting on 2016/12/27.
 */

public class IconItem {
    private int icon;
    private String name;
    private Class className;
    public void OnClick(){}
    public IconItem(int icon, String name, Class className) {
        this.icon = icon;
        this.name = name;
        this.className = className;
    }

    public Class getClassName() {
        return className;
    }

    public void setClassName(Class className) {
        this.className = className;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
