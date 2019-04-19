package com.lefuorgn.oa.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.base.BaseViewPagerAdapter;
import com.lefuorgn.oa.fragment.MyAttendanceApplyFragment;
import com.lefuorgn.widget.PagerSlidingTabStrip;


/**
 * 我审批的或者我申请的
 */

public class MyAttendanceApplyActivity extends BaseActivity {

    private PagerSlidingTabStrip mTabStrip;
    private ViewPager mViewPager;

    private boolean dataChange;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_attendance_apply;
    }

    @Override
    protected void initView() {
        mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.psts_activity_my_attendance_apply);
        mViewPager = (ViewPager) findViewById(R.id.vp_activity_my_attendance_apply);
    }

    @Override
    protected void initData() {
        boolean approval = getIntent().getBooleanExtra("approval", false);
        dataChange = false;
        BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getSupportFragmentManager(), mTabStrip, mViewPager);
        if(approval) {
            // 我审批的
            setToolBarTitle("我审批的");
            adapter.addTab("待处理", MyAttendanceApplyFragment.class,
                    getBundle(MyAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_UNRESOLVED, true),
                    R.drawable.alarm_pending_selector);
            adapter.addTab("已处理", MyAttendanceApplyFragment.class,
                    getBundle(MyAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_SOLVED, true),
                    R.drawable.alarm_processed_selector);
            adapter.addTab("抄送我的", MyAttendanceApplyFragment.class,
                    getBundle(MyAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_COPY_FOR_ME, true),
                    R.drawable.alarm_reject_selector);
            adapter.addTab("已撤销", MyAttendanceApplyFragment.class,
                    getBundle(MyAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_REVOKE, true),
                    R.drawable.alarm_revoke_selector);
        }else {
            // 我申请的
            setToolBarTitle("我申请的");
            adapter.addTab("进行中", MyAttendanceApplyFragment.class,
                    getBundle(MyAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_SOLVING, false),
                    R.drawable.alarm_pending_selector);
            adapter.addTab("同意", MyAttendanceApplyFragment.class,
                    getBundle(MyAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_AGREE, false),
                    R.drawable.alarm_processed_selector);
            adapter.addTab("驳回", MyAttendanceApplyFragment.class,
                    getBundle(MyAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_REJECT, false),
                    R.drawable.alarm_reject_selector);
            adapter.addTab("撤销", MyAttendanceApplyFragment.class,
                    getBundle(MyAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_REVOKE, false),
                    R.drawable.alarm_revoke_selector);
        }

    }

    private Bundle getBundle(int status, boolean approval) {
        Bundle bundle = new Bundle();
        bundle.putInt(MyAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS, status);
        bundle.putBoolean(MyAttendanceApplyFragment.BUNDLE_ATTENDANCE_APPROVAL, approval);
        return bundle;
    }

    /**
     * 刷新指定的页面
     * @param resultCode 2: 驳回 3: 同意 4: 撤销
     */
    public void refreshPage(int resultCode) {
        dataChange = true;
        FragmentManager manager = getSupportFragmentManager();
        for (Fragment fragment : manager.getFragments()) {
            MyAttendanceApplyFragment aFragment;
            if(fragment instanceof MyAttendanceApplyFragment) {
                aFragment = (MyAttendanceApplyFragment) fragment;
            }else {
                continue;
            }
            int status = aFragment.getStatus();
            if(resultCode == 3) {
                // 同意, 刷新已处理和抄送我的页面
                if(status == MyAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_SOLVED ||
                        status == MyAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_COPY_FOR_ME) {
                    aFragment.refresh();
                }
            }else if(resultCode == 2) {
                // 驳回, 刷新抄送我的页面
                if(status == MyAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_COPY_FOR_ME) {
                    aFragment.refresh();
                }
            }else if(resultCode == 4) {
                // 撤销, 刷新撤销页面
                if(status == MyAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_REVOKE) {
                    aFragment.refresh();
                }
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
