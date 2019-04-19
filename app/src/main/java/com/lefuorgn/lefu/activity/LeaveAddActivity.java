package com.lefuorgn.lefu.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.bean.User;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.db.util.OldPeopleManager;
import com.lefuorgn.dialog.DatePickerDialog;
import com.lefuorgn.lefu.adapter.PersonalDataSearchAdapter;
import com.lefuorgn.lefu.bean.Leave;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.widget.ClearEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * 老人外出请假申请页面
 */

public class LeaveAddActivity extends BaseActivity {

    private TextView mOutTime, mBackTime;
    private EditText mSignature, mReason;

    private EditText mNotes;

    private View mContentView; // 搜索内容视图

    private ClearEditText mSearchView;
    private List<OldPeople> mOldPeoples; // 所有老人列表

    /**
     * 当前页面被选择的老人
     */
    private OldPeople mOldPeople;

    /**
     * 搜索内容展示适配器
     */
    private PersonalDataSearchAdapter mAdapter;

    // 离院和返回时间选择控件工具类
    private DatePickerDialog mOutDialog, mBackDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_leave_add;
    }

    @Override
    protected void initView() {
        // 初始化信息展示控件
        mOutTime = (TextView) findViewById(R.id.tv_activity_leave_add_rl_time);
        mOutTime.setOnClickListener(this);
        mBackTime = (TextView) findViewById(R.id.tv_activity_leave_add_eb_time);
        mBackTime.setOnClickListener(this);
        mSignature = (EditText) findViewById(R.id.et_activity_leave_add_signature);
        mReason = (EditText) findViewById(R.id.et_activity_leave_add_reason);
        mNotes = (EditText) findViewById(R.id.et_activity_leave_add_note);
        findViewById(R.id.tv_activity_leave_add).setOnClickListener(this);
        mSearchView = (ClearEditText) findViewById(R.id.cet_activity_leave_add);
        initContentPopWindow();
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
                    mOldPeople = null;
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
        setToolBarTitle("创建请假");
        mOldPeoples = OldPeopleManager.getOldPeople(false);
    }

    /**
     * 初始化老人选择PopWindow控件
     */
    private void initContentPopWindow() {
        mContentView = findViewById(R.id.ll_activity_leave_add);
        findViewById(R.id.iv_activity_leave_add)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mAdapter.getData().size()==1){
                            if(((OldPeople)mAdapter.getData().get(0)).getElderly_name().equals(mSearchView.getText().toString().trim())){
                                mOldPeople = (OldPeople) mAdapter.getData().get(0);
                            }else{
                                mOldPeople=null;
                            }
                        }else{
                            mOldPeople=null;
                        }
                        mContentView.setVisibility(View.GONE);
                    }
                });
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_activity_leave_add);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
        mAdapter = new PersonalDataSearchAdapter();
        rv.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                mOldPeople = (OldPeople) mAdapter.getData().get(i);
                mSearchView.setText(mOldPeople.getElderly_name());
                mSearchView.setSelection(mSearchView.getText().length());
                mContentView.setVisibility(View.GONE);
                hideKeyBoard(view);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_activity_leave_add_rl_time:
                // 选择离院时间点击按钮
                if (mOutDialog == null) {
                    mOutDialog = new DatePickerDialog();
                    mOutDialog.setTitle("选择离院时间")
                            .setMinDate(System.currentTimeMillis())
                            .setClickCallBack(new DatePickerDialog.ClickCallBack() {
                        @Override
                        public void leftClick() {
                            mOutDialog.dismiss();
                        }

                        @Override
                        public void rightClick(long time) {
                            mOutTime.setText(StringUtils.getFormatData(time, StringUtils.FORMAT));
                            mBackTime.setText("");
                            mOutDialog.dismiss();
                        }
                    });
                }
                mOutDialog.show(getSupportFragmentManager(), "OutDialog");
                break;
            case R.id.tv_activity_leave_add_eb_time:
                // 选择返回时间点击按钮
                if (StringUtils.isEmpty(mOutTime.getText().toString())) {
                    showToast("请选择离院时间");
                    return;
                }
                if (mBackDialog == null) {
                    mBackDialog = new DatePickerDialog();
                    mBackDialog.setTitle("选择预计返回时间")
                            .setMinDate(System.currentTimeMillis())
                            .setClickCallBack(new DatePickerDialog.ClickCallBack() {
                                @Override
                                public void leftClick() {
                                    mBackDialog.dismiss();
                                }

                                @Override
                                public void rightClick(long time) {
                                    if(time <= StringUtils.getFormatData(mOutTime.getText().toString(), StringUtils.FORMAT)) {
                                        showToast("不能早于实际离院时间");
                                        return;
                                    }
                                    mBackTime.setText(StringUtils.getFormatData(time, StringUtils.FORMAT));
                                    mBackDialog.dismiss();
                                }
                            });
                }
                mBackDialog.show(getSupportFragmentManager(), "BackDialog");
                break;
            case R.id.tv_activity_leave_add:
                confirmData();
                break;
        }
    }

    /**
     * 通过字段过滤老人列表
     * @param filterStr 指定的字符串
     */
    private void filterData(String filterStr) {
        List<OldPeople> filterDateList = new ArrayList<OldPeople>();
        filterDateList.clear();
        filterStr = filterStr.toLowerCase();
        for (OldPeople o : mOldPeoples) {
            if (o.getCharacters().startsWith(filterStr) ||
                    o.getFullPinYin().startsWith(filterStr) ||
                    o.getInitial().startsWith(filterStr)) {
                filterDateList.add(o);
            }
        }
        mAdapter.setNewData(filterDateList);
    }

    /**
     * 提交请假信息
     */
    private void confirmData() {
        if (mOldPeople == null) {
            showToast("请选择老人");
            return;
        }
        if (TextUtils.isEmpty(mOutTime.getText().toString())) {
            showToast("请选择离院时间");
            return;
        }
        if (TextUtils.isEmpty(mSignature.getText().toString().trim())) {
            showToast("请填写本人或家属签名");
            return;
        }
        if (TextUtils.isEmpty(mReason.getText().toString().trim())) {
            showToast("请填写请假事由");
            return;
        }
        showLoadingDialog();
        Leave leave = new Leave();
        leave.setOld_people_id(mOldPeople.getId());
        leave.setLeave_hospital_dt(StringUtils.getFormatData(mOutTime.getText().toString(), StringUtils.FORMAT));
        long backTime = 0;
        if(!TextUtils.isEmpty(mBackTime.getText().toString())) {
            backTime = StringUtils.getFormatData(mBackTime.getText().toString(), StringUtils.FORMAT);
        }
        leave.setExpected_return_dt(backTime);
        leave.setParty_signature(mSignature.getText().toString());
        leave.setLeave_reason(mReason.getText().toString());
        leave.setNotes_matters(mNotes.getText().toString());
        User user = AppContext.getInstance().getUser();
        leave.setSignature_id(user.getUser_id());
        leave.setAttn_signature(user.getUser_name());
        LefuApi.addLeave(leave, new RequestCallback<String>() {
            @Override
            public void onSuccess(String result) {
                showToast("请假成功");
                setResult(300);
                hideLoadingDialog();
                finish();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
                hideLoadingDialog();
            }
        });

    }

    @Override
    protected boolean hasToolBar() {
        return true;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
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
}
