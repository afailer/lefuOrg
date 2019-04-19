package com.lefuorgn.lefu.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
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
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.db.util.DictionaryManager;
import com.lefuorgn.db.util.OldPeopleManager;
import com.lefuorgn.lefu.adapter.GridLayoutItemAdapter;
import com.lefuorgn.lefu.adapter.PersonalDataSearchAdapter;
import com.lefuorgn.lefu.bean.GridLayoutItem;
import com.lefuorgn.lefu.util.GridLayoutItemUtils;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.NetworkUtils;
import com.lefuorgn.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人数据页面
 */

public class PersonalDataActivity extends BaseActivity {

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

    private RecyclerView mOldPeopleView;

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
    private PersonalDataSearchAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_data;
    }

    @Override
    protected void initView() {
        setToolBarTitle("个人数据");
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
        // 初始化邀请码和二维码生成控件
        findViewById(R.id.ll_activity_personal_data_invitation).setOnClickListener(this);
        findViewById(R.id.ll_activity_personal_data_qr).setOnClickListener(this);
        // 初始化老人模块化信息控件
        mOldPeopleView = (RecyclerView) findViewById(R.id.rv_activity_personal_data);
        mOldPeopleView.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
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
        // 所有按钮搜索事件
        findViewById(R.id.iv_activity_personal_data_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContentView.getVisibility() == View.GONE) {
                    mContentView.setVisibility(View.VISIBLE);
                    filterData(mSearchView.getText().toString());
                }
            }
        });
    }

    @Override
    protected void initData() {
        // 获取当前是否有老人传递过来
        mOldPeople = (OldPeople) getIntent().getSerializableExtra("oldPeople");
        mOldPeoples = OldPeopleManager.getOldPeople(false);
        refreshOldPeopleInfoView();
        final GridLayoutItemAdapter adapter = new GridLayoutItemAdapter(
                GridLayoutItemUtils.getOldPeopleInfoItem());
        adapter.setOnRecyclerViewItemClickListener(
                new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        GridLayoutItem item = adapter.getItem(i);
                        if(!item.isPermission()) {
                            showToast(R.string.permission_no);
                            return;
                        }
                        if(item.isNetwork() && NetworkUtils.NETWORK_NONE == AppContext.getInstance().getNetState()) {
                            showToast("网络不可用");
                            return;
                        }
                        if(item.getCls() != null) {
                            if(mOldPeople == null) {
                                showToast("请先搜索一个老人");
                                return;
                            }
                            Intent intent= new Intent(PersonalDataActivity.this, item.getCls());
                            intent.putExtra("oldPeople", mOldPeople);
                            startActivity(intent);
                        }else {
                            showToast(item.getErrorInfo());
                        }
                    }
                });
        mOldPeopleView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(mOldPeople == null) {
            showToast("请先搜索一个老人");
            return;
        }
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_activity_personal_data_invitation:
                // 创建邀请码
                intent = new Intent(PersonalDataActivity.this, BuildInviteCodeActivity.class);
                intent.putExtra("oldPeople", mOldPeople);
                startActivity(intent);
                break;
            case R.id.ll_activity_personal_data_qr:
                // 创建二维码
                intent = new Intent(PersonalDataActivity.this, BuildZxingCodeActivity.class);
                intent.putExtra("oldPeople", mOldPeople);
                startActivity(intent);
                break;
        }
    }

    /**
     * 刷新老人基本信息控件
     */
    private void refreshOldPeopleInfoView() {
        if(mOldPeople != null) {
            ImageLoader.loadCircleImg(mOldPeople.getIcon(), mHeadImg);
            mWarningNoSelectView.setVisibility(View.GONE);
            mOldPeopleNameView.setText(mOldPeople.getElderly_name());
            mOldPeopleSexAgeView.setText(DictionaryManager.getContent(mOldPeople.getGender())
                    + "  " + mOldPeople.getAge() + "岁");

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
        mAdapter = new PersonalDataSearchAdapter();
        rv.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                mOldPeople = (OldPeople) mAdapter.getData().get(i);
                refreshOldPeopleInfoView();
                mContentView.setVisibility(View.GONE);
                hideKeyBoard(view);
            }
        });
    }

    /**
     * 通过字段过滤老人列表
     * @param filterStr 指定的字符串
     */
    private void filterData(String filterStr) {
        List<OldPeople> filterDateList = new ArrayList<OldPeople>();
        if(StringUtils.isEmpty(filterStr)) {
            filterDateList.addAll(mOldPeoples);
            mAdapter.setNewData(filterDateList);
            return;
        }
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
                if (o.getBed_no().contains(filterStr)) {
                    filterDateList.add(o);
                }
            }
        }else if(searchType == SEARCH_ID_CARD) {
            for (OldPeople o : mOldPeoples) {
                if (o.getDocument_number().contains(filterStr)) {
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
            mPopupWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
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
    protected boolean hasToolBar() {
        return true;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }
}
