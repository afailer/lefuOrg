package com.lefuorgn.lefu.bean;

/**
 * 网格布局条目信息类
 */

public class GridLayoutItem {

    private String name; // 当前条目的名称
    private Class<?> cls; // 当前条目要跳转到相应activity类的字节码
    private int drawableRes; // 当前条目图片资源
    private boolean permission; // 是否拥有权限
    private boolean network; // 是否需要网络
    private String errorInfo; // cls为null的提示信息

    public GridLayoutItem(String name, Class<?> cls, int drawableRes,
                          boolean permission, boolean network) {
        this(name, cls, drawableRes, permission, network, "");
    }

    public GridLayoutItem(String name, Class<?> cls, int drawableRes,
                          boolean permission, boolean network, String errorInfo) {
        this.name = name;
        this.cls = cls;
        this.drawableRes = drawableRes;
        this.permission = permission;
        this.network = network;
        this.errorInfo = errorInfo;
    }

    public String getName() {
        return name;
    }

    public Class<?> getCls() {
        return cls;
    }

    public int getDrawableRes() {
        return drawableRes;
    }

    public boolean isPermission() {
        return permission;
    }

    public boolean isNetwork() {
        return network;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

}
