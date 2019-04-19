package com.lefuorgn.oa.bean;

import java.util.List;

/**
 * 提交审批申请时审批人信息以及所有的抄送人信息
 */

public class AttendanceApprovalSubmit {

    private long verify_user_id; // 审批人ID
    private List<AttendanceApprovalSubmitCopy> verifyAskCopys; // 抄送人信息

    public long getVerify_user_id() {
        return verify_user_id;
    }

    public void setVerify_user_id(long verify_user_id) {
        this.verify_user_id = verify_user_id;
    }

    public List<AttendanceApprovalSubmitCopy> getVerifyAskCopys() {
        return verifyAskCopys;
    }

    public void setVerifyAskCopys(List<AttendanceApprovalSubmitCopy> verifyAskCopys) {
        this.verifyAskCopys = verifyAskCopys;
    }

    @Override
    public String toString() {
        return "AttendanceApprovalSubmit{" +
                "verify_user_id=" + verify_user_id +
                ", verifyAskCopys=" + verifyAskCopys +
                '}';
    }
}
