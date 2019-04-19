package com.lefuorgn.bean;

/**
 * 版本更新信息
 */

public class Version {

    private String appUrl; // 下载地址
    private String version; // 最新数据版本号
    private String desc; // 当前版本描述

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Version{" +
                "appUrl='" + appUrl + '\'' +
                ", version='" + version + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
