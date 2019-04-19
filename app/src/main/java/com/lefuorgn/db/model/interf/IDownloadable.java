package com.lefuorgn.db.model.interf;

/**
 * 上传表和下载表,主要是兼容各个表之间的差异性问题
 */
public interface IDownloadable {
	
	/**
	 * 获取本地数据库主键值
	 * @return 返回本地数据库主键ID
	 */
	long getC_id();
	
	/**
	 * 设置本地数据表ID
	 */
	void setC_id(long _id);
	
	/**
	 * 获取数据库的ID值,该值与服务器上的主键值一致
	 * @return 返回条目服务器主键ID
	 */
	long getCId();
	/**
	 * 获取当前条目最近更新时间值
	 * @return 返回更新时间
	 */
	long getCUpdateTime();
	/**
	 * 获取当前条目的的创建时间值
	 * @return 返回创建时间
	 */
	long getCCreateTime();

}
