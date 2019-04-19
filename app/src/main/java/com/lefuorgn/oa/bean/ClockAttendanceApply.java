package com.lefuorgn.oa.bean;

/**
 * 我审批的或者我申请的列表信息(考勤信息)
 */

public class ClockAttendanceApply {

    private long id; // 信息条目ID
    private String user_name; // 申请人名称
    private long oa_vacation_type; // 申请类型ID
    private String oa_vacation_name; // 申请类型名称
    private String oa_vacation_logo; // 申请类型图标
    private long verify_start_time; // 申请时间
    private int status; // 当前申请信息状态

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public long getOa_vacation_type() {
        return oa_vacation_type;
    }

    public void setOa_vacation_type(long oa_vacation_type) {
        this.oa_vacation_type = oa_vacation_type;
    }

    public String getOa_vacation_name() {
        return oa_vacation_name;
    }

    public void setOa_vacation_name(String oa_vacation_name) {
        this.oa_vacation_name = oa_vacation_name;
    }

    public String getOa_vacation_logo() {
        return oa_vacation_logo;
    }

    public void setOa_vacation_logo(String oa_vacation_logo) {
        this.oa_vacation_logo = oa_vacation_logo;
    }

    public long getVerify_start_time() {
        return verify_start_time;
    }

    public void setVerify_start_time(long verify_start_time) {
        this.verify_start_time = verify_start_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClockAttendanceApply{" +
                "id=" + id +
                ", user_name='" + user_name + '\'' +
                ", oa_vacation_type=" + oa_vacation_type +
                ", oa_vacation_name='" + oa_vacation_name + '\'' +
                ", oa_vacation_logo='" + oa_vacation_logo + '\'' +
                ", verify_start_time=" + verify_start_time +
                ", status=" + status +
                '}';
    }
}
