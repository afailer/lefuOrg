package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.base.BaseViewPagerAdapter;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.db.util.SignConfigManager;
import com.lefuorgn.lefu.fragment.NursingInfoDetailsFragment;
import com.lefuorgn.widget.PagerSlidingTabStrip;

import java.util.List;

/**
 * 护理信息详情页
 */

public class NursingInfoDetailsActivity extends BaseActivity {

    private PagerSlidingTabStrip mTabStrip;
    private ViewPager mViewPager;

    private long mOldPeopleId;
    private String mOldPeopleName;

    private boolean modify; // 有数据删除了

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
        mOldPeopleName = intent.getStringExtra("name");
        setToolBarTitle(mOldPeopleName);
        // 获取当前需要显示的条目
        List<DisplaySignOrNursingItem> items = SignConfigManager.getNursingItem();
        BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getSupportFragmentManager(), mTabStrip, mViewPager);
        for (DisplaySignOrNursingItem item : items) {
            adapter.addTab(item.getTitle(), NursingInfoDetailsFragment.class, getBundle(item));
        }
    }

    private Bundle getBundle(DisplaySignOrNursingItem item) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(NursingInfoDetailsFragment.BUNDLE_NURSING_INFO_TYPE, item);
        bundle.putLong(NursingInfoDetailsFragment.BUNDLE_NURSING_INFO_ID, mOldPeopleId);
        bundle.putString(NursingInfoDetailsFragment.BUNDLE_NURSING_INFO_NAME, mOldPeopleName);
        return bundle;
    }

    /**
     * 设置当前数据是否有删除
     */
    public void setModify(boolean modify) {
        this.modify = modify;
    }

    @Override
    public void finish() {
        if(modify) {
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
