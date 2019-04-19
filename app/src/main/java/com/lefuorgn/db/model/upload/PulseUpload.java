package com.lefuorgn.db.model.upload;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lefuorgn.db.model.base.BaseUpload;
import com.lefuorgn.db.model.download.PulseDownload;

/**
 *	心率上传表
 */
@DatabaseTable(tableName = "PulseUpload")
public class PulseUpload extends BaseUpload {

	@DatabaseField(columnName = "id")
	private long pulse_id = -1; // 关联下载表主键ID
	@DatabaseField
	private int pulse_number;

	public PulseUpload() {}

	public PulseUpload(PulseDownload p) {
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
		this.pulse_id = p.getPulse_id();
		this.pulse_number = p.getPulse_number();
	}

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
	public String toString() {
		return "PulseUpload{" +
				"pulse_id=" + pulse_id +
				", pulse_number=" + pulse_number +
				'}';
	}
}
