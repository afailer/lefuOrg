package com.lefuorgn.lefu.bean;

/**
 * 老人病史信息
 */

public class MedicalHistory {

    private long health_id;
    private String items; // 老人多个病史,由逗号拼接
    private String elderly_name; // 老人名称
    private long agency_id;
    private String approval_status_content;
    private int disease_history;
    private String disease_description;
    private String diseaseCheckbox;
    private int approval_status;
    private String reserved; // 备注

    public long getHealth_id() {
        return health_id;
    }

    public void setHealth_id(long health_id) {
        this.health_id = health_id;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getElderly_name() {
        return elderly_name;
    }

    public void setElderly_name(String elderly_name) {
        this.elderly_name = elderly_name;
    }

    public long getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(long agency_id) {
        this.agency_id = agency_id;
    }

    public String getApproval_status_content() {
        return approval_status_content;
    }

    public void setApproval_status_content(String approval_status_content) {
        this.approval_status_content = approval_status_content;
    }

    public int getDisease_history() {
        return disease_history;
    }

    public void setDisease_history(int disease_history) {
        this.disease_history = disease_history;
    }

    public String getDisease_description() {
        return disease_description;
    }

    public void setDisease_description(String disease_description) {
        this.disease_description = disease_description;
    }

    public String getDiseaseCheckbox() {
        return diseaseCheckbox;
    }

    public void setDiseaseCheckbox(String diseaseCheckbox) {
        this.diseaseCheckbox = diseaseCheckbox;
    }

    public int getApproval_status() {
        return approval_status;
    }

    public void setApproval_status(int approval_status) {
        this.approval_status = approval_status;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    @Override
    public String toString() {
        return "MedicalHistory{" +
                "health_id=" + health_id +
                ", items='" + items + '\'' +
                ", elderly_name='" + elderly_name + '\'' +
                ", agency_id=" + agency_id +
                ", approval_status_content='" + approval_status_content + '\'' +
                ", disease_history=" + disease_history +
                ", disease_description='" + disease_description + '\'' +
                ", diseaseCheckbox='" + diseaseCheckbox + '\'' +
                ", approval_status=" + approval_status +
                ", reserved='" + reserved + '\'' +
                '}';
    }
}
