package com.lefuorgn.db.model.upload;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lefuorgn.db.model.base.BaseUpload;
import com.lefuorgn.db.model.download.DrinkingDownload;

/**
 * 饮水上传表
 */
@DatabaseTable(tableName = "DrinkingUpload")
public class DrinkingUpload extends BaseUpload {

	@DatabaseField
	private long id = -1; // 关联下载表主键ID
	@DatabaseField
	private int water_amount;

	public DrinkingUpload() {}

	public DrinkingUpload(DrinkingDownload d) {
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
		this.water_amount = d.getWater_amount();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getWater_amount() {
		return water_amount;
	}

	public void setWater_amount(int water_amount) {
		this.water_amount = water_amount;
	}

	@Override
	public String toString() {
		return "DrinkingUpload{" +
				"id=" + id +
				", water_amount=" + water_amount +
				'}';
	}
}
