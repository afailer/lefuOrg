package com.lefuorgn.db.model.upload;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lefuorgn.db.model.base.BaseUpload;
import com.lefuorgn.db.model.download.SleepingDownload;

/**
 * 睡眠上传表
 */
@DatabaseTable(tableName = "SleepingUpload")
public class SleepingUpload extends BaseUpload {

	@DatabaseField
	private long id = -1; // 关联下载表主键ID
	@DatabaseField
	private String sleep_quality;

	public SleepingUpload() {}

	public SleepingUpload(SleepingDownload s) {
		this.dId = s.get_id();
		this.old_people_id = s.getOld_people_id();
		this.old_people_name = s.getOld_people_name();
		this.agency_id = s.getAgency_id();
		this.inspect_dt = s.getInspect_dt();
		this.inspect_user_id = s.getInspect_user_id();
		this.inspect_user_name = s.getInspect_user_name();
		this.entry_user_id = s.getEntry_user_id();
		this.entry_user_name = s.getEntry_user_name();
		this.entry_dt = s.getEntry_dt();
		this.approval_status = s.getApproval_status();
		this.reserved = s.getReserved();
		this.id = s.getId();
		this.sleep_quality = s.getSleep_quality();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSleep_quality() {
		return sleep_quality;
	}

	public void setSleep_quality(String sleep_quality) {
		this.sleep_quality = sleep_quality;
	}

	@Override
	public String toString() {
		return "SleepingUpload{" +
				"id=" + id +
				", sleep_quality='" + sleep_quality + '\'' +
				'}';
	}
}
