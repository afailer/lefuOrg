package com.lefuorgn.interactive.module;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.lefuorgn.AppContext;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.synch.DownloadApi;
import com.lefuorgn.api.remote.synch.UploadApi;
import com.lefuorgn.db.base.BaseDao;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.db.util.DaoHelper;
import com.lefuorgn.db.util.OldPeopleManager;
import com.lefuorgn.interactive.bean.CareOldPeople;
import com.lefuorgn.interactive.interf.OnSyncChangeListener;
import com.lefuorgn.util.TLog;

import java.util.List;

/**
 * 特殊表的上传和下载
 */

public class SpecialModule {

    public static final int CONFIG_NUM = 1;

    private static SpecialModule instance;
    private DaoHelper mDaoHelper;
    private Handler mCurrentHandler;

    private OnSyncChangeListener mListener;

    private SpecialModule(Looper looper) {
        mDaoHelper = DaoHelper.getInstance();
        mCurrentHandler = new Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                // 下载关注的老人
                downloadOldPeopleAttention();
            }
        };
    }

    /**
     * 获取当前下载模块的实例
     *
     * @param looper 指定线程中的Looper
     * @return 当前类实例化对象
     */
    public static SpecialModule getInstance(Looper looper) {
        if (instance == null) {
            synchronized (SpecialModule.class) {
                if (instance == null) {
                    instance = new SpecialModule(looper);
                }
            }
        }
        return instance;
    }

    /**
     * 开启下载模块
     */
    public void start() {
        if (mListener != null) {
            // 开始下载, 起始点
            mListener.onStart();
            mListener.onStateChange(CareOldPeople.class, OnSyncChangeListener.STATE_START);
        }
        // 处理老人数据
        OldPeopleManager.convertedToPinyin();
        // 上传关注的老人
        if(AppContext.isCareForTheElderly()) {
            // 存在关注的老人
            // 置状态为初始化
            AppContext.setCareForTheElderly(false);
            uploadOldPeopleAttention();
        }else {
            // 下载关注的老人
            mCurrentHandler.sendEmptyMessage(0);
        }
    }

    /**
     * 下载当前用户关注的老人
     */
    private void downloadOldPeopleAttention() {
        DownloadApi.downloadOldPeopleAttention(new RequestCallback<List<CareOldPeople>>(mCurrentHandler) {

            @Override
            public void onSuccess(List<CareOldPeople> result) {
                TLog.log("关注的老人: " + result.toString());
                BaseDao<OldPeople, Long> dao = mDaoHelper.getDao(OldPeople.class);
                for (CareOldPeople attention : result) {
                    OldPeople o = dao.query("id", attention.getOlder_id());
                    if (o != null) {
                        TLog.error(o.getElderly_name() + " 老人为被关注老人");
                        o.setAttention(true);
                        o.setcAttention(true);
                        dao.update(o);
                    }else {
                        TLog.error("id" + attention.getOlder_id() + "的老人为被关注老人");
                    }
                }
                if (mListener != null) {
                    // 开始下载, 起始点
                    mListener.onStateChange(CareOldPeople.class, OnSyncChangeListener.STATE_SUCCESS);
                    mListener.onEnd();
                }
            }

            @Override
            public void onFailure(ApiHttpException e) {
                if (mListener != null) {
                    // 开始下载, 起始点
                    mListener.onStateChange(CareOldPeople.class, OnSyncChangeListener.STATE_ERROR);
                    mListener.onEnd();
                }
            }
        });
    }

    /**
     * 上传用户所关注的老人
     */
    private void uploadOldPeopleAttention() {
        TLog.error("=========上传老人数据=============");
        // 获取要同步的老人
        String ids = AppContext.getCareForTheElderlyIds();
        UploadApi.uploadOldPeopleAttention(ids, new RequestCallback<String>(mCurrentHandler) {

            @Override
            public void onSuccess(String result) {
                AppContext.setCareForTheElderlyIds("");
                mCurrentHandler.sendEmptyMessage(0);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                AppContext.setCareForTheElderlyIds("");
                mCurrentHandler.sendEmptyMessage(0);
            }
        });
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
