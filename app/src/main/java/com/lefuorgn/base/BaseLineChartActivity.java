package com.lefuorgn.base;

import android.graphics.Color;
import android.graphics.LinearGradient;

import com.baidu.platform.comapi.map.B;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.lefuorgn.gov.bean.GraphicsData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuting on 2016/12/22.
 */

public abstract class BaseLineChartActivity extends BaseChartActivity{

    public void initChart(LineChart mChart){
        mChart.setDescription("");
        mChart.setGridBackgroundColor(Color.parseColor("#FFFFFF"));
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        YAxis axisRight = mChart.getAxisRight();
        axisRight.setEnabled(false);
    }
    public void setData(List<GraphicsData> datas, String lable,LineChart mChart){
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < datas.size(); i++) {
            xVals.add(datas.get(i).getName());
        }
        for (int i = 0; i < datas.size(); i++) {
            yVals.add(new Entry((float) datas.get(i).getY(), i));
        }
        LineDataSet set1 = new LineDataSet(yVals,lable);
        set1.enableDashedLine(10f, 5f, 0f);
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.parseColor("#35aeff"));
        set1.setCircleColor(Color.parseColor("#1495eb"));
        set1.setLineWidth(1f);
        set1.setCircleSize(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.BLACK);
        set1.setDrawFilled(true);
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets
        LineData data = new LineData(xVals, dataSets);
        mChart.setData(data);
        mChart.animateXY(1000,1000);
    }
}