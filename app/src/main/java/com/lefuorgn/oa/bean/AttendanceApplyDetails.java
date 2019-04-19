package com.lefuorgn.oa.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 我审批的和我申请的页面处理详情bean类
 */

public class AttendanceApplyDetails implements Serializable {

    private long id; // 当前信息ID
    private long oa_verify_from_id; // 信息类型ID
    private String oa_verify_from_name; // 信息类型名称
    private long create_time; // 信息创建时间
    private long agency_id; // 当前申请人所属机构ID
    private String content; // 当前内容信息详情
    private int status; // 当前信息状态
    private long user_id; // 申请人ID
    private String user_name; // 申请人名称
    private String user_icon; // 申请人头像

    private List<ApprovalProcess> verifyAskLines; // 审批流程信息

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOa_verify_from_id() {
        return oa_verify_from_id;
    }

    public void setOa_verify_from_id(long oa_verify_from_id) {
        this.oa_verify_from_id = oa_verify_from_id;
    }

    public String getOa_verify_from_name() {
        return oa_verify_from_name;
    }

    public void setOa_verify_from_name(String oa_verify_from_name) {
        this.oa_verify_from_name = oa_verify_from_name;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public long getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(long agency_id) {
        this.agency_id = agency_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    public List<ApprovalProcess> getVerifyAskLines() {
        return verifyAskLines;
    }

    public void setVerifyAskLines(List<ApprovalProcess> verifyAskLines) {
        this.verifyAskLines = verifyAskLines;
    }

    @Override
    public String toString() {
        return "AttendanceApplyDetails{" +
                "id=" + id +
                ", oa_verify_from_id=" + oa_verify_from_id +
                ", oa_verify_from_name='" + oa_verify_from_name + '\'' +
                ", create_time=" + create_time +
                ", agency_id=" + agency_id +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", user_icon='" + user_icon + '\'' +
                ", verifyAskLines=" + verifyAskLines +
                '}';
    }
}
