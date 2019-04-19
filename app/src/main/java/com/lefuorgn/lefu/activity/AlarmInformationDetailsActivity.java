package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.base.BaseViewPagerAdapter;
import com.lefuorgn.lefu.fragment.AlarmInformationDetailsFragment;
import com.lefuorgn.widget.PagerSlidingTabStrip;

/**
 * 告警信息类别详情
 */

public class AlarmInformationDetailsActivity extends BaseActivity {

    /**
     * SOS报警类型
     */
    public static final int INTENT_ALARM_TYPE_SOS = 1;
    /**
     * 电子围栏报警类型
     */
    public static final int INTENT_ALARM_TYPE_ELECTRONIC_FENCE = 2;
    /**
     * 低电量报警信息
     */
    public static final int INTENT_ALARM_TYPE_LOW_BATTERY = 3;

    private PagerSlidingTabStrip mTabStrip;
    private ViewPager mViewPager;
    private int mType; // 当前报警页面所属类型
    private boolean dataChange;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alarm_information_details;
    }

    @Override
    protected void initView() {
        mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.psts_activity_alarm_info_details);
        mViewPager = (ViewPager) findViewById(R.id.vp_activity_alarm_info_details);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        mType = intent.getIntExtra("type", 0);
        setToolBarTitle(title);
        dataChange = false;
        BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getSupportFragmentManager(), mTabStrip, mViewPager);
        adapter.addTab("未处理", AlarmInformationDetailsFragment.class,
                getBundle(AlarmInformationDetailsFragment.BUNDLE_ALARM_INFO_STATUS_UNRESOLVED, title),
                R.drawable.alarm_pending_selector);
        if(mType != INTENT_ALARM_TYPE_LOW_BATTERY) {
            adapter.addTab("处理中", AlarmInformationDetailsFragment.class,
                    getBundle(AlarmInformationDetailsFragment.BUNDLE_ALARM_INFO_STATUS_SOLVING, title),
                    R.drawable.alarm_processed_selector);
        }
        adapter.addTab("已处理", AlarmInformationDetailsFragment.class,
                getBundle(AlarmInformationDetailsFragment.BUNDLE_ALARM_INFO_STATUS_SOLVED, title),
                R.drawable.alarm_reject_selector);
    }

    private Bundle getBundle(int status, String title) {
        Bundle bundle = new Bundle();
        bundle.putInt(AlarmInformationDetailsFragment.BUNDLE_ALARM_INFO_TYPE, mType);
        bundle.putInt(AlarmInformationDetailsFragment.BUNDLE_ALARM_INFO_STATUS, status);
        bundle.putString(AlarmInformationDetailsFragment.BUNDLE_ALARM_INFO_TITLE, title);
        return bundle;
    }

    /**
     * 根据不同的状态页刷新页面
     */
    public void refreshPage(int status) {
        FragmentManager manager = getSupportFragmentManager();
        for (Fragment fragment : manager.getFragments()) {
            AlarmInformationDetailsFragment aFragment;
            if(fragment instanceof AlarmInformationDetailsFragment) {
                aFragment = (AlarmInformationDetailsFragment) fragment;
            }else {
                continue;
            }
            if(aFragment.getStatus() == AlarmInformationDetailsFragment.BUNDLE_ALARM_INFO_STATUS_SOLVING) {
                // 处理中页面数据必刷新
                aFragment.refresh();
                continue;
            }
            // 判断是那个页面进行的数据刷新
            if(status == AlarmInformationDetailsFragment.BUNDLE_ALARM_INFO_STATUS_UNRESOLVED && status == aFragment.getStatus()) {
                // 未处理页面
                aFragment.refresh();
                dataChange = true;
            }else if(status == AlarmInformationDetailsFragment.BUNDLE_ALARM_INFO_STATUS_SOLVING
                    && aFragment.getStatus() == AlarmInformationDetailsFragment.BUNDLE_ALARM_INFO_STATUS_SOLVED) {
                // 处理中页面
                aFragment.refresh();
            }
        }
    }

    @Override
    public void finish() {
        if(dataChange) {
            setResult(200);
        }
        super.finish();
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
