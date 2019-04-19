package com.lefuorgn.api.http.request;

import com.lefuorgn.api.common.MultiMediaUpload;

import java.util.List;

/**
 * 多媒体文件上传请求，上传文件时的key值均为文件的文件名，去掉后缀
 */
public class UploadMultiMediaApiRequest<T> extends JsonApiRequest<T> {
	/**
	 * 要上传的文件
	 */
	private List<MultiMediaUpload> files;

	/**
	 * 上传多个文件
	 * @param url			上传文件请求地址
	 * @param files			要上传的文件
	 */
	public UploadMultiMediaApiRequest(String url, List<MultiMediaUpload> files) {
		super(url);
		this.files = files;
	}

	public List<MultiMediaUpload> getFiles() {
		return files;
	}

}
