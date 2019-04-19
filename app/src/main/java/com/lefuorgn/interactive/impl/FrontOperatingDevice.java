package com.lefuorgn.interactive.impl;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.lefuorgn.interactive.interf.Callback;
import com.lefuorgn.interactive.interf.DataFilter;
import com.lefuorgn.interactive.interf.OperatingDevice;
import com.lefuorgn.interactive.util.SyncUtils;

/**
 * 前台服务运行器
 */

public class FrontOperatingDevice implements OperatingDevice {

    private Context context;

    private volatile Looper mServiceLooper;
    private volatile ServiceHandler mServiceHandler;

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
    private Callback callback;

    private final class ServiceHandler extends Handler {

        ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            onHandleIntent();
        }

    }

    public FrontOperatingDevice(Context context) {
        this.context = context;
        HandlerThread thread = new HandlerThread("Service[FrontOperatingDevice]");
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public void running() {
        Message msg = mServiceHandler.obtainMessage();
        mServiceHandler.sendMessage(msg);
    }

    private void onHandleIntent() {
        SyncUtils syncUtils = new SyncUtils(context, mServiceLooper);
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
        syncUtils.setCallback(callback);
        syncUtils.running();
    }

    public void setLimitTime(long limitTime) {
        this.limitTime = limitTime;
    }

    public void setFilter(DataFilter filter) {
        this.filter = filter;
    }

    public void setAgencyId(long agencyId) {
        this.agencyId = agencyId;
    }

    public void setUploadNumber(int uploadNumber) {
        this.uploadNumber = uploadNumber;
    }

    public void setUploadMediaNumber(int uploadMediaNumber) {
        this.uploadMediaNumber = uploadMediaNumber;
    }

    public void setDownloadNumber(int downloadNumber) {
        this.downloadNumber = downloadNumber;
    }

    public void setConfigTable(boolean configTable) {
        this.configTable = configTable;
    }

    public void setDownloadTable(boolean downloadTable) {
        this.downloadTable = downloadTable;
    }

    public void setClearDownloadTable(boolean clearDownloadTable) {
        this.clearDownloadTable = clearDownloadTable;
    }

    public void setUploadTable(boolean uploadTable) {
        this.uploadTable = uploadTable;
    }

    public void setElderlyRelatedTable(boolean elderlyRelatedTable) {
        this.elderlyRelatedTable = elderlyRelatedTable;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
