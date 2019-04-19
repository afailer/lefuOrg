package com.lefuorgn.lefu.bean;

/**
 * 首页模块信息
 */

public class HomePageItem {

    private int imgRes; // 条目图片资源ID
    private String name; // 条目名称
    private Class clazz; // 条目点击要跳转页面的类字节码
    private boolean permission; // 是否拥有跳转权限
    private boolean network; // 跳转的页面是否需要网络

    public HomePageItem(int imgRes, String name, Class clazz, boolean permission, boolean network) {
        this.imgRes = imgRes;
        this.name = name;
        this.clazz = clazz;
        this.permission = permission;
        this.network = network;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public boolean isPermission() {
        return permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    public boolean isNetwork() {
        return network;
    }

    public void setNetwork(boolean network) {
        this.network = network;
    }

    @Override
    public String toString() {
        return "HomePageItem{" +
                "imgRes=" + imgRes +
                ", name='" + name + '\'' +
                ", clazz=" + clazz +
                ", permission=" + permission +
                ", network=" + network +
                '}';
    }

}
