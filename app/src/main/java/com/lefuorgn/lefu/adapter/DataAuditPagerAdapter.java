package com.lefuorgn.lefu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.lefuorgn.base.BaseViewPagerAdapter;
import com.lefuorgn.lefu.activity.DataAuditActivity;
import com.lefuorgn.lefu.fragment.DataAuditFragment;
import com.lefuorgn.widget.PagerSlidingTabStrip;

/**
 * 数据审核页面适配器
 */

public class DataAuditPagerAdapter extends BaseViewPagerAdapter {

    private DataAuditFragment mFragment;
    private DataAuditActivity mActivity;

    public DataAuditPagerAdapter(DataAuditActivity activity, FragmentManager fm,
                                 PagerSlidingTabStrip pageStrip, ViewPager pager) {
        super(fm, pageStrip, pager);
        mActivity = activity;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mFragment = (DataAuditFragment) object;
        mActivity.setSelectAll(mFragment.isSelect());
        super.setPrimaryItem(container, position, object);
    }

    public Fragment getFragment() {
        return mFragment;
    }

}
