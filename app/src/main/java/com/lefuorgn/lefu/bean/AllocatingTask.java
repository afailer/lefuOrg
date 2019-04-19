package com.lefuorgn.lefu.bean;

/**
 * 今日工作,配单信息bean类
 */

public class AllocatingTask {

    /**
     * {@link #task_state}
     * 已配单, 但未发布
     */
    public static final int TASK_STATE_NO_RELEASE = 0;
    /**
     * {@link #task_state}
     * 配单已发布, 正在执行中
     */
    public static final int TASK_STATE_EXECUTING = 1;
    /**
     * {@link #task_state}
     * 配单已完成
     */
    public static final int TASK_STATE_COMPLETE = 2;
    /**
     * {@link #task_state}
     * 未配单
     */
    public static final int TASK_STATE_NO_ALLOT = 3;

    private long id; // 当前配单信息ID
    private long agency_id; // 当前机构ID
    private long task_time; // 任务时间
    private long update_time;
    private int task_number; // 任务单数 = 老人1被选择任务类型和 + 老人2被选择任务类型和 + ...;
    private long create_time;
    private int task_state; //0 未发布, #FF343F; 1 执行中 #9FAFB6; ，2 已完成, 主色, 3, 未配单;
    private long head_nurse_id;

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

    public long getTask_time() {
        return task_time;
    }

    public void setTask_time(long task_time) {
        this.task_time = task_time;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public int getTask_number() {
        return task_number;
    }

    public void setTask_number(int task_number) {
        this.task_number = task_number;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getTask_state() {
        return task_state;
    }

    public void setTask_state(int task_state) {
        this.task_state = task_state;
    }

    public long getHead_nurse_id() {
        return head_nurse_id;
    }

    public void setHead_nurse_id(long head_nurse_id) {
        this.head_nurse_id = head_nurse_id;
    }

    @Override
    public String toString() {
        return "AllocatingTask{" +
                "id=" + id +
                ", agency_id=" + agency_id +
                ", task_time=" + task_time +
                ", update_time=" + update_time +
                ", task_number=" + task_number +
                ", create_time=" + create_time +
                ", task_state=" + task_state +
                ", head_nurse_id=" + head_nurse_id +
                '}';
    }
}
