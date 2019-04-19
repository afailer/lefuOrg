package com.lefuorgn.db.model.basic;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lefuorgn.db.model.interf.IDownloadable;
import com.lefuorgn.interf.Pinyinable;

import java.io.Serializable;

/**
 * 老人基本信息表
 */
@DatabaseTable(tableName = "OldPeople")
public class OldPeople implements IDownloadable, Pinyinable, Serializable {

    private static final long serialVersionUID = 1L;

	@DatabaseField(generatedId = true)
	private long _id; // 数据id
	@DatabaseField
	private long id; // 老人服务器数据库表主键ID
	@DatabaseField
	private long agency_id; // 机构id
	@DatabaseField
	private String elderly_name; // 老人姓名
	@DatabaseField
	private int gender; // 性别
	@DatabaseField
	private int age; // 年龄
	@DatabaseField
	private String document_number; // 证件号码
	@DatabaseField
	private String mobile; // 手机号
	@DatabaseField
	private long bed_id; // 床位ID
	@DatabaseField(defaultValue = "")
	private String floor_no; // 楼栋号
	@DatabaseField(defaultValue = "")
	private String floor_layer; // 楼层
	@DatabaseField(defaultValue = "")
	private String room_no; // 房间号
	@DatabaseField
	private String bed_no; // 床位号
    @DatabaseField(defaultValue = "")
    private String face; // 朝向
	@DatabaseField
	private boolean attention; // 通讯录中的红心关注,来自服务器
	@DatabaseField
	private boolean cAttention; // 本地修改的红心关注
	@DatabaseField(defaultValue = "")
	private String sortLetters; // 做通讯录时的字母索引
	@DatabaseField
	private String fullPinYin; // 老人姓名的全拼
	@DatabaseField
	private String initial; // 老人姓名首字母全拼
	@DatabaseField
	private String icon; // 头像
	@DatabaseField
	private int check_in_status; // 入住状态
	@DatabaseField
	private String config; // 老人个人配置
	@DatabaseField
	private long income_level_id; // 月收入ID
	@DatabaseField
	private int son_number; // 儿子数
	@DatabaseField
	private int daughter_number; // 女儿数
	@DatabaseField
	private int nation; // 民族
	@DatabaseField
	private String account; // 户口类型
	@DatabaseField
	private int family_number; // 家庭人口数
	@DatabaseField
	private int social_security; // 社保
	@DatabaseField
	private String hobbies; // 爱好
	@DatabaseField
	private int marital_status; // 婚姻状况
	@DatabaseField
	private int political_affiliation; // 政治面貌
	@DatabaseField
	private int education; // 教育程度
	@DatabaseField
	private String religion; // 宗教信仰
	@DatabaseField
	private String address; // 通信地址
	@DatabaseField
	private int person_type; // 人员类型
	@DatabaseField(columnName = "createTime")
	private long create_dt; // 创建日期
	@DatabaseField(columnName = "updateTime")
	private long update_time;
	@DatabaseField
	private int monitor_level = 0; // 关注级别
	@DatabaseField
	private long nursing_level_id;// 护理级别ID
	@DatabaseField
	private long check_in_dt; // 入住日期
	@DatabaseField
	private long check_out_dt; // 退住日期
	@DatabaseField
	private String mailbox; // 邮箱
	@DatabaseField
	private String phone; // 固定电话
	@DatabaseField
	private long birthday_dt; // 出生日期
	@DatabaseField
	private int birthday_type; // 出生日期类型
	@DatabaseField
	private int document_type; // 证件类型
	@DatabaseField
	private String account_address = ""; // 户籍地址
	@DatabaseField
	private int scode; // 0没删，1删除
	@DatabaseField
	private int check_in_pay; // 入住费用支付方式入住费用支付，1：子女支付；2：老年人支付；3：政府支付；4：单位支付；5：其它

	private int countNum;
	private boolean checked = true;

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

	public String getFloor_no() {
		return floor_no;
	}

	public void setFloor_no(String floor_no) {
		this.floor_no = floor_no;
	}

	public String getFloor_layer() {
		return floor_layer;
	}

	public void setFloor_layer(String floor_layer) {
		this.floor_layer = floor_layer;
	}

	public String getRoom_no() {
		return room_no;
	}

	public void setRoom_no(String room_no) {
		this.room_no = room_no;
	}

	public String getBed_no() {
		return bed_no;
	}

	public void setBed_no(String bed_no) {
		this.bed_no = bed_no;
	}

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
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

	public int getNation() {
		return nation;
	}

