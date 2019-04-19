package com.lefuorgn.lefu.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.lefu.fragment.AllocatingTaskProgressDetailsFragment;

/**
 * 配单任务条目详细信息
 */

public class AllocatingTaskProgressDetailsActivity extends BaseActivity {

    private final String BEING_FRAGMENT = "BEING";
    private final String OVER_FRAGMENT = "OVER";

    private int mBtnId;

    private TextView mBeingBtn, mOverBtn;
    private AllocatingTaskProgressDetailsFragment mBeingFragment;
    private AllocatingTaskProgressDetailsFragment mOverFragment;
    private FragmentManager mFManager;

    private long mTypeId;
    private long mTime;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_allocating_task_progress_details;
    }

    @Override
    protected void initView() {
        // 初始化控件
        mBeingBtn = (TextView) findViewById(R.id.btn_activity_allocating_task_progress_details_being);
        mOverBtn = (TextView) findViewById(R.id.btn_activity_allocating_task_progress_details_over);
        mBeingBtn.setOnClickListener(this);
        mOverBtn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mTypeId = getIntent().getLongExtra("item_id", 0);
        mTime = getIntent().getLongExtra("time", 0);
        addAllFragment();
        // 设置默认的Fragment
        setFragment(BEING_FRAGMENT);
        mBtnId = R.id.btn_activity_allocating_task_progress_details_being;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity_allocating_task_progress_details_being:
                // 进行中的按钮点击事件
                if (mBtnId == R.id.btn_activity_allocating_task_progress_details_being) {
                    // 如果页面没有进行切换则返回
                    return;
                }
                // 切换fragment
                setFragment(BEING_FRAGMENT);
                // 记录当前按钮的ID
                mBtnId = R.id.btn_activity_allocating_task_progress_details_being;
                break;
            case R.id.btn_activity_allocating_task_progress_details_over:
                // 已结束的按钮点击事件
                if (mBtnId == R.id.btn_activity_allocating_task_progress_details_over) {
                    return;
                }
                setFragment(OVER_FRAGMENT);
                mBtnId = R.id.btn_activity_allocating_task_progress_details_over;
                break;
        }
    }

    /**
     * 添加所有的fragment到FragmentManager中
     */
    private void addAllFragment() {
        if (mFManager == null) {
            mFManager = getSupportFragmentManager();
        }
        mBeingFragment = (AllocatingTaskProgressDetailsFragment) Fragment
                .instantiate(this, AllocatingTaskProgressDetailsFragment.class.getName(), getBundle(1));
        mOverFragment = (AllocatingTaskProgressDetailsFragment) Fragment
                .instantiate(this, AllocatingTaskProgressDetailsFragment.class.getName(), getBundle(3));
        FragmentTransaction transaction = mFManager.beginTransaction();
        transaction.add(R.id.fl_activity_allocating_task_progress_details, mBeingFragment,
                BEING_FRAGMENT);
        transaction.add(R.id.fl_activity_allocating_task_progress_details, mOverFragment,
                OVER_FRAGMENT);
        transaction.commit();
    }

    private Bundle getBundle(int page) {
        Bundle bundle = new Bundle();
        bundle.putInt(AllocatingTaskProgressDetailsFragment.BUNDLE_PROGRESS_DETAILS_PAGE, page);
        bundle.putLong(AllocatingTaskProgressDetailsFragment.BUNDLE_PROGRESS_DETAILS_TYPE, mTypeId);
        bundle.putLong(AllocatingTaskProgressDetailsFragment.BUNDLE_PROGRESS_DETAILS_TIME, mTime);
        return bundle;
    }

    /**
     * 进行fragment切换
     *
     * @param tag 按钮标签
     */
    private void setFragment(String tag) {
        if (mBeingFragment == null) {
            // 如果引用被置为空,则获取到其原有对象的引用, 防止对象引用被置空引起页面出现重叠的现象
            mBeingFragment = (AllocatingTaskProgressDetailsFragment) mFManager
                    .findFragmentByTag(BEING_FRAGMENT);
        }
        if (mOverFragment == null) {
            mOverFragment = (AllocatingTaskProgressDetailsFragment) mFManager
                    .findFragmentByTag(OVER_FRAGMENT);
        }
        FragmentTransaction transaction = mFManager.beginTransaction();
        // 显示指定页面,影藏对应的页面
        if (BEING_FRAGMENT.equals(tag)) {
            transaction.show(mBeingFragment);
            transaction.hide(mOverFragment);
            // 切换指示器文字颜色
            mBeingBtn.setTextColor(Color.WHITE);
            mBeingBtn.setBackgroundResource(R.color.colorPrimary);
            mOverBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
            mOverBtn.setBackgroundColor(Color.parseColor("#D9D9D9"));
            setToolBarTitle("未完成");
        } else if (OVER_FRAGMENT.equals(tag)) {
            transaction.show(mOverFragment);
            transaction.hide(mBeingFragment);
            // 切换指示器文字颜色
            mBeingBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
            mBeingBtn.setBackgroundColor(Color.parseColor("#D9D9D9"));
            mOverBtn.setTextColor(Color.WHITE);
            mOverBtn.setBackgroundResource(R.color.colorPrimary);
            setToolBarTitle("已完成");
        }
        transaction.commit();
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
