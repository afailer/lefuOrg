package com.lefuorgn.gov.activity;

import com.github.mikephil.charting.charts.BarChart;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.GovApi;
import com.lefuorgn.base.BaseBarChartActivity;
import com.lefuorgn.gov.bean.OldMsg;

public class ChartDiseaseActivity extends BaseBarChartActivity{

    BarChart barChart;

    @Override
    protected void initView() {
        super.initView();
        setToolBarTitle("慢病统计");
        barChart= (BarChart) findViewById(R.id.chart_disease);
        initChart(barChart);
        initChartTimeView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chart_disease;
    }

    @Override
    public void getChartData(Long time) {
        GovApi.getSexAgeDieases(time, agencys, new RequestCallback<OldMsg>() {
            @Override
            public void onSuccess(OldMsg result) {
                if(result!=null) {
                    setData(result.getDiseases(), "慢病统计", barChart);
                }
            }

            @Override
            public void onFailure(ApiHttpException e) {

            }
        });
    }

    @Override
    public String getChartName() {
        return "慢病统计";
    }

}
