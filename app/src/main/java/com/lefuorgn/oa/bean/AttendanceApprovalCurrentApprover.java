package com.lefuorgn.oa.bean;

/**
 * 审批中审批人信息
 */

public class AttendanceApprovalCurrentApprover {

    private long id;
    private long oa_verify_ask_id;
    private long verify_user_id; // 审批人ID
    private String verify_user_name; // 审批人名称
    private int is_stop; // 1: 可以终止; 其他: 不可终止
    private int stop; // 是否终止审核（0： 不终止 1：终止）
    private int status; // 1：待审核 2：驳回 3：同意  4：撤销 5：退回
    private String remark;

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
        return "AttendanceApprovalCurrentApprover{" +
                "id=" + id +
                ", oa_verify_ask_id=" + oa_verify_ask_id +
                ", verify_user_id=" + verify_user_id +
                ", verify_user_name='" + verify_user_name + '\'' +
                ", is_stop=" + is_stop +
                ", stop=" + stop +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                '}';
    }
}
