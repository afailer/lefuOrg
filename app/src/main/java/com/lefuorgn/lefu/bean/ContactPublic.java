package com.lefuorgn.lefu.bean;

import java.io.Serializable;

/**
 * Created by 韩春 on 2016/11/10.
 */

public class ContactPublic implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id; // 联系人ID
    private String work; // 工作名称
    private String phone; // 联系电话
    private String contacts; // 所属人
    private long agency_id; //机构ID
    private long create_dt; // 创建时间
    private long update_dt; // 修改时间

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public long getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(long agency_id) {
        this.agency_id = agency_id;
    }

    public long getCreate_dt() {
        return create_dt;
    }

    public void setCreate_dt(long create_dt) {
        this.create_dt = create_dt;
    }

    public long getUpdate_dt() {
        return update_dt;
    }

    public void setUpdate_dt(long update_dt) {
        this.update_dt = update_dt;
    }

    @Override
    public String toString() {
        return "ContactPublic{" +
                "id=" + id +
                ", work='" + work + '\'' +
                ", phone='" + phone + '\'' +
                ", contacts='" + contacts + '\'' +
                ", agency_id=" + agency_id +
                ", create_dt=" + create_dt +
                ", update_dt=" + update_dt +
                '}';
    }
}
