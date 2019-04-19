package com.lefuorgn.oa.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.OaApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.base.BaseViewPagerAdapter;
import com.lefuorgn.bean.OrgInfo;
import com.lefuorgn.bean.User;
import com.lefuorgn.bean.UserItem;
import com.lefuorgn.oa.bean.DepartmentList;
import com.lefuorgn.oa.fragment.SelectStuffByNameFragment;
import com.lefuorgn.oa.fragment.SelectStuffInDeptFragment;
import com.lefuorgn.oa.interf.StuffInterface;
import com.lefuorgn.widget.PagerSlidingTabStrip;

import java.util.List;

public class SelectStuffActivity extends BaseActivity {
    private PagerSlidingTabStrip mTabStrip;
    private ViewPager mViewPager;
    private BaseViewPagerAdapter mAdapter;
    boolean isOnlyShowMyOrg=false;
    public static final String DIVIDE_STR="&#@!";

    @Override
    protected void initView() {
        super.initView();
        setToolBarTitle("员工列表");
        mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.psts_activity_contact);
        mViewPager = (ViewPager) findViewById(R.id.vp_activity_contact);
    }
    String isCopy="";
    @Override
    protected void initData() {
        super.initData();
        isCopy= getIntent().getStringExtra("isCopy");
        mAdapter = new BaseViewPagerAdapter(getSupportFragmentManager(), mTabStrip, mViewPager);
        String attendance = getIntent().getStringExtra("attendance");
        Bundle bundle = new Bundle();
        bundle.putString("attendance","attendance");
        if("attendance".equals(attendance)){
            bundle.putString("attendance","attendance");
        }
        mAdapter.addTab("全部", SelectStuffByNameFragment.class, bundle);
        mAdapter.addTab("部门", SelectStuffInDeptFragment.class, bundle);
        isOnlyShowMyOrg = getIntent().getBooleanExtra("isOnlyShowMyOrg",false);
        loadStuffList(isOnlyShowMyOrg);
    }

    /**
     * 拿到审核员工列表，type：1:按机构id（如果是机构，就可以直接传3，服务器自己拿当前机构id），2:按组织id，3:按用户持有机构
     */
    private void loadStuffList(boolean isOnlyShowMyOrg){
        AppContext instance = AppContext.getInstance();
        int type=3;
        String ids;
        if(isOnlyShowMyOrg){
            type=1;
            ids=instance.getUser().getAgency_id()+"";
        }else if(instance.getUser().isGroup()){
            type=2;
            ids=instance.getUser().getGroupOrg_id()+"";
        }else{
            type=3;
            ids="";
        }
        OaApi.queryStuffList(type, ids, new RequestCallback<List<DepartmentList>>() {
            @Override
            public void onSuccess(List<DepartmentList> result) {
                RefreshStuffResult(result);
            }

            @Override
            public void onFailure(ApiHttpException e) {

            }
        });
    }
    public void RefreshStuffResult(List<DepartmentList> departmentList){
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for(Fragment fragment:fragments){
            if(fragment instanceof StuffInterface){
                if("isCopy".equals(isCopy)) {
                    ((StuffInterface) fragment).setStuff(departmentList, true);
                }else{
                    ((StuffInterface) fragment).setStuff(departmentList, false);
                }
            }
        }
    }
    private String getGroupOrGovAgencyIds(User user) {
        if(user == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if(user.isGroup()) {
            // 集团优先级最高
            for (UserItem userItem : user.getGroupOrg()) {
                for (OrgInfo orgInfo : userItem.getTblOrganizeAgencyMapBeans()) {
                    sb.append(orgInfo.getAgency_id()).append(",");
                }
            }
        }else if(user.getAgency_id() > 0) {
            // 机构优先级次之
            for (OrgInfo orgInfo : user.getAgencys()) {
                sb.append(orgInfo.getAgency_id()).append(",");
            }
        }else {
            // 政府优先级最后
            for (UserItem userItem : user.getGovOrg()) {
                for (OrgInfo orgInfo : userItem.getTblOrganizeAgencyMapBeans()) {
                    sb.append(orgInfo.getAgency_id()).append(",");
                }
            }
        }
        if(sb.toString().length() > 0) {
            return sb.substring(0, sb.length() - 1);
        }else {
            return "";
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

    @Override
    protected int getLayoutId() {
        return R.layout.select_stuff;
    }
}
