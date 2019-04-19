package com.lefuorgn.lefu.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


/**
 * 交班记录类型名称
 */

public class LogbookType{

    private long id; // 类型ID
    private String content; // 类型名称
    private int order;//排序

    public LogbookType(long id, String content,int order) {
        this.id = id;
        this.content=content;
        this.order = order;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
