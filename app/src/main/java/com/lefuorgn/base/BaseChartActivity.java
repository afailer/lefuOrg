package com.lefuorgn.base;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.gov.activity.GovOrganizationActivity;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;


public abstract class BaseChartActivity extends BaseActivity {

    public static final String INTENT_TYPE_AGENCY = "intent_type_agency";
    public static final String IS_SHOW_SEARCH="isShowSearch";
    long time=System.currentTimeMillis();
    long monthTime=2592000000l;
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM月");
    public abstract void getChartData(Long time);
    TextView cbT1,cbT2,cbT3,cbT1Val,cbT2Val,cbT3Val,chartName;
    public String agencys;
    protected void initChartTimeView(){
        agencys = getIntent().getStringExtra(INTENT_TYPE_AGENCY);
        chartName= (TextView) findViewById(R.id.chartName);
        setChartName();
        boolean b = getIntent().getBooleanExtra(IS_SHOW_SEARCH, false);
        if(b){
            setMenuImageView(R.mipmap.search_org);
        }
        final TextView chartTime= (TextView)findViewById(R.id.chart_time);
        chartTime.setText(sdf.format(new Date(time)));
        getChartData(time);
        findViewById(R.id.chartLastMonthData).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                time=time-monthTime;
                chartTime.setText(sdf.format(new Date(time)));
                getChartData(time);
            }
        });
        findViewById(R.id.chartNextMonthData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canLoadData()){
                    time=time+monthTime;
                    chartTime.setText(sdf.format(new Date(time)));
                    getChartData(time);
                }else{
                    ToastUtils.show(getApplicationContext(),"已是最新数据");
                }
            }
        });
    }
    protected boolean canLoadData(){
        if(time+86400000>System.currentTimeMillis()){
            return false;
        }else{
            return true;
        }
    }
    @Override
    protected boolean hasToolBar() {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 800 && data != null) {
            String extra = data.getStringExtra("ids");
            if(!StringUtils.isEmpty(extra)) {
                agencys = data.getStringExtra("ids");
                getChartData(time);
            }
        }
    }

    @Override
    protected void onMenuClick(View v) {
        startActivityForResult(new Intent(getApplicationContext(), GovOrganizationActivity.class),600);
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }
    private void setChartName(){
        chartName.setText(getChartName());
    }
    public abstract String getChartName();
    protected void initChartBelowView(String cbt1Name,String cbt2Name,String cbt3Name){
        cbT1= (TextView) findViewById(R.id.cb_t1);
        cbT1Val= (TextView)findViewById(R.id.cb_t1_val);
        cbT2= (TextView)findViewById(R.id.cb_t2);
        cbT2Val= (TextView)findViewById(R.id.cb_t2_val);
        cbT3= (TextView)findViewById(R.id.cb_t3);
        cbT3Val= (TextView)findViewById(R.id.cb_t3_val);
        cbT1.setText(cbt1Name);
        cbT2.setText(cbt2Name);
        cbT3.setText(cbt3Name);
    }
    protected void setChartBelowValue(String Val1,String Val2,String Val3){
        cbT1Val.setText(Val1);
        cbT2Val.setText(Val2);
        cbT3Val.setText(Val3);
    }
}
