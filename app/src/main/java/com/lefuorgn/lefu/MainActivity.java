package com.lefuorgn.lefu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.lefuorgn.AppConfig;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.bean.MainFirstPageTab;
import com.lefuorgn.bean.MainTwoPageTab;
import com.lefuorgn.db.util.DaoHelper;
import com.lefuorgn.dialog.ProgressDialog;
import com.lefuorgn.interactive.Interactive;
import com.lefuorgn.interactive.interf.SyncCallback;
import com.lefuorgn.util.DoubleClickExitUtils;
import com.lefuorgn.util.NetworkUtils;
import com.lefuorgn.util.UpdateUtils;

/**
 * 机构详情展示入口页面
 */
public class MainActivity extends BaseActivity implements OnTabChangeListener{

    /**
     * 当前页面标记位
     */
    public static final String FIRST_LEVEL_PAGE = "FirstLevelPage";
    /**
     * 当期机构的机构ID
     */
    public static final String INTENT_AGENCY_ID = "intent_agency_id";
    /**
     * 当前机构的机构名称
     */
    public static final String INTENT_AGENCY_NAME = "intent_agency_name";

    private FragmentTabHost mTabHost;

    private DoubleClickExitUtils mDoubleClickExit;

    private boolean mFirstLevelPage; // 当前页面是否是一级页面

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        // 先拿到当前页面是否是一级页面
        mFirstLevelPage = getIntent().getBooleanExtra(FIRST_LEVEL_PAGE, true);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.real_tab_content);
        if (android.os.Build.VERSION.SDK_INT > 10) {
            mTabHost.getTabWidget().setShowDividers(0);
        }
        initTabs();

        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(this);
        mTabHost.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        // 初始化双击退出工具类
        if(mFirstLevelPage) {
            mDoubleClickExit = new DoubleClickExitUtils(this);
        }else {
            // 初始化返回按钮
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl_activity_tab_host);
            @SuppressLint("InflateParams")
            View indicator = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.tab_indicator, null);
            indicator.setBackgroundColor(Color.parseColor("#FFD301"));
            TextView title = (TextView) indicator.findViewById(R.id.tab_title);
            MainTwoPageTab mainTab = MainTwoPageTab.values()[2];
            title.setCompoundDrawablesWithIntrinsicBounds(0, mainTab.getResIcon(), 0, 0);
            title.setText(getString(mainTab.getResName()));
            title.setTextColor(Color.WHITE);
            // 获取屏幕宽
            Display display = getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            int width = metrics.widthPixels / 3;
            int height = getResources().getDimensionPixelSize(R.dimen.tabHost_height);
            // 设置布局参数
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            indicator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            // 添加返回按钮到指示条目中
            rl.addView(indicator, params);
        }
    }

    @Override
    protected void initData() {
        // 获取是否有用户查看某一通知的事件
        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.getBoolean("push", false)) {
            // 存在推送内容发送广播
            Intent broadcast = new Intent(AppConfig.INTENT_ACTION_NOTICE_MAIN_PAGE_OPEN);
            broadcast.putExtras(bundle);
            sendBroadcast(broadcast);
        }
        if(mFirstLevelPage) {
            // 一级页面
            syncData(0);
        }else {
            // 二级页面
            Intent intent = getIntent();
            long id = intent.getLongExtra(INTENT_AGENCY_ID, 0);
            String name = intent.getStringExtra(INTENT_AGENCY_NAME);
            String dataBaseName = "lefuOrg" + id + ".db";
            DaoHelper.getInstance().reset(dataBaseName, id, name);
            syncData(id);
        }
    }

    /**
     * 同步数据
     * @param id 机构ID
     */
    private void syncData(final long id) {
        if(AppContext.getInstance().getNetState() == NetworkUtils.NETWORK_NONE) {
            showToast("网络不可用");
            return;
        }
        if(AppContext.isFirstLoadingData(id)) {
            final ProgressDialog bf = new ProgressDialog();
            // 记录当前配置文件同步的时间
            AppContext.setSyncConfigTime(System.currentTimeMillis());
            // 数据库首次加载数据
            Interactive interactive = new Interactive.Builder()
                    .agencyId(AppContext.getInstance().getAgencyId())
                    .configTable(true)
                    .downloadTable(true)
                    .elderlyRelatedTable(true)
                    .runningService(false)
                    .build();
            interactive.newSync().enqueue(new SyncCallback() {
                @Override
                public void onStart() {
                    bf.show(getSupportFragmentManager(), "");
                }

                @Override
                public void onLoading(final long count, final long current, final String info) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bf.setProgressBar((int) current, (int) count);
                            bf.setNote(info);
                        }
                    });
                }

                @Override
                public void onStop() {
                    bf.dismiss();
                    AppContext.setFirstLoadingData(id, false);
                }

                @Override
                public void syncing() {
                    showToast("数据正在同步中...");
                }
            });
        }else if (mFirstLevelPage) {
            // 后台同步
            syncBackground();
            // 应用是否有更新
            UpdateUtils uu = new UpdateUtils(this, false);
            uu.checkUpdate();
        }else {
            // 后台同步
            syncBackground();
        }
    }

    /**
     * 进行数据同步
     */
    public void syncBackground() {
        if(AppContext.getInstance().getNetState() == NetworkUtils.NETWORK_NONE) {
            return;
        }
        long time = System.currentTimeMillis()- AppContext.getSyncConfigTime();
        boolean flag = time >= AppConfig.SYNC_CONFIG_TIME;
        if(flag) {
            // 同步配置,刷新上一次同步配置的时间
            AppContext.setSyncConfigTime(System.currentTimeMillis());
        }
        Interactive interactive = new Interactive.Builder()
                .agencyId(AppContext.getInstance().getAgencyId())
                .configTable(flag)
                .uploadTable(true)
                .downloadTable(true)
                .elderlyRelatedTable(true)
                .runningService(true)
                .build();
        interactive.newSync().enqueue(null);
    }

    private void initTabs() {
        if(mFirstLevelPage) {
            // 一级页面
            MainFirstPageTab[] tabs = MainFirstPageTab.values();
            final int size = tabs.length;
            for (int i = 0; i < size; i++) {
                MainFirstPageTab mainTab = tabs[i];
                TabSpec tab = mTabHost.newTabSpec(getString(mainTab.getResName()));
                @SuppressLint("InflateParams")
                View indicator = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.tab_indicator, null);
                TextView title = (TextView) indicator.findViewById(R.id.tab_title);
                title.setCompoundDrawablesWithIntrinsicBounds(0, mainTab.getResIcon(), 0, 0);
                title.setText(getString(mainTab.getResName()));
                if(i == 0) {
                    title.setTextColor(getResources().getColor(R.color.tabHost_text_color_select));
                }
                tab.setIndicator(indicator);
                mTabHost.addTab(tab, mainTab.getClz(), null);
            }
        }else {
            // 二级页面
            MainTwoPageTab[] tabs = MainTwoPageTab.values();
            final int size = tabs.length;
            for (int i = 0; i < size; i++) {
                MainTwoPageTab mainTab = tabs[i];
                TabSpec tab = mTabHost.newTabSpec(getString(mainTab.getResName()));
                @SuppressLint("InflateParams")
                View indicator = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.tab_indicator, null);
                TextView title = (TextView) indicator.findViewById(R.id.tab_title);
                title.setCompoundDrawablesWithIntrinsicBounds(0, mainTab.getResIcon(), 0, 0);
                title.setText(getString(mainTab.getResName()));
                if(i == 0) {
                    title.setTextColor(getResources().getColor(R.color.tabHost_text_color_select));
                }
                tab.setIndicator(indicator);
                mTabHost.addTab(tab, mainTab.getClz(), null);
            }
        }

    }

    @Override
    public void onTabChanged(String tabId) {
        // 监听Tab是否发生改变
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
     * 监听返回--是否退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mFirstLevelPage && keyCode == KeyEvent.KEYCODE_BACK) {
            // 是否退出应用
            return mDoubleClickExit.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

}