	public void setNation(int nation) {
		this.nation = nation;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getFamily_number() {
		return family_number;
	}

	public void setFamily_number(int family_number) {
		this.family_number = family_number;
	}

	public int getSocial_security() {
		return social_security;
	}

	public void setSocial_security(int social_security) {
		this.social_security = social_security;
	}

	public String getHobbies() {
		return hobbies;
	}

	public void setHobbies(String hobbies) {
		this.hobbies = hobbies;
	}

	public int getMarital_status() {
		return marital_status;
	}

	public void setMarital_status(int marital_status) {
		this.marital_status = marital_status;
	}

	public int getPolitical_affiliation() {
		return political_affiliation;
	}

	public void setPolitical_affiliation(int political_affiliation) {
		this.political_affiliation = political_affiliation;
	}

	public int getEducation() {
		return education;
	}

	public void setEducation(int education) {
		this.education = education;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPerson_type() {
		return person_type;
	}

	public void setPerson_type(int person_type) {
		this.person_type = person_type;
	}

	public long getCreate_dt() {
		return create_dt;
	}

	public void setCreate_dt(long create_dt) {
		this.create_dt = create_dt;
	}

	public long getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(long update_time) {
		this.update_time = update_time;
	}

	public int getMonitor_level() {
		return monitor_level;
	}

	public void setMonitor_level(int monitor_level) {
		this.monitor_level = monitor_level;
	}

	public long getNursing_level_id() {
		return nursing_level_id;
	}

	public void setNursing_level_id(long nursing_level_id) {
		this.nursing_level_id = nursing_level_id;
	}

	public long getCheck_in_dt() {
		return check_in_dt;
	}

	public void setCheck_in_dt(long check_in_dt) {
		this.check_in_dt = check_in_dt;
	}

	public long getCheck_out_dt() {
		return check_out_dt;
	}

	public void setCheck_out_dt(long check_out_dt) {
		this.check_out_dt = check_out_dt;
	}

	public String getMailbox() {
		return mailbox;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getBirthday_dt() {
		return birthday_dt;
	}

	public void setBirthday_dt(long birthday_dt) {
		this.birthday_dt = birthday_dt;
	}

	public int getBirthday_type() {
		return birthday_type;
	}

	public void setBirthday_type(int birthday_type) {
		this.birthday_type = birthday_type;
	}

	public int getDocument_type() {
		return document_type;
	}

	public void setDocument_type(int document_type) {
		this.document_type = document_type;
	}

	public String getAccount_address() {
		return account_address;
	}

	public void setAccount_address(String account_address) {
		this.account_address = account_address;
	}

	public int getScode() {
		return scode;
	}

	public void setScode(int scode) {
		this.scode = scode;
	}

	public int getCheck_in_pay() {
		return check_in_pay;
	}

	public void setCheck_in_pay(int check_in_pay) {
		this.check_in_pay = check_in_pay;
	}

	public int getCountNum() {
		return countNum;
	}

	public void setCountNum(int countNum) {
		this.countNum = countNum;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
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
		return update_time;
	}

	@Override
	public long getCCreateTime() {
		return create_dt;
	}

    @Override
    public String getCharacters() {
        return elderly_name;
    }

    public String getCheck_in_statusStr() {
        //0在院 1出院 2过世
        switch(check_in_status){
            case 0:
                return "在院";
            case 1:
                return "出院";
            case 2:
                return "过世";
        }
        return "";
    }

    public String getCheck_in_payStr() {
		switch (check_in_pay) {
		case 1:
			return "子女支付";
		case 2:
			return "老年人支付";
		case 3:
			return "政府支付";
		case 4:
			return "单位支付";
		case 5:
			return "其它";
		}
		return "";
	}

	@Override
	public String toString() {
		return "OldPeople{" +
				"_id=" + _id +
				", id=" + id +
				", agency_id=" + agency_id +
				", elderly_name='" + elderly_name + '\'' +
				", gender=" + gender +
				", age=" + age +
				", document_number='" + document_number + '\'' +
				", mobile='" + mobile + '\'' +
				", bed_id=" + bed_id +
				", floor_no='" + floor_no + '\'' +
				", floor_layer='" + floor_layer + '\'' +
				", room_no='" + room_no + '\'' +
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
				", nation=" + nation +
				", account='" + account + '\'' +
				", family_number=" + family_number +
				", social_security=" + social_security +
				", hobbies='" + hobbies + '\'' +
				", marital_status=" + marital_status +
				", political_affiliation=" + political_affiliation +
				", education=" + education +
				", religion='" + religion + '\'' +
				", address='" + address + '\'' +
				", person_type=" + person_type +
				", create_dt=" + create_dt +
				", update_time=" + update_time +
				", monitor_level=" + monitor_level +
				", nursing_level_id=" + nursing_level_id +
				", check_in_dt=" + check_in_dt +
				", check_out_dt=" + check_out_dt +
				", mailbox='" + mailbox + '\'' +
				", phone='" + phone + '\'' +
				", birthday_dt=" + birthday_dt +
				", birthday_type=" + birthday_type +
				", document_type=" + document_type +
				", account_address='" + account_address + '\'' +
				", scode=" + scode +
				", check_in_pay=" + check_in_pay +
				", countNum=" + countNum +
				", checked=" + checked +
				'}';
	}
}
