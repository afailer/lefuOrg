package com.lefuorgn.oa.bean;


import java.io.Serializable;

/**
 * 申请审批人
 */

public class ApplyApproval implements Serializable{

    private long verify_user_id; // 审批人ID
    private String verify_user_name; // 审批人名称
    private int type; // 1: 自由审批; 2: 固定审批
    private int is_stop; // 是否可以终止审核（0：不可以 1：可以）
    private int level; // 当前审批人审批级别

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIs_stop() {
        return is_stop;
    }

    public void setIs_stop(int is_stop) {
        this.is_stop = is_stop;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "ApplyApproval{" +
                "verify_user_id=" + verify_user_id +
                ", verify_user_name='" + verify_user_name + '\'' +
                ", type=" + type +
                ", is_stop=" + is_stop +
                ", level=" + level +
                '}';
    }
}
