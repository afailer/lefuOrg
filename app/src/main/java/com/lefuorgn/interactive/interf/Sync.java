package com.lefuorgn.interactive.interf;

/**
 * 服务器与本地数据库数据同步接口
 */

public interface Sync {

    /**
     * 执行当前同步任务
     * @param syncCallback 同步接口回调
     */
    void enqueue(SyncCallback syncCallback);

    /**
     * 如果{@linkplain #enqueue(SyncCallback) enqueued}被调用则返回true. 如果多次调用则会报错
     */
    boolean isExecuted();

}
