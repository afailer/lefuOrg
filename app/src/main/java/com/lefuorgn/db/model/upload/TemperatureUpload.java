package com.lefuorgn.db.model.upload;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lefuorgn.db.model.base.BaseUpload;
import com.lefuorgn.db.model.download.TemperatureDownload;

/**
 * 体温下载表
 */
@DatabaseTable(tableName = "TemperatureUpload")
public class TemperatureUpload extends BaseUpload {

	@DatabaseField
	private long id = -1; // 关联下载表主键ID
	@DatabaseField
	private double temperature;

	public TemperatureUpload() {}

	public TemperatureUpload(TemperatureDownload t) {
		this.dId = t.get_id();
		this.old_people_id = t.getOld_people_id();
		this.old_people_name = t.getOld_people_name();
		this.agency_id = t.getAgency_id();
		this.inspect_dt = t.getInspect_dt();
		this.inspect_user_id = t.getInspect_user_id();
		this.inspect_user_name = t.getInspect_user_name();
		this.entry_user_id = t.getEntry_user_id();
		this.entry_user_name = t.getEntry_user_name();
		this.entry_dt = t.getEntry_dt();
		this.approval_status = t.getApproval_status();
		this.reserved = t.getReserved();
        this.id = t.getId();
        this.temperature = t.getTemperature();
	}

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
	public String toString() {
		return "TemperatureUpload{" +
				"id=" + id +
				", temperature=" + temperature +
				'}';
	}
}
