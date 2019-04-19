package com.lefuorgn.lefu.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.lefuorgn.base.BaseViewPagerAdapter;
import com.lefuorgn.lefu.fragment.AllocatingTaskNursingFragment;
import com.lefuorgn.widget.PagerSlidingTabStrip;

/**
 * 数据审核页面适配器
 */

public class AllocatingTaskAdapter extends BaseViewPagerAdapter {

    private AllocatingTaskNursingFragment mFragment;

    public AllocatingTaskAdapter(FragmentManager fm, PagerSlidingTabStrip pageStrip, ViewPager pager) {
        super(fm, pageStrip, pager);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mFragment = (AllocatingTaskNursingFragment) object;
        mFragment.refreshData();
        super.setPrimaryItem(container, position, object);
    }

    public AllocatingTaskNursingFragment getFragment() {
        return mFragment;
    }

}
