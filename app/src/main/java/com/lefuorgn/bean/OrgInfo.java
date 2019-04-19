package com.lefuorgn.bean;

/**
 * 机构详情
 */

public class OrgInfo {

    private long agency_id; // 机构id
    private String agency_name; // 机构名称

    public long getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(long agency_id) {
        this.agency_id = agency_id;
    }

    public String getAgency_name() {
        return agency_name;
    }

    public void setAgency_name(String agency_name) {
        this.agency_name = agency_name;
    }

    @Override
    public String toString() {
        return "OrgInfo{" +
                "agency_id=" + agency_id +
                ", agency_name='" + agency_name + '\'' +
                '}';
    }
}
