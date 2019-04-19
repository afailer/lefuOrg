package com.lefuorgn.gov.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.GovApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.base.BaseViewPagerAdapter;
import com.lefuorgn.gov.bean.NewsType;
import com.lefuorgn.gov.fragment.LeaderNewsFragment;
import com.lefuorgn.util.TLog;
import com.lefuorgn.widget.PagerSlidingTabStrip;

import java.util.List;

public class AllNewsActivity extends BaseActivity {

    private PagerSlidingTabStrip strip;
    private ViewPager pager;
    private BaseViewPagerAdapter adapter;
    private long mGovId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_all_news;
    }

    @Override
    protected void initView() {
        setToolBarTitle("民生政话");
        mGovId = getIntent().getLongExtra("govId", 0);
        strip = (PagerSlidingTabStrip) findViewById(R.id.newsTypeStrap);
        pager = (ViewPager) findViewById(R.id.newsPager);
    }

    @Override
    protected void initData() {
        adapter = new BaseViewPagerAdapter(getSupportFragmentManager(), strip, pager);
        GovApi.requestNewsType(mGovId, new RequestCallback<List<NewsType>>() {
            @Override
            public void onSuccess(List<NewsType> result) {
                TLog.error(result.toString());
                adapter.addTab("所有", LeaderNewsFragment.class, getBundle(0));
                for(NewsType newType : result){
                    if("".equals(newType.getName())){
                        newType.setName(newType.getId() + "+" + newType.getOrganize_id());
                    }
                    adapter.addTab(newType.getName(), LeaderNewsFragment.class, getBundle(newType.getId()));
                }
            }

            @Override
            public void onFailure(ApiHttpException e) {
                adapter.addTab("所有", LeaderNewsFragment.class, getBundle(0));
                showToast(e.getMessage());
            }
        });
    }

    private Bundle getBundle(long id) {
        Bundle bundle = new Bundle();
        bundle.putLong("typeId", id);
        bundle.putLong("govId", mGovId);
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
