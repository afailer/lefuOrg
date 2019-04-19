package com.lefuorgn.oa.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.api.remote.OaApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.db.model.basic.StaffCache;
import com.lefuorgn.db.util.PermissionManager;
import com.lefuorgn.db.util.StaffCacheManager;
import com.lefuorgn.oa.bean.ApplyApproval;
import com.lefuorgn.oa.bean.ApplyDetails;
import com.lefuorgn.oa.bean.ApplyDetailsApplicant;
import com.lefuorgn.oa.bean.ApplyDetailsApprover;
import com.lefuorgn.oa.bean.ApprovalInformation;
import com.lefuorgn.oa.bean.ClockType;
import com.lefuorgn.oa.bean.OaConfig;
import com.lefuorgn.oa.dialog.RejectApplyDialog;
import com.lefuorgn.oa.fragment.ClockAttendanceApplyFragment;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;
import com.lefuorgn.viewloader.ViewLoader;
import com.lefuorgn.viewloader.base.DynamicViewBuilder;
import com.lefuorgn.viewloader.builder.DynamicRadioBuilder;
import com.lefuorgn.viewloader.impl.PersonEntryPageBuilder;
import com.lefuorgn.widget.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 我审批的和我申请的页面的
 */

public class ClockAttendanceApplyHandleActivity extends BaseActivity {

    private static final long DIVISOR_DEFAULT = 8;

    private long mId; // 审批或申请信息ID
    private boolean approval; // 当前页面所属类型; true: 我审批的页面; false: 我申请的页面
    private int mStatus;
    private long mDivisor;

    private CircleImageView mHeadImg;
    private TextView mNameView, mLabelView;
    // 申请内容开始时间和结束时间控件
    private TextView mTimeStartView, mTimeEndView;
    // 实际请假时长容器
    private LinearLayout mHourContainer;
    private EditText mDayView, mHourView;
    private TextView mTypeView, mDateView, mRemarkView;

    private int netNum;

    private LinearLayout mParentView;
    private DynamicRadioBuilder mDynamicRadioBuilder; // 审批人控件
    private boolean radioDelete; // 审批人当前状态是否是删除状态

    private StaffCacheManager mStaffCacheManager;

    private ApplyDetails mApplyDetails; // 申请人和审批人信息
    private ApplyDetailsApprover mApplyApprover; // 当前审批人信息
    private ApplyApproval mNextApprover; // 下一级审批人信息
    private ApplyDetailsApplicant mApplyDetailsApplicant; // 申请条目信息

    @Override
    protected int getLayoutId() {
        return R.layout.activity_clock_attendance_apply_handle;
    }

