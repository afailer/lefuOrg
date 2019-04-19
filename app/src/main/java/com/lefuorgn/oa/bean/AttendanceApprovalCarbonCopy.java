package com.lefuorgn.oa.bean;

/**
 * 审批中抄送者信息
 */

public class AttendanceApprovalCarbonCopy {

    private long copy_user_id;
    private String copy_user_name;

    public long getCopy_user_id() {
        return copy_user_id;
    }

    public void setCopy_user_id(long copy_user_id) {
        this.copy_user_id = copy_user_id;
    }

    public String getCopy_user_name() {
        return copy_user_name;
    }

    public void setCopy_user_name(String copy_user_name) {
        this.copy_user_name = copy_user_name;
    }

    @Override
    public String toString() {
        return "AttendanceApprovalCarbonCopy{" +
                "copy_user_id=" + copy_user_id +
                ", copy_user_name='" + copy_user_name + '\'' +
                '}';
    }
}
