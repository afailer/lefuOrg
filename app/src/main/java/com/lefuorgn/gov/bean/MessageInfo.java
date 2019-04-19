package com.lefuorgn.gov.bean;

import java.io.Serializable;

public class MessageInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id; // 通知ID
	private String picture; // 当前通知的图片
	private String author; // 通知者
	private long create_dt; // 创建时间
	private int status; // 当前通知的状态 1: 未读, 2已读
	private String theme; // 主题
	private String content; // 通知的正文
	private int type;//状态
	private long type_id;
	
	public long getType_id() {
		return type_id;
	}

	public void setType_id(long type_id) {
		this.type_id = type_id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public long getCreate_dt() {
		return create_dt;
	}

	public void setCreate_dt(long create_dt) {
		this.create_dt = create_dt;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "MessageInfo [id=" + id + ", picture=" + picture + ", author="
				+ author + ", create_dt=" + create_dt + ", status=" + status
				+ ", theme=" + theme + ", content=" + content + "]";
	}

}
