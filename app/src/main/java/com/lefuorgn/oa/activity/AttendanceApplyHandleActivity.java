package com.lefuorgn.oa.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.api.remote.OaApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.db.model.basic.StaffCache;
import com.lefuorgn.db.util.PermissionManager;
import com.lefuorgn.db.util.StaffCacheManager;
import com.lefuorgn.oa.bean.AttendanceApplyDetails;
import com.lefuorgn.oa.bean.AttendanceApprovalApprover;
import com.lefuorgn.oa.bean.AttendanceApprovalCarbonCopy;
import com.lefuorgn.oa.bean.AttendanceApprovalSubmit;
import com.lefuorgn.oa.bean.AttendanceApprovalSubmitCopy;
import com.lefuorgn.oa.bean.ExternalPackage;
import com.lefuorgn.oa.dialog.RejectApplyDialog;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;
import com.lefuorgn.viewloader.ViewLoader;
import com.lefuorgn.viewloader.base.DynamicViewBuilder;
import com.lefuorgn.viewloader.builder.DynamicCheckBoxBuilder;
import com.lefuorgn.viewloader.builder.DynamicRadioBuilder;
import com.lefuorgn.viewloader.impl.DisplayPageBuilder;
import com.lefuorgn.viewloader.impl.PersonEntryPageBuilder;
import com.lefuorgn.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 我审批的和我申请的页面的
 */

public class AttendanceApplyHandleActivity extends BaseActivity {

    private long mId;
    private boolean approval; // 当前页面所属类型; true: 我审批的页面; false: 我申请的页面
    private int mStatus;

    private CircleImageView mHeadImg;
    private TextView mNameView, mAgencyView, mLabelView;
    private LinearLayout mContentParent;

    private AttendanceApplyDetails mAttendanceApplyDetails;
    private ExternalPackage mExternalPackage;

    private DynamicRadioBuilder mDynamicRadioBuilder; // 审批人控件
    private DynamicCheckBoxBuilder mDynamicCheckBoxBuilder; // 抄送人控件

    private StaffCacheManager mStaffCacheManager;

    private boolean checkBoxDelete; // 抄送人当前状态是否是删除状态
    private boolean radioDelete; // 审批人当前状态是否是删除状态

    @Override
    protected int getLayoutId() {
        return R.layout.activity_attendance_apply_handle;
    }

