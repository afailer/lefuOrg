package com.lefuorgn.db.model.download;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lefuorgn.db.model.base.BaseDownload;
import com.lefuorgn.db.model.interf.IDownloadable;

/**
 * 体温下载表
 */
@DatabaseTable(tableName = "TemperatureDownload")
public class TemperatureDownload extends BaseDownload implements
		IDownloadable {

	@DatabaseField
	private long id = -1; // 当前数据服务器上主键ID
	@DatabaseField
	private double temperature;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
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
		return id;
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
		return "TemperatureDownload [_id=" + _id + ", id=" + id
				+ ", temperature=" + temperature + ", createTime=" + createTime
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
