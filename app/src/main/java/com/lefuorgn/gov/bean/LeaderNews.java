package com.lefuorgn.gov.bean;

import java.io.Serializable;

public class LeaderNews implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id; // 新闻ID
	private int type; // 新闻类型 1：养老新闻，2：养生课堂
	private String theme=""; // 新闻标题
	private String picture=""; // 新闻标题图片
	private String headline=""; // 内容提要
	private String content=""; // 新闻内容
	private int read_number; // 阅读次数
	private int praise_number; // 点赞次数
	private long create_by; // 创建人ID
	private long create_dt; // 创建时间
	private long update_dt; // 修改时间
	private int pageNo;//当前页号
	private int startRow;//起始行
	private String type_name;
	
	
	
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public long getId() {
		return id;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getRead_number() {
		return read_number;
	}
	public void setRead_number(int read_number) {
		this.read_number = read_number;
	}
	public int getPraise_number() {
		return praise_number;
	}
	public void setPraise_number(int praise_number) {
		this.praise_number = praise_number;
	}
	public long getCreate_by() {
		return create_by;
	}
	public void setCreate_by(long create_by) {
		this.create_by = create_by;
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
	
}
