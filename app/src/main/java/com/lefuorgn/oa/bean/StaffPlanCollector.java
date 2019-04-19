package com.lefuorgn.oa.bean;

import com.lefuorgn.interf.Pinyinable;
import com.lefuorgn.lefu.base.BaseGridItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuting on 2017/4/24.
 */

public class StaffPlanCollector implements BaseGridItem,Pinyinable {
    List<StaffPlan> staffPlan=new ArrayList<StaffPlan>();
    private String staffName;
    private String sortLetters; // 做通讯录时的字母索引
    private String fullPinYin; // 姓名的全拼
    private String initial; // 姓名首字母全拼

    public StaffPlanCollector(List<StaffPlan> staffPlan, String staffName) {
        this.staffPlan = staffPlan;
        this.staffName = staffName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public List<StaffPlan> getStaffPlan() {
        return staffPlan;
    }

    public void setStaffPlan(List<StaffPlan> staffPlan) {
        this.staffPlan = staffPlan;
    }

    @Override
    public String getCharacters() {
        return staffName;
    }

    @Override
    public String getSortLetters() {
        return sortLetters;
    }

    @Override
    public void setSortLetters(String sortLetters) {
        this.sortLetters=sortLetters;
    }

    @Override
    public String getFullPinYin() {
        return fullPinYin;
    }

    @Override
    public void setFullPinYin(String fullPinYin) {
        this.fullPinYin=fullPinYin;
    }

    @Override
    public String getInitial() {
        return initial;
    }

    @Override
    public void setInitial(String initial) {
        this.initial=initial;
    }
}
