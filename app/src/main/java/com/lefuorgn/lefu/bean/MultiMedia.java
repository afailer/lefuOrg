package com.lefuorgn.lefu.bean;

import java.io.Serializable;

/**
 * 多媒体信息类
 */

public class MultiMedia implements Serializable {

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

    /**
     * 多媒体信息来源本地
     */
    public static final int SOURCE_FROM_LOCAL = 4;
    /**
     * 多媒体信息来源相机, 当前应用创建
     */
    public static final int SOURCE_FROM_CAMERA = 5;

    private int type; // 当前多媒体类型
    private String uri; // 文件uri
    private int sourceType; // 多媒体来源
    private String alias; // 当前是图片而且来源于相册, 则存在别名

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "MultiMedia{" +
                "type=" + type +
                ", uri='" + uri + '\'' +
                ", sourceType=" + sourceType +
                ", alias='" + alias + '\'' +
                '}';
    }
}
