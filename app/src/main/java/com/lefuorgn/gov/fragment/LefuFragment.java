package com.lefuorgn.gov.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.GovApi;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.base.BaseChartActivity;
import com.lefuorgn.base.BaseFragment;
import com.lefuorgn.bean.User;
import com.lefuorgn.gov.GovMainActivity;
import com.lefuorgn.gov.activity.AllNewsActivity;
import com.lefuorgn.gov.activity.AllOrgActiveActivity;
import com.lefuorgn.gov.activity.AreaAllDataActivity;
import com.lefuorgn.gov.activity.ChartAgeActivity;
import com.lefuorgn.gov.activity.ChartDiseaseActivity;
import com.lefuorgn.gov.activity.ChartMedicalInsuranceActivity;
import com.lefuorgn.gov.activity.ChartNurseLevalActivity;
import com.lefuorgn.gov.activity.ChartSexRatioActivity;
import com.lefuorgn.gov.activity.ChartUseRateActivity;
import com.lefuorgn.gov.activity.ChoseOrganizationActivity;
import com.lefuorgn.gov.activity.OrgActiveActivity;
import com.lefuorgn.gov.activity.OrgNewsActivity;
import com.lefuorgn.gov.adapter.ActiveAdapter;
import com.lefuorgn.gov.adapter.NewsAdapter;
import com.lefuorgn.gov.bean.IconItem;
import com.lefuorgn.gov.bean.LeaderNews;
import com.lefuorgn.gov.bean.MessageInfo;
import com.lefuorgn.gov.bean.OrgActive;
import com.lefuorgn.lefu.adapter.HomePageAdapter;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.UpdateUtils;
import com.lefuorgn.widget.CircleImageView;
import com.lefuorgn.widget.MarqueeTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 政府页面主页
 */

public class LefuFragment extends BaseFragment{

    private CircleImageView mHeadView; // 机构的图片
    private TextView mNameView, mDateView; // 机构的名字; 当前日期
    private MarqueeTextView mMarqueeView; // 跑马灯控件
    private RecyclerView mChartRecycleView; // 机构信息展示控件

    private ImageView mNoticeView; // 新信息提示控件

    // 集团活动信息展示控件; 政府活动信息展示控件
    private LinearLayout mGroupModel, mGovModel;
    // 集团新闻信息展示控件; 政府新闻信息展示孔家
    private RecyclerView mGroupNewsRecycleView, mGovNewsRecycleView;
    // 集团新闻信息适配器
    private ActiveAdapter mGroupActivityAdapter;
    // 政府新闻信息适配器
    private NewsAdapter mGovNewsAdapter;
    // 当前用户信息
    private User mUser;

