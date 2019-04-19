package com.lefuorgn.db.model.basic;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 当前用户查看员工的历史记录
 */
@DatabaseTable(tableName = "VerifyLine")
public class StaffCache {

    @DatabaseField(generatedId = true, columnName = "mId")
    private long _id; // 本地数据库表主键
    @DatabaseField(columnName = "verify_user_id")
    private long id; // 员工ID
    @DatabaseField(columnName = "verify_user_name")
    private String name; // 员工姓名
    @DatabaseField(columnName = "isCopy")
    private boolean copy; // true: 当前员工属于抄送员工; false: 为审批员工
    @DatabaseField(columnName = "saveTime")
    private long time; // 最后修改时间

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCopy() {
        return copy;
    }

    public void setCopy(boolean copy) {
        this.copy = copy;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "StaffCache{" +
                "_id=" + _id +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", copy=" + copy +
                ", time=" + time +
                '}';
    }
}
