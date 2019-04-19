package com.lefuorgn.db.base;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class BaseDaoImpl<T, ID> extends BaseDao<T, ID> {

	private Class<T> mClazz;
	
	private Map<Class<T>, Dao<T, ID>> mDaoMap = new HashMap<Class<T>, Dao<T, ID>>();

	public BaseDaoImpl(Context context, Class<T> clazz) {
		super(context);
		mClazz = clazz;
	}

	@Override
	public Dao<T, ID> getDao() throws SQLException {
		Dao<T, ID> dao = mDaoMap.get(mClazz);
        if (null == dao) {
            dao = mDatabaseHelper.getDao(mClazz);
            mDaoMap.put(mClazz, dao);
        }
		return dao;
	}

}
