package com.lefuorgn.oa.bean;

/**
 * 实际数据外部包裹类
 */

public class ExternalPackage {

    private RemainingTime defaultVacationTime;
    private ApplyApproval verifyLine;
    // 上面字段是用在申请模块中, 下面是用在审批模块的审批详情中
    private AttendanceApprovalCurrentApprover audit;
    private AttendanceApprovalApprover nextVerifyLine;

    public RemainingTime getDefaultVacationTime() {
        return defaultVacationTime;
    }

    public void setDefaultVacationTime(RemainingTime defaultVacationTime) {
        this.defaultVacationTime = defaultVacationTime;
    }

    public ApplyApproval getVerifyLine() {
        return verifyLine;
    }

    public void setVerifyLine(ApplyApproval verifyLine) {
        this.verifyLine = verifyLine;
    }

    public AttendanceApprovalCurrentApprover getAudit() {
        return audit;
    }

    public void setAudit(AttendanceApprovalCurrentApprover audit) {
        this.audit = audit;
    }

    public AttendanceApprovalApprover getNextVerifyLine() {
        return nextVerifyLine;
    }

    public void setNextVerifyLine(AttendanceApprovalApprover nextVerifyLine) {
        this.nextVerifyLine = nextVerifyLine;
    }

    @Override
    public String toString() {
        return "ExternalPackage{" +
                "defaultVacationTime=" + defaultVacationTime +
                ", verifyLine=" + verifyLine +
                ", audit=" + audit +
                ", nextVerifyLine=" + nextVerifyLine +
                '}';
    }

}