    @Override
    protected void initView() {
        mParentView = (LinearLayout) findViewById(R.id.ll_activity_clock_attendance_apply_handle);
        mHeadImg = (CircleImageView) findViewById(R.id.iv_activity_clock_attendance_apply_handle);
        mHeadImg.setBorderColor(Color.parseColor("#56D5B3"));
        mHeadImg.setBorderWidth(StringUtils.dip2px(this, 4f));
        mNameView = (TextView) findViewById(R.id.tv_activity_clock_attendance_apply_handle_name);
        mLabelView = (TextView) findViewById(R.id.tv_activity_clock_attendance_apply_handle_label);
        findViewById(R.id.btn_activity_clock_attendance_apply_handle).setOnClickListener(this);
        mTimeStartView = (TextView) findViewById(R.id.tv_activity_clock_attendance_apply_handle_time_start);
        mTimeEndView = (TextView) findViewById(R.id.tv_activity_clock_attendance_apply_handle_time_end);
        mHourContainer = (LinearLayout) findViewById(R.id.ll_activity_clock_attendance_apply_handle_time);
        mDayView = (EditText) findViewById(R.id.et_activity_clock_attendance_apply_handle_day);
        mHourView = (EditText) findViewById(R.id.et_activity_clock_attendance_apply_handle_hour);
        mTypeView = (TextView) findViewById(R.id.tv_activity_clock_attendance_apply_handle_type);
        mDateView = (TextView) findViewById(R.id.tv_activity_clock_attendance_apply_handle_date);
        mRemarkView = (TextView) findViewById(R.id.tv_activity_clock_attendance_apply_handle_remark);
        mStatus = getIntent().getIntExtra("status", 0);
        if(mStatus != 1) {
            findViewById(R.id.ll_activity_clock_attendance_apply_handle_btn).setVisibility(View.GONE);
            mDayView.setFocusable(false);
            mDayView.setEnabled(false);
            mHourView.setFocusable(false);
            mHourView.setEnabled(false);
            return;
        }
        approval = getIntent().getBooleanExtra("approval", false);
        TextView tv = (TextView) findViewById(R.id.btn_activity_clock_attendance_apply_handle_reject);
        tv.setOnClickListener(this);
        if(approval) {
            // 我审批的
            tv.setText("驳回");
            findViewById(R.id.btn_activity_clock_attendance_apply_handle_agree).setOnClickListener(this);
        }else {
            // 我申请的
            if(!PermissionManager.hasPermission(PermissionManager.OA_ASKFORLEAVEBACK + PermissionManager.P_U)) {
                // 无撤销的权限
                findViewById(R.id.ll_activity_clock_attendance_apply_handle_btn).setVisibility(View.GONE);
            }else {
                tv.setText("撤销");
                findViewById(R.id.btn_activity_clock_attendance_apply_handle_agree).setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void initData() {
        mId = getIntent().getLongExtra("id", 0);
        mStaffCacheManager = StaffCacheManager.getInstance();
        showWaitDialog();
        getOaConfig();
    }

    /**
     * 获取OA配置文件
     */
    private void getOaConfig() {
        OaApi.getOaConfig(new RequestCallback<OaConfig>() {
            @Override
            public void onSuccess(OaConfig result) {
                if(result == null || StringUtils.isEmpty(result.getContent())) {
                    mDivisor = DIVISOR_DEFAULT;
                    getApplyDetails();
                    return;
                }
                try {
                    JSONObject json = new JSONObject(result.getContent());
                    long divisor = json.getLong("daily_work_hours");
                    mDivisor = divisor == 0 ? DIVISOR_DEFAULT : divisor;
                }catch (JSONException e) {
                    mDivisor = DIVISOR_DEFAULT;
                }
                getApplyDetails();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                mDivisor = DIVISOR_DEFAULT;
                getApplyDetails();
            }
        });
    }

    private void getApplyDetails() {
        OaApi.getApplyDetails(mId, new RequestCallback<ApplyDetails>() {
            @Override
            public void onSuccess(ApplyDetails result) {
                mApplyDetails = result;
                if(result == null) {
                    showToast("信息获取失败...");
                    hideWaitDialog();
                    return;
                }
                netNum = 0;
                // 初始化页面信息
                initApplicantView(result.getAskForLeaveForm());
                if(approval && mStatus == 1) {
                    // 获取当前审批人信息
                    List<ApplyDetailsApprover> approvers = result.getAskForLeaveVerifyForms();
                    if(approvers == null || approvers.size() == 0) {
                        statisticsNetWork();
                        return;
                    }
                    ApplyDetailsApprover approver = approvers.get(approvers.size() - 1);
                    getApplyDetailsApprover(approver);
                }else {
                    statisticsNetWork();
                }

            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
                hideWaitDialog();
            }
        });
    }

    /**
     * 初始化申请人信息
     */
    private void initApplicantView(ApplyDetailsApplicant applicant) {
        if(applicant == null) {
            statisticsNetWork();
            return;
        }
        // 显示申请人信息
        String name = applicant.getOa_vacation_name() + "申请";
        setToolBarTitle(name);
        mLabelView.setText(name);
        mApplyDetailsApplicant = applicant;
        ImageLoader.loadImgForUserDefinedView(applicant.getUser_icon(), mHeadImg);
        mNameView.setText(applicant.getUser_name());
        mTimeStartView.setText(StringUtils.getFormatData(applicant.getStart_time(), StringUtils.FORMAT));
        mTimeEndView.setText(StringUtils.getFormatData(applicant.getEnd_time(), StringUtils.FORMAT));
        mTypeView.setText(applicant.getOa_vacation_name());
        mDateView.setText(StringUtils.getFormatData(applicant.getVerify_start_time(), StringUtils.FORMAT));
        mRemarkView.setText(applicant.getRemark());
        TLog.error("mDivisor == " + mDivisor);
        String day = applicant.getDuration_hour() / mDivisor + "";
        String hour = applicant.getDuration_hour() % mDivisor + "";
        mDayView.setText(day);
        mDayView.setSelection(day.length());
        mHourView.setText(hour);
        mHourView.setSelection(hour.length());
        // 获取申请类型详情信息
        getApplyDetailsApplicant(applicant.getOa_vacation_id());
    }

    /**
     * 获取申请类型详情信息
     */
    private void getApplyDetailsApplicant(long id) {
        TLog.error("申请人ID = " + id);
        OaApi.getApplyDetailsApplicant(id, new RequestCallback<ClockType>() {
            @Override
            public void onSuccess(ClockType result) {
                if(result == null) {
                    statisticsNetWork();
                    return;
                }
                if(result.getMin_type() == 2) {
                    mHourContainer.setVisibility(View.GONE);
                }else {
                    mHourContainer.setVisibility(View.VISIBLE);
                }
                statisticsNetWork();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
                statisticsNetWork();
            }
        });
    }

    /**
     * 获取审批人信息
     */
    private void getApplyDetailsApprover(ApplyDetailsApprover approver) {
        mApplyApprover = approver;
        TLog.error("审批人ID = " + approver.getId());
        TLog.error("审批人级别 = " + approver.getLevel());
        if(approver.getLevel() > 1) {
            mDayView.setFocusable(false);
            mDayView.setEnabled(false);
            mHourView.setFocusable(false);
            mHourView.setEnabled(false);
        }else {
            // 一级审批人
            if("0".equals(mDayView.getText().toString())) {
                mDayView.setText("");
            }
            if("0".equals(mHourView.getText().toString())) {
                mHourView.setText("");
            }
        }
        OaApi.getApplyDetailsApprover(approver.getId(), new RequestCallback<ApprovalInformation>() {
            @Override
            public void onSuccess(ApprovalInformation result) {
                int type = 0;
                int stop = 0;
                if(result != null) {
                    // 获取当前审批人信息
                    ApplyApproval approval = result.getAskForLeaveVerifyForm();
                    if(approval != null) {
                        stop = approval.getIs_stop();
                    }
                    // 获取下一级审批人信息
                    mNextApprover = result.getVacationVerifyLineForm();
                    if(mNextApprover != null) {
                        type = mNextApprover.getType();
                    }
                }
                ViewLoader loader = new ViewLoader.Builder()
                        .context(ClockAttendanceApplyHandleActivity.this).copy(false).dynamic(type != 2)
                        .paddingWidth(16).paddingHeight(8)
                        .labelSize(14).labelColor(Color.BLACK)
                        .valueSize(14).valueColor(Color.BLACK).build();
                // 初始化审批人信息
                PersonEntryPageBuilder personPageBuilder = new PersonEntryPageBuilder();
                loader.build(personPageBuilder);
                mParentView.addView(personPageBuilder.getPageView());
                mDynamicRadioBuilder = personPageBuilder.getDynamicRadioBuilder();
                mDynamicRadioBuilder.showStopView(stop == 1);
                if(type == 2) {
                    // 固定审批
                    mDynamicRadioBuilder.setTextView(mNextApprover.getVerify_user_name(), mNextApprover.getVerify_user_id());
                }else {
                    mDynamicRadioBuilder.setOnButtonClickListener(new DynamicViewBuilder.OnButtonClickListener() {
                        @Override
                        public void onUpClick(View v) {
                            Intent intent = new Intent(ClockAttendanceApplyHandleActivity.this, SelectStuffActivity.class);
                            intent.putExtra("isCopy", "notCopy");
                            startActivityForResult(intent, 600);
                            radioDelete = false;
                            mDynamicRadioBuilder.switchView(false);
                        }

                        @Override
                        public void onDownClick(View v) {
                            radioDelete = !radioDelete;
                            mDynamicRadioBuilder.switchView(radioDelete);
                        }

                        @Override
                        public void onViewClick(long tag) {
                            mStaffCacheManager.removeStaffCache(tag, false);
                        }
                    });
                    initDynamicRadioBuilder(-1);
                }
                statisticsNetWork();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
                statisticsNetWork();
            }
        });
    }

    /**
     * 统计网络请求数
     */
    private synchronized void statisticsNetWork() {
        netNum++;
        if(netNum == 2) {
            hideWaitDialog();
        }
    }

    /**
     * 显示审批人控件
     */
    private void initDynamicRadioBuilder(long id) {
        List<StaffCache> staffFalseCaches = mStaffCacheManager.getStaffCache(false);
        int index = 10000;
        mDynamicRadioBuilder.removeAllViews();
        for (StaffCache cache : staffFalseCaches) {
            mDynamicRadioBuilder.addRadioButton(++index, cache.getName(),
                    cache.getId(), cache.getId() == id);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TLog.error(resultCode + "");
        if(resultCode == 800 && data != null) {
            // 添加审批员工
            String stuff = data.getStringExtra("stuff");
            String[] splits = stuff.substring(0, stuff.length() - SelectStuffActivity.DIVIDE_STR.length()).split(",");
            long id = -1;
            if(splits.length == 2) {
                id = StringUtils.toLong(splits[0], 0);
                if(id == 0) {
                    return;
                }
                StaffCache cache = new StaffCache();
                cache.setId(id);
                cache.setName(splits[1]);
                mStaffCacheManager.addStaffCache(cache, false);
            }
            initDynamicRadioBuilder(id);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity_clock_attendance_apply_handle:
                // 查看进度
                if(mApplyDetails == null) {
                    showToast("数据获取失败...");
                    return;
                }
                Intent intent = new Intent(this, ClockApplicationProgressActivity.class);
                intent.putExtra("details", mApplyDetails);
                startActivity(intent);
                break;
            case R.id.btn_activity_clock_attendance_apply_handle_agree:
                // 我审批的, 同意按钮
                doAgreeOrReject(ClockAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_AGREE, "");
                break;
            case R.id.btn_activity_clock_attendance_apply_handle_reject:
                if(approval) {
                    // 驳回
                    new RejectApplyDialog().setCallBack(new RejectApplyDialog.Callback() {
                        @Override
                        public void onItemClick(String note) {
                            doAgreeOrReject(ClockAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_REJECT, note);
                        }
                    }).show(getSupportFragmentManager(), "reject");
                }else {
                    // 撤销
                    doRevoke();
                }
                break;
        }
    }

    /**
     * 同意或者驳回
     */
    private void doAgreeOrReject(final int status, String remark) {
        if(mDynamicRadioBuilder == null || mApplyApprover == null || mApplyDetailsApplicant == null) {
            // 下一级审批人信息、 审批人信息以及审批信息为空
            showToast("审批人信息获取失败");
            return;
        }
        long approverInfoId = mApplyApprover.getId(); // 当前审批人的ID
        int leave = mApplyApprover.getLevel(); // 当前审批人的级别
        int time = mApplyDetailsApplicant.getDuration_hour(); // 请假时长
        if(status == ClockAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_REJECT) {
            // 驳回
            rejectOrAgreeApplyApprover(status, approverInfoId, time, 0, leave, remark, 0);
            return;
        }
        if(mApplyApprover.getLevel() == 1) {
            // 一级审批人, 重新计算时间
            int day = StringUtils.toInt(mDayView.getText().toString(), 0);
            int hour = StringUtils.toInt(mHourView.getText().toString(), 0);
            time = day * (int) mDivisor + hour;
            if(time == 0) {
                showToast("请填写有效时长");
                return;
            }
        }
        if(mDynamicRadioBuilder.isStop()) {
            // 终止审批
            rejectOrAgreeApplyApprover(status, approverInfoId, time, 1, leave, remark, 0);
            return;
        }
        long approvalId = mDynamicRadioBuilder.getValue();
        if(mNextApprover == null) {
            // 未指定类型, 可以不指定下一级审批人
            rejectOrAgreeApplyApprover(status, approverInfoId, time, 0, leave, remark, approvalId);
            return;
        }else if(mNextApprover.getType() == 1 || mNextApprover.getType() == 2) {
            // 自由审批或者固定审批
            if(approvalId == 0) {
                showToast("请选择审批人");
                return;
            }
        }
        rejectOrAgreeApplyApprover(status, approverInfoId, time, 0, leave, remark, approvalId);
    }

    /**
     * 同意或者驳回
     * @param status 状态 驳回或者同意
     * @param approverId 当前审批人条目信息ID
     * @param time 请假时长
     * @param stop 是否停止
     * @param leave 当前审批人审批级别
     * @param remark 当前驳回信息备注
     * @param nextApproverId 下一级审批人ID
     */
    private void rejectOrAgreeApplyApprover(final int status, long approverId, int time, int stop,
                                            int leave, String remark, long nextApproverId) {
        showLoadingDialog();
        OaApi.rejectOrAgreeApplyApprover(mId, approverId, time, stop
                , status, leave, remark, nextApproverId, new RequestCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        showToast("提交成功");
                        hideLoadingDialog();
                        if(status == ClockAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_AGREE) {
                            // 同意
                            setResult(200);
                        }else {
                            // 驳回
                            setResult(300);
                        }
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
     * 撤销
     */
    private void doRevoke() {
        showLoadingDialog();
        OaApi.revokeApplyApprover(mId, new RequestCallback<String>() {
            @Override
            public void onSuccess(String result) {
                hideLoadingDialog();
                showToast("撤销成功");
                setResult(400);
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

}
