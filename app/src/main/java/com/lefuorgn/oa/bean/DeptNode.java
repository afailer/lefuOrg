package com.lefuorgn.oa.bean;

import com.lefuorgn.widget.tree.annotation.TreeNodeExtra1;
import com.lefuorgn.widget.tree.annotation.TreeNodeExtra2;
import com.lefuorgn.widget.tree.annotation.TreeNodeId;
import com.lefuorgn.widget.tree.annotation.TreeNodeIntExtra1;
import com.lefuorgn.widget.tree.annotation.TreeNodeLabel;
import com.lefuorgn.widget.tree.annotation.TreeNodeNum;
import com.lefuorgn.widget.tree.annotation.TreeNodePid;

/**
 * Created by liuting on 2017/4/5.
 */

public class DeptNode {
    @TreeNodePid
    private long pid;

    @TreeNodeId
    private long id;

    @TreeNodeLabel
    private String name;

    @TreeNodeNum
    private int nodeType;

    @TreeNodeExtra1
    private String deptName;

    @TreeNodeExtra2
    private String posName;

    @TreeNodeIntExtra1
    private int gender;

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public DeptNode(long pid, long id, String name, int nodeType, String deptName, String posName,int gender) {
        this.pid = pid;
        this.id = id;
        this.name = name;
        this.nodeType = nodeType;
        this.deptName = deptName;
        this.posName = posName;
        this.gender=gender;
    }
}
