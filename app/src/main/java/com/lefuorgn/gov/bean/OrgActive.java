package com.lefuorgn.gov.bean;

import java.io.Serializable;

public class OrgActive implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String theme="";//标题
	private long agencyId=0;
	private long creatDt=0;
	private String reserved="";
	private String agency_name="";
	private long read_number=0;
	private String holdTimeView="";
	private long holdTime=0;
	private String pic="";
	private long id=0;
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public long getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(long agencyId) {
		this.agencyId = agencyId;
	}
	public long getCreatDt() {
		return creatDt;
	}
	public void setCreatDt(long creatDt) {
		this.creatDt = creatDt;
	}
	public String getReserved() {
		return reserved;
	}
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	public String getAgency_name() {
		return agency_name;
	}
	public void setAgency_name(String agency_name) {
		this.agency_name = agency_name;
	}
	public long getRead_number() {
		return read_number;
	}
	public void setRead_number(long read_number) {
		this.read_number = read_number;
	}
	public String getHoldTimeView() {
		return holdTimeView;
	}
	public void setHoldTimeView(String holdTimeView) {
		this.holdTimeView = holdTimeView;
	}
	public long getHoldTime() {
		return holdTime;
	}
	public void setHoldTime(long holdTime) {
		this.holdTime = holdTime;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
}
