package com.lefuorgn.bean;

import android.graphics.Typeface;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.github.mikephil.charting.data.ChartData;

/**
 * chart-listview items
 */

public abstract class ChartItem extends MultiItemEntity {

    public static final int TYPE_BARCHART = 0;
    public static final int TYPE_PIECHART = 1;

    protected ChartData<?> mChartData;
    protected String chartText;

    public ChartItem(ChartData<?> cd, String chartText) {
        super();
        this.mChartData = cd;
        this.chartText = chartText;
    }

    public ChartData<?> getChartData() {
        return mChartData;
    }

    /**
     * 获取当前视图的标题
     * @return 标题
     */
    public String getChartItemTitle() {
        return chartText;
    }

    /**
     * 获取当前视图类型
     * @return 类型
     */
    public abstract Typeface getTypeface();

}
