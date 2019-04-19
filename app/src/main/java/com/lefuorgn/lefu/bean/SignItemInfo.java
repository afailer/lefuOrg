package com.lefuorgn.lefu.bean;

import java.io.Serializable;

/**
 * 体征条目信息
 */

public class SignItemInfo implements Serializable{

    private long _id; // 当前条目在下载表中的主键ID值
    private long id; // 当前条目在服务器中的id
    private long type; // 当前条目类型
    private String name; // 当前条目名称
    private String content; // 当前条目内容
    private int color; // 当前条目颜色
    private long time; // 当前条目创建时间
    private long inspect_user_id; // 测量员工ID
    private String inspect_user_name; // 测量员工名称, 条目名称显示测量员工名称
    private long entry_user_id; // 录入员工ID
    private String entry_user_name; // 录入员工名称
    private int approvalStatus; // 当前条目审批状态
    private String reserved; // 备注: 主要用在进食和睡眠中

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getInspect_user_id() {
        return inspect_user_id;
    }

    public void setInspect_user_id(long inspect_user_id) {
        this.inspect_user_id = inspect_user_id;
    }

    public String getInspect_user_name() {
        return inspect_user_name;
    }

    public void setInspect_user_name(String inspect_user_name) {
        this.inspect_user_name = inspect_user_name;
    }

    public long getEntry_user_id() {
        return entry_user_id;
    }

    public void setEntry_user_id(long entry_user_id) {
        this.entry_user_id = entry_user_id;
    }

    public String getEntry_user_name() {
        return entry_user_name;
    }

    public void setEntry_user_name(String entry_user_name) {
        this.entry_user_name = entry_user_name;
    }

    public int getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(int approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    @Override
    public String toString() {
        return "SignItemInfo{" +
                "_id=" + _id +
                ", id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", color=" + color +
                ", time=" + time +
                ", inspect_user_id=" + inspect_user_id +
                ", inspect_user_name='" + inspect_user_name + '\'' +
                ", entry_user_id=" + entry_user_id +
                ", entry_user_name='" + entry_user_name + '\'' +
                ", approvalStatus=" + approvalStatus +
                ", reserved='" + reserved + '\'' +
                '}';
    }
}
