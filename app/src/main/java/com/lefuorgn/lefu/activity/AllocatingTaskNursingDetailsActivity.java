package com.lefuorgn.lefu.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.util.LongSparseArray;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.db.util.OldPeopleManager;
import com.lefuorgn.db.util.SignConfigManager;
import com.lefuorgn.dialog.AlertDialog;
import com.lefuorgn.lefu.adapter.AllocatingTaskAdapter;
import com.lefuorgn.lefu.adapter.PersonalDataSearchAdapter;
import com.lefuorgn.lefu.fragment.AllocatingTaskNursingFragment;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据审核页面
 */

public class AllocatingTaskNursingDetailsActivity extends BaseActivity {

    private static final String format = "yyyy-MM-dd";
    /**
     * 搜索类型: 姓名
     */
    private static final int SEARCH_NAME = 1;
    /**
     * 搜索类型: 床位号
     */
    private static final int SEARCH_BED_NO = 2;
    /**
     * 搜索类型: 身份证号
     */
    private static final int SEARCH_ID_CARD = 3;

    private int searchType = SEARCH_NAME; // 查找类型, 默认是姓名

    private TextView mErrorView;
    private TextView mConditionBtn;
    private EditText mSearchView;
    private PagerSlidingTabStrip mTabStrip;
    private ViewPager mViewPager;

    private PopupWindow mPopupWindow; // 搜索条件
    private View mConditionView; // 搜索条件视图
    private View mContentView; // 搜索内容视图

    private LongSparseArray<Boolean> refreshBit; // 用于存放条目刷新位
    private long mTime; // 配单时间
    private long mUserId; // 当前用户ID

    private List<OldPeople> mOldPeoples; // 所有老人列表

