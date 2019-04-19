package com.lefuorgn.db.model.upload;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lefuorgn.db.model.download.DailyNursingDownload;

/**
 * 照护记录上传表
 */
@DatabaseTable(tableName = "DailyNursingUpload")
public class DailyNursingUpload {

	@DatabaseField(generatedId = true)
	public long _id;// 数据id
	@DatabaseField(columnName = "id")
	private long daily_id = -1; // 照护ID
    @DatabaseField
    private long dId; // 下载表中的主键值
	@DatabaseField
	private long agency_id; // 机构ID
	@DatabaseField
	private long old_people_id; // 老人ID
	@DatabaseField
	private long caregiver_id; // 护工ID
	@DatabaseField
	private long nurs_items_id; // 照护项ID
	@DatabaseField
	private String caregiver_name; // 护工姓名
	@DatabaseField
	private long nursing_dt; // 护理时间
	@DatabaseField
	private long entry_staff; // 录入人员
	@DatabaseField(columnName = "createTime")
	private long create_dt; // 创建时间
	@DatabaseField(columnName = "updateTime")
	private long update_time; // 更新时间
	@DatabaseField
	private String reserved = ""; // 备注
	@DatabaseField
	private String media = "";

	public DailyNursingUpload() {}

	public DailyNursingUpload(DailyNursingDownload d, String uploadMedia) {
		this.daily_id = d.getDaily_id();
        this.dId = d.get_id();
		this.agency_id = d.getAgency_id();
		this.old_people_id = d.getOld_people_id();
		this.caregiver_id = d.getCaregiver_id();
		this.nurs_items_id = d.getNurs_items_id();
		this.caregiver_name = d.getCaregiver_name();
		this.nursing_dt = d.getNursing_dt();
		this.entry_staff = d.getEntry_staff();
		this.create_dt = d.getCreate_dt();
		this.update_time = d.getUpdate_time();
		this.reserved = d.getReserved();
		this.media = uploadMedia;
	}

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public long getDaily_id() {
		return daily_id;
	}

	public void setDaily_id(long daily_id) {
		this.daily_id = daily_id;
	}

    public long getdId() {
        return dId;
    }

    public void setdId(long dId) {
        this.dId = dId;
    }

    public long getAgency_id() {
		return agency_id;
	}

	public void setAgency_id(long agency_id) {
		this.agency_id = agency_id;
	}

	public long getOld_people_id() {
		return old_people_id;
	}

	public void setOld_people_id(long old_people_id) {
		this.old_people_id = old_people_id;
	}

	public long getCaregiver_id() {
		return caregiver_id;
	}

	public void setCaregiver_id(long caregiver_id) {
		this.caregiver_id = caregiver_id;
	}

	public long getNurs_items_id() {
		return nurs_items_id;
	}

	public void setNurs_items_id(long nurs_items_id) {
		this.nurs_items_id = nurs_items_id;
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

	public long getEntry_staff() {
		return entry_staff;
	}

	public void setEntry_staff(long entry_staff) {
		this.entry_staff = entry_staff;
	}

	public long getCreate_dt() {
		return create_dt;
	}

	public void setCreate_dt(long create_dt) {
		this.create_dt = create_dt;
	}

	public long getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(long update_time) {
		this.update_time = update_time;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

	@Override
	public String toString() {
		return "DailyNursingUpload [_id=" + _id + ", daily_id="
				+ daily_id + ", agency_id=" + agency_id + ", old_people_id="
				+ old_people_id + ", caregiver_id=" + caregiver_id
				+ ", nurs_items_id=" + nurs_items_id + ", caregiver_name="
				+ caregiver_name + ", nursing_dt=" + nursing_dt
				+ ", entry_staff=" + entry_staff + ", create_dt=" + create_dt
				+ ", update_time=" + update_time + ", reserved=" + reserved
				+ ", media=" + media + "]";
	};

}
