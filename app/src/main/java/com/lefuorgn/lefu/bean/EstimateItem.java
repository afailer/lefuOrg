package com.lefuorgn.lefu.bean;

import java.io.Serializable;

/**
 * 评估表条目
 */

public class EstimateItem implements Serializable {

    private long id; // 条目ID
    private String title; // 条目名称

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "EstimateItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