    /**
     * 搜索内容展示适配器
     */
    private PersonalDataSearchAdapter mAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_allocating_task_nursing_details;
    }

    @Override
    protected void initView() {
        mErrorView = (TextView) findViewById(R.id.tv_activity_allocating_task_nursing_details_error);
        mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.psts_activity_allocating_task_nursing_details);
        mViewPager = (ViewPager) findViewById(R.id.vp_activity_allocating_task_nursing_details);
        findViewById(R.id.btn_activity_allocating_task_nursing_details).setOnClickListener(this);
        mConditionBtn = (TextView) findViewById(R.id.tv_activity_allocating_task_nursing_details_condition);
        mSearchView = (EditText) findViewById(R.id.et_activity_allocating_task_nursing_details);
        initConditionPopWindow();
        initContentPopWindow();

        mConditionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContentView.getVisibility() == View.VISIBLE) {
                    mContentView.setVisibility(View.GONE);
                }
                showPopWindow(mConditionView, mConditionBtn);
            }
        });

        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                if("".equals(s.toString())) {
                    if(mContentView.getVisibility() == View.VISIBLE) {
                        mContentView.setVisibility(View.GONE);
                    }
                    return;
                }
                if(mContentView.getVisibility() == View.GONE) {
                    mContentView.setVisibility(View.VISIBLE);
                }
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    /**
     * 初始化条件展示控件
     */
    @SuppressLint("InflateParams")
    private void initConditionPopWindow() {
        mConditionView = getLayoutInflater().inflate(R.layout.popup_window_personal_data_condition, null);
        mConditionView.findViewById(R.id.v_popup_window_personal_data_condition)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closePopWindow();
                    }
                });
        mConditionView.findViewById(R.id.v_popup_window_personal_data_name)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 条件选择为姓名
                        if(searchType != SEARCH_NAME) {
                            searchType = SEARCH_NAME;
                            mConditionBtn.setText("姓名");
                        }
                        mSearchView.setText("");
                        closePopWindow();
                    }
                });
        mConditionView.findViewById(R.id.v_popup_window_personal_data_bed_no)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 条件选择为床位号
                        if(searchType != SEARCH_BED_NO) {
                            searchType = SEARCH_BED_NO;
                            mConditionBtn.setText("床位号");
                        }
                        mSearchView.setText("");
                        closePopWindow();
                    }
                });
        mConditionView.findViewById(R.id.v_popup_window_personal_data_id_card)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 条件选择为身份证号
                        if(searchType != SEARCH_ID_CARD) {
                            searchType = SEARCH_ID_CARD;
                            mConditionBtn.setText("身份证号");
                        }
                        mSearchView.setText("");
                        closePopWindow();
                    }
                });
    }

    /**
     * 初始化老人选择PopWindow控件
     */
    private void initContentPopWindow() {
        mContentView = findViewById(R.id.ll_popup_window_allocating_task_content);
        findViewById(R.id.v_popup_window_allocating_task_content)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContentView.setVisibility(View.GONE);
                    }
                });
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_popup_window_allocating_task_content);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
        mAdapter = new PersonalDataSearchAdapter();
        rv.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                OldPeople oldPeople = mAdapter.getItem(i);
                mContentView.setVisibility(View.GONE);
                hideKeyBoard(view);
                // 跳转到测量任务页面
                Intent intent = new Intent(AllocatingTaskNursingDetailsActivity.this, MeasurementTaskActivity.class);
                intent.putExtra("oldPeople", oldPeople);
                intent.putExtra("time", mTime);
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 200) {
            for (int i = 0; i < refreshBit.size(); i++) {
                long key = refreshBit.keyAt(i);
                refreshBit.put(key, true);
            }
        }
    }

    @Override
    protected void initData() {
        mOldPeoples = OldPeopleManager.getOldPeople(false);
        mTime = getIntent().getLongExtra("time", 0);
        setToolBarTitle(StringUtils.getFormatData(mTime, format));
        mUserId = AppContext.getInstance().getUser().getUser_id();
        List<DisplaySignOrNursingItem> items = SignConfigManager.getNursingItem();
        if(items.size() == 0) {
            // 没有护理条目项
            mErrorView.setVisibility(View.VISIBLE);
            mErrorView.setText("服务器没有配置指定项");
            return;
        }
        AllocatingTaskAdapter adapter = new AllocatingTaskAdapter(
                getSupportFragmentManager(), mTabStrip, mViewPager);
        refreshBit = new LongSparseArray<Boolean>();
        for (DisplaySignOrNursingItem item : items) {
            refreshBit.put(item.getType(), false);
            adapter.addTab(item.getTitle(), AllocatingTaskNursingFragment.class, getBundle(item));
        }
    }

    private Bundle getBundle(DisplaySignOrNursingItem item) {
        Bundle bundle = new Bundle();
        bundle.putLong(AllocatingTaskNursingFragment.BUNDLE_DATA_ALLOCATING_TIME, mTime);
        bundle.putSerializable(AllocatingTaskNursingFragment.BUNDLE_DATA_ALLOCATING_TYPE, item);
        return bundle;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_activity_allocating_task_nursing_details) {
            // 发布任务
            new AlertDialog().setContent("确定发布任务吗?")
                    .setCancelBtnText("确定")
                    .setConfirmBtnText("取消")
                    .setClickCallBack(new AlertDialog.ClickCallBack() {
                        @Override
                        public void cancel() {
                            // 确认按钮点击
                            LefuApi.releaseAllocatingTask(mTime, mUserId, new RequestCallback<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    showToast("任务发布成功");
                                    closeActivity();
                                }

                                @Override
                                public void onFailure(ApiHttpException e) {
                                    showToast(e.getMessage());
                                    closeActivity();
                                }
                            });
                        }

                        @Override
                        public void confirm() {
                            // 取消按钮点击
                        }
                    }).show(getSupportFragmentManager(), "AlertDialog");
        }
    }

    /**
     * 通过字段过滤老人列表
     * @param filterStr 指定的字符串
     */
    private void filterData(String filterStr) {
        List<OldPeople> filterDateList = new ArrayList<OldPeople>();
        filterStr = filterStr.toLowerCase();
        if(searchType == SEARCH_NAME) {
            // 按老人姓名进行查找
            for (OldPeople o : mOldPeoples) {
                if (o.getCharacters().startsWith(filterStr) ||
                        o.getFullPinYin().startsWith(filterStr) ||
                        o.getInitial().startsWith(filterStr)) {
                    filterDateList.add(o);
                }
            }
        }else if(searchType == SEARCH_BED_NO) {
            for (OldPeople o : mOldPeoples) {
                if (o.getBed_no().startsWith(filterStr)) {
                    filterDateList.add(o);
                }
            }
        }else if(searchType == SEARCH_ID_CARD) {
            for (OldPeople o : mOldPeoples) {
                if (o.getDocument_number().startsWith(filterStr)) {
                    filterDateList.add(o);
                }
            }
        }
        mAdapter.setNewData(filterDateList);
    }

    /**
     * 显示PopupWindow
     * @param contentView 展示内容控件
     * @param view 依附的控件
     */
    @SuppressWarnings("deprecation")
    private void showPopWindow(View contentView, View view) {
        // 创建PopupWindow
        if(mPopupWindow == null) {
            mPopupWindow = new PopupWindow(contentView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, true);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.showAsDropDown(view, 0, StringUtils.dip2px(this, 10));
        }else {
            mPopupWindow.showAsDropDown(view, 0, StringUtils.dip2px(this, 10));
        }

    }

    /**
     * 关闭PopupWindow
     */
    private void closePopWindow() {
        if(mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    /**
     * 退出Activity并设置返回状态
     */
    private void closeActivity() {
        if(StringUtils.getFormatData(mTime, format).equals(
                StringUtils.getFormatData(System.currentTimeMillis(), format))) {
            // 当前任务
            setResult(200);
        }else {
            setResult(300);
        }
        finish();
    }

    /**
     * 隐藏软件盘
     * @param v 视图
     */
    private void hideKeyBoard(View v) {
        // 1.得到InputMethodManager对象
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 2.调用hideSoftInputFromWindow方法隐藏软键盘
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0); // 强制隐藏键盘
    }

    /**
     * 判断当前条目是否要刷新
     * @param type 当前条目类型ID
     * @return 是否可以刷新
     */
    public boolean isRefresh(long type) {
        return refreshBit.get(type, false);
    }

    /**
     * 重置指定的位置
     * @param type 当前条目类型ID
     */
    public void resetRefreshBit(long type) {
        refreshBit.put(type, false);
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
