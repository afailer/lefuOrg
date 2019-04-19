package com.lefuorgn.db.model.download;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lefuorgn.db.model.base.BaseDownload;
import com.lefuorgn.db.model.interf.IDownloadable;

/**
 * 饮食下载表
 */
@DatabaseTable(tableName = "EatDownload")
public class EatDownload extends BaseDownload implements IDownloadable {

	@DatabaseField
	private long id = -1; // 当前数据服务器上主键ID
	@DatabaseField
	private int meal_amount = -1; // 饮食量   1：偏少 2：正常 3：偏多
	@DatabaseField
	private int meal_type = -1; // 1：早餐 2：中餐 3：晚餐

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
	public long getC_id() {
		return _id;
	}
	
	@Override
	public void setC_id(long _id) {
		this._id = _id;
	}

	@Override
	public long getCId() {
		return id;
	}

	@Override
	public long getCUpdateTime() {
		return updateTime;
	}

	@Override
	public long getCCreateTime() {
		return createTime;
	}

	/**
	 * 获取饮食组合信息
	 * @return 饮食状况
	 */
	public String getDescriptiveInfo() {
		StringBuilder info = new StringBuilder();
		switch (meal_type) {
			case 1:
				info.append("早餐");
				break;
			case 2:
				info.append("午餐");
				break;
			case 3:
				info.append("晚餐");
				break;
			default:
				info.append("");
				break;
		}
		switch (meal_amount) {
			case 1:
				info.append("偏少");
				break;
			case 2:
				info.append("正常");
				break;
			case 3:
				info.append("偏多");
				break;
			default:
				info.append("");
				break;
		}
		return info.toString();
	}

	@Override
	public String toString() {
		return "EatDownload [_id=" + _id + ", id=" + id + ", meal_amount="
				+ meal_amount + ", meal_type=" + meal_type + ", createTime="
				+ createTime + ", updateTime=" + updateTime
				+ ", old_people_id=" + old_people_id + ", old_people_name="
				+ old_people_name + ", agency_id=" + agency_id
				+ ", inspect_dt=" + inspect_dt + ", scene=" + scene
				+ ", inspect_user_id=" + inspect_user_id
				+ ", inspect_user_name=" + inspect_user_name
				+ ", entry_user_id=" + entry_user_id + ", entry_user_name="
				+ entry_user_name + ", entry_dt=" + entry_dt
				+ ", approval_status=" + approval_status + ", reserved="
				+ reserved + ", scode=" + scode + "]";
	}

}
