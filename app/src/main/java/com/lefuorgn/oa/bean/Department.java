package com.lefuorgn.oa.bean;

import com.lefuorgn.widget.tree.annotation.TreeNodeId;
import com.lefuorgn.widget.tree.annotation.TreeNodeLabel;
import com.lefuorgn.widget.tree.annotation.TreeNodePid;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuting on 2017/4/1.
 */

public class Department implements Serializable{
    private long agency_id;

    @TreeNodePid
    private long pid;

    @TreeNodeId
    private long dept_id;

    @TreeNodeLabel
    private String dept_name;


    private List<Department> childDepts;
    private List<OaUser> users;

    public long getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(long agency_id) {
        this.agency_id = agency_id;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
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

    public List<Department> getChildDepts() {
        return childDepts;
    }

    public void setChildDepts(List<Department> childDepts) {
        this.childDepts = childDepts;
    }

    public List<OaUser> getUsers() {
        return users;
    }

    public void setUsers(List<OaUser> users) {
        this.users = users;
    }
}
