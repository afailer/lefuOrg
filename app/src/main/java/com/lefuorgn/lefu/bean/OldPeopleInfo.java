package com.lefuorgn.lefu.bean;

/**
 * 展示老人信息或者老人住院信息的bean类
 */

public class OldPeopleInfo {

    private String key;
    private String value;

    public OldPeopleInfo(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
