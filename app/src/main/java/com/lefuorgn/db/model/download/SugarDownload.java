package com.lefuorgn.db.model.download;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lefuorgn.db.model.base.BaseDownload;
import com.lefuorgn.db.model.interf.IDownloadable;

/**
 * 血糖下载表
 */
@DatabaseTable(tableName = "SugarDownload")
public class SugarDownload extends BaseDownload implements IDownloadable {

	@DatabaseField(columnName = "id")
	private long blood_sugar_id = -1; // 当前数据服务器上主键ID
	@DatabaseField
	private double blood_sugar;
	
	public long getBlood_sugar_id() {
		return blood_sugar_id;
	}

	public void setBlood_sugar_id(long blood_sugar_id) {
		this.blood_sugar_id = blood_sugar_id;
	}

	public double getBlood_sugar() {
		return blood_sugar;
	}

	public void setBlood_sugar(double blood_sugar) {
		this.blood_sugar = blood_sugar;
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
		return blood_sugar_id;
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
		return "SugarDownload [_id=" + _id + ", blood_sugar_id="
				+ blood_sugar_id + ", blood_sugar=" + blood_sugar
				+ ", createTime=" + createTime + ", updateTime=" + updateTime
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
