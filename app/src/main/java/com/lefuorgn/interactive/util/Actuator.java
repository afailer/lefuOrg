package com.lefuorgn.interactive.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.lefuorgn.interactive.Interactive;
import com.lefuorgn.interactive.impl.BackgroundOperatingDevice;
import com.lefuorgn.interactive.impl.FrontOperatingDevice;
import com.lefuorgn.interactive.interf.Callback;
import com.lefuorgn.interactive.interf.SyncCallback;
import com.lefuorgn.util.TLog;


/**
 * 同步执行器(单例模式, 同一时间只能执行一次)
 */

public class Actuator {

    private static Actuator mInstance;
    private boolean syncing; // 数据同步状态
    private Callback mSyncCallback; // 外部接口
    private Interactive mInteractive;

    private Actuator() {
    }

    public static Actuator getInstance() {
        if(mInstance == null) {
            synchronized (Actuator.class) {
                if(mInstance == null) {
                    mInstance = new Actuator();
                }
            }
        }
        return mInstance;
    }

    private Callback mLocalCallback = new Callback() {
        @Override
        public void onStart() {
            syncing = true;
            if(mSyncCallback != null) {
                mSyncCallback.onStart();
            }
        }

        @Override
        public void onLoading(long count, long current, String info) {
            if(mSyncCallback != null) {
                mSyncCallback.onLoading(count, current, info);
            }
        }

        @Override
        public void onStop() {
            syncing = false;
            if(mSyncCallback != null) {
                mSyncCallback.onStop();
            }
            if(mInteractive.runningService()) {
                mInteractive.context().unbindService(conn);
            }
            mInteractive = null;
            mSyncCallback = null;
        }
    };

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BackgroundOperatingDevice.MyBinder binder = (BackgroundOperatingDevice.MyBinder) service;
            binder.addCallback(mLocalCallback);
            binder.start();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            TLog.error("=========服务解绑...");
        }
    };

    public void start(Interactive interactive, final SyncCallback syncCallback) {
        mInteractive = interactive;
        mSyncCallback = syncCallback;
        if(interactive.runningService()) {
            TLog.log("运行在后台服务中");
            // 运行在后台服务中
            Intent intent = new Intent(interactive.context(), BackgroundOperatingDevice.class);
            intent.putExtra("limitTime", interactive.limitTime());
            intent.putExtra("filter", interactive.filter());
            intent.putExtra("agencyId", interactive.agencyId());
            intent.putExtra("uploadNumber", interactive.uploadNumber());
            intent.putExtra("uploadMediaNumber", interactive.uploadMediaNumber());
            intent.putExtra("downloadNumber", interactive.downloadNumber());
            intent.putExtra("configTable", interactive.configTable());
            intent.putExtra("downloadTable", interactive.downloadTable());
            intent.putExtra("clearDownloadTable", interactive.clearDownloadTable());
            intent.putExtra("uploadTable", interactive.uploadTable());
            intent.putExtra("elderlyRelatedTable", interactive.elderlyRelatedTable());
            interactive.context().bindService(intent, conn, Context.BIND_AUTO_CREATE);
        }else {
            // 运行在子线程中
            FrontOperatingDevice f = new FrontOperatingDevice(interactive.context());
            f.setLimitTime(interactive.limitTime());
            f.setFilter(interactive.filter());
            f.setAgencyId(interactive.agencyId());
            f.setUploadNumber(interactive.uploadNumber());
            f.setUploadMediaNumber(interactive.uploadMediaNumber());
            f.setDownloadNumber(interactive.downloadNumber());
            f.setConfigTable(interactive.configTable());
            f.setDownloadTable(interactive.downloadTable());
            f.setClearDownloadTable(interactive.clearDownloadTable());
            f.setUploadTable(interactive.uploadTable());
            f.setElderlyRelatedTable(interactive.elderlyRelatedTable());
            f.setCallback(mLocalCallback);
            f.running();
        }
    }

    public boolean isSyncing() {
        return syncing;
    }

    public void close() {
        mInstance = null;
    }

}
