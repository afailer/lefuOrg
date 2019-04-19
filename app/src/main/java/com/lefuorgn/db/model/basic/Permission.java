package com.lefuorgn.db.model.basic;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 当前用户权限管理表
 */
@DatabaseTable(tableName = "Permission")
public class Permission {

	@DatabaseField(generatedId = true)
	private long _id;
	@DatabaseField
	private String remark; // value 日常护理
	@DatabaseField
	private String key; // key daily_V

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "Permission [_id=" + _id + ", remark=" + remark + ", key=" + key
				+ "]";
	}

}
