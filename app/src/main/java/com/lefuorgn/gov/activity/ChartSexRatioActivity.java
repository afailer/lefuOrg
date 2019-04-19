package com.lefuorgn.gov.activity;

import com.github.mikephil.charting.charts.PieChart;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.GovApi;
import com.lefuorgn.base.BasePieChartActivity;
import com.lefuorgn.gov.bean.OldMsg;

public class ChartSexRatioActivity extends BasePieChartActivity {

    PieChart sexPieChart;

    @Override
    protected void initView() {
        super.initView();
        sexPieChart= (PieChart) findViewById(R.id.sex_chart);
        setToolBarTitle("男女比例");
        initChart("男女比例",sexPieChart);
        initChartBelowView("男性","女性","老人总量");
        initChartTimeView();

    }

    @Override
    public void getChartData(Long time) {
        GovApi.getSexAgeDieases(time, agencys, new RequestCallback<OldMsg>() {
            @Override
            public void onSuccess(OldMsg result) {
                if(result!=null) {
                    setData(result.getSex(), "男女比例", sexPieChart);
                    setChartBelowValue(((int) result.getSex().get(0).getY()) + "", ((int) result.getSex().get(1).getY()) + "", (((int) result.getSex().get(0).getY()) + ((int) result.getSex().get(1).getY())) + "");
                }
            }

            @Override
            public void onFailure(ApiHttpException e) {

            }
        });
    }

    @Override
    public String getChartName() {
        return "男女比例";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chart_sex_ratio;
    }
}
