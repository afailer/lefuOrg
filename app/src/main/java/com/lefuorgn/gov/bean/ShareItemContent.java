package com.lefuorgn.gov.bean;

import java.io.Serializable;

/**
 * Created by liuting on 2017/1/11.
 */

public class ShareItemContent implements Serializable{

    String title;//分享条目标题
    String des;//分享条目描述
    String desImgUrl;//分享条目描述图片URL
    String url;//分享详细内容URL
    boolean isTitle;//微信朋友支持text而新浪微博不支持title

    public ShareItemContent(String title, String des, String desImgUrl, String url, boolean isTitle) {
        this.title = title;
        this.des = des;
        this.desImgUrl = desImgUrl;
        this.url = url;
        this.isTitle = isTitle;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDesImgUrl() {
        return desImgUrl;
    }

    public void setDesImgUrl(String desImgUrl) {
        this.desImgUrl = desImgUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }
}
