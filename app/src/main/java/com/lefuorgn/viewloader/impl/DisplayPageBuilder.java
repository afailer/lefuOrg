package com.lefuorgn.viewloader.impl;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.lefuorgn.util.StringUtils;
import com.lefuorgn.viewloader.ViewLoader;
import com.lefuorgn.viewloader.base.ViewBuilder;
import com.lefuorgn.viewloader.config.Config;
import com.lefuorgn.viewloader.config.DisplayViewBuilderFactory;
import com.lefuorgn.viewloader.interf.PageBuilder;
import com.lefuorgn.viewloader.util.BuilderUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 信息展示页面构造器实现类
 */

public class DisplayPageBuilder implements PageBuilder {

    private ViewLoader mViewLoader;
    private LinearLayout mPageView;

    @Override
    public void setParams(ViewLoader viewLoader) {
        mViewLoader = viewLoader;
    }

    @Override
    public void buildView() {
        mPageView = new LinearLayout(mViewLoader.context());
        mPageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mPageView.setOrientation(LinearLayout.VERTICAL);
        DisplayViewBuilderFactory factory = new DisplayViewBuilderFactory(mViewLoader);
        JSONArray jsonArray = BuilderUtils.getArray(mViewLoader.content());
        buildItemView(jsonArray, factory);
    }

    /**
     * 创建条目控件
     * @param jsonArray 条目JSON集合
     * @param factory View构建工厂类
     */
    private void buildItemView(JSONArray jsonArray, DisplayViewBuilderFactory factory) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = BuilderUtils.getObject(jsonArray, i);
            // 判断当前控件类型
            String type = BuilderUtils.getString(jsonObject, "comType");
            if(Config.GRID_BUILDER.equals(type)) {
                // 网格控件
                JSONArray childJSONArray = BuilderUtils.getArray(jsonObject, "childrens");
                buildItemView(childJSONArray, factory);
            }else {
                ViewBuilder builder = factory.makeViewBuilder(type);
                if(builder == null) {
                    continue;
                }
                mPageView.addView(builder.buildView(jsonObject, mPageView));
                mPageView.addView(getBlankLine());
            }
        }
    }

    /**
     * 获取分割线
     */
    private View getBlankLine() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, StringUtils.dip2px(mViewLoader.context(), 1));
        View space = new View(mViewLoader.context());
        space.setLayoutParams(params);
        space.setBackgroundColor(Color.parseColor("#E0E0E0"));
        return space;
    }

    @Override
    public View getPageView() {
        return mPageView;
    }

}
