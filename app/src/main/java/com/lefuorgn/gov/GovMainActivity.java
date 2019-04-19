package com.lefuorgn.gov;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.lefuorgn.AppConfig;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.bean.GovMainTab;
import com.lefuorgn.bean.OrgInfo;
import com.lefuorgn.bean.User;
import com.lefuorgn.bean.UserItem;
import com.lefuorgn.gov.fragment.LefuFragment;
import com.lefuorgn.util.DoubleClickExitUtils;

import java.util.List;

/**
 * 集团或者政府页面
 */
public class GovMainActivity extends BaseActivity implements TabHost.OnTabChangeListener {

    private FragmentTabHost mTabHost;
    private DoubleClickExitUtils mDoubleClickExit;
    private TextView mRedDotView; // 通知页面指示器上的红点
    private String mName; // 指定用户类型名称
    private String mAgencyIds; // 指定用户类型下的ids; "1,2,3"

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.real_tab_content);
        if (android.os.Build.VERSION.SDK_INT > 10) {
            mTabHost.getTabWidget().setShowDividers(0);
        }
        initTabs();
        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(this);
        mTabHost.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        User user = AppContext.getInstance().getUser();
        mName = getGroupOrGovName(user);
        mAgencyIds = getGroupOrGovAgencyIds(user);
        mDoubleClickExit = new DoubleClickExitUtils(this);
        // 获取是否有用户查看某一通知的事件
        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.getBoolean("push", false)) {
            // 存在推送内容发送广播
            Intent broadcast = new Intent(AppConfig.INTENT_ACTION_NOTICE_MAIN_PAGE_OPEN);
            broadcast.putExtras(bundle);
            sendBroadcast(broadcast);
        }
    }

    private void initTabs() {
        GovMainTab[] tabs = GovMainTab.values();
        final int size = tabs.length;
        for (int i = 0; i < size; i++) {
            GovMainTab mainTab = tabs[i];
            TabHost.TabSpec tab = mTabHost.newTabSpec(getString(mainTab.getResName()));
            @SuppressLint("InflateParams")
            View indicator = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.tab_indicator, null);
            TextView title = (TextView) indicator.findViewById(R.id.tab_title);
            title.setCompoundDrawablesWithIntrinsicBounds(0, mainTab.getResIcon(), 0, 0);
            title.setText(getString(mainTab.getResName()));
            if(i == 0) {
                title.setTextColor(getResources().getColor(R.color.tabHost_text_color_select));
            }
            if(i==2){
                mRedDotView= (TextView) indicator.findViewById(R.id.tab_red_dot);
            }
            tab.setIndicator(indicator);
            mTabHost.addTab(tab, mainTab.getClz(), null);
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        final int id = mTabHost.getCurrentTab();
        final int size = mTabHost.getTabWidget().getTabCount();
        for (int i = 0; i < size; i++) {
            View v = mTabHost.getTabWidget().getChildAt(i);
            TextView title = (TextView) v.findViewById(R.id.tab_title);
            if (i == id) {
                // 设置选中状态
                v.setSelected(true);
                title.setTextColor(getResources().getColor(R.color.tabHost_text_color_select));
            } else {
                // 设置未选中状态
                v.setSelected(false);
                title.setTextColor(getResources().getColor(R.color.tabHost_text_color_normal));
            }
        }
    }

    /**
     * 跳转到通知页面
     */
    public void skipCircularFragment() {
        mTabHost.setCurrentTab(2);
    }

    /**
     * 重置通知任务
     */
    public void resetNoticeAll() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        LefuFragment lefuFragment = null;
        if(fragments != null) {
            for(Fragment fragment : fragments) {
                if(fragment instanceof LefuFragment) {
                    lefuFragment = (LefuFragment) fragment;
                    break;
                }
            }
        }
        if(lefuFragment != null) {
            lefuFragment.getNoticeAll();
        }
    }

    /**
     * 设置是否存在未读通知
     * @param state true: 存在未读状态; false: 不存在未读状态
     */
    public void setRedDotState(boolean state){
        if(state){
            mRedDotView.setVisibility(View.VISIBLE);
        }else{
            mRedDotView.setVisibility(View.GONE);
        }

    }

    /**
     * 获取指定类型的名称
     * @return 返回指定的类型名称
     */
    public String getName() {
        return mName;
    }

    /**
     * 返回指定类型下的机构集合
     * @return 集合内容; "1,2,3"
     */
    public String getAgencyIds() {
        return mAgencyIds;
    }

    /**
     * 获取集团或政府页面当前显示的名称
     * @return 指定的名称
     */
    private String getGroupOrGovName(User user) {
        if(user == null) {
            return "";
        }
        if(user.isGroup()) {
            // 集团优先级最高
            return user.getGroupOrgName();
        }else if(user.getAgency_id() > 0) {
            // 机构优先级次之
            return user.getAgencyName();
        }else {
            // 政府优先级最后
            return user.getGovOrgName();
        }
    }

    /**
     * 获取集团或政府页面当前指定的机构ids
     * @return 指定的机构ids
     */
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

    /**
     * 监听返回--是否退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
            return mDoubleClickExit.onKeyDown(keyCode, event);
    }

}
