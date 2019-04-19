package com.lefuorgn.db.model.base;

import com.j256.ormlite.field.DatabaseField;

public class BaseDownload {

	@DatabaseField(generatedId = true)
	protected long _id;// 本地数据库主键ID
	@DatabaseField
	protected long createTime;// 创建时间
	@DatabaseField
	protected long updateTime = -28800000; // 更新时间
	@DatabaseField
	protected long old_people_id; // 老人id
	@DatabaseField
	protected String old_people_name; // 老人姓名
	@DatabaseField
	protected long agency_id; // 机构id
	@DatabaseField
	protected long inspect_dt; // 测量时间
	@DatabaseField
	protected int scene; // 当前测量场景    
	@DatabaseField
	protected long inspect_user_id; // 测量员工ID
	@DatabaseField
	protected String inspect_user_name; // 测量员工name
	@DatabaseField
	protected long entry_user_id; // 录入员工id
	@DatabaseField
	protected String entry_user_name;
	@DatabaseField
	protected long entry_dt; // 录入时间
	@DatabaseField
	protected int approval_status; // 审核状态
	@DatabaseField
	protected String reserved; // 备注
	@DatabaseField
	protected int scode; // 0: 是正常        1:本条数据服务器已经删除

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public long getOld_people_id() {
		return old_people_id;
	}

	public void setOld_people_id(long old_people_id) {
		this.old_people_id = old_people_id;
	}

	public String getOld_people_name() {
		return old_people_name;
	}

	public void setOld_people_name(String old_people_name) {
		this.old_people_name = old_people_name;
	}

	public long getAgency_id() {
		return agency_id;
	}

	public void setAgency_id(long agency_id) {
		this.agency_id = agency_id;
	}

	public long getInspect_dt() {
		return inspect_dt;
	}

	public void setInspect_dt(long inspect_dt) {
		this.inspect_dt = inspect_dt;
	}

	public int getScene() {
		return scene;
	}

	public void setScene(int scene) {
		this.scene = scene;
	}

	public long getInspect_user_id() {
		return inspect_user_id;
	}

	public void setInspect_user_id(long inspect_user_id) {
		this.inspect_user_id = inspect_user_id;
	}

	public String getInspect_user_name() {
		return inspect_user_name;
	}

	public void setInspect_user_name(String inspect_user_name) {
		this.inspect_user_name = inspect_user_name;
	}

	public long getEntry_user_id() {
		return entry_user_id;
	}

	public void setEntry_user_id(long entry_user_id) {
		this.entry_user_id = entry_user_id;
	}

	public String getEntry_user_name() {
		return entry_user_name;
	}

	public void setEntry_user_name(String entry_user_name) {
		this.entry_user_name = entry_user_name;
	}

	public long getEntry_dt() {
		return entry_dt;
	}

	public void setEntry_dt(long entry_dt) {
		this.entry_dt = entry_dt;
	}

	public int getApproval_status() {
		return approval_status;
	}

	public void setApproval_status(int approval_status) {
		this.approval_status = approval_status;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	public int getScode() {
		return scode;
	}

	public void setScode(int scode) {
		this.scode = scode;
	}

}
