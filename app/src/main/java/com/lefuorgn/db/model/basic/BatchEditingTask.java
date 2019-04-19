package com.lefuorgn.db.model.basic;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * 体征和护理配单信息
 */
@DatabaseTable(tableName = "BatchEditingTask")
public class BatchEditingTask {

    @DatabaseField(generatedId = true)
    private long _id;
    @DatabaseField
    private long id; // 级别ID
    @DatabaseField
    private String level; // 级别名称
    @DatabaseField
    private long agency_id; // 机构ID
    @ForeignCollectionField
    private ForeignCollection<SignAndNursingTask> nursingTask; // 护理项目列表
    @ForeignCollectionField
    private ForeignCollection<SignAndNursingTask> signTask; // 体征项目列表

    private List<SignAndNursingTask> nursingItmes; // 区间颜色值
    private List<SignAndNursingTask> signItems; // 端点颜色值

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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public long getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(long agency_id) {
        this.agency_id = agency_id;
    }

    public ForeignCollection<SignAndNursingTask> getNursingTask() {
        return nursingTask;
    }

    public void setNursingTask(ForeignCollection<SignAndNursingTask> nursingTask) {
        this.nursingTask = nursingTask;
    }

    public ForeignCollection<SignAndNursingTask> getSignTask() {
        return signTask;
    }

    public void setSignTask(ForeignCollection<SignAndNursingTask> signTask) {
        this.signTask = signTask;
    }

    public List<SignAndNursingTask> getNursingItmes() {
        return nursingItmes;
    }

    public void setNursingItmes(List<SignAndNursingTask> nursingItmes) {
        this.nursingItmes = nursingItmes;
    }

    public List<SignAndNursingTask> getSignItems() {
        return signItems;
    }

    public void setSignItems(List<SignAndNursingTask> signItems) {
        this.signItems = signItems;
    }
}
