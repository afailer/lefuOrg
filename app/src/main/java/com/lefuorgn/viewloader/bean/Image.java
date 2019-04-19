package com.lefuorgn.viewloader.bean;

/**
 * 图片信息
 */

public class Image {

    /**
     * 图片来源本地
     */
    public static final int IMAGE_TYPE_LOCAL = 0x1;
    /**
     * 图片来源网络
     */
    public static final int IMAGE_TYPE_NET = 0x2;
    /**
     * 图片来源资源
     */
    public static final int IMAGE_TYPE_RES = 0x3;

    private String uri; // 图片地址
    private int res; // 资源图片
    private int type; // 本地图片

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Image{" +
                "uri='" + uri + '\'' +
                ", res=" + res +
                ", type=" + type +
                '}';
    }
}
