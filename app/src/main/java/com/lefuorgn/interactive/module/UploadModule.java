package com.lefuorgn.interactive.module;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.synch.UploadApi;
import com.lefuorgn.db.util.DaoHelper;
import com.lefuorgn.interactive.bean.UploadTableTree;
import com.lefuorgn.interactive.interf.OnSyncChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 同步上传表模块
 */
public class UploadModule {
	
	private static UploadModule instance;
	private DaoHelper mDaoHelper;
	private Handler mCurrentHandler;
    private long mLimitedNumber;

	private OnSyncChangeListener mListener;
	
	private UploadModule (Looper looper) {
		mDaoHelper = DaoHelper.getInstance();
		mCurrentHandler = new Handler(looper){
			@Override
			public void handleMessage(Message msg) {
				// 切换上传表进行上传
				switchTable(msg);
			}
		};
	}

    /**
     * 获取当前上传模块的实例
     *
     * @param looper 指定线程中的Looper
     * @return 当前类实例化对象
     */
	public static UploadModule getInstance(Looper looper) {
		if(instance == null) {
			synchronized (UploadModule.class) {
				if(instance == null) {
					instance = new UploadModule(looper);
				}
			}
		}
		return instance;
	}

    /**
     * 开启上传模块,上传服务
     * @param tree 下载表链表
     * @param <T> 泛型
     */
	public <T> void start(UploadTableTree<T> tree) {
        if(tree == null) {
            throw new IllegalArgumentException("The tree is null");
        }
		if(mListener != null) {
			mListener.onStart();
		}
        if (mListener != null) {
            // 开始
            mListener.onStateChange(tree.getClazz(), OnSyncChangeListener.STATE_START);
        }
		// 上传上传表信息
		uploadDataToServer(1, tree);
	}

    /**
     * 切换表进行下载
     * @param msg 上一个节点的同步信息
     * @param <T> 泛型
     */
    @SuppressWarnings("unchecked")
	private <T> void switchTable(Message msg) {

        UploadTableTree<T> tree = (UploadTableTree<T>) msg.obj;
        // 表下载完成,
        clearTable(tree.getClazz());
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
            uploadDataToServer(1, tree.getSubClazz());
        }else if (mListener != null){
            // 所有表下载完毕, 结束点
            mListener.onEnd();
        }

	}
	
	/**
	 * 上传指定表中的数据到服务器
	 * @param pageNo 当前上传的页面
	 * @param tree 当前表节点
	 */
	private <T> void uploadDataToServer(long pageNo, final UploadTableTree<T> tree) {
		// 从数据库中获取数据
		List<T> list = getDataFromBD(pageNo, tree.getClazz());
		if(list.size() > 0) {
			// 当前存在要提交的额数据
			final long fPageNo = ++pageNo;
			UploadApi.uploadData(tree.getClazz(), list, new RequestCallback<String>(mCurrentHandler) {

				@Override
				public void onSuccess(String result) {
					// 继续提交数据
					uploadDataToServer(fPageNo, tree);
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					// 数据上传失败
					uploadDataToServer(fPageNo, tree);
				}
				
			});
		}else {
			// 当前不存在要提交的数据
			if(mListener != null && pageNo == 1) {
                // 空表
                mListener.onStateChange(tree.getClazz(), OnSyncChangeListener.STATE_UPLOAD_NULL);
			}
			// 数据上传完毕
			Message msg = Message.obtain();
			msg.obj = tree;
			mCurrentHandler.sendMessage(msg);
		}
	}
	
	/**
	 * 获取指定表中指定页面的数据
	 * @param pageNo 当前页面,从1开始
	 * @param clazz 当前表类字节码
	 * @return 要上传的数据
	 */
	private <T> List<T> getDataFromBD(long pageNo, Class<T> clazz) {
		if(pageNo < 1) {
			throw new IllegalArgumentException("The pageNo is less than 1, the current pageNo = " + pageNo);
		}
		List<T> list = mDaoHelper.getDao(clazz).queryAll((pageNo - 1) * mLimitedNumber, mLimitedNumber);
		if(list == null) {
			// 数据库操作失败
			list = new ArrayList<T>();
		}
		return list;
	}

    /**
     * 清除指定表中的数据
     * @param clazz 当前上传表字节码
     * @param <T> 泛型
     */
    private <T> void clearTable(Class<T> clazz) {
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
			throw new IllegalArgumentException(
					"The OnDownloadStateChangeListener is null");
		}
		mListener = l;
	}

}
