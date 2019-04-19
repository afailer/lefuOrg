package com.lefuorgn.lefu.activity;

import android.text.TextUtils;
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
import com.lefuorgn.db.util.PermissionManager;
import com.lefuorgn.dialog.DatePickerDialog;
import com.lefuorgn.lefu.bean.Leave;
import com.lefuorgn.lefu.dialog.LeaveSpendDialog;
import com.lefuorgn.util.StringUtils;

/**
 * 老人外出请假详情, 包括销假、修改
 */

public class LeaveOperationActivity extends BaseActivity {

    private TextView mElderName;

    private TextView mOutTime, mBackTime;
    private EditText mSignature, mReason;

    private EditText mNotes;
    // 销假按钮和修改按钮
    private TextView mSpendBtn, mUpdateBtn;
    private Leave mLeave;
    private User mUser;

    // 离院和返回时间选择控件工具类
    private DatePickerDialog mOutDialog, mBackDialog;
    private LeaveSpendDialog mSpendDialog; // 销假提示框

    @Override
    protected int getLayoutId() {
        return R.layout.activity_leave_operation;
    }

    @Override
    protected void initView() {
        // 初始化信息展示控件
        mElderName = (TextView) findViewById(R.id.tv_activity_leave_operation_name);
        mOutTime = (TextView) findViewById(R.id.tv_activity_leave_operation_rl_time);
        mOutTime.setOnClickListener(this);
        mBackTime = (TextView) findViewById(R.id.tv_activity_leave_operation_eb_time);
        mBackTime.setOnClickListener(this);
        mSignature = (EditText) findViewById(R.id.et_activity_leave_operation_signature);
        mReason = (EditText) findViewById(R.id.et_activity_leave_operation_reason);
        mNotes = (EditText) findViewById(R.id.et_activity_leave_operation_note);
        mSpendBtn = (TextView) findViewById(R.id.tv_activity_leave_operation_spend);
        mUpdateBtn = (TextView) findViewById(R.id.tv_activity_leave_operation_update);
    }

    @Override
    protected void initData() {
        setToolBarTitle("请假详情");
        mUser = AppContext.getInstance().getUser();
        mLeave = (Leave) getIntent().getSerializableExtra("leave");
        mElderName.setText(mLeave.getElderly_name());
        mOutTime.setText(StringUtils.getFormatData(mLeave.getLeave_hospital_dt(), StringUtils.FORMAT));
        mBackTime.setText(mLeave.getExpected_return_dt() == 0 ? "" : StringUtils
                .getFormatData(mLeave.getExpected_return_dt(), StringUtils.FORMAT));
        mSignature.setText(mLeave.getParty_signature());
        mReason.setText(mLeave.getLeave_reason());
        mNotes.setText(mLeave.getNotes_matters());
        // 获取请假修改权限
        boolean update = PermissionManager
                .hasPermission(PermissionManager.LEAVE_OUT + PermissionManager.P_U);
        if(update) {
            mSpendBtn.setOnClickListener(this);
            mUpdateBtn.setOnClickListener(this);
        }else {
            mSpendBtn.setVisibility(View.GONE);
            mUpdateBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_activity_leave_operation_rl_time:
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
            case R.id.tv_activity_leave_operation_eb_time:
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
            case R.id.tv_activity_leave_operation_spend:
                // 销假
                if(mSpendDialog == null) {
                    mSpendDialog = new LeaveSpendDialog();
                    mSpendDialog.setLimitTime(mLeave.getLeave_hospital_dt())
                            .setClickCallBack(new LeaveSpendDialog.ClickCallBack() {
                        @Override
                        public void confirm(long time) {
                            // 销假
                            spendLeave(time);
                        }
                    });
                }
                mSpendDialog.show(getSupportFragmentManager(), "LeaveSpendDialog");
                break;
            case R.id.tv_activity_leave_operation_update:
                // 修改
                updateLeave();
                break;
        }
    }

    /**
     * 销假
     * @param time 注销时间
     */
    private void spendLeave(long time) {
        showLoadingDialog();
        LefuApi.spendLeave(mLeave.getId(), time, mUser, new RequestCallback<String>() {
            @Override
            public void onSuccess(String result) {
                showToast("销假成功");
                setResult(400);
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

    /**
     * 修改请假内容
     */
    private void updateLeave() {
        if (StringUtils.isEmpty(mOutTime.getText().toString())) {
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
        leave.setId(mLeave.getId());
        leave.setOld_people_id(mLeave.getOld_people_id());
        leave.setLeave_hospital_dt(StringUtils.getFormatData(mOutTime.getText().toString(), StringUtils.FORMAT));
        long backTime = 0;
        if(!TextUtils.isEmpty(mBackTime.getText().toString())) {
            backTime = StringUtils.getFormatData(mBackTime.getText().toString(), StringUtils.FORMAT);
        }
        leave.setExpected_return_dt(backTime);
        leave.setParty_signature(mSignature.getText().toString());
        leave.setLeave_reason(mReason.getText().toString());
        leave.setNotes_matters(mNotes.getText().toString());
        leave.setSignature_id(mUser.getUser_id());
        leave.setAttn_signature(mUser.getUser_name());
        LefuApi.updateLeave(leave, new RequestCallback<String>() {
            @Override
            public void onSuccess(String result) {
                showToast("修改成功");
                setResult(500);
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
}
