package com.lefuorgn.db.util;


import android.annotation.SuppressLint;
import android.content.Context;

import com.lefuorgn.AppContext;
import com.lefuorgn.db.base.BaseDao;
import com.lefuorgn.db.base.BaseDaoImpl;
import com.lefuorgn.db.helper.DatabaseHelper;


public class DaoHelper {
	
	private Context mContext;
	@SuppressLint("StaticFieldLeak")
	private static DaoHelper helper;
	/** 基本表DAO对象 */
	
	private DaoHelper() {
        mContext = AppContext.getInstance().getApplicationContext();
    }
	
	public static DaoHelper getInstance() {
        if (helper == null) {
        	synchronized (DatabaseHelper.class) {
				if (helper == null) {
					helper = new DaoHelper();
				}
			}
        }
        return helper;
    }

	/**
	 * 将数据库的状态置为空
	 */
	public void reset() {
		DatabaseHelper.restDatabaseName();
	}

    /**
     * 重置数据库
     * @param databaseName 数据库名称
     * @param agencyId 机构ID
     * @param agencyName 机构名称
     */
	public void reset(String databaseName, long agencyId, String agencyName) {
		AppContext appContext = AppContext.getInstance();
		appContext.setAgencyId(agencyId);
		appContext.setAgencyName(agencyName);
		DatabaseHelper.reset(mContext, databaseName);
	}

	/**
	 * 获取DAO的实例对象
	 * @param clazz 类字节码
	 * @return Dao实例对象
	 */
	public <T> BaseDao<T, Long> getDao(Class<T> clazz) {
		return new BaseDaoImpl<T, Long>(mContext, clazz);
	}
	
}
