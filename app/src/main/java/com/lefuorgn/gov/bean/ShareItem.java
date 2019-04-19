package com.lefuorgn.gov.bean;

/**
 * Created by liuting on 2017/1/11.
 */

public class ShareItem {
    int resIcon;//图标资源
    String name;//名称
    String platFormName;//平台名称
    ShareItemContent shareItemContent;

    public ShareItem(int resIcon, String name, String platFormName, ShareItemContent shareItemContent) {
        this.resIcon = resIcon;
        this.name = name;
        this.platFormName = platFormName;
        this.shareItemContent = shareItemContent;
    }

    public String getPlatFormName() {
        return platFormName;
    }

    public void setPlatFormName(String platFormName) {
        this.platFormName = platFormName;
    }

    public ShareItemContent getShareItemContent() {
        return shareItemContent;
    }

    public void setShareItemContent(ShareItemContent shareItemContent) {
        this.shareItemContent = shareItemContent;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
