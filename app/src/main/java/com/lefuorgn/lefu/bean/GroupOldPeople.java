package com.lefuorgn.lefu.bean;

/**
 * 交班记录中分组老人信息
 */

public class GroupOldPeople {

    private long old_people_id; // 老人ID
    private String old_people_name; // 老人姓名
    private long group_id; // 分组ID

    public long getOld_people_id() {
        return old_people_id;
    }

    public void setOld_people_id(long old_people_id) {
        this.old_people_id = old_people_id;
    }

    public String getOld_people_name() {
        return old_people_name;
    }

    public void setOld_people_name(String old_people_name) {
        this.old_people_name = old_people_name;
    }

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }
}
