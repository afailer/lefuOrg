package com.lefuorgn.oa.bean;

/**
 * 审批中审批人详细信息
 */

public class ApprovalInformation {

    private ApplyApproval askForLeaveVerifyForm; // 当前审批人信息
    private ApplyApproval vacationVerifyLineForm; // 下级审批人信息(可能为NULL)

    public ApplyApproval getAskForLeaveVerifyForm() {
        return askForLeaveVerifyForm;
    }

    public void setAskForLeaveVerifyForm(ApplyApproval askForLeaveVerifyForm) {
        this.askForLeaveVerifyForm = askForLeaveVerifyForm;
    }

    public ApplyApproval getVacationVerifyLineForm() {
        return vacationVerifyLineForm;
    }

    public void setVacationVerifyLineForm(ApplyApproval vacationVerifyLineForm) {
        this.vacationVerifyLineForm = vacationVerifyLineForm;
    }

    @Override
    public String toString() {
        return "ApprovalInformation{" +
                "askForLeaveVerifyForm=" + askForLeaveVerifyForm +
                ", vacationVerifyLineForm=" + vacationVerifyLineForm +
                '}';
    }
}
