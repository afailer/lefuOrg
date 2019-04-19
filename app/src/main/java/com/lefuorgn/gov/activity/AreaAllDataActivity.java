package com.lefuorgn.gov.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.GovApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.base.BaseChartActivity;
import com.lefuorgn.gov.adapter.AreaDataAdapter;
import com.lefuorgn.gov.adapter.AreaDataPagerAdapter;
import com.lefuorgn.gov.bean.AgencyCount;
import com.lefuorgn.gov.bean.AllDataItem;
import com.lefuorgn.gov.bean.GovAgencyMsg;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AreaAllDataActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private AreaDataAdapter mDataAdapter;
    private ViewPager mViewPager;
    private String agencyIds;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_area_alldata;
    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.areaAllDataRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
        mViewPager = (ViewPager) findViewById(R.id.area_pager);
    }

    @Override
    protected void initData() {
        agencyIds = getIntent().getStringExtra("agencyIds");
        setToolBarTitle("辖区数据");
        setMenuImageView(R.mipmap.search_org);
        loadAreaData();
    }

    /**
     * 加载辖区数据
     */
    private void loadAreaData() {
        showWaitDialog();
        GovApi.getAreaData(agencyIds, new RequestCallback<GovAgencyMsg>() {
            @Override
            public void onSuccess(GovAgencyMsg result) {
                hideWaitDialog();
                setData(getAllDataItem(result));
            }

            @Override
            public void onFailure(ApiHttpException e) {
                hideWaitDialog();
                showToast(e.getMessage());
                setData(new ArrayList<AllDataItem>());
            }
        });
    }

    /**
     * 设置列表数据
     * @param data 数据集合
     */
    private void setData(List<AllDataItem> data) {
        if(mDataAdapter == null) {
            mDataAdapter = new AreaDataAdapter(R.layout.all_data_item, data);
            mRecyclerView.setAdapter(mDataAdapter);
            mDataAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {
                    AllDataItem item = mDataAdapter.getItem(i);
                    startChartActivity(item);
                }
            });
        }else {
            mDataAdapter.setNewData(data);
        }
        AreaDataPagerAdapter pagerAdapter = new AreaDataPagerAdapter(data) {
            @Override
            public void skipChartActivity(AllDataItem item) {
                startChartActivity(item);
            }
        };
        mViewPager.setAdapter(pagerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 800 && data != null) {
            String extra = data.getStringExtra("ids");
            if(!StringUtils.isEmpty(extra)) {
                agencyIds =data.getStringExtra("ids");
                loadAreaData();
            }
        }
    }

    @Override
    protected void onMenuClick(View v) {
        startActivityForResult(new Intent(getApplicationContext(), GovOrganizationActivity.class),600);
    }

    /**
     * 跳转到指定的页面
     * @param item 当前条目内容
     */
    public void startChartActivity(AllDataItem item){
        if(item.getClazz() != null){
            Intent intent=new Intent(AppContext.getInstance(), item.getClazz());
            intent.putExtra(BaseChartActivity.IS_SHOW_SEARCH,false);
            intent.putExtra(BaseChartActivity.INTENT_TYPE_AGENCY, agencyIds);
            startActivity(intent);
        }else{
            startActivityForResult(new Intent(getApplicationContext(), GovOrganizationActivity.class),600);
        }
    }

    /**
     * 转换数据
     */
    private List<AllDataItem> getAllDataItem(GovAgencyMsg agencyMsg){
        List<AllDataItem> result = new ArrayList<AllDataItem>();
        AgencyCount agencyCount = agencyMsg.getAgencyCountBean();
        // 机构总量
        AllDataItem orgTotal=new AllDataItem();
        orgTotal.setType("机构总量");
        orgTotal.setValue(agencyIds.split(",").length + "");
        orgTotal.setIcon(R.mipmap.gov_org_total);
        orgTotal.setTime(agencyCount.getCreat_time());
        orgTotal.setCountName("机构统计");
        result.add(orgTotal);
        //床位总数
        AllDataItem bedTotal=new AllDataItem();
        bedTotal.setType("床位总数");
        bedTotal.setValue(agencyCount.getBed_total() + "");
        bedTotal.setIcon(R.mipmap.gov_org_allbed);
        bedTotal.setTime(agencyCount.getCreat_time());
        bedTotal.setClazz(ChartUseRateActivity.class);
        result.add(bedTotal);
        //剩余床位
        AllDataItem leftBedNum=new AllDataItem();
        leftBedNum.setType("剩余床位");
        leftBedNum.setIcon(R.mipmap.gov_bed_left);
        leftBedNum.setTime(agencyCount.getCreat_time());
        leftBedNum.setValue(agencyCount.getBed_surplus() + "");
        leftBedNum.setClazz(ChartUseRateActivity.class);
        result.add(leftBedNum);
        //老人总量
        AllDataItem oldTotal=new AllDataItem();
        oldTotal.setType("老人总量");
        oldTotal.setIcon(R.mipmap.gov_old);
        oldTotal.setValue((agencyCount.getMen() + agencyCount.getWomen()) + "");
        oldTotal.setTime(agencyCount.getCreat_time());
        oldTotal.setCountName("老人统计");
        oldTotal.setClazz(ChartAgeActivity.class);
        result.add(oldTotal);
        //男女比例
        AllDataItem menRate=new AllDataItem();
        menRate.setType("男女比例");
        menRate.setIcon(R.mipmap.gov_sex_rate);
        menRate.setValue(getSimpleVal(agencyCount.getMen(), agencyCount.getWomen()));
        menRate.setTime(agencyCount.getCreat_time());
        menRate.setClazz(ChartSexRatioActivity.class);
        result.add(menRate);
        return result;
    }

    public String getSimpleVal(double a, double b){
        if(a == 0) {
            return "1:0";
        }
        double temp = b/a;
        String string = new DecimalFormat("#0.00").format(temp);
        return "1:" + string;
    }

    @Override
    protected boolean hasToolBar() {
        return true;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

}
