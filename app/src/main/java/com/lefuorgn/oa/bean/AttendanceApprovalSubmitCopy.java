package com.lefuorgn.oa.bean;

/**
 * 提交审批申请时抄送人信息
 */

public class AttendanceApprovalSubmitCopy {

    private long copy_user_id; // 抄送人ID

    public long getCopy_user_id() {
        return copy_user_id;
    }

    public void setCopy_user_id(long copy_user_id) {
        this.copy_user_id = copy_user_id;
    }

    @Override
    public String toString() {
        return "AttendanceApprovalSubmitCopy{" +
                "copy_user_id=" + copy_user_id +
                '}';
    }
}
