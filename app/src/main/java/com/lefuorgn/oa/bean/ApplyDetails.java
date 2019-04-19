package com.lefuorgn.oa.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 请假申请详情
 */

public class ApplyDetails implements Serializable {

    private ApplyDetailsApplicant askForLeaveForm; // 申请人信息
    private List<ApplyDetailsApprover> askForLeaveVerifyForms; // 审批人信息

    public ApplyDetailsApplicant getAskForLeaveForm() {
        return askForLeaveForm;
    }

    public void setAskForLeaveForm(ApplyDetailsApplicant askForLeaveForm) {
        this.askForLeaveForm = askForLeaveForm;
    }

    public List<ApplyDetailsApprover> getAskForLeaveVerifyForms() {
        return askForLeaveVerifyForms;
    }

    public void setAskForLeaveVerifyForms(List<ApplyDetailsApprover> askForLeaveVerifyForms) {
        this.askForLeaveVerifyForms = askForLeaveVerifyForms;
    }

    @Override
    public String toString() {
        return "ApplyDetails{" +
                "askForLeaveForm=" + askForLeaveForm +
                ", askForLeaveVerifyForms=" + askForLeaveVerifyForms +
                '}';
    }
}
