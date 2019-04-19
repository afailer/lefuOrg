package com.lefuorgn.bean;

import java.util.List;

public class User{

	/**
	 * 普通用户
	 */
	public static final String ORDINARY_USERS = "ordinary_users";
	/**
	 * 特殊用户, 政府用户
	 */
	public static final String SPECIAL_USER_GOV = "special_user_gov";
	/**
	 * 特殊用户, 集团用户 , 其中集团用户优先政府用户
	 */
	public static final String SPECIAL_USER_GROUP = "special_user_group";

	private long user_id; // 用户ID
	private String user_name; // 用户名称
	private String password; // 用户密码
	private int gender; // 用户性别 14: 男 15: 女
	private String icon; // 用户头像
	private String mobile; // 用户手机号码
	private String mailbox; // 用户邮箱
	private long agency_id; // 用户所属机构ID
    private String agencyName; // 机构名称
	private List<OrgInfo> agencys; //// 集团或者政府账号下的关联机构ID
	private String document_number; // 身份证号码
	private long birthday_dt; // 生日
	private long create_dt; // 用户创建时间
	private String address; // 用户地址
	private boolean gov; // 当前账号是不是政府账号
	private long govOrg_id; // 政府账号ID
    private String govOrgName; // 政府名称
    private List<UserItem> govOrg; // 政府所包含机构集合
    private boolean group; // 当前账号是不是集团账号
    private long groupOrg_id; // 集团账号ID
    private String groupOrgName; // 集团账号名称
	private List<UserItem> groupOrg; // 集团所包含机构集合
    private UserConfig agencyInfo; // 用户配置信息

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public long getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(long agency_id) {
        this.agency_id = agency_id;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public List<OrgInfo> getAgencys() {
        return agencys;
    }

    public void setAgencys(List<OrgInfo> agencys) {
        this.agencys = agencys;
    }

    public String getDocument_number() {
        return document_number;
    }

    public void setDocument_number(String document_number) {
        this.document_number = document_number;
    }

    public long getBirthday_dt() {
        return birthday_dt;
    }

    public void setBirthday_dt(long birthday_dt) {
        this.birthday_dt = birthday_dt;
    }

    public long getCreate_dt() {
        return create_dt;
    }

    public void setCreate_dt(long create_dt) {
        this.create_dt = create_dt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isGov() {
        return gov;
    }

    public void setGov(boolean gov) {
        this.gov = gov;
    }

    public long getGovOrg_id() {
        return govOrg_id;
    }

    public void setGovOrg_id(long govOrg_id) {
        this.govOrg_id = govOrg_id;
    }

    public String getGovOrgName() {
        return govOrgName;
    }

    public void setGovOrgName(String govOrgName) {
        this.govOrgName = govOrgName;
    }

    public List<UserItem> getGovOrg() {
        return govOrg;
    }

    public void setGovOrg(List<UserItem> govOrg) {
        this.govOrg = govOrg;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public long getGroupOrg_id() {
        return groupOrg_id;
    }

    public void setGroupOrg_id(long groupOrg_id) {
        this.groupOrg_id = groupOrg_id;
    }

    public String getGroupOrgName() {
        return groupOrgName;
    }

    public void setGroupOrgName(String groupOrgName) {
        this.groupOrgName = groupOrgName;
    }

    public List<UserItem> getGroupOrg() {
        return groupOrg;
    }

    public void setGroupOrg(List<UserItem> groupOrg) {
        this.groupOrg = groupOrg;
    }

    public UserConfig getAgencyInfo() {
        return agencyInfo;
    }

    public void setAgencyInfo(UserConfig agencyInfo) {
        this.agencyInfo = agencyInfo;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", password='" + password + '\'' +
                ", gender=" + gender +
                ", icon='" + icon + '\'' +
                ", mobile='" + mobile + '\'' +
                ", mailbox='" + mailbox + '\'' +
                ", agency_id=" + agency_id +
                ", agencyName=" + agencyName +
                ", agencys=" + agencys +
                ", document_number='" + document_number + '\'' +
                ", birthday_dt=" + birthday_dt +
                ", create_dt=" + create_dt +
                ", address='" + address + '\'' +
                ", gov=" + gov +
                ", govOrg_id=" + govOrg_id +
                ", govOrgName='" + govOrgName + '\'' +
                ", govOrg=" + govOrg +
                ", group=" + group +
                ", groupOrg_id=" + groupOrg_id +
                ", groupOrgName='" + groupOrgName + '\'' +
                ", groupOrg=" + groupOrg +
                ", agencyInfo=" + agencyInfo +
                '}';
    }
}