    @Override
    protected void initView() {
        // 初始化用户信息
        mHeadImg = (CircleImageView) findViewById(R.id.iv_activity_attendance_apply_handle);
        mHeadImg.setBorderColor(Color.parseColor("#56D5B3"));
        mHeadImg.setBorderWidth(StringUtils.dip2px(this, 4f));
        mNameView = (TextView) findViewById(R.id.tv_activity_attendance_apply_handle_name);
        mAgencyView = (TextView) findViewById(R.id.tv_activity_attendance_apply_handle_agency);
        findViewById(R.id.btn_activity_attendance_apply_handle).setOnClickListener(this);
        // 申请类型标签
        mLabelView = (TextView) findViewById(R.id.tv_activity_attendance_apply_handle_label);
        // 内容父控件
        mContentParent = (LinearLayout) findViewById(R.id.ll_activity_attendance_apply_handle_content);
        mStatus = getIntent().getIntExtra("status", 0);
        if(mStatus != 1) {
            findViewById(R.id.ll_activity_attendance_apply_handle_btn).setVisibility(View.GONE);
            return;
        }
        approval = getIntent().getBooleanExtra("approval", false);
        // 操作权限(驳回或者同意)
        boolean handlePermission = PermissionManager.hasPermission(
                PermissionManager.OA_SAVEASKAUDIT + PermissionManager.P_C);
        TextView tv = (TextView) findViewById(R.id.btn_activity_attendance_apply_handle_reject);
        tv.setOnClickListener(this);
        if(approval) {
            // 我审批的
            if(!handlePermission) {
                findViewById(R.id.ll_activity_attendance_apply_handle_btn).setVisibility(View.GONE);
            }else {
                tv.setText("驳回");
                findViewById(R.id.btn_activity_attendance_apply_handle_agree).setOnClickListener(this);
            }
        }else {
            // 我申请的
            tv.setText("撤销");
            findViewById(R.id.btn_activity_attendance_apply_handle_agree).setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        mId = getIntent().getLongExtra("id", 0);
        mStaffCacheManager = StaffCacheManager.getInstance();
        showWaitDialog();
        OaApi.getAttendanceApplyDetails(mId, new RequestCallback<AttendanceApplyDetails>() {
            @Override
            public void onSuccess(AttendanceApplyDetails result) {
                mAttendanceApplyDetails = result;
                initInfo(result);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
                hideWaitDialog();
            }
        });
    }

    /**
     * 初始化内容
     */
    private void initInfo(AttendanceApplyDetails details) {
        if(details == null) {
            hideWaitDialog();
            return;
        }
        // 设置用户信息
        setToolBarTitle(details.getOa_verify_from_name());
        mLabelView.setText(details.getOa_verify_from_name());
        ImageLoader.loadImgForUserDefinedView(details.getUser_icon(), mHeadImg);
        mNameView.setText(details.getUser_name());
        mAgencyView.setText(AppContext.getInstance().getAgencyName());
        // 加载审批内容
        ViewLoader loader = new ViewLoader.Builder().content(details.getContent())
                .context(this).build();
        DisplayPageBuilder pageBuilder = new DisplayPageBuilder();
        loader.build(pageBuilder);
        mContentParent.addView(pageBuilder.getPageView());
        if(mStatus == 1){
            // 需要审批信息的控件
            OaApi.getAttendanceApprovalApprover(mId, new RequestCallback<ExternalPackage>() {
                @Override
                public void onSuccess(ExternalPackage result) {
                    mExternalPackage = result;
                    if(approval) {
                        checkPersonPage(result);
                    }
                    hideWaitDialog();
                }

                @Override
                public void onFailure(ApiHttpException e) {
                    showToast(e.getMessage());
                    hideWaitDialog();
                }

            });
        }else {
            hideWaitDialog();
        }
    }

    /**
     * 校验审批人信息
     */
    private void checkPersonPage(ExternalPackage external) {
        if(external == null) {
            loadPersonPage(0, 0, null);
            return;
        }
        AttendanceApprovalApprover person = external.getNextVerifyLine();
        if(person == null) {
            loadPersonPage(0, 0, null);
            return;
        }
        loadPersonPage(external.getAudit() != null ? external.getAudit().getIs_stop() : 0, person.getType(), person);
    }

    /**
     * 加载审批人或者抄送人控件
     * @param stopId 1: 可以阻止; 0: 不可阻止
     * @param type // 1: 自由审批; 2: 固定审批 0: 其他
     */
    private void loadPersonPage(int stopId, int type, AttendanceApprovalApprover approver) {
        ViewLoader loader = new ViewLoader.Builder()
                .context(this).copy(true).dynamic(type != 2).build();
        // 初始化审批人信息
        PersonEntryPageBuilder personPageBuilder = new PersonEntryPageBuilder();
        loader.build(personPageBuilder);
        mContentParent.addView(personPageBuilder.getPageView());
        mDynamicRadioBuilder = personPageBuilder.getDynamicRadioBuilder();
        mDynamicRadioBuilder.showStopView(stopId == 1);
        if(type == 2) {
            // 固定审批
            mDynamicRadioBuilder.setTextView(approver.getVerify_user_name(), approver.getVerify_user_id());
        }else {
            mDynamicRadioBuilder.setOnButtonClickListener(new DynamicViewBuilder.OnButtonClickListener() {
                @Override
                public void onUpClick(View v) {
                    Intent intent = new Intent(AttendanceApplyHandleActivity.this, SelectStuffActivity.class);
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
        // 初始化抄送人信息
        mDynamicCheckBoxBuilder = personPageBuilder.getDynamicCheckBoxBuilder();
        if(approver != null) {
            // 初始化固定人
            List<AttendanceApprovalCarbonCopy> copies = approver.getVerifyLineCopys();
            if(copies != null) {
                for (AttendanceApprovalCarbonCopy carbonCopy : copies) {
                    mDynamicCheckBoxBuilder.addTextView(carbonCopy.getCopy_user_name(), carbonCopy.getCopy_user_id());
                }
            }
        }
        // 添加历史记录
        initDynamicCheckBoxBuilder(null);
        mDynamicCheckBoxBuilder.setOnButtonClickListener(new DynamicViewBuilder.OnButtonClickListener() {
            @Override
            public void onUpClick(View v) {
                Intent intent = new Intent(AttendanceApplyHandleActivity.this, SelectStuffActivity.class);
                intent.putExtra("isCopy", "isCopy");
                startActivityForResult(intent, 600);
                checkBoxDelete = false;
                mDynamicCheckBoxBuilder.switchView(false);
            }

            @Override
            public void onDownClick(View v) {
                checkBoxDelete = !checkBoxDelete;
                mDynamicCheckBoxBuilder.switchView(checkBoxDelete);
            }

            @Override
            public void onViewClick(long tag) {
                mStaffCacheManager.removeStaffCache(tag, true);
            }
        });

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

    /**
     * 显示抄送人控件
     */
    private void initDynamicCheckBoxBuilder(List<Long> ids) {
        List<StaffCache> staffTrueCaches = mStaffCacheManager.getStaffCache(true);
        int index = 100000;
        mDynamicCheckBoxBuilder.removeAllCheckBox();
        for (StaffCache cache : staffTrueCaches) {
            mDynamicCheckBoxBuilder.addCheckBox(++index, cache.getName(),
                    cache.getId(), ids != null && ids.contains(cache.getId()));
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
        }else if(resultCode == 500 && data != null) {
            // 添加抄送人员
            String stuffStr = data.getStringExtra("stuff");
            if(StringUtils.isEmpty(stuffStr)) {
                return;
            }
            String[] stuffs = stuffStr
                    .substring(0, stuffStr.length() - SelectStuffActivity.DIVIDE_STR.length())
                    .split(SelectStuffActivity.DIVIDE_STR);
            List<Long> ids = new ArrayList<Long>();
            for (String s : stuffs) {
                String[] stuff = s.split(",");
                if(stuff.length == 2) {
                    long id;
                    id = StringUtils.toLong(stuff[0], 0);
                    if(id == 0) {
                        return;
                    }
                    ids.add(id);
                    StaffCache cache = new StaffCache();
                    cache.setId(id);
                    cache.setName(stuff[1]);
                    mStaffCacheManager.addStaffCache(cache, true);
                }
            }
            initDynamicCheckBoxBuilder(ids);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity_attendance_apply_handle:
                // 查看进度
                Intent intent = new Intent(this, ApplicationProgressActivity.class);
                intent.putExtra("details", mAttendanceApplyDetails);
                startActivity(intent);
                break;
            case R.id.btn_activity_attendance_apply_handle_agree:
                // 我审批的, 同意按钮
                if(mExternalPackage == null || mExternalPackage.getAudit() == null) {
                    showToast("请求错误, 请关闭页面重新进入...");
                    return;
                }
                AttendanceApprovalApprover approver = mExternalPackage.getNextVerifyLine();
                if(mDynamicRadioBuilder.isStop()) {
                    // 终止审批
                    TLog.error("终止审批");
                    mExternalPackage.getAudit().setStop(1);
                    approvalOperation(3, "", null, "审批");
                    return;
                }
                // 不终止
                mExternalPackage.getAudit().setStop(0);
                long submitId = mDynamicRadioBuilder.getValue();
                if(approver == null) {
                    // 未指定类型
                    if(submitId == 0) {
                        TLog.error("未指定类型");
                        approvalOperation(3, "", null, "审批");
                        return;
                    }
                }else if(approver.getType() == 1 || approver.getType() == 2) {
                    // 自由审批或者固定审批
                    if(submitId == 0) {
                        showToast("请选择审批人");
                        return;
                    }
                }else {
                    if(submitId == 0) {
                        TLog.error("其他类型");
                        approvalOperation(3, "", null, "审批");
                        return;
                    }
                }
                AttendanceApprovalSubmit submit = new AttendanceApprovalSubmit();
                submit.setVerify_user_id(submitId);
                submit.setVerifyAskCopys(new ArrayList<AttendanceApprovalSubmitCopy>());
                if(mDynamicCheckBoxBuilder != null) {
                    List<Long> ids = mDynamicCheckBoxBuilder.getValue();
                    for (Long id : ids) {
                        AttendanceApprovalSubmitCopy copy = new AttendanceApprovalSubmitCopy();
                        copy.setCopy_user_id(id);
                        submit.getVerifyAskCopys().add(copy);
                    }
                }
                approvalOperation(3, "", submit, "审批");
                break;
            case R.id.btn_activity_attendance_apply_handle_reject:
                if(mExternalPackage == null || mExternalPackage.getAudit() == null) {
                    showToast("请求错误, 请关闭页面重新进入...");
                    return;
                }
                if(approval) {
                    // 驳回
                    new RejectApplyDialog().setCallBack(new RejectApplyDialog.Callback() {
                        @Override
                        public void onItemClick(String note) {
                            approvalOperation(2, note, null, "驳回");
                        }
                    }).show(getSupportFragmentManager(), "reject");
                }else {
                    // 撤销
                    mExternalPackage.getAudit()
                            .setVerify_user_id(AppContext.getInstance().getUser().getUser_id());
                    approvalOperation(4, "", null, "撤销");
                }
                break;
        }
    }

    /**
     * 审核申请 通过、驳回、撤销均调用此接口
     */
    private void approvalOperation(final int status, String remark, AttendanceApprovalSubmit submit, final String message) {
        mExternalPackage.getAudit().setStatus(status);
        mExternalPackage.getAudit().setRemark(remark);
        showLoadingDialog();
        OaApi.approvalOperation(mExternalPackage.getAudit(), submit, new RequestCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                showToast(message + "成功");
                hideLoadingDialog();
                // 2: 驳回 3: 同意 4: 撤销
                setResult(status);
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