    @Override
    protected int getLayoutId() {
        return R.layout.gov_lefu_fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        // 获取用户信息
        mUser = AppContext.getInstance().getUser();
        mHeadView = (CircleImageView) view.findViewById(R.id.iv_fragment_gov_head);
        mNameView = (TextView) view.findViewById(R.id.tv_fragment_gov_name);
        mDateView = (TextView) view.findViewById(R.id.tv_fragment_gov_date);
        view.findViewById(R.id.rl_fragment_gov_seek).setOnClickListener(this); // 查找机构
        mMarqueeView = (MarqueeTextView) view.findViewById(R.id.tv_fragment_gov_marquee);
        mChartRecycleView = (RecyclerView) view.findViewById(R.id.rv_fragment_gov_chart);

        mNoticeView = (ImageView) view.findViewById(R.id.iv_fragment_gov_notice);
        view.findViewById(R.id.rl_fragment_gov_area_data).setOnClickListener(this); // 辖区数据
        view.findViewById(R.id.rl_fragment_gov_area_member).setOnClickListener(this); // 辖区成员
        view.findViewById(R.id.rl_fragment_gov_circular).setOnClickListener(this); // 通知公告

        // 集团新闻模块
        mGroupModel = (LinearLayout) view.findViewById(R.id.ll_fragment_gov_group_model);
        // 全部活动(机构活动)
        view.findViewById(R.id.tv_fragment_gov_group_navigation).setOnClickListener(this);
        mGroupNewsRecycleView = (RecyclerView) view.findViewById(R.id.rv_fragment_gov_group_news);
        mGroupNewsRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mGroupNewsRecycleView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));

        // 政府新闻模块
        mGovModel= (LinearLayout) view.findViewById(R.id.ll_fragment_gov_gov_model);
        // 全部活动(民政生活)
        view.findViewById(R.id.tv_fragment_gov_gov_navigation).setOnClickListener(this);
        mGovNewsRecycleView= (RecyclerView) view.findViewById(R.id.rv_fragment_gov_gov_news);
        mGovNewsRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mGovNewsRecycleView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));

    }

    @Override
    protected void initData() {
        // 加载用户头像
        ImageLoader.loadImgForUserDefinedView(mUser.getIcon(), mHeadView);
        mDateView.setText(StringUtils.getFormatData(System.currentTimeMillis(),"MM/dd"));
        // 初始化机构信息展示控件
        mChartRecycleView.setLayoutManager(new GridLayoutManager(getContext(),4));
        mChartRecycleView.addItemDecoration(
                new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST, Color.parseColor("#EFEFEF")));
        final HomePageAdapter adapter = new HomePageAdapter(getChartItems(),true);
        mChartRecycleView.setAdapter(adapter);
        mNameView.setText(((GovMainActivity) getActivity()).getName());
        adapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter quickAdapter, View view, int i) {
                IconItem iconItem = adapter.getItem(i);
                if(iconItem.getClassName() == null) {
                    return;
                }
                Intent intent = new Intent(getActivity(), iconItem.getClassName());
                intent.putExtra(BaseChartActivity.IS_SHOW_SEARCH,true);
                intent.putExtra(BaseChartActivity.INTENT_TYPE_AGENCY, ((GovMainActivity) getActivity()).getAgencyIds());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        // 初始化通告信息(未读信息)
        GovApi.getNotice(1, 1, 1, new RequestCallback<List<MessageInfo>>() {
            @Override
            public void onSuccess(List<MessageInfo> result) {
                if(result.size()>0){
                    // 存在未读信息
                    mNoticeView.setVisibility(View.VISIBLE);
                    mMarqueeView.setText(result.get(0).getTheme());
                    // 提示红色图标(存在未读状态的数据)
                    ((GovMainActivity) getActivity()).setRedDotState(true);
                }else{
                    // 没有未读信息(获取已读信息)
                    getNoticeAll();
                }
            }
            @Override
            public void onFailure(ApiHttpException e) {
                // 未读信息获取失败
                mNoticeView.setVisibility(View.INVISIBLE);
                getNoticeAll();
            }
        });
        // 判断当前用户类型, 根据类型加载数据
        if(mUser.isGov()) {
            // 政府账号
            mGroupModel.setVisibility(View.GONE);
            mGovNewsAdapter = new NewsAdapter(R.layout.gov_item, new ArrayList<LeaderNews>());
            mGovNewsRecycleView.setAdapter(mGovNewsAdapter);
            // 政府新闻
            GovApi.getOrgNews(mUser.getGovOrg_id(), 0, 0, 2, new RequestCallback<List<LeaderNews>>() {
                @Override
                public void onSuccess(List<LeaderNews> result) {
                    if(result.size()>0){
                        mGovNewsAdapter.add(0, result.get(0));
                    }
                    if(result.size()>1){
                        mGovNewsAdapter.add(1, result.get(1));
                    }
                }

                @Override
                public void onFailure(ApiHttpException e) {

                }
            });
        }else {
            // 集团账号
            mGovModel.setVisibility(View.GONE);
            mGroupActivityAdapter = new ActiveAdapter(R.layout.gov_item, new ArrayList<OrgActive>());
            mGroupNewsRecycleView.setAdapter(mGroupActivityAdapter);
            // 集团活动
            GovApi.getOrgActivities(mUser.getGroupOrg_id(), 1, 2, new RequestCallback<List<OrgActive>>() {
                @Override
                public void onSuccess(List<OrgActive> result) {
                    mGroupActivityAdapter.setNewData(result);
                }

                @Override
                public void onFailure(ApiHttpException e) {

                }
            });
        }
        initAdapterListener();
        new UpdateUtils((BaseActivity) getActivity(), false).checkUpdate();
    }

    /**
     * 获取机构信息展示控件信息
     */
    public List<IconItem> getChartItems() {
        List<IconItem> iconItems=new ArrayList<IconItem>();
        iconItems.add(new IconItem(R.mipmap.gov_bed_utilization,"床位使用",ChartUseRateActivity.class));
        iconItems.add(new IconItem(R.mipmap.gov_age_classify,"年龄结构",ChartAgeActivity.class));
        iconItems.add(new IconItem(R.mipmap.gov_male_female_ratio,"男女比例",ChartSexRatioActivity.class));
        iconItems.add(new IconItem(R.mipmap.gov_chronic_disease_statistics,"慢病统计",ChartDiseaseActivity.class));
        iconItems.add(new IconItem(R.mipmap.gov_nursing_level,"照护级别", ChartNurseLevalActivity.class));
        iconItems.add(new IconItem(R.mipmap.gov_medical_insurance_ratio,"医保比例",ChartMedicalInsuranceActivity.class));
        iconItems.add(new IconItem(R.mipmap.gray_nurse_rate,"照护比例",null));
        iconItems.add(new IconItem(R.mipmap.gray_org_type,"机构性质",null));
        return iconItems;
    }

    private void initAdapterListener() {
        if(mUser.isGov()) {
            mGovNewsAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {
                    LeaderNews leaderNews = (LeaderNews) mGovNewsAdapter.getData().get(i);
                    String url= "lefuyun/leaderNewsCtr/toInfoPage"+"?id="+leaderNews.getId()+"&uid="+ mUser.getUser_id();
                    Intent intent=new Intent(getActivity(),OrgNewsActivity.class);
                    intent.putExtra("url",url);
                    intent.putExtra("extra",leaderNews);
                    startActivity(intent);
                }
            });
        }else{
            mGroupActivityAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {
                    OrgActive orgActive = (OrgActive) mGroupActivityAdapter.getData().get(i);
                    String url="lefuyun/agencyActivites/toInfoPage"+"?id="+orgActive.getId()+"&version=160427";
                    Intent intent=new Intent(getActivity(),OrgActiveActivity.class);
                    intent.putExtra("url",url);
                    intent.putExtra("extra",orgActive);
                    startActivity(intent);
                }
            });
        }

    }

    /**
     * 不存在未读信息, 则读取已读信息
     */
    public void getNoticeAll() {
        mNoticeView.setVisibility(View.INVISIBLE);
        GovApi.getNotice(0, 1, 1, new RequestCallback<List<MessageInfo>>() {
            @Override
            public void onSuccess(List<MessageInfo> result) {
                if(result.size()>0){
                    mMarqueeView.setText(result.get(0).getTheme());
                }else{
                    mMarqueeView.setText("欢迎来到乐福养老平台");
                }

            }

            @Override
            public void onFailure(ApiHttpException e) {
                mMarqueeView.setText("欢迎来到乐福养老平台");
            }

        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.rl_fragment_gov_circular: // 跳转到通知公告
                ((GovMainActivity)getActivity()).skipCircularFragment();
                break;
            case R.id.rl_fragment_gov_area_data: // 辖区数据
                intent = new Intent(getActivity(), AreaAllDataActivity.class);
                intent.putExtra("agencyIds", ((GovMainActivity)getActivity()).getAgencyIds());
                startActivity(intent);
                break;
            case R.id.rl_fragment_gov_area_member:
                // 辖区成员
                intent = new Intent(getActivity(), ChoseOrganizationActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_fragment_gov_gov_navigation:
                // 民生政话  查看全部
                intent = new Intent(getActivity(), AllNewsActivity.class);
                intent.putExtra("govId", mUser.getGovOrg_id());
                startActivity(intent);
                break;
            case R.id.tv_fragment_gov_group_navigation:
                // 机构活动 查看全部
                intent = new Intent(getActivity(), AllOrgActiveActivity.class);
                intent.putExtra("groupId", mUser.getGroupOrg_id());
                startActivity(intent);
                break;
            case R.id.rl_fragment_gov_seek: // 查找机构
                intent = new Intent(getActivity(), ChoseOrganizationActivity.class);
                startActivity(intent);
                break;
        }
    }
}
