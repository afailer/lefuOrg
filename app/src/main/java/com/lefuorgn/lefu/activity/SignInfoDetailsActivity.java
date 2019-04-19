package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.base.BaseViewPagerAdapter;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.db.util.SignConfigManager;
import com.lefuorgn.lefu.fragment.SignInfoDetailsFragment;
import com.lefuorgn.widget.PagerSlidingTabStrip;

import java.util.List;

/**
 * 体征信息详情页
 */

public class SignInfoDetailsActivity extends BaseActivity {

    private PagerSlidingTabStrip mTabStrip;
    private ViewPager mViewPager;
    private ImageView mChartView;

    private BaseViewPagerAdapter mAdapter;

    private String mItemName; // 当前选中的条目类型

    private long mOldPeopleId;

    private boolean modify; // 有数据删除了


    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_info_details;
    }

    @Override
    protected void initView() {
        mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.psts_activity_sign_info_details);
        mViewPager = (ViewPager) findViewById(R.id.vp_activity_sign_info_details);
        mChartView = setMenuImageView(R.mipmap.chart_sign);
        mTabStrip.setOnPagerChange(new PagerSlidingTabStrip.OnPagerChangeLis() {
            @Override
            public void onChanged(int page) {
                mItemName = mAdapter.getPageTitle(page).toString();
                mChartView.setVisibility(isShowChartImg(mItemName) ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mOldPeopleId = intent.getLongExtra("id", 0);
        String name = intent.getStringExtra("name");
        setToolBarTitle(name);
        // 获取当前需要显示的条目
        List<DisplaySignOrNursingItem> items = SignConfigManager.getSignItem();
        if(items.size() > 0) {
            mItemName = items.get(0).getTitle();
            mChartView.setVisibility(isShowChartImg(mItemName) ? View.VISIBLE : View.GONE);
        }
        mAdapter = new BaseViewPagerAdapter(getSupportFragmentManager(), mTabStrip, mViewPager);
        for (DisplaySignOrNursingItem item : items) {
            mAdapter.addTab(item.getTitle(), SignInfoDetailsFragment.class, getBundle(item));
        }
    }

    private Bundle getBundle(DisplaySignOrNursingItem item) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(SignInfoDetailsFragment.BUNDLE_SIGN_INFO_TYPE, item);
        bundle.putLong(SignInfoDetailsFragment.BUNDLE_SIGN_INFO_ID, mOldPeopleId);
        return bundle;
    }

    @Override
    protected void onMenuClick(View v) {
        Intent intent = new Intent(this, SignGraphicDataActivity.class);
        intent.putExtra("name", mItemName);
        intent.putExtra("id", mOldPeopleId);
        startActivity(intent);
    }

    /**
     * 是否显示图标页面跳转按钮
     * @param type 当前类型
     * @return true: 显示; false: 不显示
     */
    private boolean isShowChartImg(String type) {
        if("血压".equals(type)) {
            return true;
        }else if("体温".equals(type)) {
            return true;
        }else if("血糖".equals(type)) {
            return true;
        }else if("心率".equals(type)) {
            return true;
        }
        return false;
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
