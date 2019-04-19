package com.lefuorgn.oa.bean;

import java.util.List;

/**
 * 审批提交信息包裹类
 */

public class AttendanceApprovalExternal {

    private List<AttendanceApprovalApprover> verifyLines;

    public List<AttendanceApprovalApprover> getVerifyLines() {
        return verifyLines;
    }

    public void setVerifyLines(List<AttendanceApprovalApprover> verifyLines) {
        this.verifyLines = verifyLines;
    }

    @Override
    public String toString() {
        return "AttendanceApprovalExternal{" +
                "verifyLines=" + verifyLines +
                '}';
    }
}
