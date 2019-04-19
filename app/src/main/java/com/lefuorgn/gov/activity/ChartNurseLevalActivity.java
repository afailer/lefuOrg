package com.lefuorgn.gov.activity;


import com.github.mikephil.charting.charts.BarChart;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.GovApi;
import com.lefuorgn.base.BaseBarChartActivity;
import com.lefuorgn.gov.bean.NurseLevel;

public class ChartNurseLevalActivity extends BaseBarChartActivity{
    BarChart barChart;

    @Override
    protected void initView() {
        super.initView();
        setToolBarTitle("护理级别");
        barChart= (BarChart) findViewById(R.id.chart_nurseLevel);
        initChart(barChart);
        initChartTimeView();
    }

    @Override
    public void getChartData(Long time) {
        GovApi.getNurseLevel(agencys, time, new RequestCallback<NurseLevel>() {
            @Override
            public void onSuccess(NurseLevel result) {
                if(result!=null){
                    setData(result.getNuring(),"护理级别",barChart);
                }
            }

            @Override
            public void onFailure(ApiHttpException e) {

            }
        });
    }

    @Override
    public String getChartName() {
        return "护理级别";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chart_nurse_leval;
    }
}
