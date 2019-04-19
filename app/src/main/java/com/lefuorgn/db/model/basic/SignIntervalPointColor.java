package com.lefuorgn.db.model.basic;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 体征数据颜色区间端点值,画基线使用
 */
@DatabaseTable(tableName = "SignIntervalPointColor")
public class SignIntervalPointColor {

	@DatabaseField(generatedId = true)
	private long _id;
	@DatabaseField
	private String color; // 颜色值
	@DatabaseField
	private double value; // 基线值(端点值)
	@DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = "signConfig_id")
	private SignConfig signConfig;

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public SignConfig getSignConfig() {
		return signConfig;
	}

	public void setSignConfig(SignConfig signConfig) {
		this.signConfig = signConfig;
	}

	@Override
	public String toString() {
		return "SignIntervalPointColor [_id=" + _id + ", color=" + color
				+ ", value=" + value + ", signConfig=" + signConfig + "]";
	}

}
