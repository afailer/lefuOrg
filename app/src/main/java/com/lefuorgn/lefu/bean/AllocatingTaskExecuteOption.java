package com.lefuorgn.lefu.bean;

import java.io.Serializable;

/**
 * 今日工作,接单信息护理选项Bean类
 */

public class AllocatingTaskExecuteOption implements Serializable {

    /**
     * 当前数据来自服务器
     */
    public static final int SAVE_TYPE_SERVICE = 0;
    /**
     * 当前数据来自当前用户添加的本地数据
     */
    public static final int SAVE_TYPE_LOCAL = 1;

    private long _id; // 本地数据库id值
    private long id; // 护理项ID备注
    private long agency_id; // 机构id
    private long head_nurse_id; // 护士长ID
    private String care_workers; // 护工ID集合
    private long care_worker; // 实际执行人ID集合
    private String worker_name; // 执行人名称
    private long nursing_item_id; // 护理项id
    private String nursing_item_name; // 护理项名称
    private String percentage; // 护理总数与完成比例 :1/7
    private int complete ; // 已完成数额,如:1
    private int total; // 需要测量总数,如:7
    private long create_time; // 创建日期
    private long update_time; // 修改日期
    private long task_time; // 任务执行时间
    private String remark; //
    private int save_type; // 0: 服务器获取的，1: 本地添加的

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
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

    public long getHead_nurse_id() {
        return head_nurse_id;
    }

    public void setHead_nurse_id(long head_nurse_id) {
        this.head_nurse_id = head_nurse_id;
    }

    public String getCare_workers() {
        return care_workers;
    }

    public void setCare_workers(String care_workers) {
        this.care_workers = care_workers;
    }

    public long getCare_worker() {
        return care_worker;
    }

    public void setCare_worker(long care_worker) {
        this.care_worker = care_worker;
    }

    public String getWorker_name() {
        return worker_name;
    }

    public void setWorker_name(String worker_name) {
        this.worker_name = worker_name;
    }

    public long getNursing_item_id() {
        return nursing_item_id;
    }

    public void setNursing_item_id(long nursing_item_id) {
        this.nursing_item_id = nursing_item_id;
    }

    public String getNursing_item_name() {
        return nursing_item_name;
    }

    public void setNursing_item_name(String nursing_item_name) {
        this.nursing_item_name = nursing_item_name;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
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

    public long getTask_time() {
        return task_time;
    }

    public void setTask_time(long task_time) {
        this.task_time = task_time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSave_type() {
        return save_type;
    }

    public void setSave_type(int save_type) {
        this.save_type = save_type;
    }

    @Override
    public String toString() {
        return "AllocatingTaskExecuteOption{" +
                "_id=" + _id +
                ", id=" + id +
                ", agency_id=" + agency_id +
                ", head_nurse_id=" + head_nurse_id +
                ", care_workers='" + care_workers + '\'' +
                ", care_worker=" + care_worker +
                ", worker_name='" + worker_name + '\'' +
                ", nursing_item_id=" + nursing_item_id +
                ", nursing_item_name='" + nursing_item_name + '\'' +
                ", percentage='" + percentage + '\'' +
                ", complete=" + complete +
                ", total=" + total +
                ", create_time=" + create_time +
                ", update_time=" + update_time +
                ", task_time=" + task_time +
                ", remark='" + remark + '\'' +
                ", save_type=" + save_type +
                '}';
    }
}
