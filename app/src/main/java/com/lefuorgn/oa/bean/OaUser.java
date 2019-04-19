package com.lefuorgn.oa.bean;

import com.lefuorgn.interf.Pinyinable;

import java.io.Serializable;

/**
 * Created by liuting on 2017/4/1.
 */

public class OaUser implements Serializable,Pinyinable{
    private long dept_id;
    private long staff_id;
    private long user_id;
    private String user_name;
    private String user_icon;
    private long post_id;//职位id
    private String post_name;//职位名称
    private String dept_name;
    private int gender;//性别
    private String sortLetters; // 做通讯录时的字母索引
    private String fullPinYin; // 姓名的全拼
    private String initial; // 姓名首字母全拼

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
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

    public long getDept_id() {
        return dept_id;
    }

    public void setDept_id(long dept_id) {
        this.dept_id = dept_id;
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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public String getCharacters() {
        return user_name;
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

    @Override
    public String toString() {
        return "OaUser{" +
                "dept_id=" + dept_id +
                ", staff_id=" + staff_id +
                ", user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", user_icon='" + user_icon + '\'' +
                ", post_id=" + post_id +
                ", post_name='" + post_name + '\'' +
                ", dept_name='" + dept_name + '\'' +
                ", gender=" + gender +
                '}';
    }
}
