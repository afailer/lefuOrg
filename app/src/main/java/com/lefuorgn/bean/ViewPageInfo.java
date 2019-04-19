package com.lefuorgn.bean;

import android.os.Bundle;

/**
 * ViewPage页面信息
 */

public final class ViewPageInfo {

    private String title; // 页面名称
    private Bundle args; // 传递信息的值
    private int drawableRes; // 当前条目图片资源
    private Class<?> clazz; // 类字节码

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bundle getArgs() {
        return args;
    }

    public void setArgs(Bundle args) {
        this.args = args;
    }

    public int getDrawableRes() {
        return drawableRes;
    }

    public void setDrawableRes(int drawableRes) {
        this.drawableRes = drawableRes;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
}
