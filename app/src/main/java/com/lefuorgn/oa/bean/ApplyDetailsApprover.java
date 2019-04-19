package com.lefuorgn.oa.bean;

import java.io.Serializable;

/**
 * 审批人信息
 */

public class ApplyDetailsApprover implements Serializable {

    private long id; // 审批人信息ID
    private long verify_user_id; // 审批人ID
    private String verify_user_name; // 审批人名称
    private String verify_user_icon; // 审批人头像
    private int level; // 当前审批过程级别
    private int is_stop; // 是否可以终止审核（1：不可以 2：可以）
    private int status; // 审批状态
    private long create_time; // 创建时间
    private long update_time; // 更新时间
    private String remark; // 驳回原因


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVerify_user_id() {
        return verify_user_id;
    }

    public void setVerify_user_id(long verify_user_id) {
        this.verify_user_id = verify_user_id;
    }

    public String getVerify_user_name() {
        return verify_user_name;
    }

    public void setVerify_user_name(String verify_user_name) {
        this.verify_user_name = verify_user_name;
    }

    public String getVerify_user_icon() {
        return verify_user_icon;
    }

    public void setVerify_user_icon(String verify_user_icon) {
        this.verify_user_icon = verify_user_icon;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getIs_stop() {
        return is_stop;
    }

    public void setIs_stop(int is_stop) {
        this.is_stop = is_stop;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "ApplyDetailsApprover{" +
                "id=" + id +
                ", verify_user_id=" + verify_user_id +
                ", verify_user_name='" + verify_user_name + '\'' +
                ", verify_user_icon='" + verify_user_icon + '\'' +
                ", level=" + level +
                ", is_stop=" + is_stop +
                ", status=" + status +
                ", create_time=" + create_time +
                ", update_time=" + update_time +
                ", remark='" + remark + '\'' +
                '}';
    }
}
