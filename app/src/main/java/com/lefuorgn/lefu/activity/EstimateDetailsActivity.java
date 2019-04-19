package com.lefuorgn.lefu.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.base.BaseViewPagerAdapter;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.lefu.bean.EstimateItem;
import com.lefuorgn.lefu.fragment.EstimateDetailsFragment;
import com.lefuorgn.widget.PagerSlidingTabStrip;

import java.util.List;

/**
 * 评估表详情页面
 */

public class EstimateDetailsActivity extends BaseActivity {

    private PagerSlidingTabStrip mTabStrip;
    private ViewPager mViewPager;
    private BaseViewPagerAdapter mAdapter;
    private TextView mLoadingView;

    private OldPeople mOldPeople;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_estimate_details;
    }

    @Override
    protected void initView() {
        setToolBarTitle("历史");
        mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.psts_activity_estimate_details);
        mViewPager = (ViewPager) findViewById(R.id.vp_activity_estimate_details);
        mLoadingView = (TextView) findViewById(R.id.tv_activity_estimate_details_error);
    }

    @Override
    protected void initData() {
        mOldPeople = (OldPeople) getIntent().getSerializableExtra("oldPeople");
        LefuApi.getEstimateItem(mOldPeople.getAgency_id(), new RequestCallback<List<EstimateItem>>() {
            @Override
            public void onSuccess(List<EstimateItem> result) {
                if(result == null || result.size() == 0) {
                    mLoadingView.setVisibility(View.VISIBLE);
                    mLoadingView.setText("没有评估表");
                    return;
                }
                mLoadingView.setVisibility(View.GONE);
                mAdapter = new BaseViewPagerAdapter(getSupportFragmentManager(), mTabStrip, mViewPager);
                for (EstimateItem item : result) {
                    mAdapter.addTab(item.getTitle(), EstimateDetailsFragment.class, getBundle(item.getId()));
                }
            }

            @Override
            public void onFailure(ApiHttpException e) {
                mLoadingView.setVisibility(View.VISIBLE);
                mLoadingView.setText("评估列表加载失败");
            }
        });
    }

    private Bundle getBundle(long id) {
        Bundle bundle = new Bundle();
        bundle.putLong(EstimateDetailsFragment.BUNDLE_ESTIMATE_DETAILS_ID, id);
        bundle.putLong(EstimateDetailsFragment.BUNDLE_ESTIMATE_DETAILS_OLDPEOPLE_ID, mOldPeople.getId());
        bundle.putLong(EstimateDetailsFragment.BUNDLE_ESTIMATE_DETAILS_AGENCY_ID, mOldPeople.getAgency_id());
        return bundle;
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
