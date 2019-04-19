package com.lefuorgn.interactive.interf;

/**
 * 用于监听数据同步状态
 */

public interface Callback {

    /**
     * 数据开始同步
     */
    void onStart();

    /**
     * 执行过程中监听响应
     * @param count 当前下载总字节数
     * @param current 当前已经下载的字节数
     * @param info 提示信息
     */
    void onLoading(long count, long current, String info);

    /**
     * 数据同步结束
     */
    void onStop();


}
