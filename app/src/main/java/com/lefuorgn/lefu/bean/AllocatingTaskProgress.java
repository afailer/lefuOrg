package com.lefuorgn.lefu.bean;

/**
 * 配单任务执行进度信息类
 */

public class AllocatingTaskProgress {

    private int total;//该护理项总的任务
    private long nursing_item_id;//护理项id
    private int unfinished;//为完成数
    private int finish;//已完成数
    private int unreceived;//未接单数

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public long getNursing_item_id() {
        return nursing_item_id;
    }

    public void setNursing_item_id(long nursing_item_id) {
        this.nursing_item_id = nursing_item_id;
    }

    public int getUnfinished() {
        return unfinished;
    }

    public void setUnfinished(int unfinished) {
        this.unfinished = unfinished;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public int getUnreceived() {
        return unreceived;
    }

    public void setUnreceived(int unreceived) {
        this.unreceived = unreceived;
    }

    @Override
    public String toString() {
        return "AllocatingTaskProgress{" +
                "total=" + total +
                ", nursing_item_id=" + nursing_item_id +
                ", unfinished=" + unfinished +
                ", finish=" + finish +
                ", unreceived=" + unreceived +
                '}';
    }
}
