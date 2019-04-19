package com.lefuorgn.lefu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 今日工作,接单信息bean类
 */

public class AllocatingTaskExecute implements Serializable{

    private long id;
    private long old_people_id; // 老人id
    private String old_people_name; // 老人姓名
    private boolean isChecked;
    private long task_time; // 任务时间
    private List<AllocatingTaskExecuteOption> options; // 护理选项

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public long getTask_time() {
        return task_time;
    }

    public void setTask_time(long task_time) {
        this.task_time = task_time;
    }

    public List<AllocatingTaskExecuteOption> getOptions() {
        return options;
    }

    public void setOptions(List<AllocatingTaskExecuteOption> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "AllocatingTaskExecute{" +
                "id=" + id +
                ", old_people_id=" + old_people_id +
                ", old_people_name='" + old_people_name + '\'' +
                ", isChecked=" + isChecked +
                ", task_time=" + task_time +
                ", options=" + options +
                '}';
    }

}
