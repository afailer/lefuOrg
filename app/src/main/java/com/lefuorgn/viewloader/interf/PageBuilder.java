package com.lefuorgn.viewloader.interf;

import android.view.View;

import com.lefuorgn.viewloader.ViewLoader;

/**
 * 页面构造器接口类
 */

public interface PageBuilder {

    /**
     * 初始化参数
     * @param viewLoader 参数配置类
     */
    void setParams(ViewLoader viewLoader);

    /**
     * 创建视图
     */
    void buildView();

    /**
     * 获取页面控件
     * @return 所需要的控件
     */
    View getPageView();

}
