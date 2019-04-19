package com.lefuorgn.lefu.bean;

import com.lefuorgn.interf.Pinyinable;

import java.io.Serializable;

/**
 * 护工信息
 */
public class Staff implements Serializable, Pinyinable {

    private static final long serialVersionUID = 1L;

    private long staff_id; // 员工ID
    private String staff_name; // 员工名称
    private long user_id;
    private int gender; // 性别 14 : 男; 15 : 女
    private long agency_id; // 机构ID
    private long dept_id; // 所在部门ID
    private String dept_name; // 部门名称
    private long post_id; // 职位ID
    private String post_name; // 职位
    private String mobile; // 手机号码
    private String phone; // 固定号码
    private String mailbox; // 邮箱
    private String icon; // 员工头像
    private String sortLetters; // 做通讯录时的字母索引
    private String fullPinYin; // 姓名的全拼
    private String initial; // 姓名首字母全拼

    public long getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(long staff_id) {
        this.staff_id = staff_id;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public long getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(long agency_id) {
        this.agency_id = agency_id;
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

    public long getPost_id() {
        return post_id;
    }

    public void setPost_id(long post_id) {
        this.post_id = post_id;
    }

    public String getPost_name() {
        return post_name;
    }

    public void setPost_name(String post_name) {
        this.post_name = post_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String getCharacters() {
        return staff_name;
    }

    @Override
    public String getSortLetters() {
        return sortLetters;
    }

    @Override
    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    @Override
    public String getFullPinYin() {
        return fullPinYin;
    }

    @Override
    public void setFullPinYin(String fullPinYin) {
        this.fullPinYin = fullPinYin;
    }

    @Override
    public String getInitial() {
        return initial;
    }

    @Override
    public void setInitial(String initial) {
        this.initial = initial;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "staff_id=" + staff_id +
                ", staff_name='" + staff_name + '\'' +
                ", user_id=" + user_id +
                ", gender=" + gender +
                ", agency_id=" + agency_id +
                ", dept_id=" + dept_id +
                ", dept_name='" + dept_name + '\'' +
                ", post_id=" + post_id +
                ", post_name='" + post_name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", phone='" + phone + '\'' +
                ", mailbox='" + mailbox + '\'' +
                ", icon='" + icon + '\'' +
                ", sortLetters='" + sortLetters + '\'' +
                ", fullPinYin='" + fullPinYin + '\'' +
                ", initial='" + initial + '\'' +
                '}';
    }
}
