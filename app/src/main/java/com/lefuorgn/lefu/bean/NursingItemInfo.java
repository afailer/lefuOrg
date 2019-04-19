package com.lefuorgn.lefu.bean;

import com.lefuorgn.db.model.download.DailyNursingDownload;

import java.io.Serializable;
import java.util.List;

/**
 * 护理条目信息
 */

public class NursingItemInfo implements Serializable {

    private long _id; // 下载表主键ID
    private long id; // 当前条目在服务器上的ID
    private long type; // 当前条目类型
    private String name; // 当前条目名称
    private int nurse_times;// 照护次数
    private String caregiver_name; // 护工姓名
    private long nursing_dt; // 护理时间
    private String reserved; // 备注
    private List<MultiMedia> media; // 多媒体信息
    private int current_times; // 当前批量添加的数据
    private int max_times; // 最大批量添加数
    private boolean select; // 当前护理项是否可以被操作
    private int period_type; // 护理重复类型 值：1：日，2：周，3：月
    private List<DailyNursingDownload> notUploaded; // 未上传的id集合

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

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNurse_times() {
        return nurse_times;
    }

    public void setNurse_times(int nurse_times) {
        this.nurse_times = nurse_times;
    }

    public String getCaregiver_name() {
        return caregiver_name;
    }

    public void setCaregiver_name(String caregiver_name) {
        this.caregiver_name = caregiver_name;
    }

    public long getNursing_dt() {
        return nursing_dt;
    }

    public void setNursing_dt(long nursing_dt) {
        this.nursing_dt = nursing_dt;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public List<MultiMedia> getMedia() {
        return media;
    }

    public void setMedia(List<MultiMedia> media) {
        this.media = media;
    }

    public int getCurrent_times() {
        return current_times;
    }

    public void setCurrent_times(int current_times) {
        this.current_times = current_times;
    }

    public int getMax_times() {
        return max_times;
    }

    public void setMax_times(int max_times) {
        this.max_times = max_times;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public int getPeriod_type() {
        return period_type;
    }

    public void setPeriod_type(int period_type) {
        this.period_type = period_type;
    }

    public List<DailyNursingDownload> getNotUploaded() {
        return notUploaded;
    }

    public void setNotUploaded(List<DailyNursingDownload> notUploaded) {
        this.notUploaded = notUploaded;
    }

    @Override
    public String toString() {
        return "NursingItemInfo{" +
                "_id=" + _id +
                ", id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", nurse_times=" + nurse_times +
                ", caregiver_name='" + caregiver_name + '\'' +
                ", nursing_dt=" + nursing_dt +
                ", reserved='" + reserved + '\'' +
                ", media='" + media.toString() + '\'' +
                ", current_times=" + current_times +
                ", max_times=" + max_times +
                ", select=" + select +
                ", period_type=" + period_type +
                ", notUploaded=" + notUploaded +
                '}';
    }
}
