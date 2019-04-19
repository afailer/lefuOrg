package com.lefuorgn.db.model.basic;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

class OldPeopleAttention implements Serializable {

	private static final long serialVersionUID = 1L;

	@DatabaseField
	protected long id; // 老人服务器数据库表主键ID
	@DatabaseField
	protected long agency_id; // 机构id
	@DatabaseField
	protected String elderly_name; // 老人姓名
	@DatabaseField
	protected int gender; // 性别
	@DatabaseField
	protected int age; // 年龄
	@DatabaseField
	protected String document_number; // 证件号码
	@DatabaseField
	protected String mobile; // 手机号
	@DatabaseField
	protected long bed_id; // 床位ID
	@DatabaseField
	protected String bed_no; // 床位号
	@DatabaseField
	protected boolean attention; // 通讯录中的红心关注,来自服务器
	@DatabaseField
	protected boolean cAttention; // 本地修改的红心关注
	@DatabaseField
	protected String sortLetters; // 做通讯录时的字母索引
	@DatabaseField
	protected String fullPinYin;// 老人姓名的全拼
	@DatabaseField
	protected String initial;// 老人姓名首字母全拼
	@DatabaseField
	protected String icon; // 头像
	@DatabaseField
	protected int check_in_status; // 入住状态
	@DatabaseField
	protected String config; // 老人个人配置
	@DatabaseField
	protected long income_level_id; // 月收入ID
	@DatabaseField
	protected int son_number; // 儿子数
	@DatabaseField
	protected int daughter_number; // 女儿数

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAgency_id() {
		return agency_id;
	}

	public void setAgency_id(long agency_id) {
		this.agency_id = agency_id;
	}

	public String getElderly_name() {
		return elderly_name;
	}

	public void setElderly_name(String elderly_name) {
		this.elderly_name = elderly_name;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getDocument_number() {
		return document_number;
	}

	public void setDocument_number(String document_number) {
		this.document_number = document_number;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public long getBed_id() {
		return bed_id;
	}

	public void setBed_id(long bed_id) {
		this.bed_id = bed_id;
	}

	public String getBed_no() {
		return bed_no;
	}

	public void setBed_no(String bed_no) {
		this.bed_no = bed_no;
	}

	public boolean isAttention() {
		return attention;
	}

	public void setAttention(boolean attention) {
		this.attention = attention;
	}

	public boolean iscAttention() {
		return cAttention;
	}

	public void setcAttention(boolean cAttention) {
		this.cAttention = cAttention;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public String getFullPinYin() {
		return fullPinYin;
	}

	public void setFullPinYin(String fullPinYin) {
		this.fullPinYin = fullPinYin;
	}

	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getCheck_in_status() {
		return check_in_status;
	}

	public void setCheck_in_status(int check_in_status) {
		this.check_in_status = check_in_status;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public long getIncome_level_id() {
		return income_level_id;
	}

	public void setIncome_level_id(long income_level_id) {
		this.income_level_id = income_level_id;
	}

	public int getSon_number() {
		return son_number;
	}

	public void setSon_number(int son_number) {
		this.son_number = son_number;
	}

	public int getDaughter_number() {
		return daughter_number;
	}

	public void setDaughter_number(int daughter_number) {
		this.daughter_number = daughter_number;
	}

	public boolean equals(OldPeopleAttention o) {
		return this.getDocument_number().equals(o.getDocument_number());
	}

	@Override
	public String toString() {
		return "OldPeopleAttention{" +
				"id=" + id +
				", agency_id=" + agency_id +
				", elderly_name='" + elderly_name + '\'' +
				", gender=" + gender +
				", age=" + age +
				", document_number='" + document_number + '\'' +
				", mobile='" + mobile + '\'' +
				", bed_id=" + bed_id +
				", bed_no='" + bed_no + '\'' +
				", attention=" + attention +
				", cAttention=" + cAttention +
				", sortLetters='" + sortLetters + '\'' +
				", fullPinYin='" + fullPinYin + '\'' +
				", initial='" + initial + '\'' +
				", icon='" + icon + '\'' +
				", check_in_status=" + check_in_status +
				", config='" + config + '\'' +
				", income_level_id=" + income_level_id +
				", son_number=" + son_number +
				", daughter_number=" + daughter_number +
				'}';
	}
}
