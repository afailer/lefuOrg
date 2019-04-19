package com.lefuorgn.db.model.basic;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 字典配置类
 */
@DatabaseTable(tableName = "Dictionary")
public class Dictionary {

	@DatabaseField(generatedId = true)
	private long _id; // 本地数据库表主键
	@DatabaseField
	private long id; // 配置ID
	@DatabaseField
	private String content; // 描述内容
	@DatabaseField
	private long dictionaries_code; // 类别ID
	@DatabaseField
	private long order; // 类别内部排序
	@DatabaseField
	private int enabled; // 是否有效 0: 有效 其他: 作废

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getDictionaries_code() {
		return dictionaries_code;
	}

	public void setDictionaries_code(long dictionaries_code) {
		this.dictionaries_code = dictionaries_code;
	}

	public long getOrder() {
		return order;
	}

	public void setOrder(long order) {
		this.order = order;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "Dictionary [_id=" + _id + ", id=" + id + ", content=" + content
				+ ", dictionaries_code=" + dictionaries_code + ", order="
				+ order + ", enabled=" + enabled + "]";
	}

}
