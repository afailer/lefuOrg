package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.bean.User;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.dialog.ListDialog;
import com.lefuorgn.lefu.bean.Logbook;
import com.lefuorgn.lefu.bean.LogbookType;

import java.util.ArrayList;
import java.util.List;

/**
 * 老人交班记录条目详情页面(包括信息展示以及交班记录的添加)
 */

public class LogbookInfoDetailsActivity extends BaseActivity {

    private TextView mNameView, mTypeView, mStaffView;
    private LinearLayout mTypeBtn, mStaffBtn;
    private View mLineView;
    private TextView mNoteView;
    private EditText mDetailsView;
    private TextView mConfirmBtn;

    private OldPeople mOldPeople;
    private User mUser;
    TextView textView;
    private List<LogbookType> mLogbookTypeData;
    // 被选中的类型
    private LogbookType mLogbookType;
    // 类型选择对话框
    private ListDialog<LogbookType> mLogbookTypeDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_logbook_info_details;
    }

    @Override
    protected void initView() {
        mNameView = (TextView) findViewById(R.id.tv_activity_logbook_info_details_name);
        mTypeBtn = (LinearLayout) findViewById(R.id.ll_activity_logbook_info_details_type);
        mTypeView = (TextView) findViewById(R.id.tv_activity_logbook_info_details_type);
        mStaffBtn = (LinearLayout) findViewById(R.id.ll_activity_logbook_info_details_staff);
        mStaffView = (TextView) findViewById(R.id.tv_activity_logbook_info_details_staff);
        mLineView = findViewById(R.id.v_activity_logbook_info_details);
        mNoteView = (TextView) findViewById(R.id.tv_activity_logbook_info_note);
        mDetailsView = (EditText) findViewById(R.id.et_activity_logbook_info_details);
        mConfirmBtn = (TextView) findViewById(R.id.tv_activity_logbook_info_details_btn);
        textView= (TextView) findViewById(R.id.tv_logbook_details);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        Logbook logbook = (Logbook) intent.getSerializableExtra("logbook");
        String name = intent.getStringExtra("name");
        setToolBarTitle(name);
        if(logbook == null) {
            showWaitDialog();
            LefuApi.getHandOverTypeList(new RequestCallback<List<LogbookType>>() {
                @Override
                public void onSuccess(List<LogbookType> result) {
                    mLogbookTypeData = result;
                    hideWaitDialog();
                }

                @Override
                public void onFailure(ApiHttpException e) {
                    showToast(e.getMessage());
                    mLogbookTypeData = new ArrayList<LogbookType>();
                    hideWaitDialog();
                }
            });
            addLogbook();
        }else {
            showLogbook(logbook);
        }
    }

    /**
     * 交班记录信息展示页面
     * @param logbook 交班记录信息bean
     */
    private void showLogbook(Logbook logbook) {
        mNameView.setText(logbook.getOld_people_name());
        mTypeView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        mTypeView.setText(logbook.getCare_name());
        mStaffView.setText(logbook.getStaff_name());
        mNoteView.setText("详情");
        mDetailsView.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setText(logbook.getContent());
        mConfirmBtn.setVisibility(View.GONE);
    }

    /**
     * 交班记录添加页面
     */
    private void addLogbook() {
        mOldPeople = (OldPeople) getIntent().getSerializableExtra("oldPeople");
        mUser = AppContext.getInstance().getUser();
        mNameView.setText(mOldPeople.getElderly_name());
        mStaffBtn.setVisibility(View.GONE);
        mLineView.setVisibility(View.GONE);
        mTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitLogbook();
            }
        });
    }

    /**
     * 展示选择事项Dialog
     */
    private void showDialog() {
        if(mLogbookTypeDialog == null) {
            mLogbookTypeDialog = new ListDialog<LogbookType>();
            mLogbookTypeDialog.setTitle("选择事项")
                    .setEmptyNote("该分组没有类别");
            mLogbookTypeDialog.setData(mLogbookTypeData);
            mLogbookTypeDialog.setCallBack(new ListDialog.Callback<LogbookType>() {
                @Override
                public void convert(TextView view, LogbookType item) {
                    view.setText(item.getContent());
                }

                @Override
                public void onItemClick(View view, LogbookType item) {
                    mLogbookType = item;
                    mTypeView.setText(item.getContent());
                }
            });
        }
        mLogbookTypeDialog.show(getSupportFragmentManager(), "mLogbookTypeDialog");
    }

    /**
     * 提交交班记录
     */
    private void submitLogbook() {
        if(mLogbookType == null) {
            showToast("请选择类别");
            return;
        }
        showLoadingDialog();
        LefuApi.submitLogbookInfo(mOldPeople.getId(), mOldPeople.getElderly_name(),
                mDetailsView.getText().toString(), mLogbookType.getId(), mUser, new RequestCallback<String>() {
            @Override
            public void onSuccess(String result) {
                showToast("添加成功");
                setResult(200);
                hideLoadingDialog();
                finish();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast("添加失败");
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
}
