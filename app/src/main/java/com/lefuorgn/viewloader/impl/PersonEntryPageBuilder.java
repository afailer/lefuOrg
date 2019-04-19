package com.lefuorgn.viewloader.impl;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.lefuorgn.viewloader.ViewLoader;
import com.lefuorgn.viewloader.builder.DynamicCheckBoxBuilder;
import com.lefuorgn.viewloader.builder.DynamicRadioBuilder;
import com.lefuorgn.viewloader.interf.PageBuilder;

/**
 * 人员信息录入页面构造器实现类
 */

public class PersonEntryPageBuilder implements PageBuilder {

    private ViewLoader mViewLoader;
    private LinearLayout mPageView;

    private DynamicRadioBuilder mDynamicRadioBuilder;
    private DynamicCheckBoxBuilder mDynamicCheckBoxBuilder;

    @Override
    public void setParams(ViewLoader viewLoader) {
        mViewLoader = viewLoader;
    }

    @Override
    public void buildView() {
        mPageView = new LinearLayout(mViewLoader.context());
        mPageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mPageView.setOrientation(LinearLayout.VERTICAL);
        mDynamicRadioBuilder = new DynamicRadioBuilder(mViewLoader.context(),
                mViewLoader.paddingWidth(), mViewLoader.paddingHeight(),
                mViewLoader.labelSize(), mViewLoader.labelColor(),
                mViewLoader.valueSize(), mViewLoader.valueColor(),
                mViewLoader.buttonSize(), mViewLoader.dynamic());
        mPageView.addView(mDynamicRadioBuilder.buildView(null, mPageView));
        if(mViewLoader.copy()) {
            mDynamicCheckBoxBuilder = new DynamicCheckBoxBuilder(mViewLoader.context(),
                    mViewLoader.paddingWidth(), mViewLoader.paddingHeight(),
                    mViewLoader.labelSize(), mViewLoader.labelColor(),
                    mViewLoader.valueSize(), mViewLoader.valueColor(),
                    mViewLoader.buttonSize());
            mPageView.addView(mDynamicCheckBoxBuilder.buildView(null, mPageView));
        }
    }

    @Override
    public View getPageView() {
        return mPageView;
    }


    /**
     * 获取单选动态控件构建器
     */
    public DynamicRadioBuilder getDynamicRadioBuilder() {
        return mDynamicRadioBuilder;
    }

    /**
     * 获取多选动态控件构建器
     */
    public DynamicCheckBoxBuilder getDynamicCheckBoxBuilder() {
        return mDynamicCheckBoxBuilder;
    }

}
