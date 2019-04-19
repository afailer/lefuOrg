package com.lefuorgn.db.model.basic;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 配单类型详情信息条目
 */
@DatabaseTable(tableName = "AllocatingTypeTask")
public class AllocatingTypeTask {

    /**
     * {@link #save_type}的取值
     * 当前信息来自服务器
     */
    public static final int TYPE_FROM_SERVICE = 0;
    /**
     * {@link #save_type}的取值
     * 当前信息来自本地数据库
     */
    public static final int TYPE_FROM_DB = 1;

    @DatabaseField(generatedId = true)
    private long _id; // 主键ID
    @DatabaseField
    private long id; // 计划ID
    @DatabaseField
    private long agency_id; // 机构id
    @DatabaseField
    private long head_nurse_id; // 护士长ID
    @DatabaseField
    private long old_people_id; // 老人ID
    @DatabaseField
    private String old_people_name; // 老人姓名
    @DatabaseField
    private int number_nursing; // 护理次数
    @DatabaseField
    private int number_current; // 当前护理次数
    @DatabaseField
    private String care_workers; // 护工ID集合
    @DatabaseField
    private long care_worker; // 实际执行人ID集合
    @DatabaseField
    private String worker_name; // 执行人名称
    @DatabaseField
    private long nursing_item_id; // 护理项id
    @DatabaseField
    private String content; // 护理项名称
    @DatabaseField(columnName = "createTime")
    private long create_time; // 创建日期
    @DatabaseField(columnName = "updateTime")
    private long update_time; // 修改日期
    @DatabaseField
    private long task_time; // 任务执行时间
    @DatabaseField
    private long task_state; // 任务状态
    @DatabaseField
    private int save_type; // 保存到表中的类型
    @DatabaseField
    private String remark; // 备注

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

    public long getOld_people_id() {
        return old_people_id;
    }

    public void setOld_people_id(long old_people_id) {
        this.old_people_id = old_people_id;
    }

    public String getOld_people_name() {
        return old_people_name;
    }

    public void setOld_people_name(String old_people_name) {
        this.old_people_name = old_people_name;
    }

    public int getNumber_nursing() {
        return number_nursing;
    }

    public void setNumber_nursing(int number_nursing) {
        this.number_nursing = number_nursing;
    }

    public int getNumber_current() {
        return number_current;
    }

    public void setNumber_current(int number_current) {
        this.number_current = number_current;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public long getTask_state() {
        return task_state;
    }

    public void setTask_state(long task_state) {
        this.task_state = task_state;
    }

    public int getSave_type() {
        return save_type;
    }

    public void setSave_type(int save_type) {
        this.save_type = save_type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "AllocatingTypeTask{" +
                "_id=" + _id +
                ", id=" + id +
                ", agency_id=" + agency_id +
                ", head_nurse_id=" + head_nurse_id +
                ", old_people_id=" + old_people_id +
                ", old_people_name='" + old_people_name + '\'' +
                ", number_nursing=" + number_nursing +
                ", number_current=" + number_current +
                ", care_workers='" + care_workers + '\'' +
                ", care_worker=" + care_worker +
                ", worker_name='" + worker_name + '\'' +
                ", nursing_item_id=" + nursing_item_id +
                ", content='" + content + '\'' +
                ", create_time=" + create_time +
                ", update_time=" + update_time +
                ", task_time=" + task_time +
                ", task_state=" + task_state +
                ", save_type=" + save_type +
                ", remark='" + remark + '\'' +
                '}';
    }

}
