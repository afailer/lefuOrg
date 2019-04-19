package com.lefuorgn.oa.bean;

/**
 * 审批内容类型
 */

public class AttendanceApprovalType {

    private long id; // 审批内容类型ID
    private String name; // 审批内容名称
    private String logo; // 审批内容指示图标
    private long agency_id; // 机构ID

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public long getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(long agency_id) {
        this.agency_id = agency_id;
    }

    @Override
    public String toString() {
        return "AttendanceApprovalType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", agency_id=" + agency_id +
                '}';
    }
}
