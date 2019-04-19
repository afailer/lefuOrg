package com.lefuorgn.api.common;

import java.io.File;

/**
 * 多媒体上传文件类
 */

public class MultiMediaUpload {

    /**
     * 多媒体图片类型
     */
    public static final int MULTI_MEDIA_TYPE_PICTURE = 1;
    /**
     * 多媒体视频类型
     */
    public static final int MULTI_MEDIA_TYPE_VIDEO = 2;
    /**
     * 多媒体音频类型
     */
    public static final int MULTI_MEDIA_TYPE_AUDIO = 3;

    private int type; // 当前多媒体类型
    private File file; // 文件
    private String alias; // 当前是图片而且来源于相册, 则存在别名

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public MultiMediaUpload(File file, int type) {
        this.file = file;
        this.type = type;
    }

    public MultiMediaUpload() {
        
    }

    @Override
    public String toString() {
        return "MultiMediaUpload{" +
                "type=" + type +
                ", file=" + file +
                ", alias='" + alias + '\'' +
                '}';
    }
}
