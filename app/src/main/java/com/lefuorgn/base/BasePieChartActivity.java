package com.lefuorgn.base;

import android.graphics.Color;
import android.graphics.Typeface;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.lefuorgn.gov.bean.GraphicsData;

import java.util.ArrayList;
import java.util.List;

public abstract class BasePieChartActivity extends BaseChartActivity {

    public void initChart(String centerText,PieChart mChart) {
        mChart.setDrawCenterText(true);
        mChart.setDrawSliceText(false);
        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        mChart.setCenterText(centerText);
        mChart.setDescription("");
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);
        Legend l = mChart.getLegend();
        l.setXEntrySpace(10f);
        l.setYEntrySpace(10f);
        l.setYOffset(0f);
        l.setXOffset(30f);
        l.setEnabled(true);
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
        mChart.animateXY(1000, 1000);
    }
    String[] colorSets={"#FE4678","#47CBFF","#FFC856","#2ACACD","#9DD758","#FF9229","#34C1E2","#FE4678"};
    public void setData(List<GraphicsData> datas, String lable,PieChart mChart){
        ArrayList<Entry> yVals=new ArrayList<Entry>();
        List<String> xVals=new ArrayList<String>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        int total = 0;
        float leavePercent=1;
        for(int i=0;i<datas.size();i++){
            total+=datas.get(i).getY();
            xVals.add(datas.get(i).getName()+"   "+(int)datas.get(i).getY()+"人");
            try{
                colors.add(Color.parseColor(datas.get(i).getColor()));
            }catch(Exception e){
                colors.add(Color.parseColor(colorSets[i%(colorSets.length-1)]));
            }
        }
        for(int i=0;i<datas.size();i++){
            if(i!=(datas.size()-1)){
                float f=(float)((float)datas.get(i).getY()/total);
                leavePercent=leavePercent-f;
                yVals.add(new Entry(f,i));
            }else{
                yVals.add(new Entry(leavePercent,i));
            }
        }
        PieDataSet pieDataSet=new PieDataSet(yVals, lable);
        pieDataSet.setColors(colors);
        PieData pieData=new PieData(xVals, pieDataSet);
        mChart.setData(pieData);
        mChart.animateXY(1000,1000);
    }
}
