package com.lefuorgn.db.model.basic;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * 可显示的体征项或者护理项条目
 */
@DatabaseTable(tableName = "DisplaySignOrNursingItem")
public class DisplaySignOrNursingItem implements Serializable{

	@DatabaseField(generatedId = true)
	private long _id;
	@DatabaseField
	private String title; // 条目名称
	@DatabaseField
	private long type; // 条目所在服务器ID
	@DatabaseField
	private boolean isSign; // true : 是体征项条目; false : 是护理项条目

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

	public boolean isSign() {
		return isSign;
	}

	public void setSign(boolean isSign) {
		this.isSign = isSign;
	}

	@Override
	public String toString() {
		return "DisplaySignOrNursingItem [_id=" + _id + ", title=" + title
				+ ", type=" + type + ", isSign=" + isSign + "]";
	}

}
