package com.lefuorgn.db.model.upload;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lefuorgn.db.model.base.BaseUpload;
import com.lefuorgn.db.model.download.SugarDownload;

/**
 * 血压上传表
 */
@DatabaseTable(tableName = "SugarUpload")
public class SugarUpload extends BaseUpload{

	@DatabaseField(columnName = "id")
	private long blood_sugar_id = -1; // 关联下载表主键ID
	@DatabaseField
	private double blood_sugar;
	
	public SugarUpload() {}

	public SugarUpload(SugarDownload s) {
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
        this.blood_sugar_id = s.getBlood_sugar_id();
        this.blood_sugar = s.getBlood_sugar();
	}

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
	public String toString() {
		return "SugarUpload{" +
				"blood_sugar_id=" + blood_sugar_id +
				", blood_sugar=" + blood_sugar +
				'}';
	}
}
