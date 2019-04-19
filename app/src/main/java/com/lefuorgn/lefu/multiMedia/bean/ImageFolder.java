package com.lefuorgn.lefu.multiMedia.bean;

/**
 * 图片文件夹信息
 */

public class ImageFolder {

    /**
     * 文件夹的名称
     */
    private String albumName;
    /**
     * 图片的数量
     */
    private int count;
    /**
     * 文件夹默认图标路径
     */
    private String albumPath;
    /**
     * 当前页面的选择状态
     */
    private boolean checked;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAlbumPath() {
        return albumPath;
    }

    public void setAlbumPath(String albumPath) {
        this.albumPath = albumPath;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "ImageFolder{" +
                "albumName='" + albumName + '\'' +
                ", count=" + count +
                ", albumPath='" + albumPath + '\'' +
                ", checked=" + checked +
                '}';
    }
}
