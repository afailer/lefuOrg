package com.lefuorgn.lefu.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseRecyclerViewActivity;
import com.lefuorgn.db.util.PermissionManager;
import com.lefuorgn.lefu.adapter.LogbookGroupAdapter;
import com.lefuorgn.lefu.adapter.LogbookTypeAdapter;
import com.lefuorgn.lefu.bean.Logbook;
import com.lefuorgn.lefu.bean.LogbookGroup;
import com.lefuorgn.lefu.bean.LogbookType;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有老人交班记录页面
 */

public class LogbookActivity extends BaseRecyclerViewActivity<Logbook> {

    private TextView mGroupView, mTypeView;
    private TextView mAddBtn;
    private PopupWindow mGroupPopupWindow, mTypePopupWindow;
    private View mLineView;

    private View mGroupPopView, mTypePopView;

    private LogbookGroupAdapter mGroupPopAdapter;

    private int mWidth, mHeight;

    /**
     * 首次加载数据记录状态
     */
    private boolean first;
    private LogbookGroup mCurrentLogbookGroup;
    private LogbookType mCurrentLogbookType;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_logbook;
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_activity_logbook_info;
    }

    @Override
    protected void initChildView() {
        mGroupView = (TextView) findViewById(R.id.tv_activity_logbook_group);
        mTypeView = (TextView) findViewById(R.id.tv_activity_logbook_type);
        mLineView = findViewById(R.id.v_activity_logbook);
        mGroupView.setOnClickListener(this);
        mTypeView.setOnClickListener(this);
        mAddBtn = (TextView) findViewById(R.id.tv_activity_logbook_btn);
        initGroupPopupWindow();
    }

    /**
     * 初始化分组PopupWindow视图
     */
    @SuppressLint("InflateParams")
    private void initGroupPopupWindow() {
        mGroupPopView = getLayoutInflater().inflate(R.layout.popup_window_logbook, null);
        RecyclerView rv = (RecyclerView) mGroupPopView.findViewById(R.id.rv_popup_window_logbook);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.white)));
        mGroupPopAdapter = new LogbookGroupAdapter();
        rv.setAdapter(mGroupPopAdapter);
        mGroupPopAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                LogbookGroup group = mGroupPopAdapter.getItem(i);
                if(mCurrentLogbookGroup != null && group.getId() != mCurrentLogbookGroup.getId()) {
                    mCurrentLogbookGroup = group;
                    resetResult();
                }
                if(mGroupPopupWindow != null && mGroupPopupWindow.isShowing()) {
                    mGroupPopupWindow.dismiss();
                }
            }
        });
    }

    /**
     * 初始化分类PopupWindow视图
     */
    @SuppressLint("InflateParams")
    private void initTypePopupWindow(List<LogbookType> data) {
        mTypePopView = getLayoutInflater().inflate(R.layout.popup_window_logbook, null);
        RecyclerView rv = (RecyclerView) mTypePopView.findViewById(R.id.rv_popup_window_logbook);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.white)));
        mCurrentLogbookType = data.get(0);
        final LogbookTypeAdapter adapter = new LogbookTypeAdapter(data);
        rv.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                LogbookType type = (LogbookType) adapter.getData().get(i);
                if(type.getId() != mCurrentLogbookType.getId()) {
                    mCurrentLogbookType = type;
                    resetResult();
                }
                if(mTypePopupWindow != null && mTypePopupWindow.isShowing()) {
                    mTypePopupWindow.dismiss();
                }
            }
        });

    }

    /**
     * 更新条件控件的内容
     */
    private void refreshView() {
        if(mCurrentLogbookGroup != null) {
            mGroupView.setText(mCurrentLogbookGroup.getGroup_name());
            mTypeView.setText(mCurrentLogbookType.getContent());
        }
    }

    @Override
    protected void initChildData() {
        setToolBarTitle("交班记录");
        first = true;
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        // 获取屏幕的宽高
        mWidth = outMetrics.widthPixels / 5;
        mHeight = outMetrics.heightPixels / 3;
        // 获取用户是否拥有添加交班信息的权限
        if(PermissionManager.hasPermission(PermissionManager.LOGBOOK + PermissionManager.P_C)) {
            // 有添加权限
            mAddBtn.setOnClickListener(this);
        }else {
            // 没有添加权限
            mAddBtn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initListener(final BaseAdapter adapter) {
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Logbook logbook = (Logbook) adapter.getData().get(position);
                Intent intent = new Intent(LogbookActivity.this, LogbookInfoDetailsActivity.class);
                intent.putExtra("name", mCurrentLogbookGroup.getGroup_name());
                intent.putExtra("logbook", logbook);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void loadData(final int pageNo) {
        // 首次加载,先加载分组信息
        if(first) {
            LefuApi.getHandOverTypeList(new RequestCallback<List<LogbookType>>() {
                @Override
                public void onSuccess(List<LogbookType> result) {
                    result.add(0,new LogbookType(0,"全部",1));
                    initTypePopupWindow(result);
                    // 加载交班分组信息
                    LefuApi.getLogbookGroup(new RequestCallback<List<LogbookGroup>>() {
                        @Override
                        public void onSuccess(List<LogbookGroup> result) {
                            if(result == null || result.size() == 0) {
                                setResult(pageNo, new ArrayList<Logbook>());
                                return;
                            }
                            mGroupPopAdapter.setNewData(result);
                            mCurrentLogbookGroup = result.get(0);
                            first = false;
                            loadLogbook(pageNo, mCurrentLogbookGroup.getId(), mCurrentLogbookType.getId());
                        }

                        @Override
                        public void onFailure(ApiHttpException e) {
                            setResult(pageNo, new ArrayList<Logbook>());
                            showToast(e.getMessage());
                        }
                    });
                }

                @Override
                public void onFailure(ApiHttpException e) {
                    setResult(pageNo, new ArrayList<Logbook>());
                    showToast(e.getMessage());
                }
            });

        }else {
            loadLogbook(pageNo, mCurrentLogbookGroup.getId(), mCurrentLogbookType.getId());
        }
    }

    /**
     * 获取指定分组以及类型下所有老人的交班记录情况
     * @param pageNo 当前页面
     * @param groupId 分组ID
     * @param type 类型ID
     */
    private void loadLogbook(final int pageNo, long groupId, long type) {
        LefuApi.getLogbookInfo(groupId, pageNo, type, new RequestCallback<List<Logbook>>() {
            @Override
            public void onSuccess(List<Logbook> result) {
                refreshView();
                setResult(pageNo, result);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                setResult(pageNo, new ArrayList<Logbook>());
                showToast(e.getMessage());
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder holder, Logbook logbook) {
        // 获取当前条目的位置
        int position = holder.getLayoutPosition() - mBaseAdapter.getHeaderViewsCount();
        boolean display;
        if(position == 0) {
            display = true;
        }else {
            // 拿到前一个数据
            Logbook preLogbook = mBaseAdapter.getItem(position - 1);
            display = isDisplay(logbook.getInspect_time(), preLogbook.getInspect_time());
        }
        holder.setText(R.id.logbook_title_time, getStringNote(logbook.getInspect_time()))
                .setVisible(R.id.logbook_title_time, display)
                .setText(R.id.tv_item_activity_logbook_info_name, logbook.getOld_people_name())
                .setText(R.id.tv_item_activity_logbook_info_date,
                        StringUtils.getFormatData(logbook.getInspect_time(), "yyyy.MM.dd HH:mm"))
                .setText(R.id.tv_item_activity_logbook_info_type, logbook.getCare_name())
                .setText(R.id.tv_item_activity_logbook_info_content, logbook.getContent());
    }

    /**
     * 判断当前条目是否显示指示字段
     * @param time1 当期条目显示的时间
     * @param time2 前一个条目显示的时间
     * @return true： 显示； false： 不显示
     */
    private boolean isDisplay(long time1, long time2) {
        // 前天凌晨12点
        long time = StringUtils.getFirstNDays(2);
        if(time1 < time) {
            return time2 >= time;
        }
        String timeStr1 = StringUtils.getFormatData(time1, "yyyy-MM-dd");
        String timeStr2 = StringUtils.getFormatData(time2, "yyyy-MM-dd");
        return !timeStr1.equals(timeStr2);
    }

    private String getStringNote(long time) {
        if(time >= StringUtils.getFirstNDays(0)) {
            return "今天";
        }else if(time >= StringUtils.getFirstNDays(1)) {
            return "昨天";
        }else if(time >= StringUtils.getFirstNDays(2)) {
            return "前天";
        }else {
            return "三天前";
        }
    }

    /**
     * 显示分组PopupWindow
     * @param contentView 展示内容控件
     */
    @SuppressWarnings("deprecation")
    private void showGroupPopWindow(View contentView) {
        if(mGroupPopupWindow == null) {
            // 创建PopupWindow
            mGroupPopupWindow = new PopupWindow(contentView, mWidth * 3, mHeight, true);
            mGroupPopupWindow.setFocusable(true);
            mGroupPopupWindow.setOutsideTouchable(true);
            mGroupPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mGroupPopupWindow.showAsDropDown(mLineView);
        }else {
            mGroupPopupWindow.showAsDropDown(mLineView);
        }
    }

    /**
     * 显示类型PopupWindow
     * @param contentView 展示内容控件
     */
    @SuppressWarnings("deprecation")
    private void showTypePopWindow(View contentView) {
        if(mTypePopupWindow == null) {
            // 创建PopupWindow
            mTypePopupWindow = new PopupWindow(contentView, mWidth * 2, mHeight, true);
            mTypePopupWindow.setFocusable(true);
            mTypePopupWindow.setOutsideTouchable(true);
            mTypePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mTypePopupWindow.showAsDropDown(mLineView, mWidth * 3, 0);
        }else {
            mTypePopupWindow.showAsDropDown(mLineView, mWidth * 3, 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_activity_logbook_group :
                // 分组按钮触发事件
                showGroupPopWindow(mGroupPopView);
                break;
            case R.id.tv_activity_logbook_type :
                // 分类按钮触发事件
                showTypePopWindow(mTypePopView);
                break;
            case R.id.tv_activity_logbook_btn :
                // 添加按钮触发事件
                if(mCurrentLogbookGroup == null) {
                    showToast("未获取到老人分组");
                    return;
                }
                Intent intent = new Intent(this, LogbookAddActivity.class);
                intent.putExtra("logbookGroup", mCurrentLogbookGroup);
                startActivityForResult(intent,100);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==200){
            //添加数据成功，刷新数据
            onRefresh();
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
