package com.lefuorgn.db.model.basic;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 体征任务
 */
@DatabaseTable(tableName = "SignAndNursingTask")
public class SignAndNursingTask {

    @DatabaseField(generatedId = true)
    private long _id;
    @DatabaseField
    private long id; // 护理项ID
    @DatabaseField
    private String content; // 护理名称
    @DatabaseField
    private long uid; // 护理项ID标识 0：护理，>0：体征
    @DatabaseField
    private int status; // 选中状态 0：选中，1：未选中
    @DatabaseField
    private long level_id; // 级别ID
    @DatabaseField
    private long agency_id; // 机构ID
    @DatabaseField
    private int number_nursing; // 护理次数
    @DatabaseField
    private int period_type; // 护理重复类型 值：0：日，1：周，2：月
    @DatabaseField
    private int period_days; // 护理重复天数
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = "batchEditingTask_id")
    private BatchEditingTask batchEditingTask;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getLevel_id() {
        return level_id;
    }

    public void setLevel_id(long level_id) {
        this.level_id = level_id;
    }

    public long getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(long agency_id) {
        this.agency_id = agency_id;
    }

    public int getNumber_nursing() {
        return number_nursing;
    }

    public void setNumber_nursing(int number_nursing) {
        this.number_nursing = number_nursing;
    }

    public int getPeriod_type() {
        return period_type;
    }

    public void setPeriod_type(int period_type) {
        this.period_type = period_type;
    }

    public int getPeriod_days() {
        return period_days;
    }

    public void setPeriod_days(int period_days) {
        this.period_days = period_days;
    }

    public BatchEditingTask getBatchEditingTask() {
        return batchEditingTask;
    }

    public void setBatchEditingTask(BatchEditingTask batchEditingTask) {
        this.batchEditingTask = batchEditingTask;
    }

    @Override
    public String toString() {
        return "SignAndNursingTask{" +
                "_id=" + _id +
                ", id=" + id +
                ", content='" + content + '\'' +
                ", uid=" + uid +
                ", status=" + status +
                ", level_id=" + level_id +
                ", agency_id=" + agency_id +
                ", number_nursing=" + number_nursing +
                ", period_type=" + period_type +
                ", period_days=" + period_days +
                ", batchEditingTask=" + batchEditingTask +
                '}';
    }
}
