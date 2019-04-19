package com.lefuorgn.oa.bean;

import java.util.List;

/**
 * 审批中审批人信息
 */

public class AttendanceApprovalApprover {

    private long verify_user_id; // 审批人ID
    private String verify_user_name; // 审批人名称
    private int is_stop; // 1: 可以终止; 其他: 不可终止
    private int type; // 1: 自由审批; 2: 固定审批; 其他:
    private List<AttendanceApprovalCarbonCopy> verifyLineCopys;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<AttendanceApprovalCarbonCopy> getVerifyLineCopys() {
        return verifyLineCopys;
    }

    public void setVerifyLineCopys(List<AttendanceApprovalCarbonCopy> verifyLineCopys) {
        this.verifyLineCopys = verifyLineCopys;
    }

    @Override
    public String toString() {
        return "AttendanceApprovalApprover{" +
                "verify_user_id=" + verify_user_id +
                ", verify_user_name='" + verify_user_name + '\'' +
                ", is_stop=" + is_stop +
                ", type=" + type +
                ", verifyLineCopys=" + verifyLineCopys +
                '}';
    }
}
