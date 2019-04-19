package com.lefuorgn.interactive.bean;


import com.lefuorgn.db.model.interf.IDownloadable;

/**
 * 下载表链式表结构
 */
public class DownloadTableTree<T> {
	
	private Class<T> clazz;
	private DownloadTableTree<? extends IDownloadable> subClazz;
	
	public Class<T> getClazz() {
		return clazz;
	}
	
	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	public DownloadTableTree<? extends IDownloadable> getSubClazz() {
		return subClazz;
	}

	public void setSubClazz(
			DownloadTableTree<? extends IDownloadable> subClazz) {
		this.subClazz = subClazz;
	}
	
}
