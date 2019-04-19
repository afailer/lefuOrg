package com.lefuorgn.db.model.upload;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lefuorgn.db.model.base.BaseUpload;
import com.lefuorgn.db.model.download.EatDownload;

/**
 * 饮食上传表
 */
@DatabaseTable(tableName = "EatUpload")
public class EatUpload extends BaseUpload {

	@DatabaseField
	private long id = -1; // 关联下载表主键ID
	@DatabaseField
	public int meal_amount;// 饮食量  0：偏少 1：正常 2：偏多
	@DatabaseField
	public int meal_type;// 0：早餐 1：中餐 2：晚餐

	public EatUpload() {}

	public EatUpload(EatDownload d) {
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
		this.meal_amount = d.getMeal_amount();
		this.meal_type = d.getMeal_type();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getMeal_amount() {
		return meal_amount;
	}

	public void setMeal_amount(int meal_amount) {
		this.meal_amount = meal_amount;
	}

	public int getMeal_type() {
		return meal_type;
	}

	public void setMeal_type(int meal_type) {
		this.meal_type = meal_type;
	}

	@Override
	public String toString() {
		return "EatUpload{" +
				"id=" + id +
				", meal_amount=" + meal_amount +
				", meal_type=" + meal_type +
				'}';
	}
}
