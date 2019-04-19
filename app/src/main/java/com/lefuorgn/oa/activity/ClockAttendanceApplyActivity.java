package com.lefuorgn.oa.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.base.BaseViewPagerAdapter;
import com.lefuorgn.oa.fragment.ClockAttendanceApplyFragment;
import com.lefuorgn.widget.PagerSlidingTabStrip;

/**
 * 我审批的或者我申请的(考勤页面)
 */

public class ClockAttendanceApplyActivity extends BaseActivity {

    private PagerSlidingTabStrip mTabStrip;
    private ViewPager mViewPager;

    private boolean dataChange;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_clock_attendance_apply;
    }

    @Override
    protected void initView() {
        mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.psts_activity_clock_attendance_apply);
        mViewPager = (ViewPager) findViewById(R.id.vp_activity_clock_attendance_apply);
    }

    @Override
    protected void initData() {
        boolean approval = getIntent().getBooleanExtra("approval", false);
        dataChange = false;
        BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getSupportFragmentManager(), mTabStrip, mViewPager);
        if(approval) {
            // 我审批的
            setToolBarTitle("我审批的");
            adapter.addTab("待处理", ClockAttendanceApplyFragment.class,
                    getBundle(ClockAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_UNRESOLVED, true),
                    R.drawable.alarm_pending_selector);
            adapter.addTab("已同意", ClockAttendanceApplyFragment.class,
                    getBundle(ClockAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_AGREE, true),
                    R.drawable.alarm_processed_selector);
            adapter.addTab("已驳回", ClockAttendanceApplyFragment.class,
                    getBundle(ClockAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_REJECT, true),
                    R.drawable.alarm_reject_selector);
        }else {
            // 我申请的
            setToolBarTitle("我申请的");
            adapter.addTab("审批中", ClockAttendanceApplyFragment.class,
                    getBundle(ClockAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_SOLVING, false),
                    R.drawable.alarm_pending_selector);
            adapter.addTab("已同意", ClockAttendanceApplyFragment.class,
                    getBundle(ClockAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_AGREE, false),
                    R.drawable.alarm_processed_selector);
            adapter.addTab("已驳回", ClockAttendanceApplyFragment.class,
                    getBundle(ClockAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_REJECT, false),
                    R.drawable.alarm_reject_selector);
        }

    }

    private Bundle getBundle(int status, boolean approval) {
        Bundle bundle = new Bundle();
        bundle.putInt(ClockAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS, status);
        bundle.putBoolean(ClockAttendanceApplyFragment.BUNDLE_ATTENDANCE_APPROVAL, approval);
        return bundle;
    }

    /**
     * 根据不同的状态页刷新页面
     * @param agree true: 同意; false: 驳回
     */
    public void refreshPage(boolean agree) {
        dataChange = true;
        FragmentManager manager = getSupportFragmentManager();
        for (Fragment fragment : manager.getFragments()) {
            ClockAttendanceApplyFragment aFragment;
            if(fragment instanceof ClockAttendanceApplyFragment) {
                aFragment = (ClockAttendanceApplyFragment) fragment;
            }else {
                continue;
            }
            if(aFragment.getStatus() == 1) {
                // 待处理或审批中页面
                aFragment.refresh();
            }else if(aFragment.getStatus() == ClockAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_AGREE && agree) {
                // 同意
                aFragment.refresh();
            }else if(aFragment.getStatus() == ClockAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_REJECT && !agree) {
                // 驳回
                aFragment.refresh();
            }
        }
    }

    /**
     * 刷新当前状态
     */
    public void refreshPage() {
        dataChange = true;
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
