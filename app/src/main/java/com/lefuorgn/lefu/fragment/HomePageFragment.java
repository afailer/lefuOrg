package com.lefuorgn.lefu.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.AppConfig;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.base.BaseChartActivity;
import com.lefuorgn.base.BaseFragment;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.db.util.SignConfigManager;
import com.lefuorgn.dialog.ListDialog;
import com.lefuorgn.gov.activity.ChartAgeActivity;
import com.lefuorgn.gov.activity.ChartDiseaseActivity;
import com.lefuorgn.gov.activity.ChartMedicalInsuranceActivity;
import com.lefuorgn.gov.activity.ChartNurseLevalActivity;
import com.lefuorgn.gov.activity.ChartSexRatioActivity;
import com.lefuorgn.gov.activity.ChartUseRateActivity;
import com.lefuorgn.gov.bean.IconItem;
import com.lefuorgn.lefu.MainActivity;
import com.lefuorgn.lefu.activity.EventActivity;
import com.lefuorgn.lefu.activity.LocationDetailsActivity;
import com.lefuorgn.lefu.activity.NursingReportDetailsActivity;
import com.lefuorgn.lefu.activity.SynopsisDetailsActivity;
import com.lefuorgn.lefu.adapter.GridLayoutItemAdapter;
import com.lefuorgn.lefu.adapter.HomePageAdapter;
import com.lefuorgn.lefu.bean.GridLayoutItem;
import com.lefuorgn.lefu.util.GridLayoutItemUtils;
import com.lefuorgn.util.NetworkUtils;
import com.lefuorgn.util.TLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页页面
 */

public class HomePageFragment extends BaseFragment {

    // 机构名称
    private TextView mOrgNameView;
    private RecyclerView mRecyclerView;

    // 数据管理条目展示控件
    private RecyclerView mDataManagementView;
    // 移动办公条目展示控件
    private RecyclerView mMobileOfficeView;
    // 数据管理条目数据适配器
    private GridLayoutItemAdapter mDataManagementAdapter;
    // 移动办公条目数据适配器
    private GridLayoutItemAdapter mMobileOfficeAdapter;

