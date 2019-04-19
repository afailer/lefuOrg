package com.lefuorgn.interf;

/**
 * Created by 韩春 on 2016/11/14.
 */

public interface ICallbackResult {

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
     * @param msg 当前同步信息
     */
    void onStateChangeMsg(String msg, int progress, int max);

}
