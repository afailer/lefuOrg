package com.lefuorgn.lefu.bean;

/**
 * 护理信息中老人的基本信息
 */

public class NursingOldPeople {

    private long old_people_id; // 老人ID
    private String old_people_name; // 老人姓名

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

    @Override
    public String toString() {
        return "NursingOldPeople{" +
                "old_people_id=" + old_people_id +
                ", old_people_name='" + old_people_name + '\'' +
                '}';
    }
}