    private List<DisplaySignOrNursingItem> mItems;
    // 护理月报dialog对象
    private ListDialog<DisplaySignOrNursingItem> mListDialog;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppConfig.INTENT_ACTION_NOTICE_CONFIG)) {
                // 刷新当前页面
                TLog.log("数据库数据配置放生变化");
                initAdapter();
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_page;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mOrgNameView = (TextView) view.findViewById(R.id.tv_fragment_home_page_org_name);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.rv_fragment_home_page_graphic_data);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        // 机构详情、机构活动、机构位置
        view.findViewById(R.id.tv_fragment_home_page_index_details).setOnClickListener(this);
        view.findViewById(R.id.tv_fragment_home_page_index_event).setOnClickListener(this);
        view.findViewById(R.id.tv_fragment_home_page_index_location).setOnClickListener(this);
        mDataManagementView = (RecyclerView) view.findViewById(R.id.rv_fragment_home_page_date_management);
        mDataManagementView.setLayoutManager(new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mMobileOfficeView = (RecyclerView) view.findViewById(R.id.rv_fragment_home_page_mobile_office);
        mMobileOfficeView.setLayoutManager(new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        // 注册刷新广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConfig.INTENT_ACTION_NOTICE_CONFIG);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    protected void initData() {
        mOrgNameView.setText(AppContext.getInstance().getAgencyName());
        final HomePageAdapter adapter = new HomePageAdapter(getIconItems(),false);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter quickAdapter, View view, int i) {
                IconItem iconItem = adapter.getItem(i);
                Intent intent = new Intent(getActivity(), iconItem.getClassName());
                intent.putExtra(BaseChartActivity.INTENT_TYPE_AGENCY, AppContext.getInstance().getAgencyId() + "");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        initAdapter();
    }

    /**
     * 初始化数据管理和移动办公控件条目或者刷新条目
     */
    private void initAdapter() {
        // 拿取护理信息配置项
        mItems = SignConfigManager.getNursingItem();
        TLog.log("获取配置文件中的配置 == " + mItems.toString());
        // 数据管理控件
        if(mDataManagementAdapter == null) {
            mDataManagementAdapter = new GridLayoutItemAdapter(GridLayoutItemUtils.getDataManagementItem());
            mDataManagementAdapter.setOnRecyclerViewItemClickListener(
                    new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {
                    GridLayoutItem item = mDataManagementAdapter.getItem(i);
                    if(!item.isPermission()) {
                        showToast(R.string.permission_no);
                        return;
                    }
                    if(item.isNetwork() && NetworkUtils.NETWORK_NONE == AppContext.getInstance().getNetState()) {
                        showToast("网络不可用");
                        return;
                    }
                    if(item.getCls() != null) {
                        Intent intent= new Intent(getActivity(), item.getCls());
                        startActivityForResult(intent, 100);
                    }else {
                        showNursingReportDialog();
                    }

                }
            });
            mDataManagementView.setAdapter(mDataManagementAdapter);
        }else {
            mDataManagementAdapter.setNewData(GridLayoutItemUtils.getDataManagementItem());
        }
        // 移动办公控件
        if(mMobileOfficeAdapter == null) {
            mMobileOfficeAdapter = new GridLayoutItemAdapter(GridLayoutItemUtils.getMobileOfficeItem());
            mMobileOfficeAdapter.setOnRecyclerViewItemClickListener(
                    new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {
                    GridLayoutItem item = mMobileOfficeAdapter.getItem(i);
                    if(!item.isPermission()) {
                        showToast(R.string.permission_no);
                        return;
                    }
                    if(item.isNetwork() && NetworkUtils.NETWORK_NONE == AppContext.getInstance().getNetState()) {
                        showToast("网络不可用");
                        return;
                    }
                    if(item.getCls() != null) {
                        Intent intent= new Intent(getActivity(), item.getCls());
                        startActivity(intent);
                    }else {
                        showToast(item.getErrorInfo());
                    }
                }
            });
            mMobileOfficeView.setAdapter(mMobileOfficeAdapter);
        }else {
            mMobileOfficeAdapter.setNewData(GridLayoutItemUtils.getMobileOfficeItem());
        }
    }

    private List<IconItem> getIconItems(){
        List<IconItem> iconItems=new ArrayList<IconItem>();
        iconItems.add(new IconItem(R.mipmap.home_chart_bed_use, "床位使用", ChartUseRateActivity.class));
        iconItems.add(new IconItem(R.mipmap.home_chart_age_structure, "年龄结构", ChartAgeActivity.class));
        iconItems.add(new IconItem(R.mipmap.home_chart_male_female_rate, "男女比例", ChartSexRatioActivity.class));
        iconItems.add(new IconItem(R.mipmap.home_chart_diease, "慢病统计", ChartDiseaseActivity.class));
        iconItems.add(new IconItem(R.mipmap.home_chart_nurse_level, "照护级别", ChartNurseLevalActivity.class));
        iconItems.add(new IconItem(R.mipmap.home_chart_medical, "医保比例", ChartMedicalInsuranceActivity.class));
        return iconItems;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_fragment_home_page_index_details :
                // 机构概况; 需要网络,不需要权限
                if(NetworkUtils.NETWORK_NONE == AppContext.getInstance().getNetState()) {
                    showToast("网络不可用");
                    return;
                }
                intent = new Intent(getActivity(), SynopsisDetailsActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_fragment_home_page_index_event :
                // 机构活动; 需要网络,不需要权限
                if(NetworkUtils.NETWORK_NONE == AppContext.getInstance().getNetState()) {
                    showToast("网络不可用");
                    return;
                }
                intent = new Intent(getActivity(), EventActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_fragment_home_page_index_location :
                // 机构位置; 需要网络,不需要权限
                if(NetworkUtils.NETWORK_NONE == AppContext.getInstance().getNetState()) {
                    showToast("网络不可用");
                    return;
                }
                intent = new Intent(getActivity(), LocationDetailsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 200) {
            // 这里进行数据同步
            ((MainActivity) getActivity()).syncBackground();
        }
    }

    private void showNursingReportDialog() {
        if(mListDialog == null) {
            mListDialog = new ListDialog<DisplaySignOrNursingItem>();
            mListDialog.setTitle("护理月报")
                    .setGravity(ListDialog.CENTER)
                    .setEmptyNote("服务器没有配置指定项");
            mListDialog.setCallBack(new ListDialog.Callback<DisplaySignOrNursingItem>() {
                @Override
                public void convert(TextView view, DisplaySignOrNursingItem item) {
                    view.setText(item.getTitle());
                }

                @Override
                public void onItemClick(View view, DisplaySignOrNursingItem item) {
                    Intent intent = new Intent(getActivity(), NursingReportDetailsActivity.class);
                    intent.putExtra("id", item.getType());
                    startActivity(intent);
                }
            });
        }
        mListDialog.setData(mItems);
        mListDialog.show(getFragmentManager(), "ListDialog");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }
}
