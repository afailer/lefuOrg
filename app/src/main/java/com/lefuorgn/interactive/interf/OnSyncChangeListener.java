package com.lefuorgn.interactive.interf;

/**
 * 同步数据状态改变监听接口
 */

public interface OnSyncChangeListener {

    /**
     * 数据开始同步状态
     */
    int STATE_START = 1;
    /**
     * 数据同步成功状态
     */
    int STATE_SUCCESS = 2;
    /**
     * 数据同步失败
     */
    int STATE_ERROR = 3;
    /**
     * 上传表为空
     */
    int STATE_UPLOAD_NULL = 4;

    /**
     * 当前模块开始运行
     */
    void onStart();

    /**
     * 当前模块运行结束
     */
    void onEnd();

    /**
     * 上传表中的表状态变换
     * @param clazz 当前同步表字节码
     * @param state 当前表的同步状态
     * @param <T> 泛型
     */
    <T> void onStateChange(Class<T> clazz, int state);

}
