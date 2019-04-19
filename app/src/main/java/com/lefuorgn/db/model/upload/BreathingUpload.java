package com.lefuorgn.db.model.upload;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lefuorgn.db.model.base.BaseUpload;
import com.lefuorgn.db.model.download.BreathingDownload;

/**
 * 呼吸上传表
 */
@DatabaseTable(tableName = "BreathingUpload")
public class BreathingUpload extends BaseUpload {

	@DatabaseField
	private long id = -1; // 关联下载表ID
	@DatabaseField
	private int breathing_times = -1;// 呼吸

	public BreathingUpload() {}

	public BreathingUpload(BreathingDownload b) {
		this.dId = b.get_id();
		this.old_people_id = b.getOld_people_id();
		this.old_people_name = b.getOld_people_name();
		this.agency_id = b.getAgency_id();
		this.inspect_dt = b.getInspect_dt();
		this.inspect_user_id = b.getInspect_user_id();
		this.inspect_user_name = b.getInspect_user_name();
		this.entry_user_id = b.getEntry_user_id();
		this.entry_user_name = b.getEntry_user_name();
		this.entry_dt = b.getEntry_dt();
		this.approval_status = b.getApproval_status();
		this.reserved = b.getReserved();
		this.id = b.getId();
		this.breathing_times = b.getBreathing_times();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getBreathing_times() {
		return breathing_times;
	}

	public void setBreathing_times(int breathing_times) {
		this.breathing_times = breathing_times;
	}

	@Override
	public String toString() {
		return "BreathingUpload{" +
				"id=" + id +
				", breathing_times=" + breathing_times +
				'}';
	}
}
