package com.lefuorgn.db.model.basic;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 体征数据颜色区间值
 */
@DatabaseTable(tableName = "SignIntervalColor")
public class SignIntervalColor {

	@DatabaseField(generatedId = true)
	private long _id;
	@DatabaseField
	private double high; // 最大值
	@DatabaseField
	private double low; // 最小值
	@DatabaseField
	private String fontColor; // 区间颜色值
	@DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = "signConfig_id")
	private SignConfig signConfig;
	@DatabaseField
	private int showLevel; // 显示级别, 一般是用在血压中,俩个值

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}
	
	public SignConfig getSignConfig() {
		return signConfig;
	}

	public void setSignConfig(SignConfig signConfig) {
		this.signConfig = signConfig;
	}

	public int getShowLevel() {
		return showLevel;
	}

	public void setShowLevel(int showLevel) {
		this.showLevel = showLevel;
	}

	@Override
	public String toString() {
		return "SignIntervalColor{" +
				"_id=" + _id +
				", high=" + high +
				", low=" + low +
				", fontColor='" + fontColor + '\'' +
				", signConfig=" + signConfig +
				", showLevel=" + showLevel +
				'}';
	}
}
