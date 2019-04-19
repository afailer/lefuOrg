package com.lefuorgn.db.model.upload;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lefuorgn.db.model.base.BaseMediaUpload;

/**
 * 护理信息多媒体信息上传表
 */
@DatabaseTable(tableName = "DailyNursingMediaUpload")
public class DailyNursingMediaUpload extends BaseMediaUpload {

    @DatabaseField(generatedId = true)
    private long _id;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "DailyNursingMediaUpload{" +
                "_id=" + _id +
                ", uId=" + uId +
                ", path='" + path + '\'' +
                '}';
    }

}
