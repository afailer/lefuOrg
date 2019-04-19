package com.lefuorgn.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.bean.ViewPageInfo;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager适配器
 */

public class BaseViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private PagerSlidingTabStrip mPagerStrip;
    private List<ViewPageInfo> mTabs = new ArrayList<ViewPageInfo>();

    public BaseViewPagerAdapter(FragmentManager fm,
        PagerSlidingTabStrip pageStrip, ViewPager pager) {
        super(fm);
        mContext = pager.getContext();
        mPagerStrip = pageStrip;
        pager.setAdapter(this);
        mPagerStrip.setViewPager(pager);
    }

    public void addTab(String title, Class<?> clazz, Bundle args) {
        addTab(title, clazz, args, 0);
    }

    public void addTab(String title, Class<?> clazz, Bundle args, int drawableRes) {
        ViewPageInfo info = new ViewPageInfo();
        info.setTitle(title);
        info.setClazz(clazz);
        info.setArgs(args);
        info.setDrawableRes(drawableRes);
        addFragment(info);
    }

    private void addFragment(ViewPageInfo info) {
        if (info == null) {
            return;
        }
        // 加入tab title
        View v = LayoutInflater.from(mContext).inflate(
                R.layout.item_base_viewpage_tab, mPagerStrip, false);
        TextView title = (TextView) v.findViewById(R.id.tv_item_base_viewpager_tab);
        if(info.getDrawableRes() != 0) {
            title.setCompoundDrawablesWithIntrinsicBounds(0, info.getDrawableRes(), 0, 0);
            title.setCompoundDrawablePadding(StringUtils.dip2px(mContext, 8f));
        }
        title.setText(info.getTitle());
        mPagerStrip.addTab(v);
        mTabs.add(info);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int arg0) {
        ViewPageInfo pageInfo = mTabs.get(arg0);
        return Fragment.instantiate(mContext, pageInfo.getClazz().getName(), pageInfo.getArgs());
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).getTitle();
    }

}
