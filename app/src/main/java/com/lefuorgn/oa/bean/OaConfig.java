package com.lefuorgn.oa.bean;

/**
 * 可用调休的配置文件
 */

public class OaConfig {

    private String content; // {"daily_work_hours":8}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "OaConfig{" +
                "content='" + content + '\'' +
                '}';
    }
}
