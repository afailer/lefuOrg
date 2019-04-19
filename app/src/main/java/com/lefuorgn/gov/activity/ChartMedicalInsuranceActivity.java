package com.lefuorgn.gov.activity;

import com.github.mikephil.charting.charts.PieChart;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.GovApi;
import com.lefuorgn.base.BasePieChartActivity;
import com.lefuorgn.gov.bean.MedicalMsg;

public class ChartMedicalInsuranceActivity extends BasePieChartActivity {

    PieChart ownChart,medicalChart;

    @Override
    protected void initView() {
        super.initView();
        ownChart = (PieChart) findViewById(R.id.own_chart);
        medicalChart= (PieChart) findViewById(R.id.medical_chart);
        setToolBarTitle("医保比例");
        initChart("医保比例",ownChart);
        initChart("",medicalChart);
        //initChartBelowView("男性","女性","老人总量");
        initChartTimeView();
    }

    @Override
    public void getChartData(Long time) {
        GovApi.getInsuranceRate(agencys, time, new RequestCallback<MedicalMsg>() {
            @Override
            public void onSuccess(MedicalMsg result) {
                if(result!=null) {
                    setData(result.getIsInsurance(), "", ownChart);
                    setData(result.getInsuranceType(), "", medicalChart);
                    initChartBelowView(result.getIsInsurance().get(0).getName(), result.getIsInsurance().get(1).getName(), "老人总量");
                    setChartBelowValue((int) result.getIsInsurance().get(0).getY() + "", (int) result.getIsInsurance().get(1).getY() + "", (int) (result.getIsInsurance().get(0).getY() + result.getIsInsurance().get(1).getY()) + "");
                }
            }

            @Override
            public void onFailure(ApiHttpException e) {

            }
        });
    }

    @Override
    public String getChartName() {
        return "医保比例";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chart_medical_insurance;
    }
}
