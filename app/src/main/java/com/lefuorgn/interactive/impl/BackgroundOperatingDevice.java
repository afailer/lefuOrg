package com.lefuorgn.interactive.impl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import com.lefuorgn.interactive.interf.Callback;
import com.lefuorgn.interactive.interf.DataFilter;
import com.lefuorgn.interactive.interf.OperatingDevice;
import com.lefuorgn.interactive.util.SyncUtils;
import com.lefuorgn.util.TLog;

/**
 * 后台服务运行器
 */

public class BackgroundOperatingDevice extends Service implements OperatingDevice {

    private volatile Looper mServiceLooper;
    private volatile ServiceHandler mServiceHandler;
    private Intent mIntent;

    private long limitTime;
    private DataFilter filter;
    private long agencyId;
    private int uploadNumber;
    private int uploadMediaNumber;
    private int downloadNumber;
    private boolean configTable;
    private boolean downloadTable;
    private boolean clearDownloadTable;
    private boolean uploadTable;
    private boolean elderlyRelatedTable;
    private Callback mCallback;

    private final class ServiceHandler extends Handler {

        ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            onHandleIntent();
        }

    }

    public BackgroundOperatingDevice() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("Service[BackgroundOperatingDevice]");
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public void running() {
        Message msg = mServiceHandler.obtainMessage();
        msg.obj = mIntent;
        mServiceHandler.sendMessage(msg);
    }

    @Override
    public void onDestroy() {
        mServiceLooper.quit();
    }

    @Override
    public IBinder onBind(Intent intent) {
        mIntent = intent;
        limitTime = intent.getLongExtra("limitTime", 0);
        filter = (DataFilter) intent.getSerializableExtra("filter");
        agencyId = intent.getLongExtra("agencyId", 0);
        uploadNumber = intent.getIntExtra("uploadNumber", 50);
        uploadMediaNumber = intent.getIntExtra("uploadMediaNumber", 20);
        downloadNumber = intent.getIntExtra("downloadNumber", 200);
        configTable = intent.getBooleanExtra("configTable", false);
        downloadTable = intent.getBooleanExtra("downloadTable", false);
        clearDownloadTable = intent.getBooleanExtra("clearDownloadTable", false);
        uploadTable = intent.getBooleanExtra("uploadTable", false);
        elderlyRelatedTable = intent.getBooleanExtra("elderlyRelatedTable", false);
        return new MyBinder();
    }

    protected void onHandleIntent() {
        TLog.log("服务运行...");
        SyncUtils syncUtils = new SyncUtils(getApplication(), mServiceLooper);
        syncUtils.setLimitTime(limitTime);
        syncUtils.setFilter(filter);
        syncUtils.setAgencyId(agencyId);
        syncUtils.setUploadNumber(uploadNumber);
        syncUtils.setUploadMediaNumber(uploadMediaNumber);
        syncUtils.setDownloadNumber(downloadNumber);
        syncUtils.setConfigTable(configTable);
        syncUtils.setDownloadTable(downloadTable);
        syncUtils.setClearDownloadTable(clearDownloadTable);
        syncUtils.setUploadTable(uploadTable);
        syncUtils.setElderlyRelatedTable(elderlyRelatedTable);
        syncUtils.setCallback(new Callback() {
            @Override
            public void onStart() {
                if(mCallback != null) {
                    mCallback.onStart();
                }
            }

            @Override
            public void onLoading(long count, long current, String info) {
                if(mCallback != null) {
                    mCallback.onLoading(count, current, info);
                }
            }

            @Override
            public void onStop() {
                if(mCallback != null) {
                    mCallback.onStop();
                }
                stopSelf();
            }
        });
        syncUtils.running();
    }

    public class MyBinder extends Binder {

        public void addCallback(Callback callback) {
            mCallback = callback;
        }

        public void start() {
            // 启动服务
            running();
        }
    }
}
