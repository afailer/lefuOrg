package com.lefuorgn.lefu.bean;

import com.lefuorgn.lefu.base.BaseGridItem;

import java.io.Serializable;
import java.util.List;

/**
 * 体征信息存放数据
 */
public class SignInfo implements BaseGridItem, Serializable {

    private long oldPeopleId; // 老人ID
    private String oldPeopleName; // 老人姓名
    private int level; // 老人被关注的级别
    private boolean cAttention; // 当前老人的关注状态

    private List<SignItemInfo> signItemInfos; // 老人关联的条目信息

    public long getOldPeopleId() {
        return oldPeopleId;
    }

    public void setOldPeopleId(long oldPeopleId) {
        this.oldPeopleId = oldPeopleId;
    }

    public String getOldPeopleName() {
        return oldPeopleName;
    }

    public void setOldPeopleName(String oldPeopleName) {
        this.oldPeopleName = oldPeopleName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean iscAttention() {
        return cAttention;
    }

    public void setcAttention(boolean cAttention) {
        this.cAttention = cAttention;
    }

    public List<SignItemInfo> getSignItemInfos() {
        return signItemInfos;
    }

    public void setSignItemInfos(List<SignItemInfo> signItemInfos) {
        this.signItemInfos = signItemInfos;
    }

    @Override
    public String toString() {
        return "SignInfo{" +
                "oldPeopleId=" + oldPeopleId +
                ", oldPeopleName='" + oldPeopleName + '\'' +
                ", level=" + level +
                ", cAttention=" + cAttention +
                ", signItemInfos=" + signItemInfos +
                '}';
    }
}
