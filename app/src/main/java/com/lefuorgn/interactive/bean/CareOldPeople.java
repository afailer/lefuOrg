package com.lefuorgn.interactive.bean;

/**
 * 被关注的老人信息类
 */

public class CareOldPeople {

    private long older_id; // 被关注的老人ID
    private long agency_id; // 机构ID

    public long getOlder_id() {
        return older_id;
    }

    public void setOlder_id(long older_id) {
        this.older_id = older_id;
    }

    public long getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(long agency_id) {
        this.agency_id = agency_id;
    }

    @Override
    public String toString() {
        return "CareOldPeople{" +
                "older_id=" + older_id +
                ", agency_id=" + agency_id +
                '}';
    }
}
