package com.lefuorgn.lefu.bean;

import android.content.Context;
import android.graphics.Typeface;

import com.github.mikephil.charting.data.ChartData;
import com.lefuorgn.bean.ChartItem;

/**
 * 柱形图数据展示适配器
 */

public class BarChartItem extends ChartItem {

    private Typeface mTf;

    public BarChartItem(ChartData<?> cd, String charText, Context context) {
        super(cd, charText);
        mTf = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
    }

    @Override
    public int getItemType() {
        return TYPE_BARCHART;
    }

    @Override
    public Typeface getTypeface() {
        return mTf;
    }

}
