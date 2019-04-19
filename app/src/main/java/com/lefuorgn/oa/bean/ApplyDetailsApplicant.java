package com.lefuorgn.oa.bean;

import java.io.Serializable;

/**
 * 审批内容信息(申请人)
 */

public class ApplyDetailsApplicant implements Serializable {

    private long id; // 申请信息ID
    private long user_id; // 申请人ID
    private String user_name; // 申请人名称
    private String user_icon; // 申请人头像
    private long oa_vacation_id; // 请假类型ID
    private String oa_vacation_name; // 请假类型名称
    private long verify_start_time; // 当前信息发布时间
    private long start_time; // 请假开始时间
    private long end_time; // 请假结束时间
    private int duration_hour; // 请假指定时长
    private long create_time; // 当前请假条目创建时间
    private long update_time; // 当前请假条目更新时间
    private int status; // 当前请假条目的状态
    private String remark;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    public long getOa_vacation_id() {
        return oa_vacation_id;
    }

    public void setOa_vacation_id(long oa_vacation_id) {
        this.oa_vacation_id = oa_vacation_id;
    }

    public String getOa_vacation_name() {
        return oa_vacation_name;
    }

    public void setOa_vacation_name(String oa_vacation_name) {
        this.oa_vacation_name = oa_vacation_name;
    }

    public long getVerify_start_time() {
        return verify_start_time;
    }

    public void setVerify_start_time(long verify_start_time) {
        this.verify_start_time = verify_start_time;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public int getDuration_hour() {
        return duration_hour;
    }

    public void setDuration_hour(int duration_hour) {
        this.duration_hour = duration_hour;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "ApplyDetailsApplicant{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", user_icon='" + user_icon + '\'' +
                ", oa_vacation_id=" + oa_vacation_id +
                ", oa_vacation_name='" + oa_vacation_name + '\'' +
                ", verify_start_time=" + verify_start_time +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", duration_hour=" + duration_hour +
                ", create_time=" + create_time +
                ", update_time=" + update_time +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                '}';
    }
}
