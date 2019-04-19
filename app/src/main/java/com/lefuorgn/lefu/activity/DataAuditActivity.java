package com.lefuorgn.lefu.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CheckedTextView;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.db.util.PermissionManager;
import com.lefuorgn.db.util.SignConfigManager;
import com.lefuorgn.lefu.adapter.DataAuditPagerAdapter;
import com.lefuorgn.lefu.fragment.DataAuditFragment;
import com.lefuorgn.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据审核页面
 */

public class DataAuditActivity extends BaseActivity {

    private PagerSlidingTabStrip mTabStrip;
    private ViewPager mViewPager;
    private DataAuditPagerAdapter mAdapter;

    private CheckedTextView mSelectView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_data_audit;
    }

    @Override
    protected void initView() {
        mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.psts_activity_data_audit);
        mViewPager = (ViewPager) findViewById(R.id.vp_activity_data_audit);
        mSelectView = (CheckedTextView) findViewById(R.id.ctv_activity_data_audit);
        mSelectView.setOnClickListener(this);
        findViewById(R.id.tv_activity_data_audit_not_approved).setOnClickListener(this);
        findViewById(R.id.tv_activity_data_audit_remark).setOnClickListener(this);
        findViewById(R.id.tv_activity_data_audit_approved).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setToolBarTitle("数据审核");
        // 存放要显示的体征项条目
        List<DisplaySignOrNursingItem> items = new ArrayList<DisplaySignOrNursingItem>();
        for (DisplaySignOrNursingItem item : SignConfigManager.getSignItem()) {
            // 通过权限过滤掉没有权限的条目
            if(PermissionManager.hasVerifyPermission(item.getTitle())) {
                items.add(item);
            }
        }
        mAdapter = new DataAuditPagerAdapter(this, getSupportFragmentManager(), mTabStrip, mViewPager);
        for (DisplaySignOrNursingItem item : items) {
            mAdapter.addTab(item.getTitle(), DataAuditFragment.class, getBundle(item));
        }
    }

    private Bundle getBundle(DisplaySignOrNursingItem item) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DataAuditFragment.BUNDLE_DATA_AUDIT_TYPE, item);
        return bundle;
    }

    @Override
    public void onClick(View v) {
        DataAuditFragment f = (DataAuditFragment) mAdapter.getFragment();
        switch (v.getId()) {
            case R.id.ctv_activity_data_audit:
                // 全选或者全不选
                f.selectAll();
                break;
            case R.id.tv_activity_data_audit_not_approved:
                // 不通过
                f.notApproved();
                break;
            case R.id.tv_activity_data_audit_remark:
                // 备注
                f.remark();
                break;
            case R.id.tv_activity_data_audit_approved:
                // 通过
                f.approved();
                break;
        }
    }

    /**
     * 初始化当前控件的选择状态
     * @param select true: 全选; false: 不全选
     */
    public void setSelectAll(boolean select) {
        mSelectView.setChecked(select);
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
