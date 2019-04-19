package com.lefuorgn.gov.bean;

import java.io.Serializable;

/**
 * 机构即养老院,详情类
 * 
 */
public class Organization implements Serializable {

	private static final long serialVersionUID = 1L;

	private long agency_id; // 机构ID
	private String agency_name; // 机构名称
	private String title; // 机构名称简称
	private String address; // 机构地址
	private int bed_total; // 床位总数
	private String exterior_pic; // 机构图片地址
	private String mailbox; // 邮箱
	private String phone; // 联系电话
	private String content; // 机构格式化内容简介
	private String remark; // 机构简介
	private double longitude; // 机构所在经度
	private double latitude; // 机构所在的纬度
	private String region_name; // 机构所在地理位置名称
	private int bed_surplus; // 剩余床位
	private String agency_property_text; // 机构性质
	private String street;// 街道名称
	private long area_id; // 区域ID
	
	private String fullPinYIn; // 名称的全拼
	private String fullFirstChar; // 每个拼音的首字母组合

	public long getAgency_id() {
		return agency_id;
	}

	public void setAgency_id(long agency_id) {
		this.agency_id = agency_id;
	}

	public String getAgency_name() {
		return agency_name;
	}

	public void setAgency_name(String agency_name) {
		this.agency_name = agency_name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getBed_total() {
		return bed_total;
	}

	public void setBed_total(int bed_total) {
		this.bed_total = bed_total;
	}

	public String getExterior_pic() {
		return exterior_pic;
	}

	public void setExterior_pic(String exterior_pic) {
		this.exterior_pic = exterior_pic;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getRegion_name() {
		return region_name;
	}

	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}

	public int getBed_surplus() {
		return bed_surplus;
	}

	public void setBed_surplus(int bed_surplus) {
		this.bed_surplus = bed_surplus;
	}

	public String getAgency_property_text() {
		return agency_property_text;
	}

	public void setAgency_property_text(String agency_property_text) {
		this.agency_property_text = agency_property_text;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public long getArea_id() {
		return area_id;
	}

	public void setArea_id(long area_id) {
		this.area_id = area_id;
	}
	
	public String getFullPinYIn() {
		return fullPinYIn;
	}

	public void setFullPinYIn(String fullPinYIn) {
		this.fullPinYIn = fullPinYIn;
	}

	public String getFullFirstChar() {
		return fullFirstChar;
	}

	public void setFullFirstChar(String fullFirstChar) {
		this.fullFirstChar = fullFirstChar;
	}

	@Override
	public String toString() {
		return "Organization [agency_id=" + agency_id + ", agency_name="
				+ agency_name + ", title=" + title + ", address=" + address
				+ ", bed_total=" + bed_total + ", exterior_pic=" + exterior_pic
				+ ", mailbox=" + mailbox + ", phone=" + phone + ", content="
				+ content + ", remark=" + remark + ", longitude=" + longitude
				+ ", latitude=" + latitude + ", region_name=" + region_name
				+ ", bed_surplus=" + bed_surplus + ", agency_property_text="
				+ agency_property_text + ", street=" + street + ", area_id="
				+ area_id + ", fullPinYIn=" + fullPinYIn + ", fullFirstChar="
				+ fullFirstChar + "]";
	}

}
