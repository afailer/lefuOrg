package com.lefuorgn.db.model.upload;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lefuorgn.db.model.base.BaseUpload;
import com.lefuorgn.db.model.download.PressureDownload;

/**
 * 血压上传表
 */
@DatabaseTable(tableName = "PressureUpload")
public class PressureUpload extends BaseUpload {

	@DatabaseField(columnName = "id")
	private long blood_pressure_id = -1; // 关联下载表主键ID
	@DatabaseField
	private int high_blood_pressure; // 高血压值
	@DatabaseField
	private int low_blood_pressure; // 低血压值

	public PressureUpload() {}

	public PressureUpload(PressureDownload p) {
		this.dId = p.get_id();
		this.old_people_id = p.getOld_people_id();
		this.old_people_name = p.getOld_people_name();
		this.agency_id = p.getAgency_id();
		this.inspect_dt = p.getInspect_dt();
		this.inspect_user_id = p.getInspect_user_id();
		this.inspect_user_name = p.getInspect_user_name();
		this.entry_user_id = p.getEntry_user_id();
		this.entry_user_name = p.getEntry_user_name();
		this.entry_dt = p.getEntry_dt();
		this.approval_status = p.getApproval_status();
		this.reserved = p.getReserved();
		this.blood_pressure_id = p.getBlood_pressure_id();
		this.high_blood_pressure = p.getHigh_blood_pressure();
		this.low_blood_pressure = p.getLow_blood_pressure();
	}

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
	public String toString() {
		return "PressureUpload{" +
				"blood_pressure_id=" + blood_pressure_id +
				", high_blood_pressure=" + high_blood_pressure +
				", low_blood_pressure=" + low_blood_pressure +
				'}';
	}
}
