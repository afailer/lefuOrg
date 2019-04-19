package com.lefuorgn.db.model.basic;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * 体征数据范围以及颜色配置表
 */
@DatabaseTable(tableName = "SignConfig")
public class SignConfig {

	@DatabaseField(generatedId = true)
	private long _id;
	@DatabaseField
	private double inputMin; // 允许输入的最大值
	@DatabaseField
	private double inputMax; // 允许输入的最小值
	@DatabaseField
	private double confirmMin; // 数据正常范围最小值
	@DatabaseField
	private double confirmMax; // 数据正常范围最大值
	@DatabaseField
	private double yMin; //
	@DatabaseField
	private double yMax; //
	@DatabaseField
	private double accur; // 当前提交的值的精度值,即保留到小数点第几位
	@DatabaseField
	private int type; // 当前配置信息类型
	@DatabaseField
	private double diff; // 差值; 用于血压中
	@ForeignCollectionField
	private ForeignCollection<SignIntervalColor> fColor; // 区间颜色值
	@ForeignCollectionField
	private ForeignCollection<SignIntervalPointColor> fLine; // 端点颜色值
	
	private List<SignIntervalColor> color; // 区间颜色值
	private List<SignIntervalPointColor> line; // 端点颜色值

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public double getInputMin() {
		return inputMin;
	}

	public void setInputMin(double inputMin) {
		this.inputMin = inputMin;
	}

	public double getInputMax() {
		return inputMax;
	}

	public void setInputMax(double inputMax) {
		this.inputMax = inputMax;
	}

	public double getConfirmMin() {
		return confirmMin;
	}

	public void setConfirmMin(double confirmMin) {
		this.confirmMin = confirmMin;
	}

	public double getConfirmMax() {
		return confirmMax;
	}

	public void setConfirmMax(double confirmMax) {
		this.confirmMax = confirmMax;
	}

	public double getyMin() {
		return yMin;
	}

	public void setyMin(double yMin) {
		this.yMin = yMin;
	}

	public double getyMax() {
		return yMax;
	}

	public void setyMax(double yMax) {
		this.yMax = yMax;
	}

	public double getAccur() {
		return accur;
	}

	public void setAccur(double accur) {
		this.accur = accur;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

    public double getDiff() {
        return diff;
    }

    public void setDiff(double diff) {
        this.diff = diff;
    }

    public List<SignIntervalColor> getColor() {
		return color;
	}

	public void setColor(List<SignIntervalColor> color) {
		this.color = color;
	}

	public List<SignIntervalPointColor> getLine() {
		return line;
	}

	public void setLine(List<SignIntervalPointColor> line) {
		this.line = line;
	}

	public ForeignCollection<SignIntervalColor> getfColor() {
		return fColor;
	}

	public void setfColor(ForeignCollection<SignIntervalColor> fColor) {
		this.fColor = fColor;
	}

	public ForeignCollection<SignIntervalPointColor> getfLine() {
		return fLine;
	}

	public void setfLine(ForeignCollection<SignIntervalPointColor> fLine) {
		this.fLine = fLine;
	}

    @Override
    public String toString() {
        return "SignConfig{" +
                "_id=" + _id +
                ", inputMin=" + inputMin +
                ", inputMax=" + inputMax +
                ", confirmMin=" + confirmMin +
                ", confirmMax=" + confirmMax +
                ", yMin=" + yMin +
                ", yMax=" + yMax +
                ", accur=" + accur +
                ", type=" + type +
                ", diff=" + diff +
                ", fColor=" + fColor +
                ", fLine=" + fLine +
                ", color=" + color +
                ", line=" + line +
                '}';
    }
}
