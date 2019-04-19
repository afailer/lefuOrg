package com.lefuorgn.interactive;

import android.content.Context;

import com.lefuorgn.AppContext;
import com.lefuorgn.interactive.impl.RealSync;
import com.lefuorgn.interactive.impl.RemoveDuplicateDataFilter;
import com.lefuorgn.interactive.interf.DataFilter;
import com.lefuorgn.interactive.interf.Sync;

/**
 * 服务器与本地数据同步工具类
 */

public class Interactive {

    /**
     * 默认上传条数
     */
    private static final int DEFAULT_UPLOAD_NUMBER = 50;
    /**
     * 默认媒体上传条数
     */
    private static final int DEFAULT_UPLOAD_MEDIA_NUMBER = 20;
    /**
     * 默认下载条数
     */
    private static final int DEFAULT_DOWNLOAD_NUMBER = 200;
    /**
     * 下载表默认加载数据的时间戳, 加载所有数据
     */
    private static final long DEFAULT_LIMIT_TIME = -28800000;

    private final Context context;
    private final long limitTime;
    private final DataFilter filter;
    private final long agencyId;
    private final int uploadNumber;
    private final int uploadMediaNumber;
    private final int downloadNumber;
    private final boolean configTable;
    private final boolean downloadTable;
    private final boolean clearDownloadTable;
    private final boolean uploadTable;
    private final boolean elderlyRelatedTable;
    private final boolean runningService;

    public Interactive() {
        this(new Builder());
    }

    private Interactive(Builder builder) {
        this.context = builder.context;
        this.limitTime = builder.limitTime;
        this.filter = builder.filter;
        this.agencyId = builder.agencyId;
        this.uploadNumber = builder.uploadNumber;
        this.uploadMediaNumber = builder.uploadMediaNumber;
        this.downloadNumber = builder.downloadNumber;
        this.configTable = builder.configTable;
        this.downloadTable = builder.downloadTable;
        this.clearDownloadTable = builder.clearDownloadTable;
        this.uploadTable = builder.uploadTable;
        this.elderlyRelatedTable = builder.elderlyRelatedTable;
        this.runningService = builder.runningService;
    }

    public Context context() {
        return context;
    }

    public long limitTime() {
        return limitTime;
    }

    public DataFilter filter() {
        return filter;
    }

    public long agencyId() {
        return agencyId;
    }

    public int uploadNumber() {
        return uploadNumber;
    }

    public int uploadMediaNumber() {
        return uploadMediaNumber;
    }

    public int downloadNumber() {
        return downloadNumber;
    }

    public boolean configTable() {
        return configTable;
    }

    public boolean downloadTable() {
        return downloadTable;
    }

    public boolean clearDownloadTable() {
        return clearDownloadTable;
    }

    public boolean uploadTable() {
        return uploadTable;
    }

    public boolean elderlyRelatedTable() {
        return elderlyRelatedTable;
    }

    public boolean runningService() {
        return runningService;
    }

    public Sync newSync() {
        return new RealSync(this);
    }

    public static final class Builder {

        Context context;
        /**
         * 加载数据的时间戳, 只用于下载表首次数据加载
         */
        long limitTime;
        /**
         * 下载表数据过滤器
         */
        DataFilter filter;
        /**
         * 机构ID
         */
        long agencyId;
        /**
         * 上传表上传数目
         */
        int uploadNumber;
        /**
         * 上传表媒体信息上传数据
         */
        int uploadMediaNumber;
        /**
         * 下载表下载数目
         */
        int downloadNumber;
        /**
         * 是否同步配置表
         */
        boolean configTable;
        /**
         * 是否同步下载表
         */
        boolean downloadTable;
        /**
         * 同步下载表前是否清空所有数据下载表(不包含老人相关信息表)
         */
        boolean clearDownloadTable;
        /**
         * 是否同步上传表
         */
        boolean uploadTable;
        /**
         * 是否同步老人相关表
         */
        boolean elderlyRelatedTable;
        /**
         * 是否运行在后台服务中
         */
        boolean runningService;

        public Builder() {
            context = AppContext.getInstance();
            limitTime = AppContext.getSyncDataForLongTime();
            filter = new RemoveDuplicateDataFilter();
            agencyId = 0;
            uploadNumber = DEFAULT_UPLOAD_NUMBER;
            uploadMediaNumber = DEFAULT_UPLOAD_MEDIA_NUMBER;
            downloadNumber = DEFAULT_DOWNLOAD_NUMBER;
            configTable = false;
            downloadTable = false;
            clearDownloadTable = false;
            uploadTable = false;
            elderlyRelatedTable = false;
            runningService = false;
        }

        Builder(Interactive interactive) {
            this.context = interactive.context;
            this.limitTime = interactive.limitTime;
            this.filter = interactive.filter;
            this.agencyId = interactive.agencyId;
            this.uploadNumber = interactive.uploadNumber;
            this.uploadMediaNumber = interactive.uploadMediaNumber;
            this.downloadNumber = interactive.downloadNumber;
            this.configTable = interactive.configTable;
            this.downloadTable = interactive.downloadTable;
            this.clearDownloadTable = interactive.clearDownloadTable;
            this.uploadTable = interactive.uploadTable;
            this.elderlyRelatedTable = interactive.elderlyRelatedTable;
        }

        /**
         * 设置下载表要开始同步数据的开始日期, 时间段是:指定日期到当前时间
         */
        public Builder limitTime(long limitTime) {
            this.limitTime = limitTime;
            return this;
        }

        /**
         * 设置下载表数据过滤器
         */
        public Builder filter(DataFilter filter) {
            if (filter == null) throw new IllegalArgumentException("filter == null");
            this.filter = filter;
            return this;
        }

        /**
         * 设置当前要同步数据的机构ID
         */
        public Builder agencyId(long agencyId) {
            this.agencyId = agencyId;
            return this;
        }

        /**
         * 设置上传表每次同步上传数据限制数目
         */
        public Builder uploadNumber(int uploadNumber) {
            this.uploadNumber = uploadNumber;
            return this;
        }

        /**
         * 设置上传表多媒体信息每次同步上传数据限制数目
         */
        public Builder uploadMediaNumber(int uploadMediaNumber) {
            this.uploadMediaNumber = uploadMediaNumber;
            return this;
        }

        /**
         * 设置下载表每次同步下载数据限制数目
         */
        public Builder downloadNumber(int downloadNumber) {
            this.downloadNumber = downloadNumber;
            return this;
        }

        /**
         * 当前要同步配置文件
         */
        public Builder configTable(boolean configTable) {
            this.configTable = configTable;
            return this;
        }

        /**
         * 当前要同步下载表
         */
        public Builder downloadTable(boolean downloadTable) {
            this.downloadTable = downloadTable;
            return this;
        }

        /**
         * 同步下载表前是否清空下载表
         */
        public Builder clearDownloadTable(boolean clearDownloadTable) {
            this.clearDownloadTable = clearDownloadTable;
            return this;
        }

        /**
         * 当前要同步上传表
         */
        public Builder uploadTable(boolean uploadTable) {
            this.uploadTable = uploadTable;
            return this;
        }

        /**
         * 当前要同步老人相关信息表
         */
        public Builder elderlyRelatedTable(boolean elderlyRelatedTable) {
            this.elderlyRelatedTable = elderlyRelatedTable;
            return this;
        }

        /**
         * 设置当前同步是否运行在后台服务中
         */
        public Builder runningService(boolean runningService) {
            this.runningService = runningService;
            return this;
        }

        public Interactive build() {
            return new Interactive(this);
        }
    }
}
