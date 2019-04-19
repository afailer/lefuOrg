package com.lefuorgn.lefu.bean;

import java.io.Serializable;

/**
 * 交班记录信息
 */

public class Logbook implements Serializable{

    private static final long serialVersionUID = 1L;

    private long id; // 记录id
    private long old_people_id; // 老人id
    private String old_people_name; // 老人姓名
    private	String care_name; // 护理类型
    private long create_time; // 创建时间
    private long update_time; // 更新时间
    private String content; // 护理内容
    private int status; // 记录状态
    private String staff_name; // 员工姓名
    private long staff_id; // 员工id
    private long inspect_time; // 护理时间

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getCare_name() {
        return care_name;
    }

    public void setCare_name(String care_name) {
        this.care_name = care_name;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public long getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(long staff_id) {
        this.staff_id = staff_id;
    }

    public long getInspect_time() {
        return inspect_time;
    }

    public void setInspect_time(long inspect_time) {
        this.inspect_time = inspect_time;
    }

    @Override
    public String toString() {
        return "Logbook{" +
                "id=" + id +
                ", old_people_id=" + old_people_id +
                ", old_people_name='" + old_people_name + '\'' +
                ", care_type=" + care_name +
                ", create_time=" + create_time +
                ", update_time=" + update_time +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", staff_name='" + staff_name + '\'' +
                ", staff_id=" + staff_id +
                ", inspect_time=" + inspect_time +
                '}';
    }
}
