package com.lefuorgn.db.util;

import com.lefuorgn.db.base.BaseDao;
import com.lefuorgn.db.model.basic.StaffCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工数据库管理类
 */

public class StaffCacheManager {

    private static final int COPY_FALSE_NUM = 3;

    private static StaffCacheManager instance;
    private BaseDao<StaffCache, Long> mDao;

    private StaffCacheManager() {
        mDao = DaoHelper.getInstance().getDao(StaffCache.class);
    }

    public static StaffCacheManager getInstance() {
        if(instance == null) {
            synchronized (StaffCacheManager.class) {
                if(instance == null) {
                    instance = new StaffCacheManager();
                }
            }
        }
        return instance;
    }

    /**
     * 添加历史员工; 如果是审批历史员工, 如果当前员工数量超出指定的
     * 值{@link #COPY_FALSE_NUM}则:删除最早的一条数据
     * @param staffCache 员工缓存
     * @param copy true: 抄送历史员工; false: 审批历史员工
     */
    public void addStaffCache(StaffCache staffCache, boolean copy) {
        if(staffCache == null) {
            return;
        }
        if(copy) {
            addCopyTrueStaff(staffCache);
        }else {
            addCopyFalseStaff(staffCache);
        }
    }

    /**
     * 移除指定的历史员工
     */
    public void removeStaffCache(long id, boolean copy) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("verify_user_id", id);
        map.put("isCopy", copy);
        mDao.deleteList(map);
    }

    /**
     * 移除指定的历史员工
     * @param staffCache 要移除的员工
     */
    public void removeStaffCache(StaffCache staffCache) {
        mDao.delete(staffCache);
    }

    public void removeAll() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isCopy", true);
        mDao.deleteList(map);
    }

    public List<StaffCache> getStaffCache(boolean copy) {
        List<StaffCache> caches = mDao.queryList("isCopy", copy, "saveTime", false);
        return caches == null ? new ArrayList<StaffCache>() : caches;
    }

    /**
     * 添加抄送员工; 如果存在则修改, 不存在则添加
     * @param staffCache 要添加的员工
     */
    private void addCopyTrueStaff(StaffCache staffCache) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("verify_user_id", staffCache.getId());
        map.put("isCopy", true);
        StaffCache cache = mDao.query(map);
        long time = System.currentTimeMillis();
        if(cache != null) {
            // 当前历史员工缓存存在, 修改时间
            cache.setTime(time);
            mDao.update(cache);
            return;
        }
        // 添加缓存
        staffCache.setTime(time);
        staffCache.setCopy(true);
        mDao.insert(staffCache);
    }

    /**
     * 添加审批员工; 如果存在则修改, 不存在则查看当前员工数量, 如果数量超出指定的
     * 值{@link #COPY_FALSE_NUM}则:删除最早的一条数据
     * @param staffCache 要添加的员工
     */
    private void addCopyFalseStaff(StaffCache staffCache) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("verify_user_id", staffCache.getId());
        map.put("isCopy", false);
        StaffCache cache = mDao.query(map);
        long time = System.currentTimeMillis();
        if(cache != null) {
            // 当前历史员工缓存存在, 修改时间
            cache.setTime(time);
            mDao.update(cache);
            return;
        }
        List<StaffCache> list = mDao.queryList("isCopy", false, "saveTime", false);
        if(list != null && list.size() >= COPY_FALSE_NUM) {
            // 当前数据大于限制个数, 删除最后一条数据(数据按时间从大到小排列)
            mDao.delete(list.get(list.size() - 1));
        }
        staffCache.setTime(time);
        staffCache.setCopy(false);
        mDao.insert(staffCache);
    }

}
