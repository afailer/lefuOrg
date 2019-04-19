package com.lefuorgn.oa.bean;

/**
 * Created by liuting on 2017/4/17.
 */

public class OaAttendanceRecord {
    private long id;//ID
    private long user_id;//用户id
    private String user_name;//员工姓名
    private long agency_id;//机构id
    private long dept_id;//部门id
    private String dept_name;//	部门名称
    private long position_id;//职位id
    private String position_name;//职位名称
    private long date_time;//日期
    private int date_type;//打卡方式名称(日期类型 有排版或打卡就是工作日，没有就是休息日 1：工作日 2：休息日)
    private long oa_scheduling_id;//班次id
    private String oa_scheduling_name;//班次名称
    private long work_start_time;//工作开始时间
    private long work_end_time;//工作结束时间
    private long real_work_time;//实际工作时间
    private long oa_attendance_arrived_id;//签到id
    private long oa_attendance_arrived_time;//签到时间
    private long oa_attendance_leave_id;//签退id
    private long oa_attendance_leave_time;//签退时间
    private long lated_time;//迟到时长
    private long absenteeism_time;//旷工时长
    private long tiaoxiu_time;//调休时长
    private long waichu_time;//外出时长
    private long chuchai_time;//出差
    private long jiaban_time;//加班
    private String vacations;//请假类型 有多个类型就是显示多个类型名称 比如 “请假 迟到 旷工”
    private long create_time;//创建时间
    private long update_time;//更新时间

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(long agency_id) {
        this.agency_id = agency_id;
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

    public long getPosition_id() {
        return position_id;
    }

    public void setPosition_id(long position_id) {
        this.position_id = position_id;
    }

    public String getPosition_name() {
        return position_name;
    }

    public void setPosition_name(String position_name) {
        this.position_name = position_name;
    }

    public long getDate_time() {
        return date_time;
    }

    public void setDate_time(long date_time) {
        this.date_time = date_time;
    }

    public int getDate_type() {
        return date_type;
    }

    public void setDate_type(int date_type) {
        this.date_type = date_type;
    }

    public long getOa_scheduling_id() {
        return oa_scheduling_id;
    }

    public void setOa_scheduling_id(long oa_scheduling_id) {
        this.oa_scheduling_id = oa_scheduling_id;
    }

    public String getOa_scheduling_name() {
        return oa_scheduling_name;
    }

    public void setOa_scheduling_name(String oa_scheduling_name) {
        this.oa_scheduling_name = oa_scheduling_name;
    }

    public long getWork_start_time() {
        return work_start_time;
    }

    public void setWork_start_time(long work_start_time) {
        this.work_start_time = work_start_time;
    }

    public long getWork_end_time() {
        return work_end_time;
    }

    public void setWork_end_time(long work_end_time) {
        this.work_end_time = work_end_time;
    }

    public long getReal_work_time() {
        return real_work_time;
    }

    public void setReal_work_time(long real_work_time) {
        this.real_work_time = real_work_time;
    }

    public long getOa_attendance_arrived_id() {
        return oa_attendance_arrived_id;
    }

    public void setOa_attendance_arrived_id(long oa_attendance_arrived_id) {
        this.oa_attendance_arrived_id = oa_attendance_arrived_id;
    }

    public long getOa_attendance_arrived_time() {
        return oa_attendance_arrived_time;
    }

    public void setOa_attendance_arrived_time(long oa_attendance_arrived_time) {
        this.oa_attendance_arrived_time = oa_attendance_arrived_time;
    }

    public long getOa_attendance_leave_id() {
        return oa_attendance_leave_id;
    }

    public void setOa_attendance_leave_id(long oa_attendance_leave_id) {
        this.oa_attendance_leave_id = oa_attendance_leave_id;
    }

    public long getOa_attendance_leave_time() {
        return oa_attendance_leave_time;
    }

    public void setOa_attendance_leave_time(long oa_attendance_leave_time) {
        this.oa_attendance_leave_time = oa_attendance_leave_time;
    }

    public long getLated_time() {
        return lated_time;
    }

    public void setLated_time(long lated_time) {
        this.lated_time = lated_time;
    }

    public long getAbsenteeism_time() {
        return absenteeism_time;
    }

    public void setAbsenteeism_time(long absenteeism_time) {
        this.absenteeism_time = absenteeism_time;
    }

    public long getTiaoxiu_time() {
        return tiaoxiu_time;
    }

    public void setTiaoxiu_time(long tiaoxiu_time) {
        this.tiaoxiu_time = tiaoxiu_time;
    }

    public long getWaichu_time() {
        return waichu_time;
    }

    public void setWaichu_time(long waichu_time) {
        this.waichu_time = waichu_time;
    }

    public long getChuchai_time() {
        return chuchai_time;
    }

    public void setChuchai_time(long chuchai_time) {
        this.chuchai_time = chuchai_time;
    }

    public long getJiaban_time() {
        return jiaban_time;
    }

    public void setJiaban_time(long jiaban_time) {
        this.jiaban_time = jiaban_time;
    }

    public String getVacations() {
        return vacations;
    }

    public void setVacations(String vacations) {
        this.vacations = vacations;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    @Override
    public String toString() {
        return "OaAttendanceRecord{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", agency_id=" + agency_id +
                ", dept_id=" + dept_id +
                ", dept_name='" + dept_name + '\'' +
                ", position_id=" + position_id +
                ", position_name='" + position_name + '\'' +
                ", date_time=" + date_time +
                ", date_type=" + date_type +
                ", oa_scheduling_id=" + oa_scheduling_id +
                ", oa_scheduling_name='" + oa_scheduling_name + '\'' +
                ", work_start_time=" + work_start_time +
                ", work_end_time=" + work_end_time +
                ", real_work_time=" + real_work_time +
                ", oa_attendance_arrived_id=" + oa_attendance_arrived_id +
                ", oa_attendance_arrived_time=" + oa_attendance_arrived_time +
                ", oa_attendance_leave_id=" + oa_attendance_leave_id +
                ", oa_attendance_leave_time=" + oa_attendance_leave_time +
                ", lated_time=" + lated_time +
                ", absenteeism_time=" + absenteeism_time +
                ", tiaoxiu_time=" + tiaoxiu_time +
                ", waichu_time=" + waichu_time +
                ", chuchai_time=" + chuchai_time +
                ", jiaban_time=" + jiaban_time +
                ", vacations='" + vacations + '\'' +
                ", create_time=" + create_time +
                ", update_time=" + update_time +
                '}';
    }
}
