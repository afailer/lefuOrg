package com.lefuorgn.db.model.download;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lefuorgn.db.model.interf.IDownloadable;

import java.io.Serializable;

/**
 * 照护记录下载表
 */
@DatabaseTable(tableName = "DailyNursingDownload")
public class DailyNursingDownload implements Serializable, IDownloadable {

	private static final long serialVersionUID = 1L;

	@DatabaseField(generatedId = true)
	private long _id;// 数据id
	@DatabaseField(columnName = "id")
	private long daily_id = -1; // 当前数据服务器上主键ID
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
	private long entry_staff; // 录入人员ID
	@DatabaseField(columnName = "createTime")
	private long create_dt; // 创建时间
	@DatabaseField(columnName = "updateTime")
	private long update_time = -1; // 更新时间
	@DatabaseField
	private String reserved = ""; // 备注
	@DatabaseField
	private String media = "";
	@DatabaseField
	private int scode;

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

	public int getScode() {
		return scode;
	}

	public void setScode(int scode) {
		this.scode = scode;
	}

	@Override
	public long getC_id() {
		return _id;
	}

	@Override
	public void setC_id(long _id) {
		this._id = _id;
	}

	@Override
	public long getCId() {
		return daily_id;
	}

	@Override
	public long getCUpdateTime() {
		return update_time;
	}

	@Override
	public long getCCreateTime() {
		return create_dt;
	}

	@Override
	public String toString() {
		return "DailyNursingDownload [_id=" + _id + ", daily_id=" + daily_id
				+ ", agency_id=" + agency_id + ", old_people_id="
				+ old_people_id + ", caregiver_id=" + caregiver_id
				+ ", nurs_items_id=" + nurs_items_id + ", caregiver_name="
				+ caregiver_name + ", nursing_dt=" + nursing_dt
				+ ", entry_staff=" + entry_staff + ", create_dt=" + create_dt
				+ ", update_time=" + update_time + ", reserved=" + reserved
				+ ", media=" + media + ", scode=" + scode + "]";
	}

}
