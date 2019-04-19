package com.lefuorgn.interactive.impl;

import com.lefuorgn.db.base.BaseDao;
import com.lefuorgn.db.model.interf.IDownloadable;
import com.lefuorgn.interactive.interf.DataFilter;
import com.lefuorgn.util.TLog;

import java.util.List;

/**
 * 获取要修改和添加的数据
 * 必须先调用{@link #setDao(BaseDao)}方法
 * 接着调用{@link #processingData(List, long)}方法;然后才能使用其他方法
 */

public class AddAndModifyDataFilter<T extends IDownloadable> implements DataFilter<T> {

    private BaseDao<T, Long> mBaseDao;

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
        for (T t : data) {
            if(lastTime < t.getCCreateTime()) {
                mBaseDao.insert(t);
            }else if(lastTime > t.getCCreateTime() && lastTime < t.getCUpdateTime()) {
                // 当前数据,数据库中的数据已经变化
                T query = mBaseDao.query("id", t.getCId());
                // query为空时, 此数据为无效数据
                if(query != null) {
                    t.setC_id(query.getC_id());
                    mBaseDao.update(t);
                }
            }else {
                // 不确定数据
                T query = mBaseDao.query("id", t.getCId());
                if (query == null) {
                    // 数据库中不存在, 添加
                    mBaseDao.insert(t);
                }else {
                    // 数据库中存在, 修改
                    t.setC_id(query.getC_id());
                    mBaseDao.update(t);
                }
            }
        }
    }

}
