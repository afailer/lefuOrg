package com.lefuorgn.interactive.util;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.lefuorgn.AppConfig;
import com.lefuorgn.AppContext;
import com.lefuorgn.db.model.basic.Bed;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.db.model.basic.OldPeopleFamily;
import com.lefuorgn.db.model.download.BreathingDownload;
import com.lefuorgn.db.model.download.DailyLifeDownload;
import com.lefuorgn.db.model.download.DailyNursingDownload;
import com.lefuorgn.db.model.download.DefecationDownload;
import com.lefuorgn.db.model.download.DrinkingDownload;
import com.lefuorgn.db.model.download.EatDownload;
import com.lefuorgn.db.model.download.PressureDownload;
import com.lefuorgn.db.model.download.PulseDownload;
import com.lefuorgn.db.model.download.SleepingDownload;
import com.lefuorgn.db.model.download.SugarDownload;
import com.lefuorgn.db.model.download.TemperatureDownload;
import com.lefuorgn.db.model.upload.BreathingUpload;
import com.lefuorgn.db.model.upload.DailyLifeMediaUpload;
import com.lefuorgn.db.model.upload.DailyLifeUpload;
import com.lefuorgn.db.model.upload.DailyNursingMediaUpload;
import com.lefuorgn.db.model.upload.DailyNursingUpload;
import com.lefuorgn.db.model.upload.DefecationUpload;
import com.lefuorgn.db.model.upload.DrinkingUpload;
import com.lefuorgn.db.model.upload.EatUpload;
import com.lefuorgn.db.model.upload.PressureUpload;
import com.lefuorgn.db.model.upload.PulseUpload;
import com.lefuorgn.db.model.upload.SleepingUpload;
import com.lefuorgn.db.model.upload.SugarUpload;
import com.lefuorgn.db.model.upload.TemperatureUpload;
import com.lefuorgn.db.util.OldPeopleManager;
import com.lefuorgn.interactive.config.TableConfig;
import com.lefuorgn.interactive.interf.Callback;
import com.lefuorgn.interactive.interf.DataFilter;
import com.lefuorgn.interactive.interf.OnSyncChangeListener;
import com.lefuorgn.interactive.module.ConfigModule;
import com.lefuorgn.interactive.module.DownloadModule;
import com.lefuorgn.interactive.module.FileModule;
import com.lefuorgn.interactive.module.SpecialModule;
import com.lefuorgn.interactive.module.UploadModule;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;


/**
 * 同步内容筛选工具类
 * <p>同步数据可能的类型有四种: 而且顺序依上而下</p>
 * <ul>
 *     <li>配置文件</li>
 *     <li>上传表信息</li>
 *     <li>下载表信息</li>
 *     <li>老人相关信息(还包括老人信息表, 床位信息表, 老人家属表)</li>
 * </ul>
 */

public class SyncUtils {

    private Context context;
    private Looper looper;

    private long limitTime; // 时间戳
    private DataFilter filter; // 数据过滤器
    private long agencyId; // 机构ID
    private int uploadNumber; // 上传限制数
    private int uploadMediaNumber; // 上传媒体限制数
    private int downloadNumber; // 下载限制数
    private boolean configTable; // 是否下载配置文件
    private boolean downloadTable; // 是否下载下载表数据
    private boolean clearDownloadTable; // 下载下载表数据前是否要清空下载表
    private boolean uploadTable; // 是否上传上传表数据
    private boolean uploadMediaTable = false; // 是否上传媒体上传表数据, 基于上传表的
    private boolean elderlyRelatedTable; // 是否下载老人相关数据
    private boolean elderlyRelatedDataProcess = false; // 处理老人相关数据, 基于老人相关下载表
    private Callback callback; // 接口回调

    private long total;
    private long current;

    private Handler handler;

