package com.lefuorgn.lefu.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.bean.User;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.db.util.AllocatingTypeTaskManager;
import com.lefuorgn.db.util.OldPeopleManager;
import com.lefuorgn.db.util.SignConfigManager;
import com.lefuorgn.lefu.adapter.ExecuteAllocatingTaskAdapter;
import com.lefuorgn.lefu.adapter.PersonalDataSearchAdapter;
import com.lefuorgn.lefu.bean.AllocatingTaskExecuteOption;
import com.lefuorgn.lefu.bean.MultiMedia;
import com.lefuorgn.lefu.dialog.TodayWorkExecuteDialog;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 护理服务执行中添加测量任务页面
 */

public class ExecuteNursingAddActivity extends BaseActivity {

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

    private TextView mConditionBtn;
    private EditText mSearchView;
    private ImageView mHeadImg;
    private TextView mWarningNoSelectView;
    private TextView mOldPeopleNameView, mOldPeopleSexAgeView;
    private TextView mBedInfoView, mRoomInfoView;
    private TextView mFloorInfoView, mBuildInfoView;
    private RecyclerView mRecyclerView;
    private ExecuteAllocatingTaskAdapter mAdapter;

    // 配置条目信息
    private List<DisplaySignOrNursingItem> mNursingItem;

    private PopupWindow mPopupWindow; // 搜索条件

    private View mConditionView; // 搜索条件视图
    private View mContentView; // 搜索内容视图

    private int searchType = SEARCH_NAME; // 查找类型, 默认是姓名

    private List<OldPeople> mOldPeoples; // 所有老人列表

    /**
     * 当前页面被选择的老人
     */
    private OldPeople mOldPeople;
    /**
     * 搜索内容展示适配器
     */
    private PersonalDataSearchAdapter mPopupWindowAdapter;

    private long mAgencyId; // 当前机构ID
    private User mUser; // 当前用户
    private boolean update; // 记录当前页面是否有数据进行更新

    @Override
    protected int getLayoutId() {
        return R.layout.activity_execute_nursing_add;
    }

