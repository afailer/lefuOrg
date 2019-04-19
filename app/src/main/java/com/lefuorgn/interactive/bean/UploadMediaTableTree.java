package com.lefuorgn.interactive.bean;


import com.lefuorgn.db.model.base.BaseMediaUpload;

/**
 * 下载表链式表结构
 */
public class UploadMediaTableTree<T extends BaseMediaUpload> {
	
	private Class<T> clazz;
	private UploadMediaTableTree<?> subClazz;

	public Class<T> getClazz() {
		return clazz;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	public UploadMediaTableTree<?> getSubClazz() {
		return subClazz;
	}

	public void setSubClazz(UploadMediaTableTree<?> subClazz) {
		this.subClazz = subClazz;
	}
}
