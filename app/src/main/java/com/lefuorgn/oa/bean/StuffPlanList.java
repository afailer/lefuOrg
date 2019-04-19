package com.lefuorgn.oa.bean;

import java.util.List;

/**
 * Created by liuting on 2017/4/20.
 */

public class StuffPlanList {
    List<StaffPlan> oaStaffPlans;
    long dept_id;
    String dept_name;
    String user_name;
    long staff_id;
    long user_id;

    public List<StaffPlan> getOaStaffPlans() {
        return oaStaffPlans;
    }

    public void setOaStaffPlans(List<StaffPlan> oaStaffPlans) {
        this.oaStaffPlans = oaStaffPlans;
    }

    public long getDept_id() {
        return dept_id;
    }

    public void setDept_id(long dept_id) {
        this.dept_id = dept_id;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public long getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(long staff_id) {
        this.staff_id = staff_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
}
