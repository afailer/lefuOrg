package com.lefuorgn.lefu.multiMedia.bean;

/**
 * 图片信息
 */

public class Image {

    private String url; // 图片本地地址
    private boolean select; // 当前

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    @Override
    public String toString() {
        return "Image{" +
                "url='" + url + '\'' +
                ", select=" + select +
                '}';
    }
}
