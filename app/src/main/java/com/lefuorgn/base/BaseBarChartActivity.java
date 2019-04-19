package com.lefuorgn.base;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.lefuorgn.gov.bean.GraphicsData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuting on 2017/1/5.
 */

public abstract class BaseBarChartActivity extends BaseChartActivity{
    public void initChart(BarChart barChart){
        barChart.setDescription("");
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        //xAxis.setSpaceBetweenLabels(80);
        //xAxis.setSpaceBetweenLabels(2);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);
        Legend l = barChart.getLegend();
        l.setEnabled(false);
    }
    String[] colorSets={"#FE4678","#cc66ff","3399ff","#47CBFF","#FFC856","#2ACACD","#9DD758","#FF9229","#34C1E2","#FE4678"};
    public void setData(List<GraphicsData> datas, String lable, BarChart barChart){
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int i = 0; i < datas.size(); i++) {
            xVals.add(datas.get(i).getName());
            yVals1.add(new BarEntry((int)datas.get(i).getY(), i));
            try{
                colors.add(Color.parseColor(datas.get(i).getColor()));
            }catch(Exception e){
                colors.add(Color.parseColor(colorSets[i%(colorSets.length-1)]));
            }
        }
        BarDataSet set1 = new BarDataSet(yVals1, lable);
        set1.setColors(colors);
        set1.setBarSpacePercent(35f);
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);
        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
        barChart.setData(data);
        barChart.animateXY(1000,1000);
    }
}
