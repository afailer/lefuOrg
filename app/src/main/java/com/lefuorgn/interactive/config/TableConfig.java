package com.lefuorgn.interactive.config;

import com.lefuorgn.db.model.base.BaseMediaUpload;
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
import com.lefuorgn.db.model.interf.IDownloadable;
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
import com.lefuorgn.interactive.bean.DownloadTableTree;
import com.lefuorgn.interactive.bean.UploadMediaTableTree;
import com.lefuorgn.interactive.bean.UploadTableTree;

/**
 * 表创建对象
 */

public class TableConfig {

    /**
     * 获取下载表的个数
     */
    public static long getDownloadTableNum() {
        return 11;
    }

    /**
     * 获取下载表链表
     */
    public static DownloadTableTree<PulseDownload> getDownloadTableTree() {
        // 添加照护记录表
        DownloadTableTree<DailyNursingDownload> dNursingTree = addDownloadTable(
                DailyNursingDownload.class, null);
        // 添加随手拍表
        DownloadTableTree<DailyLifeDownload> dLifeTree = addDownloadTable(
                DailyLifeDownload.class, dNursingTree);
        // 添加呼吸表
        DownloadTableTree<BreathingDownload> breathingTree = addDownloadTable(
                BreathingDownload.class, dLifeTree);
        // 添加排便表
        DownloadTableTree<DefecationDownload> defecationTree = addDownloadTable(
                DefecationDownload.class, breathingTree);
        // 添加饮食表
        DownloadTableTree<EatDownload> eatTree = addDownloadTable(
                EatDownload.class, defecationTree);
        // 添加睡眠表
        DownloadTableTree<SleepingDownload> sleepTree = addDownloadTable(
                SleepingDownload.class, eatTree);
        // 添加饮水表
        DownloadTableTree<DrinkingDownload> drinkTree = addDownloadTable(
                DrinkingDownload.class, sleepTree);
        // 添加体温表
        DownloadTableTree<TemperatureDownload> tTree = addDownloadTable(
                TemperatureDownload.class, drinkTree);
        // 添加血糖表
        DownloadTableTree<SugarDownload> sugarTree = addDownloadTable(
                SugarDownload.class, tTree);
        // 添加血压表
        DownloadTableTree<PressureDownload> pTree = addDownloadTable(
                PressureDownload.class, sugarTree);
        // 返回心率表头
        return addDownloadTable(PulseDownload.class, pTree);
    }

    /**
     * 获取老人数据相关下载表的个数
     */
    public static long getElderlyRelatedTableNum() {
        return 3;
    }

    /**
     * 获取老人信息相关下载表链表
     */
    public static DownloadTableTree<OldPeople> getElderlyRelatedTableTree() {
        // 添加老人家属表
        DownloadTableTree<OldPeopleFamily> elderFTree = addDownloadTable(
                OldPeopleFamily.class, null);
        // 添加床位表
        DownloadTableTree<Bed> bedTree = addDownloadTable(
                Bed.class, elderFTree);
        // 添加老人表
        return addDownloadTable(OldPeople.class, bedTree);
    }

    /**
     * 获取上传表的个数
     */
    public static long getUploadTableNum() {
        return 11;
    }

    /**
     * 获取上传表链表
     */
    public static UploadTableTree<PulseUpload> getUploadTableTree() {

        // 添加照护记录表
        UploadTableTree<DailyNursingUpload> dNursingTree = addUploadTable(
                DailyNursingUpload.class, null);
        // 添加随手拍表
        UploadTableTree<DailyLifeUpload> dLifeTree = addUploadTable(
                DailyLifeUpload.class, dNursingTree);
        // 添加呼吸表
        UploadTableTree<BreathingUpload> breathingTree = addUploadTable(
                BreathingUpload.class, dLifeTree);
        // 添加排便表
        UploadTableTree<DefecationUpload> defecationTree = addUploadTable(
                DefecationUpload.class, breathingTree);
        // 添加饮食表
        UploadTableTree<EatUpload> eatTree = addUploadTable(
                EatUpload.class, defecationTree);
        // 添加睡眠表
        UploadTableTree<SleepingUpload> sleepTree = addUploadTable(
                SleepingUpload.class, eatTree);
        // 添加饮水表
        UploadTableTree<DrinkingUpload> drinkTree = addUploadTable(
                DrinkingUpload.class, sleepTree);
        // 添加体温表
        UploadTableTree<TemperatureUpload> tTree = addUploadTable(
                TemperatureUpload.class, drinkTree);
        // 添加血糖表
        UploadTableTree<SugarUpload> sugarTree = addUploadTable(
                SugarUpload.class, tTree);
        // 添加血压表
        UploadTableTree<PressureUpload> pTree = addUploadTable(
                PressureUpload.class, sugarTree);
        // 返回心率表头
        return addUploadTable(PulseUpload.class, pTree);
    }

    /**
     * 获取上传表的个数
     */
    public static long getUploadMediaTableNum() {
        return 2;
    }

    /**
     * 获取媒体上传表链表
     */
    public static UploadMediaTableTree<DailyLifeMediaUpload> getUploadMediaTableTree() {

        // 添加日常护理媒体表
        UploadMediaTableTree<DailyNursingMediaUpload> pTree = addUploadMediaTable(
                DailyNursingMediaUpload.class, null);
        // 返回随手拍媒体表头
        return addUploadMediaTable(DailyLifeMediaUpload.class, pTree);
    }

    /**
     * 创建表链节点
     * @param clazz 当前表类字节码
     * @param subTree 子节点
     * @return 需要下载数据表的链式结构
     */
    private static <T extends IDownloadable> DownloadTableTree<T> addDownloadTable(
            Class<T> clazz,
            DownloadTableTree<? extends IDownloadable> subTree) {
        DownloadTableTree<T> pTree = new DownloadTableTree<T>();
        pTree.setClazz(clazz);
        pTree.setSubClazz(subTree);
        return pTree;
    }

    /**
     * 创建表链节点
     * @param clazz 当前表类字节码
     * @param subTree 子节点
     * @return 需要下载数据表的链式结构
     */
    private static <T> UploadTableTree<T> addUploadTable(
            Class<T> clazz,
            UploadTableTree<?> subTree) {
        UploadTableTree<T> pTree = new UploadTableTree<T>();
        pTree.setClazz(clazz);
        pTree.setSubClazz(subTree);
        return pTree;
    }

    /**
     * 创建表链节点
     * @param clazz 当前表类字节码
     * @param subTree 子节点
     * @return 需要下载数据表的链式结构
     */
    private static <T extends BaseMediaUpload> UploadMediaTableTree<T> addUploadMediaTable(
            Class<T> clazz,
            UploadMediaTableTree<?> subTree) {
        UploadMediaTableTree<T> pTree = new UploadMediaTableTree<T>();
        pTree.setClazz(clazz);
        pTree.setSubClazz(subTree);
        return pTree;
    }

}
