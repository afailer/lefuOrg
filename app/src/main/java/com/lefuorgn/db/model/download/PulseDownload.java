package com.lefuorgn.db.model.download;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lefuorgn.db.model.base.BaseDownload;
import com.lefuorgn.db.model.interf.IDownloadable;

/**
 * 心率下载表
 */
@DatabaseTable(tableName = "PulseDownload")
public class PulseDownload extends BaseDownload implements IDownloadable {

	@DatabaseField(columnName = "id")
	private long pulse_id = -1; // 当前数据服务器上主键ID
	@DatabaseField
	private int pulse_number;

	public long getPulse_id() {
		return pulse_id;
	}

	public void setPulse_id(long pulse_id) {
		this.pulse_id = pulse_id;
	}

	public int getPulse_number() {
		return pulse_number;
	}

	public void setPulse_number(int pulse_number) {
		this.pulse_number = pulse_number;
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
		return pulse_id;
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
		return "PulseDownload [_id=" + _id + ", pulse_id=" + pulse_id
				+ ", pulse_number=" + pulse_number + ", createTime="
				+ createTime + ", updateTime=" + updateTime
				+ ", old_people_id=" + old_people_id + ", old_people_name="
				+ old_people_name + ", agency_id=" + agency_id
				+ ", inspect_dt=" + inspect_dt + ", scene=" + scene
				+ ", inspect_user_id=" + inspect_user_id
				+ ", inspect_user_name=" + inspect_user_name
				+ ", entry_user_id=" + entry_user_id + ", entry_user_name="
				+ entry_user_name + ", entry_dt=" + entry_dt
				+ ", approval_status=" + approval_status + ", reserved="
				+ reserved + ", scode=" + scode + "]";
	}

}
