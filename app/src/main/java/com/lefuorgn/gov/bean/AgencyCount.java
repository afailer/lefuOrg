package com.lefuorgn.gov.bean;

public class AgencyCount {
	int id;
	int bed_total;//床位总数
	int bed_surplus;//剩余床位总数
	int staff;//员工数
	int men;//男头子数
	int women;//老太太数
	long creat_time;//创建时间
	public long getCreat_time() {
		return creat_time;
	}
	public void setCreat_time(long creat_time) {
		this.creat_time = creat_time;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBed_total() {
		return bed_total;
	}
	public void setBed_total(int bed_total) {
		this.bed_total = bed_total;
	}
	public int getBed_surplus() {
		return bed_surplus;
	}
	public void setBed_surplus(int bed_surplus) {
		this.bed_surplus = bed_surplus;
	}
	public int getStaff() {
		return staff;
	}
	public void setStaff(int staff) {
		this.staff = staff;
	}
	public int getMen() {
		return men;
	}
	public void setMen(int men) {
		this.men = men;
	}
	public int getWomen() {
		return women;
	}
	public void setWomen(int women) {
		this.women = women;
	}
	
}
