package com.lefuorgn.api.http.request;

import com.lefuorgn.api.http.parser.ApiParser;
import com.lefuorgn.api.http.parser.FileApiParser;

import java.io.File;


/**
 * 文件下载请求
 */
public class FileApiRequest extends ApiRequest<File> {
	/**
	 * 下载文件的存量地址(文件所在绝对地址，包含文件名
	 * 只需要给出地址就行，不需要保证其父目录存在，解析的时候会自动判断，父目录不存在会自动创建)
	 */
	private String fileDownloadPath;
	/**
	 * 文件下载成功的解析器
	 */
	private FileApiParser fileParser;
	
	public FileApiRequest(String url, String fileDownloadPath) {
		super(url);
		this.fileDownloadPath = fileDownloadPath;
	}

	/**
	 * 获取文件下载后的存储路径
	 * @return 存储路径
	 */
	public String getFileDownloadPath() {
		return fileDownloadPath;
	}

	@Override
	public ApiParser<File> getDataParser() {
		if(fileParser == null) {
			fileParser = new FileApiParser(fileDownloadPath, this);
		}
		return fileParser;
	}
}
