package com.lefuorgn.oa.bean;

/**
 * 审批内容(实际内容)
 */

public class AttendanceApprovalClock {

    private long id; // 审批内容类型id
    private String name; // 审批内容类型名称
    private String logo; // 审批内容图片
    private long agency_id; // 机构ID
    private String content; // 审批控件内容
    private AttendanceApprovalExternal verify;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AttendanceApprovalExternal getVerify() {
        return verify;
    }

    public void setVerify(AttendanceApprovalExternal verify) {
        this.verify = verify;
    }

    @Override
    public String toString() {
        return "AttendanceApprovalClock{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", agency_id=" + agency_id +
                ", content='" + content + '\'' +
                ", verify=" + verify +
                '}';
    }
}
