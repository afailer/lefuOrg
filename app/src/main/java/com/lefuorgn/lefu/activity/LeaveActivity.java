package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.db.util.PermissionManager;
import com.lefuorgn.lefu.fragment.LeaveFragment;

/**
 * 老人外出页面
 */
public class LeaveActivity extends BaseActivity {

    private final String BEING_FRAGMENT = "BEING";
    private final String OVER_FRAGMENT = "OVER";

    private int mBtnId;

    private TextView mBeingBtn, mOverBtn;
    private LeaveFragment mBeingFragment;
    private LeaveFragment mOverFragment;
    private FragmentManager mFManager;
    private TextView mAddBtn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_leave;
    }

    @Override
    protected void initView() {
        // 初始化控件
        mBeingBtn = (TextView) findViewById(R.id.tv_activity_leave_being);
        mOverBtn = (TextView) findViewById(R.id.tv_activity_leave_over);
        mBeingBtn.setOnClickListener(this);
        mOverBtn.setOnClickListener(this);
        mAddBtn = (TextView) findViewById(R.id.btn_activity_leave_add);

        addAllFragment();
        // 设置默认的Fragment
        setFragment(BEING_FRAGMENT);
        mBtnId = R.id.tv_activity_leave_being;
    }

    @Override
    protected void initData() {
        setToolBarTitle("老人外出");
        // 获取老人外出添加权限
        if(PermissionManager.hasPermission(PermissionManager.LEAVE_OUT + PermissionManager.P_C)) {
            // 拥有添加请假的权限
            mAddBtn.setOnClickListener(this);
        }else {
            mAddBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_activity_leave_being:
                // 进行中的按钮点击事件
                if (mBtnId == R.id.tv_activity_leave_being) {
                    // 如果页面没有进行切换则返回
                    return;
                }
                // 切换fragment
                setFragment(BEING_FRAGMENT);
                // 记录当前按钮的ID
                mBtnId = R.id.tv_activity_leave_being;
                break;
            case R.id.tv_activity_leave_over:
                // 已结束的按钮点击事件
                if (mBtnId == R.id.tv_activity_leave_over) {
                    return;
                }
                setFragment(OVER_FRAGMENT);
                mBtnId = R.id.tv_activity_leave_over;
                break;
            case R.id.btn_activity_leave_add:
                Intent intent = new Intent(this, LeaveAddActivity.class);
                startActivityForResult(intent, 100);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 300) {
            // 添加请假成功
            if (mBeingFragment == null) {
                // 如果引用被置为空,则获取到其原有对象的引用, 防止对象引用被置空引起页面出现重叠的现象
                mBeingFragment = (LeaveFragment) mFManager.findFragmentByTag(BEING_FRAGMENT);
            }
            mBeingFragment.updateLeave();
        }
    }

    /**
     * 刷新完成页面的数据
     */
    public void overFragmentRefresh() {
        // 销假后刷新俩个页面
        if (mOverFragment == null) {
            mOverFragment = (LeaveFragment) mFManager
                    .findFragmentByTag(OVER_FRAGMENT);
        }
        mOverFragment.updateLeave();
    }

    /**
     * 添加所有的fragment到FragmentManager中
     */
    private void addAllFragment() {
        if (mFManager == null) {
            mFManager = getSupportFragmentManager();
        }
        mBeingFragment = (LeaveFragment) Fragment
                .instantiate(this, LeaveFragment.class.getName(), getBundle(1));
        mOverFragment = (LeaveFragment) Fragment
                .instantiate(this, LeaveFragment.class.getName(), getBundle(2));
        FragmentTransaction transaction = mFManager.beginTransaction();
        transaction.add(R.id.fl_activity_leave, mBeingFragment,
                BEING_FRAGMENT);
        transaction.add(R.id.fl_activity_leave, mOverFragment,
                OVER_FRAGMENT);
        transaction.commit();
    }

    private Bundle getBundle(int leaveType) {
        Bundle bundle = new Bundle();
        bundle.putInt(LeaveFragment.BUNDLE_LEAVE_TYPE, leaveType);
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
            mBeingFragment = (LeaveFragment) mFManager
                    .findFragmentByTag(BEING_FRAGMENT);
        }
        if (mOverFragment == null) {
            mOverFragment = (LeaveFragment) mFManager
                    .findFragmentByTag(OVER_FRAGMENT);
        }
        FragmentTransaction transaction = mFManager.beginTransaction();
        // 显示指定页面,影藏对应的页面
        if (BEING_FRAGMENT.equals(tag)) {
            transaction.show(mBeingFragment);
            transaction.hide(mOverFragment);
            // 切换指示器文字颜色
            mBeingBtn.setTextColor(getResources().getColor(
                    R.color.colorPrimary));
            mOverBtn.setTextColor(getResources().getColor(R.color.gray));
        } else if (OVER_FRAGMENT.equals(tag)) {
            transaction.show(mOverFragment);
            transaction.hide(mBeingFragment);
            // 切换指示器文字颜色
            mBeingBtn.setTextColor(getResources().getColor(R.color.gray));
            mOverBtn.setTextColor(getResources().getColor(
                    R.color.colorPrimary));
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
