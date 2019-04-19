package com.lefuorgn.lefu.activity;

import android.view.View;
import android.widget.TextView;

import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.dialog.ListDialog;
import com.lefuorgn.dialog.ProgressDialog;
import com.lefuorgn.interactive.Interactive;
import com.lefuorgn.interactive.interf.SyncCallback;
import com.lefuorgn.interactive.util.InteractiveUtils;
import com.lefuorgn.util.NetworkUtils;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.widget.togglebutton.ToggleButton;

import java.util.ArrayList;
import java.util.List;

/**
 * 同步设置
 */

public class SettingSyncActivity extends BaseActivity {

    private ToggleButton mToggleButton;
    private TextView mDataRange;
    private ListDialog<String> mListDialog;
    private String mOldSyncNote; // 记录上一次数据同步信息

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_sync;
    }

    @Override
    protected void initView() {
        mToggleButton = (ToggleButton) findViewById(R.id.tb_activity_setting_sync);
        mDataRange = (TextView) findViewById(R.id.tv_activity_setting_sync);
    }

    @Override
    protected void initData() {
        setToolBarTitle("网络设置");
        if(AppContext.isNetFlowAllowed()) {
            mToggleButton.setToggleOn();
        }else {
            mToggleButton.setToggleOff();
        }
        mDataRange.setText(String.format("同步  %s  数据", AppContext.getSyncDataForNote()));
        mOldSyncNote = AppContext.getSyncDataForNote();
        mDataRange.setOnClickListener(this);
        mToggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                AppContext.setNetFlowAllowed(on);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(AppContext.getInstance().getNetState() == NetworkUtils.NETWORK_NONE) {
            showToast("网络不可用");
            return;
        }
        if(mListDialog == null) {
            mListDialog = new ListDialog<String>();
            mListDialog.setTitle("时长")
                    .setGravity(ListDialog.LEFT);
            mListDialog.setData(getData());
            mListDialog.setCallBack(new ListDialog.Callback<String>() {
                        @Override
                        public void convert(TextView view, String title) {
                            view.setText(title);
                        }

                        @Override
                        public void onItemClick(View view, final String item) {
                            if(mOldSyncNote.equals(item)) {
                                return;
                            }
                            InteractiveUtils interactiveUtils = new InteractiveUtils(SettingSyncActivity.this);
                            interactiveUtils.setClickCallBack(new InteractiveUtils.ClickCallBack() {
                                @Override
                                public void sync() {
                                    // 记录当前被选中的条目
                                    mOldSyncNote = item;
                                    mDataRange.setText(String.format("同步  %s  数据", item));
                                    AppContext.setSyncDataForNote(item);
                                    AppContext.setSyncDataForLongTime(getSyncDataForLongTime(item));
                                    // 同时进行数据同步
                                    syncData();
                                }
                            });
                            interactiveUtils.start();

                        }
                    });
        }
        mListDialog.show(getSupportFragmentManager(), "ListDialog");
    }

    /**
     * 同步指定时间段的数据
     */
    private void syncData() {
        // 记录当前配置文件同步的时间
        AppContext.setSyncConfigTime(System.currentTimeMillis());
        final ProgressDialog bf = new ProgressDialog();
        Interactive interactive = new Interactive.Builder()
                .agencyId(AppContext.getInstance().getAgencyId())
                .configTable(true)
                .uploadTable(true) // 上传上传表中的数据
                .downloadTable(true) // 下载下载表的数据
                .clearDownloadTable(true) // 下载下载表数据前, 先清空数据表
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
            }

            @Override
            public void syncing() {
                showToast("数据正在同步中...");
            }
        });
    }

    /**
     * 获取同步设置数据加载时间段信息
     * @return 字符串集合
     */
    private List<String> getData() {
        List<String> data = new ArrayList<String>();
        data.add("近一天");
        data.add("近一周");
        data.add("近一月");
        data.add("近半年");
        data.add("近一年");
        data.add("所有");
        return data;
    }

    /**
     * 获取指定条目信息的时间
     * @param item 条目类型
     * @return long类型时间
     */
    private long getSyncDataForLongTime(String item) {
        long time;
        if("近一天".equals(item)) {
            time = StringUtils.getFirstNDays(1);
        }else if("近一周".equals(item)) {
            time = StringUtils.getFirstNDays(7);
        }else if("近半年".equals(item)) {
            time = StringUtils.getFirstNMonths(6);
        }else if("近一年".equals(item)) {
            time = StringUtils.getFirstNMonths(12);
        }else if("所有".equals(item)) {
            time = -28800000;
        }else {
            // 其他情况为近一个月
            time = StringUtils.getFirstNMonths(1);
        }
        return time;
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
