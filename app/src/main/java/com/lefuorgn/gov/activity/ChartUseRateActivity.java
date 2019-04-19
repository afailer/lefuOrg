package com.lefuorgn.gov.activity;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.GovApi;
import com.lefuorgn.base.BaseLineChartActivity;
import com.lefuorgn.bean.UseRateItem;
import com.lefuorgn.gov.adapter.ChartBedUseRateAdapter;
import com.lefuorgn.gov.bean.UseRate;
import com.lefuorgn.util.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class ChartUseRateActivity extends BaseLineChartActivity {
    LineChart chartUseRate,chartLiveNum;
    RecyclerView recyclerView;
    ChartBedUseRateAdapter adapter;
    TextView use,notUse;
    UseRate useRate;
    int currentUseSet;
    @Override
    protected void initView() {
        super.initView();
        setToolBarTitle("床位使用");
        chartUseRate= (LineChart) findViewById(R.id.live_rate_chart);
        chartLiveNum= (LineChart) findViewById(R.id.live_num_chart);
        recyclerView= (RecyclerView) findViewById(R.id.use_rate_recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(LinearLayoutManager.VERTICAL, Color.parseColor("#EFEFEF")));
        adapter=new ChartBedUseRateAdapter(R.layout.chart_recycle_item,new ArrayList<UseRateItem>());
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);
        use= (TextView) findViewById(R.id.use);
        notUse= (TextView) findViewById(R.id.notUse);
        use.setOnClickListener(this);
        notUse.setOnClickListener(this);
        initChartBelowView("床位","剩余","床位入住率");
        initChart(chartUseRate);
        initChart(chartLiveNum);
        initChartTimeView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chart_use_rate;
    }


    @Override
    public void getChartData(Long time) {
        showWaitDialog();
        GovApi.getBedUseRate(time, agencys, new RequestCallback<UseRate>() {
            @Override
            public void onSuccess(UseRate result) {
                if(result!=null) {
                    useRate = result;
                    hideWaitDialog();
                    setChartBelowValue(((int) result.getBedTotal().get(result.getBedTotal().size() - 1).getY()) + "", ((int) result.getBedSurplus().get(result.getBedTotal().size() - 1).getY()) + "", result.getBedProportion().get(result.getBedTotal().size() - 1).getY() + "%");
                    setData(result.getBedProportion(), "床位入住率图示", chartUseRate);
                    setData(result.getBedUse(), "床位使用数量", chartLiveNum);
                    adapter.getData().clear();

                    adapter.addData(getUseRateItem(result));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(ApiHttpException e) {
                hideWaitDialog();
            }
        });
    }

    private List<UseRateItem> getUseRateItem(UseRate useRate){
        List<UseRateItem> useRateItems=new ArrayList<UseRateItem>();
        for(int i=useRate.getBedProportion().size()-1;i>0;i--){
            UseRateItem useRateitem=new UseRateItem();
            useRateitem.setTime(useRate.getBedProportion().get(i).getName());
            useRateitem.setBedLiveRate(useRate.getBedProportion().get(i).getY()+"%");
            useRateitem.setBedLeftNum((int)useRate.getBedSurplus().get(i).getY()+"");
            useRateitem.setBedTotalNum((int)useRate.getBedTotal().get(i).getY()+"");
            useRateItems.add(useRateitem);
        }
        return useRateItems;
    }

    @Override
    public String getChartName() {
        return "床位入住率图示";
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.use :
                if(currentUseSet==R.id.use){
                    return;
                }
                currentUseSet=R.id.use;
                use.setBackgroundColor(Color.parseColor("#b6e1f2"));
                use.setTextColor(Color.parseColor("#FFFFFF"));
                notUse.setBackgroundColor(Color.parseColor("#F7F7F7"));
                notUse.setTextColor(Color.parseColor("#b6e1f2"));
                setData(useRate.getBedUse(), "床位使用数量",chartLiveNum);
                break;
            case R.id.notUse:
                if(currentUseSet==R.id.notUse){
                    return;
                }
                currentUseSet=R.id.notUse;
                notUse.setBackgroundColor(Color.parseColor("#b6e1f2"));
                notUse.setTextColor(Color.parseColor("#FFFFFF"));
                use.setBackgroundColor(Color.parseColor("#F7F7F7"));
                use.setTextColor(Color.parseColor("#b6e1f2"));
                setData(useRate.getBedSurplus(),"床位使用数量",chartLiveNum);
                break;
        }
    }
}