    @Override
    protected void initView() {
        mConditionBtn = (TextView) findViewById(R.id.tv_activity_personal_data_condition);
        mSearchView = (EditText) findViewById(R.id.et_activity_personal_data);
        // 初始化老人信息展示控件
        mWarningNoSelectView = (TextView) findViewById(R.id.tv_activity_personal_data_warning);
        mHeadImg = (ImageView) findViewById(R.id.iv_activity_personal_data);
        mOldPeopleNameView = (TextView) findViewById(R.id.tv_activity_personal_data_name);
        mOldPeopleSexAgeView = (TextView) findViewById(R.id.tv_activity_personal_data_sex_age);
        mBedInfoView = (TextView) findViewById(R.id.tv_activity_personal_data_bed);
        mRoomInfoView = (TextView) findViewById(R.id.tv_activity_personal_data_room);
        mFloorInfoView = (TextView) findViewById(R.id.tv_activity_personal_data_floor);
        mBuildInfoView = (TextView) findViewById(R.id.tv_activity_personal_data_build);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_activity_personal_data);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));

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

    @Override
    protected void initData() {
        setToolBarTitle("添加测量任务");
        mOldPeoples = OldPeopleManager.getOldPeople(false);
        mAgencyId = AppContext.getInstance().getAgencyId();
        mUser = AppContext.getInstance().getUser();
        mNursingItem = SignConfigManager.getSignOrNursingItem(false);
    }

    private void initListener() {
        // 添加条目模块点击事件
        mAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int i) {
                final AllocatingTaskExecuteOption option = mAdapter.getItem(i);
                switch (view.getId()) {
                    case R.id.tv_item_activity_execute_allocating_task_time:
                        // 数据录入
                        new TodayWorkExecuteDialog()
                                .setTitle(option.getNursing_item_name()
                                        + " "
                                        + mOldPeople.getElderly_name())
                                .setClickCallBack(new TodayWorkExecuteDialog.ClickCallBack() {
                                    @Override
                                    public void saveClick(String remarks, List<MultiMedia> multiMedia) {
                                        // 保存当前信息条目数据
                                        update = true;
                                        option.setRemark(remarks);
                                        option.setComplete(option.getComplete() + 1);
                                        option.setPercentage(option.getComplete() + "/" + option.getTotal());
                                        AllocatingTypeTaskManager.updateAllocatingTaskExecute(
                                                mUser, mAgencyId,
                                                mOldPeople.getId(),
                                                mOldPeople.getElderly_name(), option, multiMedia);
                                        mAdapter.notifyItemChanged(i);
                                    }
                                })
                                .show(getSupportFragmentManager(), "TodayWorkExecuteDialog");
                        break;
                    case R.id.tv_item_activity_execute_allocating_task_total:
                        // 添加备注
                        showToast("添加备注");
                        break;
                }
            }
        });
    }

    /**
     * 刷新老人基本信息控件
     */
    private void refreshOldPeopleInfoView() {
        if(mOldPeople != null) {
            ImageLoader.loadCircleImg(mOldPeople.getIcon(), mHeadImg);
            mWarningNoSelectView.setVisibility(View.GONE);
            mOldPeopleNameView.setText(mOldPeople.getElderly_name());
            mOldPeopleSexAgeView.setText(mOldPeople.getGender() == 15 ? "女  " : "男  "
                    + mOldPeople.getAge() + "岁");
            mBedInfoView.setText(String.format("床位: %s", mOldPeople.getBed_no()));
            mRoomInfoView.setText(String.format("房间: %s", mOldPeople.getRoom_no()));
            mFloorInfoView.setText(String.format("楼层: %s", mOldPeople.getFloor_layer()));
            mBuildInfoView.setText(String.format("楼栋: %s", mOldPeople.getFloor_no()));
        }
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
        mContentView = findViewById(R.id.ll_popup_window_personal_data_content);
        findViewById(R.id.v_popup_window_personal_data_content)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContentView.setVisibility(View.GONE);
                    }
                });
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_popup_window_personal_data_content);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
        mPopupWindowAdapter = new PersonalDataSearchAdapter();
        rv.setAdapter(mPopupWindowAdapter);
        mPopupWindowAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                mOldPeople = mPopupWindowAdapter.getItem(i);
                refreshOldPeopleInfoView();
                if(mAdapter == null) {
                    mAdapter = new ExecuteAllocatingTaskAdapter(getAllocatingTaskExecuteOption());
                    mRecyclerView.setAdapter(mAdapter);
                    initListener();
                }else {
                    mAdapter.setNewData(getAllocatingTaskExecuteOption());
                    mAdapter.notifyDataSetChanged();
                }
                mContentView.setVisibility(View.GONE);
                hideKeyBoard(view);
            }
        });
    }

    /**
     * 获取当前页面要展示的数据
     */
    private List<AllocatingTaskExecuteOption> getAllocatingTaskExecuteOption() {
        // 存放要保存的数据
        List<AllocatingTaskExecuteOption> resultData = new ArrayList<AllocatingTaskExecuteOption>();
        long time = System.currentTimeMillis();
        // 添加服务器没有配置的条目信息
        for (DisplaySignOrNursingItem item : mNursingItem) {
            AllocatingTaskExecuteOption option = new AllocatingTaskExecuteOption();
            // 本地数据库中不存在本条数据
            option.set_id(0);
            // 服务器数据库中不存在本条数据
            option.setId(0);
            option.setAgency_id(mAgencyId);
            option.setCare_workers("," + mUser.getUser_id() + ",");
            option.setCare_worker(mUser.getUser_id());
            option.setWorker_name(mUser.getUser_name());
            option.setNursing_item_id(item.getType());
            option.setNursing_item_name(item.getTitle());
            option.setPercentage("0/0");
            option.setComplete(0);
            option.setTotal(0);
            option.setCreate_time(time);
            option.setUpdate_time(time);
            option.setTask_time(time);
            option.setRemark("");
            // 记录当前信息条目为本地数据添加
            option.setSave_type(AllocatingTaskExecuteOption.SAVE_TYPE_LOCAL);
            resultData.add(option);
        }
        TLog.error("resultData == " + resultData.toString());
        return resultData;
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
        mPopupWindowAdapter.setNewData(filterDateList);
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
            mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 是否退出应用
            if(mContentView.getVisibility() == View.VISIBLE) {
                mContentView.setVisibility(View.GONE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
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

    @Override
    public void finish() {
        if(update) {
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
