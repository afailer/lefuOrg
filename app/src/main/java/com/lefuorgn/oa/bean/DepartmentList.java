package com.lefuorgn.oa.bean;

import java.util.List;

/**
 * Created by liuting on 2017/4/1.
 */

public class DepartmentList {
    private long agency_id;
    private String agency_name;
    private List<Department> depts;

    public long getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(long agency_id) {
        this.agency_id = agency_id;
    }

    public String getAgency_name() {
        return agency_name;
    }

    public void setAgency_name(String agency_name) {
        this.agency_name = agency_name;
    }

    public List<Department> getDepts() {
        return depts;
    }

    public void setDepts(List<Department> depts) {
        this.depts = depts;
    }
}
