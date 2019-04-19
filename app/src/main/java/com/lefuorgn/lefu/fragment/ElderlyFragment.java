package com.lefuorgn.lefu.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseFragment;
import com.lefuorgn.lefu.adapter.ElderlyFragmentAdapter;
import com.lefuorgn.widget.PagerSlidingTabStrip;

/**
 * 所有老人联系页面,以及关注的老人页面
 */

public class ElderlyFragment extends BaseFragment {

    private PagerSlidingTabStrip mTabStrip;
    private ViewPager mViewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_elderly;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.psts_fragment_elderly);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_fragment_elderly);
    }

    @Override
    protected void initData() {
        ElderlyFragmentAdapter mAdapter = new ElderlyFragmentAdapter(getFragmentManager(), mTabStrip, mViewPager);
        mAdapter.addTab("关心的人", ElderlyTypeFragment.class, getBundle(true));
        mAdapter.addTab("所有老人", ElderlyTypeFragment.class, getBundle(false));
    }

    private Bundle getBundle(boolean attention) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ElderlyTypeFragment.BUNDLE_ELDERLY_TYPE, attention);
        return bundle;
    }

}
