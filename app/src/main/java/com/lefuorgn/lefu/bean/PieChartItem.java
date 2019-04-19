package com.lefuorgn.lefu.bean;

import android.content.Context;
import android.graphics.Typeface;

import com.github.mikephil.charting.data.ChartData;
import com.lefuorgn.bean.ChartItem;

/**
 * 圆形图数据展示适配器
 */

public class PieChartItem extends ChartItem {

    private Typeface mTf;

    public PieChartItem(ChartData<?> cd, String charText, Context context) {
        super(cd, charText);
        mTf = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
    }

    @Override
    public int getItemType() {
        return TYPE_PIECHART;
    }

    @Override
    public Typeface getTypeface() {
        return mTf;
    }

}
