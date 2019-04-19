package com.lefuorgn.db.model.download;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lefuorgn.db.model.base.BaseDownload;
import com.lefuorgn.db.model.interf.IDownloadable;

/**
 * 血压下载表
 */
@DatabaseTable(tableName = "PressureDownload")
public class PressureDownload extends BaseDownload implements IDownloadable {

	@DatabaseField(columnName = "id")
	private long blood_pressure_id = -1; // 当前数据服务器上主键ID
	@DatabaseField
	private int high_blood_pressure; // 高血压值
	@DatabaseField
	private int low_blood_pressure; // 低血压值

	public long getBlood_pressure_id() {
		return blood_pressure_id;
	}

	public void setBlood_pressure_id(long blood_pressure_id) {
		this.blood_pressure_id = blood_pressure_id;
	}

	public int getHigh_blood_pressure() {
		return high_blood_pressure;
	}

	public void setHigh_blood_pressure(int high_blood_pressure) {
		this.high_blood_pressure = high_blood_pressure;
	}

	public int getLow_blood_pressure() {
		return low_blood_pressure;
	}

	public void setLow_blood_pressure(int low_blood_pressure) {
		this.low_blood_pressure = low_blood_pressure;
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
		return blood_pressure_id;
	}

	@Override
	public long getCUpdateTime() {
		return updateTime;
	}

	@Override
	public long getCCreateTime() {
		return createTime;
	}

	@Override
	public String toString() {
		return "PressureDownload [_id=" + _id + ", blood_pressure_id="
				+ blood_pressure_id + ", high_blood_pressure="
				+ high_blood_pressure + ", low_blood_pressure="
				+ low_blood_pressure + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", old_people_id="
				+ old_people_id + ", old_people_name=" + old_people_name
				+ ", agency_id=" + agency_id + ", inspect_dt=" + inspect_dt
				+ ", scene=" + scene + ", inspect_user_id=" + inspect_user_id
				+ ", inspect_user_name=" + inspect_user_name
				+ ", entry_user_id=" + entry_user_id + ", entry_user_name="
				+ entry_user_name + ", entry_dt=" + entry_dt
				+ ", approval_status=" + approval_status + ", reserved="
				+ reserved + ", scode=" + scode + "]";
	}

}
