package com.lefuorgn.oa.bean;

/**
 * 我审批的或者我申请的列表信息
 */

public class AttendanceApply {

    private long id; // 审批或者申请信息ID
    private long agency_id; // 机构ID
    private long oa_verify_from_id; // 审批或者申请信息类型ID
    private String oa_verify_from_logo; // 显示图片
    private String oa_verify_from_name; // 信息内容名称
    private long create_time; // 信息创建时间
    private long verify_end_time; // 信息结束时间
    private long verify_start_time; // 信息开始时间
    private long user_id; // 申请用户ID
    private String user_name; // 申请用户名称
    private String remark;//驳回理由
    private int status; // 信息状态 1：未审核 2：驳回 3：同意  4：撤销 5：退回

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

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

    public long getOa_verify_from_id() {
        return oa_verify_from_id;
    }

    public void setOa_verify_from_id(long oa_verify_from_id) {
        this.oa_verify_from_id = oa_verify_from_id;
    }

    public String getOa_verify_from_logo() {
        return oa_verify_from_logo;
    }

    public void setOa_verify_from_logo(String oa_verify_from_logo) {
        this.oa_verify_from_logo = oa_verify_from_logo;
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

    public long getVerify_end_time() {
        return verify_end_time;
    }

    public void setVerify_end_time(long verify_end_time) {
        this.verify_end_time = verify_end_time;
    }

    public long getVerify_start_time() {
        return verify_start_time;
    }

    public void setVerify_start_time(long verify_start_time) {
        this.verify_start_time = verify_start_time;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AttendanceApply{" +
                "id=" + id +
                ", agency_id=" + agency_id +
                ", oa_verify_from_id=" + oa_verify_from_id +
                ", oa_verify_from_logo='" + oa_verify_from_logo + '\'' +
                ", oa_verify_from_name='" + oa_verify_from_name + '\'' +
                ", create_time=" + create_time +
                ", verify_end_time=" + verify_end_time +
                ", verify_start_time=" + verify_start_time +
                ", user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", status=" + status +
                '}';
    }
}
