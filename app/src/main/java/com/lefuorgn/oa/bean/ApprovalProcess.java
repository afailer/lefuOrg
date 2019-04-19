package com.lefuorgn.oa.bean;

import java.io.Serializable;

/**
 * 审批流程
 */

public class ApprovalProcess implements Serializable {

    private long id; // 审批流程ID
    private long oa_verify_ask_id; // 审批信息ID
    private long verify_user_id; // 审批人ID
    private String verify_user_name; // 审批人名称
    private String verify_user_icon; // 审批人头像
    private long create_time; // 审批人指定创建时间
    private long update_time; // 审批人指定修改时间
    private int is_stop; // 是否可以终止审核（1：不可以终止 2：可以终止）
    private int stop; // 是否终止审核（0： 不终止 1：终止）
    private long agency_id; // 申请人所在机构ID
    private int status; // 审核状态(1：待审核 2：驳回 3：同意 5：退回)
    private String remark; // 驳回原因

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOa_verify_ask_id() {
        return oa_verify_ask_id;
    }

    public void setOa_verify_ask_id(long oa_verify_ask_id) {
        this.oa_verify_ask_id = oa_verify_ask_id;
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

    public int getIs_stop() {
        return is_stop;
    }

    public void setIs_stop(int is_stop) {
        this.is_stop = is_stop;
    }

    public int getStop() {
        return stop;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }

    public long getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(long agency_id) {
        this.agency_id = agency_id;
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
        return "ApprovalProcess{" +
                "id=" + id +
                ", oa_verify_ask_id=" + oa_verify_ask_id +
                ", verify_user_id=" + verify_user_id +
                ", verify_user_name='" + verify_user_name + '\'' +
                ", verify_user_icon='" + verify_user_icon + '\'' +
                ", create_time=" + create_time +
                ", update_time=" + update_time +
                ", is_stop=" + is_stop +
                ", stop=" + stop +
                ", agency_id=" + agency_id +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                '}';
    }
}
