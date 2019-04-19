package com.lefuorgn.db.model.upload;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lefuorgn.db.model.download.DailyLifeDownload;

/**
 * 随手拍上传表
 */
@DatabaseTable(tableName = "DailyLifeUpload")
public class DailyLifeUpload {

	@DatabaseField(generatedId = true)
	private long _id;// 数据id
	@DatabaseField(columnName = "id")
	private long daily_id = -1;
    @DatabaseField
    private long dId; // 下载表中的主键值
	@DatabaseField
	private long nursing_dt;
	@DatabaseField
	private String media;
	@DatabaseField
	private long agency_id;
	@DatabaseField
	private String reserved;
	@DatabaseField(columnName = "createTime")
	private long create_dt;
	@DatabaseField
	private long caregiver_id;
	@DatabaseField
	private long entry_staff;
	@DatabaseField
	private long old_people_id;
	@DatabaseField
	private int scode;
	@DatabaseField(columnName = "updateTime")
	private long update_time;
	@DatabaseField
	private int praise_number;
	@DatabaseField
	private String caregiver_name;

	public DailyLifeUpload() {}

	public DailyLifeUpload(DailyLifeDownload d, String uploadMedia) {
		this.daily_id = d.getDaily_id();
		this.dId = d.get_id();
		this.agency_id = d.getAgency_id();
		this.old_people_id = d.getOld_people_id();
		this.caregiver_id = d.getCaregiver_id();
		this.caregiver_name = d.getCaregiver_name();
		this.nursing_dt = d.getNursing_dt();
		this.entry_staff = d.getEntry_staff();
		this.create_dt = d.getCreate_dt();
		this.update_time = d.getUpdate_time();
		this.reserved = d.getReserved();
		this.media = uploadMedia;
        this.praise_number = d.getPraise_number();
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

    public long getNursing_dt() {
		return nursing_dt;
	}

	public void setNursing_dt(long nursing_dt) {
		this.nursing_dt = nursing_dt;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

	public long getAgency_id() {
		return agency_id;
	}

	public void setAgency_id(long agency_id) {
		this.agency_id = agency_id;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	public long getCreate_dt() {
		return create_dt;
	}

	public void setCreate_dt(long create_dt) {
		this.create_dt = create_dt;
	}

	public long getCaregiver_id() {
		return caregiver_id;
	}

	public void setCaregiver_id(long caregiver_id) {
		this.caregiver_id = caregiver_id;
	}

	public long getEntry_staff() {
		return entry_staff;
	}

	public void setEntry_staff(long entry_staff) {
		this.entry_staff = entry_staff;
	}

	public long getOld_people_id() {
		return old_people_id;
	}

	public void setOld_people_id(long old_people_id) {
		this.old_people_id = old_people_id;
	}

	public int getScode() {
		return scode;
	}

	public void setScode(int scode) {
		this.scode = scode;
	}

	public long getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(long update_time) {
		this.update_time = update_time;
	}

	public int getPraise_number() {
		return praise_number;
	}

	public void setPraise_number(int praise_number) {
		this.praise_number = praise_number;
	}

	public String getCaregiver_name() {
		return caregiver_name;
	}

	public void setCaregiver_name(String caregiver_name) {
		this.caregiver_name = caregiver_name;
	}

	@Override
	public String toString() {
		return "DailyLifeUpload [_id=" + _id + ", daily_id=" + daily_id
				+ ", nursing_dt=" + nursing_dt + ", media=" + media
				+ ", agency_id=" + agency_id + ", reserved=" + reserved
				+ ", create_dt=" + create_dt + ", caregiver_id=" + caregiver_id
				+ ", entry_staff=" + entry_staff + ", elderly_name="
				+ ", old_people_id=" + old_people_id
				+ ", scode=" + scode + ", update_time=" + update_time
				+ ", praise_number=" + praise_number + ", caregiver_name="
				+ caregiver_name + "]";
	}

}