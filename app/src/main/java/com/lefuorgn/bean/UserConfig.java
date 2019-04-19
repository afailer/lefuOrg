package com.lefuorgn.bean;

/**
 * 用户配置信息
 */

public class UserConfig {

    private boolean community;
    private boolean crm;
    private boolean erp;
    private boolean oa;
    private boolean pension;
    private boolean tourism;
    private boolean volunteer;

    public boolean isCommunity() {
        return community;
    }

    public void setCommunity(boolean community) {
        this.community = community;
    }

    public boolean isCrm() {
        return crm;
    }

    public void setCrm(boolean crm) {
        this.crm = crm;
    }

    public boolean isErp() {
        return erp;
    }

    public void setErp(boolean erp) {
        this.erp = erp;
    }

    public boolean isOa() {
        return oa;
    }

    public void setOa(boolean oa) {
        this.oa = oa;
    }

    public boolean isPension() {
        return pension;
    }

    public void setPension(boolean pension) {
        this.pension = pension;
    }

    public boolean isTourism() {
        return tourism;
    }

    public void setTourism(boolean tourism) {
        this.tourism = tourism;
    }

    public boolean isVolunteer() {
        return volunteer;
    }

    public void setVolunteer(boolean volunteer) {
        this.volunteer = volunteer;
    }

    @Override
    public String toString() {
        return "UserConfig{" +
                "community=" + community +
                ", crm=" + crm +
                ", erp=" + erp +
                ", oa=" + oa +
                ", pension=" + pension +
                ", tourism=" + tourism +
                ", volunteer=" + volunteer +
                '}';
    }
}
