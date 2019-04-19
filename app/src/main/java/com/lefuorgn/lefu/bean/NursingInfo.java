package com.lefuorgn.lefu.bean;

import com.lefuorgn.lefu.base.BaseGridItem;

import java.util.List;

/**
 * 护理信息存放数据
 */

public class NursingInfo implements BaseGridItem {

    private long oldPeopleId; // 老人ID
    private String oldPeopleName; // 老人姓名
    private int level; // 老人被关注的级别
    private boolean cAttention; // 当前老人的关注状态
    private boolean select; // 当前老人是否被选中
    private List<NursingItemInfo> nursingItemInfoList; // 老人关联的条目信息

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

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public List<NursingItemInfo> getNursingItemInfoList() {
        return nursingItemInfoList;
    }

    public void setNursingItemInfoList(List<NursingItemInfo> nursingItemInfoList) {
        this.nursingItemInfoList = nursingItemInfoList;
    }

    @Override
    public String toString() {
        return "NursingInfo{" +
                "oldPeopleId=" + oldPeopleId +
                ", oldPeopleName='" + oldPeopleName + '\'' +
                ", level=" + level +
                ", cAttention=" + cAttention +
                ", select=" + select +
                ", nursingItemInfoList=" + nursingItemInfoList +
                '}';
    }
}