    public SyncUtils(Context context, Looper looper) {
        this.context = context;
        this.looper = looper;
        handler = new Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                sync();
            }
        };
    }

    /**
     * 运行
     */
    public void running() {
        // 统计当前运行情况
        if(callback != null) {
            callback.onStart();
        }
        initData();
        // 开始执行
        Intent intent = new Intent(AppConfig.INTENT_ACTION_NOTICE_START);
        context.sendBroadcast(intent);
        handler.sendEmptyMessage(0);
    }

    /**
     * 初始化当前
     */
    private void initData() {
        if(configTable) {
            // 加载配置文件
            total += ConfigModule.CONFIG_NUM;
        }
        if(uploadTable) {
            // 同步上传表
            total += TableConfig.getUploadTableNum();
            total += TableConfig.getUploadMediaTableNum();
        }
        if(downloadTable) {
            // 同步下载表
            total += TableConfig.getDownloadTableNum();
        }
        if(elderlyRelatedTable) {
            // 同步老人相关信息
            total += TableConfig.getElderlyRelatedTableNum();
            total += SpecialModule.CONFIG_NUM;
        }
    }

    private void sync() {
        if(configTable) {
            // 加载配置文件
            configTable = false;
            TLog.error("================1111111111111111111");
            syncConfig();
            return;
        }
        if(uploadTable) {
            // 同步上传表
            uploadTable = false;
            // 同步上传表, 则媒体表一定也要上传
            uploadMediaTable = true;
            TLog.error("================2222222222222222222");
            syncUpload();
            return;
        }
        if(uploadMediaTable) {
            uploadMediaTable = false;
            TLog.error("================3333333333333333333");
            syncMediaUpload();
            return;
        }
        if(downloadTable) {
            // 同步下载表
            downloadTable = false;
            TLog.error("================4444444444444444444");
            syncDownload();
            return;
        }
        if(elderlyRelatedTable) {
            // 同步老人相关信息
            elderlyRelatedTable = false;
            // 同步老人相关数据, 则进行数据处理
            elderlyRelatedDataProcess = true;
            TLog.error("================5555555555555555555");
            if(AppContext.isCareForTheElderly()) {
                // 先获取当前关注的老人, 老人数据已经修改
                StringBuilder sb = new StringBuilder();
                for (OldPeople oldPeople : OldPeopleManager.getOldPeople(true)) {
                    sb.append(oldPeople.getId()).append(",");
                }
                if(!StringUtils.isEmpty(sb.toString())) {
                    AppContext.setCareForTheElderlyIds(sb.substring(0, sb.length() - 1));
                }else {
                    AppContext.setCareForTheElderlyIds("");
                }
            }
            // 同步老人相关信息
            syncElderlyRelated();
            return;
        }
        if(elderlyRelatedDataProcess) {
            // 处理老人相关数据
            elderlyRelatedDataProcess = false;
            TLog.error("================6666666666666666666");
            processElderlyRelated();
            return;
        }
        if(callback != null) {
            callback.onStop();
        }
        // 同步完成广播
        Intent intent = new Intent(AppConfig.INTENT_ACTION_NOTICE_END);
        context.sendBroadcast(intent);
    }


    /**
     * 同步配置文件
     */
    private void syncConfig() {
        final ConfigModule cModule = ConfigModule.getInstance(looper);
        cModule.setAgencyId(agencyId);
        cModule.setOnSyncChangeListener(new OnSyncChangeListener() {

            @Override
            public <T> void onStateChange(Class<T> clazz, int state) {
                if(state == OnSyncChangeListener.STATE_START) {
                    // 开始下载
                    current++;
                    if(callback != null) {
                        callback.onLoading(total, current, getConfigInfo());
                    }
                }
            }

            @Override
            public void onStart() {
                TLog.log("开始下载所有的表...");
            }

            @Override
            public void onEnd() {
                TLog.error("============================syncConfig");
                // 配置文件同步完成, 发送配置刷新广播
                Intent intent = new Intent(AppConfig.INTENT_ACTION_NOTICE_CONFIG);
                context.sendBroadcast(intent);
                // 通过handler执行下一个类型同步
                handler.sendEmptyMessage(0);
                cModule.close();
            }
        });
        cModule.start();
    }

    /**
     * 同步上传表
     */
    private void syncUpload() {
        final UploadModule uModule = UploadModule.getInstance(looper);
        uModule.setLimitedNumber(uploadNumber);
        uModule.setOnSyncChangeListener(new OnSyncChangeListener() {
            @Override
            public void onStart() {
                TLog.log("上传表开始上传");
            }

            @Override
            public void onEnd() {
                TLog.log("所有表上传结束...");
                uModule.close();
                handler.sendEmptyMessage(0);
                TLog.error("============================syncUpload");
            }

            @Override
            public <T> void onStateChange(Class<T> clazz, int state) {
                if(state == OnSyncChangeListener.STATE_START) {
                    // 开始上传
                    current++;
                    if(callback != null) {
                        callback.onLoading(total, current, getUploadInfo(clazz));
                    }
                }
            }
        });
        uModule.start(TableConfig.getUploadTableTree());
    }

    /**
     * 同步媒体信息上传表
     */
    private void syncMediaUpload() {
        final FileModule uModule = FileModule.getInstance(looper);
        uModule.setLimitedNumber(uploadMediaNumber);
        uModule.setOnSyncChangeListener(new OnSyncChangeListener() {
            @Override
            public void onStart() {
                TLog.log("媒体信息上传表开始上传");
            }

            @Override
            public void onEnd() {
                TLog.log("所有媒体信息表上传结束...");
                uModule.close();
                handler.sendEmptyMessage(0);
                TLog.error("============================syncMediaUpload");
            }

            @Override
            public <T> void onStateChange(Class<T> clazz, int state) {
                if(state == OnSyncChangeListener.STATE_START) {
                    // 开始上传
                    current++;
                    if(callback != null) {
                        callback.onLoading(total, current, getMediaUploadInfo(clazz));
                    }
                }
            }
        });
        uModule.start(TableConfig.getUploadMediaTableTree());
    }

    /**
     * 同步下载表
     */
    private void syncDownload() {
        final DownloadModule dModule = DownloadModule.getInstance(looper);
        dModule.setDateFilter(filter);
        dModule.setLimitedNumber(downloadNumber);
        dModule.setAgencyId(agencyId);
        dModule.setTimeStamp(limitTime);
        dModule.clearDownloadTable(clearDownloadTable);
        dModule.setOnSyncChangeListener(new OnSyncChangeListener() {

            @Override
            public <T> void onStateChange(Class<T> clazz, int state) {
                if(state == OnSyncChangeListener.STATE_START) {
                    // 开始下载
                    current++;
                    if(callback != null) {
                        callback.onLoading(total, current, getDownloadInfo(clazz));
                    }
                }
            }

            @Override
            public void onStart() {
                TLog.log("下载表开始下载");
            }

            @Override
            public void onEnd() {
                TLog.log("所有表下载结束...");
                // 下载表数据同步完成, 发送数据刷新广播
                Intent intent = new Intent(AppConfig.INTENT_ACTION_NOTICE_DATA);
                context.sendBroadcast(intent);
                // 通过handler执行下一个类型同步
                handler.sendEmptyMessage(0);
                dModule.close();
                TLog.error("============================syncDownload");
            }
        });
        dModule.start(TableConfig.getDownloadTableTree());
    }

    /**
     * 同步老人相关信息表
     */
    private void syncElderlyRelated() {
        final DownloadModule dModule = DownloadModule.getInstance(looper);
        dModule.setDateFilter(filter);
        dModule.setLimitedNumber(downloadNumber);
        dModule.setAgencyId(agencyId);
        // 加载所有的老人
        dModule.setTimeStamp(-28800000);
        dModule.setOnSyncChangeListener(new OnSyncChangeListener() {

            @Override
            public <T> void onStateChange(Class<T> clazz, int state) {
                if(state == OnSyncChangeListener.STATE_START) {
                    // 开始下载
                    current++;
                    if(callback != null) {
                        callback.onLoading(total, current, getDownloadInfo(clazz));
                    }
                }
            }

            @Override
            public void onStart() {
                TLog.log("下载表开始下载");
            }

            @Override
            public void onEnd() {
                TLog.log("所有表下载结束...");
                dModule.close();
                handler.sendEmptyMessage(0);
                TLog.error("============================syncElderlyRelated");
            }
        });
        dModule.start(TableConfig.getElderlyRelatedTableTree());
    }

    /**
     * 老人相关数据表处理
     */
    private void processElderlyRelated() {
        final SpecialModule sModule = SpecialModule.getInstance(looper);
        sModule.setOnSyncChangeListener(new OnSyncChangeListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onEnd() {
                // 老人数据同步完成, 发送老人数据刷新广播
                Intent intent = new Intent(AppConfig.INTENT_ACTION_NOTICE_ELDERLY);
                context.sendBroadcast(intent);
                // 通过handler执行下一个类型同步
                handler.sendEmptyMessage(0);
                sModule.close();
                TLog.error("============================processElderlyRelated");
            }

            @Override
            public <T> void onStateChange(Class<T> clazz, int state) {
                if(state == OnSyncChangeListener.STATE_START) {
                    // 开始下载
                    current++;
                    if(callback != null) {
                        callback.onLoading(total, current, "老人数据处理中...");
                    }
                }
            }
        });
        sModule.start();
    }

    /**
     * 设置数据加载时间戳
     */
    public SyncUtils setLimitTime(long limitTime) {
        this.limitTime = limitTime;
        return this;
    }

    /**
     * 设置数据过滤器
     */
    public SyncUtils setFilter(DataFilter filter) {
        this.filter = filter;
        return this;
    }

    /**
     * 设置数据所属机构
     */
    public SyncUtils setAgencyId(long agencyId) {
        this.agencyId = agencyId;
        return this;
    }

    /**
     * 设置上传表中上传数据条目限制个数
     */
    public SyncUtils setUploadNumber(int uploadNumber) {
        this.uploadNumber = uploadNumber;
        return this;
    }

    /**
     * 设置多媒体上传表上传条数
     */
    public SyncUtils setUploadMediaNumber(int uploadMediaNumber) {
        this.uploadMediaNumber = uploadMediaNumber;
        return this;
    }

    /**
     * 设置下载表中下载数据条目限制个数
     */
    public SyncUtils setDownloadNumber(int downloadNumber) {
        this.downloadNumber = downloadNumber;
        return this;
    }

    /**
     * 是否加载配置文件
     */
    public SyncUtils setConfigTable(boolean configTable) {
        this.configTable = configTable;
        return this;
    }

    /**
     * 是否加载下载表数据
     */
    public SyncUtils setDownloadTable(boolean downloadTable) {
        this.downloadTable = downloadTable;
        return this;
    }

    /**
     * 加载下载表中的数据时,是否要清空下载表
     */
    public SyncUtils setClearDownloadTable(boolean clearDownloadTable) {
        this.clearDownloadTable = clearDownloadTable;
        return this;
    }

    /**
     * 是否进行上传表上传数据
     */
    public SyncUtils setUploadTable(boolean uploadTable) {
        this.uploadTable = uploadTable;
        return this;
    }

    /**
     * 是否同步老人信息相关数据
     */
    public SyncUtils setElderlyRelatedTable(boolean elderlyRelatedTable) {
        this.elderlyRelatedTable = elderlyRelatedTable;
        return this;
    }

    public SyncUtils setCallback(Callback callback) {
        this.callback = callback;
        return this;
    }

    /**
     * 获取上传表信息
     */
    private <T> String getUploadInfo(Class<T> clazz) {
        String str;
        if(clazz == DailyNursingUpload.class) {
            str = "上传照护记录表...";
        }else if(clazz == DailyLifeUpload.class) {
            str = "上传随手拍表...";
        }else if(clazz == BreathingUpload.class) {
            str = "上传呼吸表...";
        }else if(clazz == DefecationUpload.class) {
            str = "上传排便表...";
        }else if(clazz == EatUpload.class) {
            str = "上传饮食表...";
        }else if(clazz == SleepingUpload.class) {
            str = "上传睡眠表...";
        }else if(clazz == DrinkingUpload.class) {
            str = "上传饮水表...";
        }else if(clazz == TemperatureUpload.class) {
            str = "上传体温表...";
        }else if(clazz == SugarUpload.class) {
            str = "上传血糖表...";
        }else if(clazz == PressureUpload.class) {
            str = "上传血压表...";
        }else if(clazz == PulseUpload.class) {
            str = "上传心率表...";
        }else {
            str = "上传中...";
        }
        return str;
    }

    /**
     * 获取上传表信息
     */
    private <T> String getMediaUploadInfo(Class<T> clazz) {
        String str;
        if(clazz == DailyNursingMediaUpload.class) {
            str = "上传照护记录媒体信息表...";
        }else if(clazz == DailyLifeMediaUpload.class) {
            str = "上传随手拍媒体信息表...";
        }else {
            str = "上传中...";
        }
        return str;
    }

    /**
     * 获取下载表信息
     */
    private <T> String getDownloadInfo(Class<T> clazz) {
        String str;
        if(clazz == OldPeopleFamily.class) {
            str = "下载老人家属表...";
        }else if(clazz == Bed.class) {
            str = "下载床位表...";
        }else if(clazz == OldPeople.class) {
            str = "下载老人表...";
        }else if(clazz == DailyNursingDownload.class) {
            str = "下载照护记录表...";
        }else if(clazz == DailyLifeDownload.class) {
            str = "下载随手拍表...";
        }else if(clazz == BreathingDownload.class) {
            str = "下载呼吸表...";
        }else if(clazz == DefecationDownload.class) {
            str = "下载排便表...";
        }else if(clazz == EatDownload.class) {
            str = "下载饮食表...";
        }else if(clazz == SleepingDownload.class) {
            str = "下载睡眠表...";
        }else if(clazz == DrinkingDownload.class) {
            str = "下载饮水表...";
        }else if(clazz == TemperatureDownload.class) {
            str = "下载体温表...";
        }else if(clazz == SugarDownload.class) {
            str = "下载血糖表...";
        }else if(clazz == PressureDownload.class) {
            str = "下载血压表...";
        }else if(clazz == PulseDownload.class) {
            str = "下载心率表...";
        }else {
            str = "下载中...";
        }
        return str;
    }

    /**
     * 获取配置文件信息
     */
    private String getConfigInfo() {
        return "下载配置文件...";
    }

}
