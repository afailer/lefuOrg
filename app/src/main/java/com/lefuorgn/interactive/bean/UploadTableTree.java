package com.lefuorgn.interactive.bean;


/**
 * 下载表链式表结构
 */
public class UploadTableTree<T> {
	
	private Class<T> clazz;
	private UploadTableTree<?> subClazz;

	public Class<T> getClazz() {
		return clazz;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	public UploadTableTree<?> getSubClazz() {
		return subClazz;
	}

	public void setSubClazz(UploadTableTree<?> subClazz) {
		this.subClazz = subClazz;
	}
}
