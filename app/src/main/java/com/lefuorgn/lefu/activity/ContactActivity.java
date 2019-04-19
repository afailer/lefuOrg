package com.lefuorgn.lefu.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.base.BaseViewPagerAdapter;
import com.lefuorgn.lefu.bean.Department;
import com.lefuorgn.lefu.bean.Staff;
import com.lefuorgn.lefu.fragment.ContactAllFragment;
import com.lefuorgn.lefu.fragment.ContactDepartmentsFragment;
import com.lefuorgn.lefu.fragment.ContactPublicFragment;
import com.lefuorgn.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * 通讯录页面
 */
public class ContactActivity extends BaseActivity {

    private PagerSlidingTabStrip mTabStrip;
    private ViewPager mViewPager;
    private BaseViewPagerAdapter mAdapter;
    private List<Department> mDepartmentDatas;
    private List<Staff> mStaffDatas;
    // 当前网络请求数统计
    private int num;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contact;
    }

    @Override
    protected void initView() {
        mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.psts_activity_contact);
        mViewPager = (ViewPager) findViewById(R.id.vp_activity_contact);
    }

    @Override
    protected void initData() {
        setToolBarTitle("通讯录");
        mAdapter = new BaseViewPagerAdapter(getSupportFragmentManager(), mTabStrip, mViewPager);
        mAdapter.addTab("全部", ContactAllFragment.class, new Bundle());
        mAdapter.addTab("部门", ContactDepartmentsFragment.class, new Bundle());
        mAdapter.addTab("公用", ContactPublicFragment.class, new Bundle());
        loadStaff();
        laodDepartment();
    }

    /**
     * 加载部门
     */
    private void laodDepartment() {
        LefuApi.getDepartments(new RequestCallback<List<Department>>() {
            @Override
            public void onSuccess(List<Department> result) {
                mDepartmentDatas = result;
                networkStatistics();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                networkStatistics();
                showToast(e.getMessage());
            }
        });
    }

    /**
     * 通知展示所有部门的页面
     * @param result 网络请求回的结果
     */
    private void notifyContactDepartmentsFragment(List<Department> result) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if(fragment instanceof ContactDepartmentsFragment) {
                ContactDepartmentsFragment f = (ContactDepartmentsFragment) fragment;
                f.setResult(result);
                break;
            }
        }
    }

    /**
     * 加载员工
     */
    private void loadStaff() {
        LefuApi.getStaffs(AppContext.getInstance().getAgencyId(), new RequestCallback<List<Staff>>() {
            @Override
            public void onSuccess(List<Staff> result) {
                mStaffDatas = result;
                notifyContactAllFragment(result);
                networkStatistics();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                notifyContactAllFragment(null);
                networkStatistics();
                showToast(e.getMessage());
            }
        });
    }

    /**
     * 通知展示所有员工的页面
     * @param result 网络请求回的结果
     */
    private void notifyContactAllFragment(List<Staff> result) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if(fragment instanceof ContactAllFragment) {
                ContactAllFragment f = (ContactAllFragment) fragment;
                f.setResult(result);
                break;
            }
        }
    }

    /**
     * 网络请求数统计
     */
    private synchronized void networkStatistics() {
        num++;
        if(num == 2) {
            // 网络请求完成
            // 统计部门下的成员个数
            statisticsDepartmentsNum();
            notifyContactDepartmentsFragment(mDepartmentDatas);
            num = 0;
        }
    }

    /**
     * 统计部门下的成员个数
     */
    private void statisticsDepartmentsNum() {
        if(mStaffDatas == null) {
            mStaffDatas = new ArrayList<Staff>();
        }
        if(mDepartmentDatas != null) {
            for (Department department : mDepartmentDatas) {
                if(department.getPid() == 0) {
                    statisticsRootDepartmentNum(department);
                }
            }
        }else {
            mDepartmentDatas = new ArrayList<Department>();
        }
    }

    /**
     *  循环统计某一个二叉树下的目录个数
     * @param d 要统计部门下的人数Department
     */
    private void statisticsRootDepartmentNum(Department d) {
        List<Department> dList = new ArrayList<Department>();
        for (Department department : mDepartmentDatas) {
            if(department.getPid() == d.getDept_id()) {
                dList.add(department);
                statisticsRootDepartmentNum(department);
            }
        }
        List<Staff> sList = new ArrayList<Staff>();
        for (Staff staff : mStaffDatas) {
            if(staff.getDept_id() == d.getDept_id()) {
                sList.add(staff);
            }
        }
        d.setNum(sList.size());
        for (Department cd : dList) {
            d.setNum(d.getNum() + cd.getNum());
        }
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
