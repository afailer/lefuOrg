package com.lefuorgn.interactive.module;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.synch.DownloadApi;
import com.lefuorgn.db.base.BaseDao;
import com.lefuorgn.db.model.interf.IDownloadable;
import com.lefuorgn.db.util.DaoHelper;
import com.lefuorgn.interactive.bean.DownloadTableTree;
import com.lefuorgn.interactive.interf.DataFilter;
import com.lefuorgn.interactive.interf.OnSyncChangeListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 同步下载表模块, 表需要实现IDownloadable接口
 */
public class DownloadModule {

	private static DownloadModule instance;
	private DaoHelper mDaoHelper;
	private Handler mCurrentHandler;
	private long mAgencyId; // 当前机构ID
    private DataFilter mDataFilter; // 数据过滤器
    private long mTimeStamp; // 时间戳
    private long mLimitedNumber;
	private boolean clearDownloadTable; // 下载数据前是否清空表数据

	private OnSyncChangeListener mListener;

	private DownloadModule(Looper looper) {
		mDaoHelper = DaoHelper.getInstance();
		mCurrentHandler = new Handler(looper) {
			@Override
			public void handleMessage(Message msg) {
				// 切换下载表进行下载
				switchTable(msg);
			}
		};
	}

	/**
	 * 获取当前下载模块的实例
	 * 
	 * @param looper 指定线程中的Looper
	 * @return 当前类实例化对象
	 */
	public static DownloadModule getInstance(Looper looper) {
		if (instance == null) {
			synchronized (DownloadModule.class) {
				if (instance == null) {
					instance = new DownloadModule(looper);
				}
			}
		}
		return instance;
	}

	/**
	 * 开启下载模块
	 * @param tree 下载表链表
	 * @param <T> 实现接口IDownloadable的子类
     */
	public <T extends IDownloadable> void start(DownloadTableTree<T> tree) {
		if(tree == null) {
			throw new IllegalArgumentException("The tree is null");
		}
		if (mListener != null) {
			// 开始下载, 起始点
			mListener.onStart();
		}
        if (mListener != null) {
            // 开始
            mListener.onStateChange(tree.getClazz(), OnSyncChangeListener.STATE_START);
        }
		// 下载第一个节点信息
		createDownloadCondition(tree);
	}

	/**
	 * 切换表进行下载
	 * @param msg 上一个节点的同步信息
	 */
	@SuppressWarnings("unchecked")
	private <T extends IDownloadable> void switchTable(Message msg) {
		DownloadTableTree<T> tree = (DownloadTableTree<T>) msg.obj;
		// 表下载完成, 清除不合格数据
		clearInvalidData(tree.getClazz());
        // 数据下载完成
        if (mListener != null) {
            mListener.onStateChange(tree.getClazz(), OnSyncChangeListener.STATE_SUCCESS);
        }
		// 是否还存在其他表要下载
		if(tree.getSubClazz() != null) {
            // 新表开始下载
            if (mListener != null) {
                mListener.onStateChange(tree.getClazz(), OnSyncChangeListener.STATE_START);
            }
			createDownloadCondition(tree.getSubClazz());
		}else if (mListener != null){
			// 所有表下载完毕, 结束点
			mListener.onEnd();
		}
	}

	/**
	 * 创建表下载, 根据当前表的状态,创建数据过滤器
	 * @param tree 当前表节点
	 */
	private <T extends IDownloadable> void createDownloadCondition(DownloadTableTree<T> tree) {
		mDataFilter.setDao(mDaoHelper.getDao(tree.getClazz()));
        if(clearDownloadTable) {
            clearTable(tree.getClazz());
        }
		downloadDataFromServer(tree);
	}

	/**
	 * 下载指定表中的数据
	 * 
	 * @param tree 下载表当前节点
	 */
	@SuppressWarnings("rawtypes")
	private <T extends IDownloadable> void downloadDataFromServer(final DownloadTableTree<T> tree) {
		final long lastTime = getLastTime(tree.getClazz());
		DownloadApi.downloadData(tree.getClazz(), lastTime, mAgencyId, new RequestCallback<List>(mCurrentHandler) {

			@SuppressWarnings({ "unchecked" })
			@Override
			public void onSuccess(List result) {
				// 当前从网络中获取数据的个数
				int size = result.size();
				// 将数据保存到数据库中
				mDataFilter.processingData(result, lastTime);
				// NUM为0,则保存失败
				if (size == mLimitedNumber) {
					// 有多余的数据继续下载
					downloadDataFromServer(tree);
				} else {
                    // 当前表同步完成
					Message msg = Message.obtain();
					msg.obj = tree;
					mCurrentHandler.sendMessage(msg);
				}
			}

			@Override
			public void onFailure(ApiHttpException e) {
				// 数据下载失败
				if (mListener != null) {
					mListener.onStateChange(tree.getClazz(), OnSyncChangeListener.STATE_ERROR);
				}
				Message msg = Message.obtain();
				msg.obj = tree;
				mCurrentHandler.sendMessage(msg);
			}

		});
	}

	/**
	 * 获取当前表中最近一条记录的时间
	 * 
	 * @param clazz 当前表的类字节码
	 * @return 当前表中最近一条记录的时间, 如果是空表则返回指定的时间戳
	 */
	private <T extends IDownloadable> long getLastTime(Class<T> clazz) {
		BaseDao<T, Long> dao = mDaoHelper.getDao(clazz);
		long value = dao.queryRawValue("select max(updateTime) from " + dao.getTableName());
		if (value >= 1) {
			return value;
		}
		return mTimeStamp;
	}
	
	/**
	 * 清除无效的数据
	 * 
	 * @param clazz
	 *            当前表的类字节码
	 */
	private <T extends IDownloadable> void clearInvalidData(Class<T> clazz) {
		BaseDao<T, Long> dao = mDaoHelper.getDao(clazz);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("scode", 1);
        // 清除过期的数据
		dao.deleteList(map);
		map.clear();
        // 清除本地添加的数据
		map.put("id", -1);
        dao.deleteList(map);
	}

    /**
     * 清除指定表中的数据
     * @param clazz 当前上传表字节码
     */
    private <T extends IDownloadable> void clearTable(Class<T> clazz) {
        mDaoHelper.getDao(clazz).clearTable();
    }
	
	/**
	 * 关闭当前模块
	 */
	public void close() {
		mDaoHelper = null;
		mCurrentHandler = null;
		instance = null;
	}

    /**
     * 设置机构ID
     */
	public void setAgencyId(long agencyId) {
		this.mAgencyId = agencyId;
	}

	/**
	 * 添加数据过滤器
     */
    public void setDateFilter(DataFilter dataFilter) {
		this.mDataFilter = dataFilter;
    }

    /**
     * 设置时间戳
     */
    public void setTimeStamp(long timeStamp) {
        this.mTimeStamp = timeStamp;
    }

	/**
	 * 下载数据前是否要清空表
     */
	public void clearDownloadTable(boolean clearDownloadTable) {
		this.clearDownloadTable = clearDownloadTable;
	}

    /**
     * 设置限制条目数
     */
    public void setLimitedNumber(long limitedNumber) {
        this.mLimitedNumber = limitedNumber;
    }

	/**
	 * 设置当前上传操作监听器
	 * 
	 * @param l 同步状态监听接口
	 */
	public void setOnSyncChangeListener(OnSyncChangeListener l) {
		if (l == null) {
			throw new IllegalArgumentException("The OnDownloadStateChangeListener is null");
		}
		mListener = l;
	}

}
