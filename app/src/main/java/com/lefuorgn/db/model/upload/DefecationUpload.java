package com.lefuorgn.db.model.upload;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lefuorgn.db.model.base.BaseUpload;
import com.lefuorgn.db.model.download.DefecationDownload;

/**
 * 排便上传表
 */
@DatabaseTable(tableName = "DefecationUpload")
public class DefecationUpload extends BaseUpload {

	@DatabaseField
	private long id = -1; // 关联下载表主键ID
	@DatabaseField
	private int defecation_times = -1;// 排便

	public DefecationUpload() {}

	public DefecationUpload(DefecationDownload d) {
		this.dId = d.get_id();
		this.old_people_id = d.getOld_people_id();
		this.old_people_name = d.getOld_people_name();
		this.agency_id = d.getAgency_id();
		this.inspect_dt = d.getInspect_dt();
		this.inspect_user_id = d.getInspect_user_id();
		this.inspect_user_name = d.getInspect_user_name();
		this.entry_user_id = d.getEntry_user_id();
		this.entry_user_name = d.getEntry_user_name();
		this.entry_dt = d.getEntry_dt();
		this.approval_status = d.getApproval_status();
		this.reserved = d.getReserved();
		this.id = d.getId();
		this.defecation_times = d.getDefecation_times();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getDefecation_times() {
		return defecation_times;
	}

	public void setDefecation_times(int defecation_times) {
		this.defecation_times = defecation_times;
	}

	@Override
	public String toString() {
		return "DefecationUpload{" +
				"id=" + id +
				", defecation_times=" + defecation_times +
				'}';
	}
}
