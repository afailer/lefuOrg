package com.lefuorgn.db.model.base;

import com.j256.ormlite.field.DatabaseField;

public class BaseMediaUpload {

    @DatabaseField
    protected long uId; // 对应护理信息表中的主键
    @DatabaseField
    protected String path; // 媒体路径
    @DatabaseField
    protected int type; // 媒体类型
    @DatabaseField
    protected String alias; // 重新的名称

    public long getuId() {
        return uId;
    }

    public void setuId(long uId) {
        this.uId = uId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "BaseMediaUpload{" +
                "uId=" + uId +
                ", path='" + path + '\'' +
                ", type=" + type +
                '}';
    }
}
