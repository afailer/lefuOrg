package com.lefuorgn.lefu.bean;

import java.io.Serializable;

/**
 * 老人外出信息类
 */

public class Leave implements Serializable {

    /**
     * 申请请假状态
     */
    public static final int LEAVE_STATE_APPLY = 1;
    /**
     * 申请请假被驳回
     */
    public static final int LEAVE_STATE_REGECT = 2;
    /**
     * 申请请假审批通过
     */
    public static final int LEAVE_STATE_PASS = 3;
    /**
     * 老人当前请假进行状态,以离开本院
     */
    public static final int LEAVE_STATE_OUT = 4;
    /**
     * 此次请假已经完成
     */
    public static final int LEAVE_STATE_COMPLETE = 5;

    private long id;
    private long agency_id; // 机构ID
    private long old_people_id; // 老人ID
    private String elderly_name; // 老人名称
    private String party_signature; // 本人或家属签字
    private String leave_reason; // 请假事由
    private String notes_matters; // 请假需遵照事项
    private long leave_hospital_dt; // 实际出院时间
    private long expected_return_dt; // 预计返回时间
    private long signature_id; // 外出时护理员ID
    private String attn_signature; // 外出时护理员签名
    private long real_return_dt; // 实际返回时间
    private int leave_state; // 请假状态
    private long return_signature_id; // 返回时护理员ID
    private String return_attn_signature; // 返回时护理员签名
    private String reserved; // 备注
    private long update_dt; // 更新时间
    private long create_dt; // 创建时间

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(long agency_id) {
        this.agency_id = agency_id;
    }

    public long getOld_people_id() {
        return old_people_id;
    }

    public void setOld_people_id(long old_people_id) {
        this.old_people_id = old_people_id;
    }

    public String getElderly_name() {
        return elderly_name;
    }

    public void setElderly_name(String elderly_name) {
        this.elderly_name = elderly_name;
    }

    public String getParty_signature() {
        return party_signature;
    }

    public void setParty_signature(String party_signature) {
        this.party_signature = party_signature;
    }

    public String getLeave_reason() {
        return leave_reason;
    }

    public void setLeave_reason(String leave_reason) {
        this.leave_reason = leave_reason;
    }

    public String getNotes_matters() {
        return notes_matters;
    }

    public void setNotes_matters(String notes_matters) {
        this.notes_matters = notes_matters;
    }

    public long getLeave_hospital_dt() {
        return leave_hospital_dt;
    }

    public void setLeave_hospital_dt(long leave_hospital_dt) {
        this.leave_hospital_dt = leave_hospital_dt;
    }

    public long getExpected_return_dt() {
        return expected_return_dt;
    }

    public void setExpected_return_dt(long expected_return_dt) {
        this.expected_return_dt = expected_return_dt;
    }

    public long getSignature_id() {
        return signature_id;
    }

    public void setSignature_id(long signature_id) {
        this.signature_id = signature_id;
    }

    public String getAttn_signature() {
        return attn_signature;
    }

    public void setAttn_signature(String attn_signature) {
        this.attn_signature = attn_signature;
    }

    public long getReal_return_dt() {
        return real_return_dt;
    }

    public void setReal_return_dt(long real_return_dt) {
        this.real_return_dt = real_return_dt;
    }

    public int getLeave_state() {
        return leave_state;
    }

    public void setLeave_state(int leave_state) {
        this.leave_state = leave_state;
    }

    public long getReturn_signature_id() {
        return return_signature_id;
    }

    public void setReturn_signature_id(long return_signature_id) {
        this.return_signature_id = return_signature_id;
    }

    public String getReturn_attn_signature() {
        return return_attn_signature;
    }

    public void setReturn_attn_signature(String return_attn_signature) {
        this.return_attn_signature = return_attn_signature;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public long getUpdate_dt() {
        return update_dt;
    }

    public void setUpdate_dt(long update_dt) {
        this.update_dt = update_dt;
    }

    public long getCreate_dt() {
        return create_dt;
    }

    public void setCreate_dt(long create_dt) {
        this.create_dt = create_dt;
    }

    @Override
    public String toString() {
        return "Leave{" +
                "id=" + id +
                ", agency_id=" + agency_id +
                ", old_people_id=" + old_people_id +
                ", elderly_name='" + elderly_name + '\'' +
                ", party_signature='" + party_signature + '\'' +
                ", leave_reason='" + leave_reason + '\'' +
                ", notes_matters='" + notes_matters + '\'' +
                ", leave_hospital_dt=" + leave_hospital_dt +
                ", expected_return_dt=" + expected_return_dt +
                ", signature_id=" + signature_id +
                ", attn_signature='" + attn_signature + '\'' +
                ", real_return_dt=" + real_return_dt +
                ", leave_state=" + leave_state +
                ", return_signature_id=" + return_signature_id +
                ", return_attn_signature='" + return_attn_signature + '\'' +
                ", reserved='" + reserved + '\'' +
                ", update_dt=" + update_dt +
                ", create_dt=" + create_dt +
                '}';
    }
}
