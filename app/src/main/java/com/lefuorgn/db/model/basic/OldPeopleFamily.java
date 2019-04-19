package com.lefuorgn.db.model.basic;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lefuorgn.db.model.interf.IDownloadable;

/**
 * 家属信息表
 */
@DatabaseTable(tableName = "OldPeopleFamily")
public class OldPeopleFamily implements IDownloadable {

	@DatabaseField(generatedId = true)
	public long _id; // 数据id
	@DatabaseField(columnName = "id")
	private long family_id; // 家属服务器主键ID
	@DatabaseField
	private long agency_id; // 所属机构ID
	@DatabaseField
	private long old_people_id; // 关联老人ID
	@DatabaseField
	private long user_id;
	@DatabaseField
	private String relatives_name; // 家属名称
	@DatabaseField
	private int gender;
	@DatabaseField
	private int document_type;
	@DatabaseField
	private String document_number;
	@DatabaseField
	private String elderly_relationship; // 家属与老人关系
	@DatabaseField
	private String workplace; // 工作地点
	@DatabaseField
	private String post;
	@DatabaseField
	private long income_level_id;
	@DatabaseField
	private String telephone;
	@DatabaseField
	private String mobile;
	@DatabaseField
	private String mailbox;
	@DatabaseField
	private String address;
	@DatabaseField(columnName = "createTime")
	private long create_dt;
	@DatabaseField(columnName = "updateTime")
	private long update_dt;
	@DatabaseField
	private int scode;// 0没删，1删除
	@DatabaseField
	private int first_contacts;// 是否是第一联系人，1：是；2：否
	@DatabaseField
	private int contacts_address;// 联络人家庭地址范围，1：是与老人同一城市；2：与老人不在同一城市，3：海外

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public long getFamily_id() {
		return family_id;
	}

	public void setFamily_id(long family_id) {
		this.family_id = family_id;
	}

	public long getAgency_id() {
		return agency_id;
	}

	public void setAgency_id(long agency_id) {
		this.agency_id = agency_id;
	}

	public long getOld_people_id() {
		return old_people_id;
	}

	public void setOld_people_id(int old_people_id) {
		this.old_people_id = old_people_id;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getRelatives_name() {
		return relatives_name;
	}

	public void setRelatives_name(String relatives_name) {
		this.relatives_name = relatives_name;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getDocument_type() {
		return document_type;
	}

	public void setDocument_type(int document_type) {
		this.document_type = document_type;
	}

	public String getDocument_number() {
		return document_number;
	}

	public void setDocument_number(String document_number) {
		this.document_number = document_number;
	}

	public String getElderly_relationship() {
		return elderly_relationship;
	}

	public void setElderly_relationship(String elderly_relationship) {
		this.elderly_relationship = elderly_relationship;
	}

	public String getWorkplace() {
		return workplace;
	}

	public void setWorkplace(String workplace) {
		this.workplace = workplace;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public long getIncome_level_id() {
		return income_level_id;
	}

	public void setIncome_level_id(long income_level_id) {
		this.income_level_id = income_level_id;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMailbox() {
		return mailbox;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getCreate_dt() {
		return create_dt;
	}

	public void setCreate_dt(long create_dt) {
		this.create_dt = create_dt;
	}

	public long getUpdate_dt() {
		return update_dt;
	}

	public void setUpdate_dt(long update_dt) {
		this.update_dt = update_dt;
	}

	public int getScode() {
		return scode;
	}

	public void setScode(int scode) {
		this.scode = scode;
	}

	public int getFirst_contacts() {
		return first_contacts;
	}

	public void setFirst_contacts(int first_contacts) {
		this.first_contacts = first_contacts;
	}

	public int getContacts_address() {
		return contacts_address;
	}

	public void setContacts_address(int contacts_address) {
		this.contacts_address = contacts_address;
	}

	@Override
	public long getC_id() {
		return _id;
	}

	@Override
	public long getCId() {
		return family_id;
	}

	@Override
	public long getCUpdateTime() {
		return update_dt;
	}

	@Override
	public long getCCreateTime() {
		return create_dt;
	}

	@Override
	public void setC_id(long _id) {
		this._id = _id;
	}

	@Override
	public String toString() {
		return "OldPeopleFamily [_id=" + _id + ", family_id=" + family_id
				+ ", agency_id=" + agency_id + ", old_people_id="
				+ old_people_id + ", user_id=" + user_id + ", relatives_name="
				+ relatives_name + ", gender=" + gender + ", document_type="
				+ document_type + ", document_number=" + document_number
				+ ", elderly_relationship=" + elderly_relationship
				+ ", workplace=" + workplace + ", post=" + post
				+ ", income_level_id=" + income_level_id + ", telephone="
				+ telephone + ", mobile=" + mobile + ", mailbox=" + mailbox
				+ ", address=" + address + ", create_dt=" + create_dt
				+ ", update_dt=" + update_dt + ", scode=" + scode
				+ ", first_contacts=" + first_contacts + ", contacts_address="
				+ contacts_address + "]";
	}

}
