package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.bean.User;
import com.lefuorgn.dialog.ListDialog;
import com.lefuorgn.lefu.bean.GroupOldPeople;
import com.lefuorgn.lefu.bean.LogbookGroup;
import com.lefuorgn.lefu.bean.LogbookType;

import java.util.ArrayList;
import java.util.List;


/**
 * 交班记录中添加某一个老人的记录
 */

public class LogbookAddActivity extends BaseActivity {

    private TextView mNameView, mTypeView;
    private EditText mDetailsView;
    private TextView mEmptyView;

    private LogbookGroup mLogbookGroup;
    // 存放老人分组信息
    private List<GroupOldPeople> mGroupOldPeople;
    // 存放当前分组数据类型
    private List<LogbookType> mLogbookType;
    private GroupOldPeople mOldPeople; // 已经选择的老人
    private LogbookType mLogbook; // 已经选择的类型
    private User mUser;

    private ListDialog<GroupOldPeople> mOldPeopleDialog;
    private ListDialog<LogbookType> mLogbookTypeDialog;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_logbook_add;
    }

    @Override
    protected void initView() {
        mNameView = (TextView) findViewById(R.id.tv_activity_logbook_add_name);
        findViewById(R.id.ll_activity_logbook_add_name).setOnClickListener(this);
        findViewById(R.id.ll_activity_logbook_add_type).setOnClickListener(this);
        mTypeView = (TextView) findViewById(R.id.tv_activity_logbook_add_type);
        mDetailsView = (EditText) findViewById(R.id.et_activity_logbook_add_details);
        findViewById(R.id.tv_activity_logbook_add_btn).setOnClickListener(this);
        mEmptyView = (TextView) findViewById(R.id.tv_activity_logbook_add);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mLogbookGroup = (LogbookGroup) intent.getSerializableExtra("logbookGroup");
        if(mLogbookGroup == null) {
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyView.setText("暂无分组信息");
            setToolBarTitle("添加交班记录");
            return;
        }
        setToolBarTitle(mLogbookGroup.getGroup_name());
        mUser = AppContext.getInstance().getUser();
        loadOldPeopleByGroupId();
        loadHandOverType();
    }

    /**
     * 加载分组类型
     */
    private void loadHandOverType(){

        LefuApi.getHandOverTypeList(new RequestCallback<List<LogbookType>>() {
            @Override
            public void onSuccess(List<LogbookType> result) {
                if(result == null || result.size() == 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    mEmptyView.setText(String.format("%s组 暂无分组类别", mLogbookGroup.getGroup_name()));
                    mLogbookType = new ArrayList<LogbookType>();
                    return;
                }
                mLogbookType = result;
            }

            @Override
            public void onFailure(ApiHttpException e) {
                mEmptyView.setVisibility(View.VISIBLE);
                mEmptyView.setText(String.format("%s组 暂无分组类别", mLogbookGroup.getGroup_name()));
                mLogbookType = new ArrayList<LogbookType>();
                showToast(e.getMessage());
            }
        });
    }

    /**
     * 加载当前分组的老人数据
     */
    private void loadOldPeopleByGroupId() {
        LefuApi.getLogbookOldPeople(mLogbookGroup.getId(), new RequestCallback<List<GroupOldPeople>>() {
            @Override
            public void onSuccess(List<GroupOldPeople> result) {
                if(result == null || result.size() == 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    mEmptyView.setText(String.format("%s组 暂无老人", mLogbookGroup.getGroup_name()));
                    mGroupOldPeople = new ArrayList<GroupOldPeople>();
                    return;
                }
                mGroupOldPeople = result;
            }

            @Override
            public void onFailure(ApiHttpException e) {
                mEmptyView.setVisibility(View.VISIBLE);
                mEmptyView.setText(String.format("%s组 暂无老人", mLogbookGroup.getGroup_name()));
                mGroupOldPeople = new ArrayList<GroupOldPeople>();
                showToast(e.getMessage());
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_activity_logbook_add_name :
                // 选择老人
                showOldPeopleDialog();
                break;
            case R.id.ll_activity_logbook_add_type :
                // 选择类别
                showTypeDialog();
                break;
            case R.id.tv_activity_logbook_add_btn :
                // 提交按钮
                confirm();
                break;
        }
    }

    private void confirm() {
        if(mOldPeople == null) {
            showToast("请选择老人");
            return;
        }
        if(mLogbook == null) {
            showToast("请选择类别");
            return;
        }
        showLoadingDialog();
        LefuApi.submitLogbookInfo(mOldPeople.getOld_people_id(), mOldPeople.getOld_people_name(),
                mDetailsView.getText().toString(), mLogbook.getId(), mUser, new RequestCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        showToast("添加成功");
                        setResult(200);
                        hideLoadingDialog();
                        finish();
                    }

                    @Override
                    public void onFailure(ApiHttpException e) {
                        showToast(e.getMessage());
                        hideLoadingDialog();
                        finish();
                    }
                });

    }

    /**
     * 显示老人Dialog
     */
    private void showOldPeopleDialog() {
        if(mOldPeopleDialog == null) {
            mOldPeopleDialog = new ListDialog<GroupOldPeople>();
            mOldPeopleDialog.setTitle("选择老人")
                    .setEmptyNote("该分组没有老人");
            mOldPeopleDialog.setData(mGroupOldPeople);
            mOldPeopleDialog.setCallBack(new ListDialog.Callback<GroupOldPeople>() {
                @Override
                public void convert(TextView view, GroupOldPeople item) {
                    view.setText(item.getOld_people_name());
                }

                @Override
                public void onItemClick(View view, GroupOldPeople item) {
                    mOldPeople = item;
                    mNameView.setText(item.getOld_people_name());
                }
            });
        }
        mOldPeopleDialog.show(getSupportFragmentManager(), "mOldPeopleDialog");
    }

    /**
     * 显示类型dialog
     */
    private void showTypeDialog() {
        if(mLogbookTypeDialog == null) {
            mLogbookTypeDialog = new ListDialog<LogbookType>();
            mLogbookTypeDialog.setTitle("选择事项")
                    .setEmptyNote("该分组没有类别");
            mLogbookTypeDialog.setData(mLogbookType);
            mLogbookTypeDialog.setCallBack(new ListDialog.Callback<LogbookType>() {
                @Override
                public void convert(TextView view, LogbookType item) {
                    view.setText(item.getContent());
                }

                @Override
                public void onItemClick(View view, LogbookType item) {
                    mLogbook = item;
                    mTypeView.setText(item.getContent());
                }
            });
        }
        mLogbookTypeDialog.show(getSupportFragmentManager(), "mLogbookTypeDialog");
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
