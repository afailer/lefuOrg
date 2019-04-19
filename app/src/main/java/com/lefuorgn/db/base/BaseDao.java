package com.lefuorgn.db.base;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.DatabaseConnection;
import com.lefuorgn.db.helper.DatabaseHelper;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 数据库CRUD操作的DAO，子类继承实现抽象方法
 * @param <T> 表类泛型
 * @param <ID> 主键泛型
 */
public abstract class BaseDao<T, ID> {

	protected Context mContext;
	DatabaseHelper mDatabaseHelper;

	public BaseDao(Context context) {
		if (context == null) {
			throw new IllegalArgumentException("Context can't be null!");
		}
		mContext = context.getApplicationContext();
		mDatabaseHelper = DatabaseHelper.getInstance(mContext);
	}

	public abstract Dao<T, ID> getDao() throws SQLException;

	/**************************************** 保存 ******************************************************/

	/**
	 * 插入一条记录
	 * 
	 * @param t 实体类对象
	 */
	public void insert(T t) {
		try {
            getDao().create(t);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 插入一组记录，使用事务处理
	 * @param list 实体类对象集合
	 * @return 返回0,则意味着插入失败,或者传入的集合为空
	 */
	public int insertList(List<T> list) {
		if (list.size() <= 0)
			return 0;
		int num = 0;
		try {
			Dao<T, ID> dao = getDao();
			DatabaseConnection databaseConnection = null;
			try {
				databaseConnection = dao.startThreadConnection();
				dao.setAutoCommit(databaseConnection, false);
				for (T t : list) {
					dao.create(t);
					num++;
				}
				dao.commit(databaseConnection);
				return num;
			} catch (SQLException e) {
				dao.rollBack(databaseConnection);
				e.printStackTrace();
			} finally {
				try{
					dao.endThreadConnection(databaseConnection);
				}catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**************************************** 更新 ******************************************************/

	/**
	 * 更新一条记录,修改的依据是主键值
	 * 
	 * @param t 实体类对象
	 */
	public void update(T t) {
		try {
            getDao().update(t);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据指定条件和指定字段更新记录
	 * 
	 * @param key 条件键
	 * @param value 条件值
	 * @param columnMap 更新字段集合
	 */
	public void update(String key, Object value, Map<String, Object> columnMap) {
		try {
			Dao<T, ID> dao = getDao();
			UpdateBuilder<T, ID> updateBuilder = dao.updateBuilder();
			updateBuilder.where().eq(key, value);
            for (String name : columnMap.keySet()) {
                // 添加要更新的字段
                updateBuilder.updateColumnValue(name, columnMap.get(name));
            }
			PreparedUpdate<T> prepareUpdate = updateBuilder.prepare();
			dao.update(prepareUpdate);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    /**
     * 根据指定条件和指定字段更新记录
     * @param map 条件集合
     * @param columnMap 更新字段集合
     */
	public void update(Map<String, Object> map, Map<String, Object> columnMap) {
		try {
			Dao<T, ID> dao = getDao();
			UpdateBuilder<T, ID> updateBuilder = dao.updateBuilder();
			Where<T, ID> wheres = updateBuilder.where();
			boolean first = true;
			for (String key : map.keySet()) {
				// 添加条件
				if (first) {
					wheres.eq(key, map.get(key));
					first = false;
				} else {
					wheres.and().eq(key, map.get(key));
				}
			}
			for (String name : columnMap.keySet()) {
				// 添加要更新的字段
				updateBuilder.updateColumnValue(name, columnMap.get(name));
			}
			PreparedUpdate<T> prepareUpdate = updateBuilder.prepare();
			dao.update(prepareUpdate);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据PreparedUpdate更新记录
	 * @param preparedUpdate 更新操作类
	 */
	public void update(PreparedUpdate<T> preparedUpdate) {
		try {
            getDao().update(preparedUpdate);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**************************************** 保存或更新 ******************************************************/

	/**
	 * 插入或更新一条记录，不存在则插入，否则更新
	 * @param t 待插入或更新的数据
	 */
	public void insertOrUpdate(T t) {
		try {
            getDao().createOrUpdate(t);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 插入或更新一组数据，使用事务处理
	 * @param list 带插入或者更新的数据集合
	 */
	public void insertOrUpdate(List<T> list) {
		if (list.size() <= 0)
			return;
		try {
			Dao<T, ID> dao = getDao();
			DatabaseConnection databaseConnection = null;
			try {
				databaseConnection = dao.startThreadConnection();
				dao.setAutoCommit(databaseConnection, false);
				for (T t : list) {
					dao.createOrUpdate(t);
				}
				dao.commit(databaseConnection);
			} catch (SQLException e) {
				dao.rollBack(databaseConnection);
				e.printStackTrace();
			} finally {
                try {
                    dao.endThreadConnection(databaseConnection);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**************************************** 删除操作 ******************************************************/

	/**
	 * 删除一条记录
	 * @param t 要删除的数据
	 */
	public void delete(T t) {
		try {
            getDao().delete(t);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 删除一组记录，使用事务处理
	 * 注意：Dao.delete(Collection<T> data)方法删除，最多只能删除999条记录
	 * @param list 要删除的数据集合
	 */
	public void deleteList(List<T> list) {
		if (list.size() <= 0)
			return;
		try {
			Dao<T, ID> dao = getDao();
			DatabaseConnection databaseConnection = null;
			try {
				databaseConnection = dao.startThreadConnection();
				dao.setAutoCommit(databaseConnection, false);
				for (T t : list) {
					dao.delete(t);
				}
				dao.commit(databaseConnection);
			} catch (SQLException e) {
				dao.rollBack(databaseConnection);
				e.printStackTrace();
			} finally {
				try{
					dao.endThreadConnection(databaseConnection);
				}catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据条件组合删除所有满足条件的记录
	 * @param map 条件
	 */
	public void deleteList(Map<String, Object> map) {
		if (map == null) {
			throw new InvalidParameterException("The map is null");
		}
		try {
			Dao<T, ID> dao = getDao();
			DeleteBuilder<T, ID> deleteBuilder = dao.deleteBuilder();
			Where<T, ID> wheres = deleteBuilder.where();
            boolean first = true;
            for (String key : map.keySet()) {
                if (first) {
                    wheres.eq(key, map.get(key));
                    first = false;
                } else {
                    wheres.and().eq(key, map.get(key));
                }
            }
			PreparedDelete<T> preparedDelete = deleteBuilder.prepare();
			dao.delete(preparedDelete);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据id删除一条记录
	 * @param id 主键ID
	 */
	public void deleteById(ID id) {
		try {
            getDao().deleteById(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据id数组删除一组记录
	 * @param ids 主键ID集合
	 */
	public void deleteByIds(List<ID> ids) {
		try {
            getDao().deleteIds(ids);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据PreparedDelete删除记录
	 * 
	 * @param preparedDelete 删除条件PreparedQuery
	 */
	public void delete(PreparedDelete<T> preparedDelete) {
		try {
            getDao().delete(preparedDelete);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 清空一张表中的数据
	 */
	public void clearTable() {
		try {
			Dao<T, ID> dao = getDao();
			String delete = String.format("delete from %s", dao.getTableName());
			dao.queryRaw(delete);// 清空数据
			String updateSeq = String.format(
					"update sqlite_sequence SET seq = 0 where name ='%s'",
					dao.getTableName());
			dao.queryRaw(updateSeq);// 自增长ID为0
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**************************************** 查询操作 ******************************************************/

	/**
	 * 根据单个条件查询一条记录
	 * 
	 * @param columnName 列名
	 * @param columnValue 列值
	 * @return 根据查询条件返回的一条记录, 不存在返回null
	 */
	public T query(String columnName, Object columnValue) {
		try {
			Dao<T, ID> dao = getDao();
			QueryBuilder<T, ID> queryBuilder = dao.queryBuilder();
			queryBuilder.where().eq(columnName, columnValue);
			PreparedQuery<T> preparedQuery = queryBuilder.prepare();
			return dao.queryForFirst(preparedQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据条件组合查询一条记录
	 * @param map 查询条件
	 * @return 根据查询条件返回的一条记录, 不存在返回null
     */
	public T query(Map<String, Object> map) {
		if (map == null) {
			throw new InvalidParameterException("The map is null");
		}
		try {
			QueryBuilder<T, ID> queryBuilder = getDao().queryBuilder();
			Where<T, ID> wheres = queryBuilder.where();
			boolean first = true;
			for (String key : map.keySet()) {
				if (first) {
					wheres.eq(key, map.get(key));
					first = false;
				} else {
					wheres.and().eq(key, map.get(key));
				}
			}
			PreparedQuery<T> preparedQuery = queryBuilder.prepare();
			return getDao().queryForFirst(preparedQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据PreparedQuery查询所有记录
	 * 
	 * @param preparedQuery 查询条件PreparedQuery
	 * @return 条件集合, 不存在返回空的集合
	 */
	public List<T> queryList(PreparedQuery<T> preparedQuery) {
		try {
			Dao<T, ID> dao = getDao();
			DatabaseConnection databaseConnection = null;
			try {
				databaseConnection = dao.startThreadConnection();
				dao.setAutoCommit(databaseConnection, false);
				List<T> query = dao.query(preparedQuery);
				dao.commit(databaseConnection);
				return query;
			} catch (SQLException e) {
				dao.rollBack(databaseConnection);
				e.printStackTrace();
			} finally {
                try {
                    dao.endThreadConnection(databaseConnection);
                }catch (SQLException e) {
                    e.printStackTrace();
                }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据单个条件查询所有满足条件的记录
	 * 
	 * @param columnName 列名
	 * @param columnValue 列值
	 * @return 条件集合, 不存在返回空的集合
	 */
	public List<T> queryList(String columnName, Object columnValue) {
		try {
			Dao<T, ID> dao = getDao();
			QueryBuilder<T, ID> queryBuilder = dao.queryBuilder();
			queryBuilder.where().eq(columnName, columnValue);
			PreparedQuery<T> preparedQuery = queryBuilder.prepare();
			return dao.query(preparedQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据单个条件查询所有满足条件的记录
	 *
	 * @param columnName 列名
	 * @param columnValue 列值
	 * @param order 排序字段
	 * @param ascending true--升序 false--降序
	 * @return 条件集合, 不存在返回空的集合
	 */
	public List<T> queryList(String columnName, Object columnValue, String order, boolean ascending) {
		try {
			Dao<T, ID> dao = getDao();
			QueryBuilder<T, ID> queryBuilder = dao.queryBuilder();
			queryBuilder.where().eq(columnName, columnValue);
			queryBuilder.orderBy(order, ascending);
			PreparedQuery<T> preparedQuery = queryBuilder.prepare();
			return dao.query(preparedQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据键值对查询所有满足条件的记录
	 * 
	 * @param map 集合查询条件
	 * @return 条件集合, 不存在返回空的集合
	 */
	public List<T> queryList(Map<String, Object> map) {
		try {
			Dao<T, ID> dao = getDao();
			QueryBuilder<T, ID> queryBuilder = dao.queryBuilder();
			Where<T, ID> wheres = queryBuilder.where();
			boolean first = true;
			for (String key : map.keySet()) {
				if (first) {
					wheres.eq(key, map.get(key));
					first = false;
				} else {
					wheres.and().eq(key, map.get(key));
				}
			}
			PreparedQuery<T> preparedQuery = queryBuilder.prepare();
			return dao.query(preparedQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据Id查询
	 * 
	 * @param id 主键ID
	 * @return 指定类型的条目
	 */
	public T queryById(ID id) {
		try {
			return getDao().queryForId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 条件查询
	 * @param map 查询列值条件
	 * @param order 排序字段
	 * @param ascending true--升序 false--降序
	 * @param offset 偏移量
	 * @param limit 查询数量
     * @return 指定条件的集合
     */
	public List<T> queryAll(Map<String, Object> map, String order, boolean ascending, long offset, long limit) {
		try {
			Dao<T, ID> dao = getDao();
			QueryBuilder<T, ID> queryBuilder = dao.queryBuilder();
			if(map != null) {
				Where<T, ID> wheres = queryBuilder.where();
				boolean first = true;
				for (String key : map.keySet()) {
					if (first) {
						wheres.eq(key, map.get(key));
						first = false;
					} else {
						wheres.and().eq(key, map.get(key));
					}
				}
			}
			queryBuilder.orderBy(order, ascending).offset(offset).limit(limit);
			PreparedQuery<T> preparedQuery = queryBuilder.prepare();
			return dao.query(preparedQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 条件查询
	 * 
	 * @param offset
	 *            偏移量
	 * @param limit
	 *            查询数量
	 * @return 指定条件的集合
	 */
	public List<T> queryAll(long offset, long limit) {
		try {
			Dao<T, ID> dao = getDao();
			QueryBuilder<T, ID> queryBuilder = dao.queryBuilder();
			queryBuilder.offset(offset).limit(limit);
			PreparedQuery<T> preparedQuery = queryBuilder.prepare();
			return dao.query(preparedQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 条件查询
	 *
	 * @param order
	 *            排序字段
	 * @param ascending
	 *            true--升序 false--降序
	 * @return 指定内容的列表
	 */
	public List<T> queryAll(String order, boolean ascending) {
		return queryAll(null, order, ascending);
	}

    /**
     * 条件查询
     *
     * @param map 查询条件
     * @param order
     *            排序字段
     * @param ascending
     *            true--升序 false--降序
     * @return 指定内容的列表
     */
    public List<T> queryAll(Map<String, Object> map, String order, boolean ascending) {
        try {
            Dao<T, ID> dao = getDao();
            QueryBuilder<T, ID> queryBuilder = dao.queryBuilder();
            if(map != null) {
                Where<T, ID> wheres = queryBuilder.where();
                boolean first = true;
                for (String key : map.keySet()) {
                    if (first) {
                        wheres.eq(key, map.get(key));
                        first = false;
                    } else {
                        wheres.and().eq(key, map.get(key));
                    }
                }
            }
            queryBuilder.orderBy(order, ascending);
            PreparedQuery<T> preparedQuery = queryBuilder.prepare();
            return dao.query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

	/**
	 * 条件查询
	 * 
	 * @param offset
	 *            偏移量
	 * @param limit
	 *            查询数量
	 * @param order
	 *            排序字段
	 * @param ascending
	 *            true--升序 false--降序
	 * @return 指定条件的集合
	 */
	public List<T> queryAll(long offset, long limit, String order,
			boolean ascending) {
		try {
			Dao<T, ID> dao = getDao();
			QueryBuilder<T, ID> queryBuilder = dao.queryBuilder();
			queryBuilder.orderBy(order, ascending).offset(offset).limit(limit);
			PreparedQuery<T> preparedQuery = queryBuilder.prepare();
			return dao.query(preparedQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**************************************** 其他操作 ******************************************************/

	/**
	 * 表是否存在
	 * 
	 * @return true--存在 false-- 不存在
	 */
	public boolean isTableExists() {
		try {
			return getDao().isTableExists();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获得记录数, 此方法可能比较耗时
	 * @return 记录数
	 */
	public long count() {
		try {
			return getDao().countOf();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * 查询指定字段的值
	 * @param sql sql数据
	 * @return 0: 就是没有值
	 */
	public long queryRawValue(String sql) {
		try {
			return getDao().queryRawValue(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获得记录数
	 * 
	 * @param preparedQuery 条件PreparedQuery
	 * @return 记录数
	 */
	public long count(PreparedQuery<T> preparedQuery) {
		try {
			return getDao().countOf(preparedQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * 获取当前表名
	 * @return 当前表名
	 */
	public String getTableName() {
		try {
			return getDao().getTableName();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

}
