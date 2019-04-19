package com.lefuorgn.interactive.impl;

import com.lefuorgn.db.base.BaseDao;
import com.lefuorgn.db.model.interf.IDownloadable;
import com.lefuorgn.interactive.interf.DataFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * 只过滤重复的数据
 * 必须先调用{@link #setDao(BaseDao)}方法
 * 接着调用{@link #processingData(List, long)}方法;然后才能使用其他方法
 */

public class RemoveDuplicateDataFilter<T extends IDownloadable> implements DataFilter<T> {

    private BaseDao<T, Long> mBaseDao;
    private List<T> mRemoveData = new ArrayList<T>();

    @Override
    public void setDao(BaseDao<T, Long> dao) {
        if (dao == null) throw new IllegalArgumentException("dao == null");
        mBaseDao = dao;
    }

    @Override
    public void processingData(List<T> data, long lastTime) {
        if(data == null || data.size() == 0) {
            return;
        }
        mRemoveData.clear();
        for (T t : data) {
            T query = mBaseDao.query("id", t.getCId());
            if (query == null) {
                break;
            }
            mRemoveData.add(t);
        }
        // 移去重复的数据
        data.removeAll(mRemoveData);
        // 将其余数据添加到数据库中
        mBaseDao.insertList(data);
    }

}
