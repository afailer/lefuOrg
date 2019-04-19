package com.lefuorgn.interactive.interf;

import com.lefuorgn.db.base.BaseDao;
import com.lefuorgn.db.model.interf.IDownloadable;

import java.io.Serializable;
import java.util.List;

/**
 * 数据过滤器,用于过滤下载表中重复或者修改的数据数据
 */

public interface DataFilter<T extends IDownloadable> extends Serializable{

    /**
     * 添加下载表操作对象
     */
    void setDao(BaseDao<T, Long> dao);

    /**
     * 处理指定的数据
     * @param data 需处理的数据
     * @param lastTime 当前请求数据的时间戳
     */
    void processingData(List<T> data, long lastTime);

}
