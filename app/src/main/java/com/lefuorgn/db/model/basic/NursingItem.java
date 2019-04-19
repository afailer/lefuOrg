package com.lefuorgn.db.model.basic;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * 护理条目
 */
@DatabaseTable(tableName = "NursingItem")
public class NursingItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@DatabaseField(generatedId = true)
	private long _Id; // 主键
	@DatabaseField(columnName = "id")
	private long item_id; // 护理项ID
	@DatabaseField
	private String nursing_content; // 护理项内容

	public long get_Id() {
		return _Id;
	}

	public void set_Id(long _Id) {
		this._Id = _Id;
	}

	public long getItem_id() {
		return item_id;
	}

	public void setItem_id(long item_id) {
		this.item_id = item_id;
	}

	public String getNursing_content() {
		return nursing_content;
	}

	public void setNursing_content(String nursing_content) {
		this.nursing_content = nursing_content;
	}

	@Override
	public String toString() {
		return "NursingItem [_Id=" + _Id + ", item_id=" + item_id
				+ ", nursing_content=" + nursing_content + "]";
	}

}
