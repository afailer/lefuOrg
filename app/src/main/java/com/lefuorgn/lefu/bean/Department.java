package com.lefuorgn.lefu.bean;

import com.lefuorgn.widget.tree.annotation.TreeNodeId;
import com.lefuorgn.widget.tree.annotation.TreeNodeLabel;
import com.lefuorgn.widget.tree.annotation.TreeNodeNum;
import com.lefuorgn.widget.tree.annotation.TreeNodePid;

import java.io.Serializable;

/**
 * 通讯录部门bean
 */

public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @TreeNodeId
    private long dept_id; // 部门ID
    @TreeNodePid
    private long pid; // 父级部门ID
    @TreeNodeLabel
    private String dept_name; // 部门名称
    @TreeNodeNum
    private int num; // 当前部门下成员的总数
    private long agency_id; // 机构ID

    public long getDept_id() {
        return dept_id;
    }

    public void setDept_id(long dept_id) {
        this.dept_id = dept_id;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public long getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(long agency_id) {
        this.agency_id = agency_id;
    }

    @Override
    public String toString() {
        return "Department{" +
                "dept_id=" + dept_id +
                ", pid=" + pid +
                ", dept_name='" + dept_name + '\'' +
                ", num=" + num +
                ", agency_id=" + agency_id +
                '}';
    }
}
