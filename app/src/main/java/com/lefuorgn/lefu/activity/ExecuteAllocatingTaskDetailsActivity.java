package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.base.BaseViewPagerAdapter;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.db.util.SignConfigManager;
import com.lefuorgn.lefu.fragment.ExecuteAllocatingTaskDetailsFragment;
import com.lefuorgn.widget.PagerSlidingTabStrip;

import java.util.List;

/**
 * 今日工作护理信息详情页
 */

public class ExecuteAllocatingTaskDetailsActivity extends BaseActivity {

    private PagerSlidingTabStrip mTabStrip;
    private ViewPager mViewPager;

    private long mOldPeopleId;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_info_details;
    }

    @Override
    protected void initView() {
        mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.psts_activity_sign_info_details);
        mViewPager = (ViewPager) findViewById(R.id.vp_activity_sign_info_details);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mOldPeopleId = intent.getLongExtra("id", 0);
        String name = intent.getStringExtra("name");
        setToolBarTitle(name);
        // 获取当前需要显示的条目
        List<DisplaySignOrNursingItem> items = SignConfigManager.getNursingItem();
        BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getSupportFragmentManager(), mTabStrip, mViewPager);
        for (DisplaySignOrNursingItem item : items) {
            adapter.addTab(item.getTitle(), ExecuteAllocatingTaskDetailsFragment.class, getBundle(item));
        }
    }

    private Bundle getBundle(DisplaySignOrNursingItem item) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ExecuteAllocatingTaskDetailsFragment.BUNDLE_NURSING_INFO_TYPE, item);
        bundle.putLong(ExecuteAllocatingTaskDetailsFragment.BUNDLE_NURSING_INFO_ID, mOldPeopleId);
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
