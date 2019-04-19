package com.lefuorgn.gov.activity;

import com.github.mikephil.charting.charts.PieChart;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.GovApi;
import com.lefuorgn.base.BasePieChartActivity;
import com.lefuorgn.gov.bean.OldMsg;

public class ChartAgeActivity extends BasePieChartActivity {

    PieChart ageChart;

    @Override
    protected void initView() {
        super.initView();
        setToolBarTitle("年龄结构");
        ageChart= (PieChart) findViewById(R.id.age_chart);
        initChart("",ageChart);
        initChartTimeView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chart_age;
    }

    @Override
    public void getChartData(Long time) {
        GovApi.getSexAgeDieases(time, agencys, new RequestCallback<OldMsg>() {
            @Override
            public void onSuccess(OldMsg result) {
                if(result!=null) {
                    setData(result.getAge(), "年龄结构", ageChart);
                }
            }

            @Override
            public void onFailure(ApiHttpException e) {

            }
        });
    }

    @Override
    public String getChartName() {
        return "年龄结构";
    }
}
