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
import com.lefuorgn.api.remote.OaApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.db.model.basic.StaffCache;
import com.lefuorgn.db.util.StaffCacheManager;
import com.lefuorgn.dialog.DatePickerDialog;
import com.lefuorgn.dialog.ListDialog;
import com.lefuorgn.oa.bean.ApplyApproval;
import com.lefuorgn.oa.bean.ClockType;
import com.lefuorgn.oa.bean.ExternalPackage;
import com.lefuorgn.oa.bean.RemainingTime;
import com.lefuorgn.oa.utils.MultipleClicks;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;
import com.lefuorgn.viewloader.ViewLoader;
import com.lefuorgn.viewloader.base.DynamicViewBuilder;
import com.lefuorgn.viewloader.builder.DynamicRadioBuilder;
import com.lefuorgn.viewloader.impl.PersonEntryPageBuilder;

import java.util.List;


/**
 * 请假申请页面
 */

public class ClockApplyActivity extends BaseActivity {

    // 从上个Activity中获取的数据
    private ClockType mClockType;
    private long mDivisor;

    private TextView mTimeStartView, mTimeEndView;
    private EditText mRemarkView;

    private LinearLayout mLeaveView; // 请假申请显示多余模块
    private TextView mLeaveTypeBtn; // 请假申请当前类型选择按钮
    private ClockType mLeaveClockType; // 请假申请的当前选择类型
    private TextView mNoteView; // 基本信息提示模块

    private LinearLayout mParentView;
    private DynamicRadioBuilder mDynamicRadioBuilder; // 审批人控件
    private boolean radioDelete; // 审批人当前状态是否是删除状态

    private StaffCacheManager mStaffCacheManager;

    private int mAvailableTime; // 可用调休时间


    // 开始和结束时间选择控件工具类
    private DatePickerDialog mStartDialog, mEndDialog;
    // 请假申请类型选择dialog
    private ListDialog<ClockType> mListDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_clock_apply;
    }

    @Override
    protected void initView() {
        mParentView = (LinearLayout) findViewById(R.id.ll_activity_clock_apply);
        mTimeStartView = (TextView) findViewById(R.id.tv_activity_clock_apply_time_start);
        mTimeEndView = (TextView) findViewById(R.id.tv_activity_clock_apply_time_end);
        findViewById(R.id.btn_activity_clock_apply_time_start).setOnClickListener(this);
        findViewById(R.id.btn_activity_clock_apply_time_end).setOnClickListener(this);
        mRemarkView = (EditText) findViewById(R.id.et_activity_clock_apply_remark);
        mLeaveView = (LinearLayout) findViewById(R.id.ll_activity_clock_apply_leave);
        mNoteView = (TextView) findViewById(R.id.tv_activity_clock_apply_note);
        findViewById(R.id.btn_activity_clock_attendance_apply_handle_cancel).setOnClickListener(this);
        findViewById(R.id.btn_activity_clock_attendance_apply_handle_confirm).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mClockType = (ClockType) getIntent().getSerializableExtra("clockType");
        mDivisor = getIntent().getLongExtra("divisor", 8);
        setToolBarTitle(mClockType.getName() + "申请");
        mStaffCacheManager = StaffCacheManager.getInstance();
        if(mClockType.getType() == -1) {
            // 请假申请页面, 获取第一个页面
            mLeaveView.setVisibility(View.VISIBLE);
            mLeaveTypeBtn = (TextView) findViewById(R.id.btn_activity_clock_apply_type);
            mLeaveTypeBtn.setOnClickListener(this);
            mLeaveClockType = mClockType.getClockTypes().get(0);
            mLeaveTypeBtn.setText(mLeaveClockType.getName());
            getRemainingTime(mLeaveClockType.getId());
        }else {
            getRemainingTime(mClockType.getId());
        }
    }

    /**
     * 获取假期类型的剩余时间
     * @param id 获取假期ID
     */
    private void getRemainingTime(long id) {
        showLoadingDialog();
        OaApi.getRemainingTime(id, new RequestCallback<ExternalPackage>() {
            @Override
            public void onSuccess(ExternalPackage result) {
                setPageInfo(result);
                hideLoadingDialog();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
                setPageInfo(null);
                hideLoadingDialog();
            }
        });
    }

    /**
     * 设置页面信息
     */
    private void setPageInfo(ExternalPackage e) {
        if(mClockType.getType() == 2) {
            // 调休模块
            setNotePageInfo(true, e != null ? e.getDefaultVacationTime() : null, "调休");
        }else if(mClockType.getType() == -1) {
            // 请假申请模块
            if(mLeaveClockType.getType() == 1) {
                // 年假
                setNotePageInfo(true, e != null ? e.getDefaultVacationTime() : null, "年假");
            }else {
                // 其他假期
                setNotePageInfo(false, null, "");
            }
        }
        int type = 0;
        ApplyApproval approval = null;
        if(e != null) {
            approval = e.getVerifyLine();
            if(approval != null) {
                type = approval.getType();
            }
        }
        ViewLoader loader = new ViewLoader.Builder()
                .context(this).copy(false).dynamic(type != 2)
                .paddingWidth(16).paddingHeight(8)
                .labelSize(14).labelColor(Color.BLACK)
                .valueSize(14).valueColor(Color.BLACK).build();
        // 初始化审批人信息
        if(mDynamicRadioBuilder != null) {
            // 审批人信息添加过
            mParentView.removeView(mParentView.getChildAt(mParentView.getChildCount() - 1));
        }
        PersonEntryPageBuilder personPageBuilder = new PersonEntryPageBuilder();
        loader.build(personPageBuilder);
        mParentView.addView(personPageBuilder.getPageView());
        mDynamicRadioBuilder = personPageBuilder.getDynamicRadioBuilder();
        mDynamicRadioBuilder.showStopView(false);
        if(type == 2) {
            // 固定审批
            mDynamicRadioBuilder.setTextView(approval.getVerify_user_name(), approval.getVerify_user_id());
        }else {
            mDynamicRadioBuilder.setOnButtonClickListener(new DynamicViewBuilder.OnButtonClickListener() {
                @Override
                public void onUpClick(View v) {
                    Intent intent = new Intent(ClockApplyActivity.this, SelectStuffActivity.class);
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

    /**
     * 设置可用调休模块
     */
    private void setNotePageInfo(boolean visible, RemainingTime info, String type) {
        if(!visible) {
            mNoteView.setVisibility(View.GONE);
            return;
        }
        mNoteView.setVisibility(View.VISIBLE);
        if(info != null) {
            long time = info.getDefault_time();
            mAvailableTime = (int) time;
            mNoteView.setText("可用" + type + time / mDivisor + "天" + time % mDivisor + "小时");
        }else {
            mNoteView.setText("可用" + type + "0天0小时");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity_clock_apply_time_start:
                // 获取开始时间
                if (mStartDialog == null) {
                    mStartDialog = new DatePickerDialog();
                    mStartDialog.setTitle("选择开始时间")
                            .setMinDate(1388550537133L)
                            .setClickCallBack(new DatePickerDialog.ClickCallBack() {
                                @Override
                                public void leftClick() {
                                    mStartDialog.dismiss();
                                }

                                @Override
                                public void rightClick(long time) {
                                    mTimeStartView.setText(StringUtils.getFormatData(time, StringUtils.FORMAT));
                                    mTimeEndView.setText("");
                                    mStartDialog.dismiss();
                                }
                            });
                }
                mStartDialog.show(getSupportFragmentManager(), "StartDialog");
                break;
            case R.id.btn_activity_clock_apply_time_end:
                // 获取结束时间
                if (StringUtils.isEmpty(mTimeStartView.getText().toString())) {
                    showToast("请选择开始时间");
                    return;
                }
                if (mEndDialog == null) {
                    mEndDialog = new DatePickerDialog();
                    mEndDialog.setTitle("选择结束时间")
                            .setMinDate(1388550537133L)
                            .setClickCallBack(new DatePickerDialog.ClickCallBack() {
                                @Override
                                public void leftClick() {
                                    mEndDialog.dismiss();
                                }

                                @Override
                                public void rightClick(long time) {
                                    if(time <= StringUtils.getFormatData(mTimeStartView.getText().toString(), StringUtils.FORMAT)) {
                                        showToast("不能早于开始时间");
                                        return;
                                    }
                                    mTimeEndView.setText(StringUtils.getFormatData(time, StringUtils.FORMAT));
                                    mEndDialog.dismiss();
                                }
                            });
                }
                mEndDialog.show(getSupportFragmentManager(), "EndDialog");
                break;
            case R.id.btn_activity_clock_apply_type:
                // 选取请假申请类型
                if(mListDialog == null) {
                    mListDialog = new ListDialog<ClockType>();
                    mListDialog.setData(mClockType.getClockTypes());
                    mListDialog.setTitle("选择请假申请类型");
                    mListDialog.setCallBack(new ListDialog.Callback<ClockType>() {
                        @Override
                        public void convert(TextView view, ClockType o) {
                            view.setText(o.getName());
                        }

                        @Override
                        public void onItemClick(View view, ClockType o) {
                            if(mLeaveClockType.getType() == o.getType()) {
                                return;
                            }
                            mLeaveClockType = o;
                            mLeaveTypeBtn.setText(o.getName());
                            getRemainingTime(mLeaveClockType.getId());
                        }
                    });
                }
                mListDialog.show(getSupportFragmentManager(), "ClockType");
                break;
            case R.id.btn_activity_clock_attendance_apply_handle_cancel:
                finish();
                break;
            case R.id.btn_activity_clock_attendance_apply_handle_confirm:
                // 提交
                doConfirm();
                break;
        }
    }

    /**
     * 提交信息
     */
    private void doConfirm() {
        if(MultipleClicks.isFastDoubleClick()) {
            TLog.error("多次点击的情况");
            return;
        }
        if(mClockType.getType() == 2 && mAvailableTime <= 0) {
            // 调休
            showToast("您的可用假期时间不足");
            return;
        }
        if(mClockType.getType() == -1 && (mLeaveClockType.getType() == 1 && mAvailableTime <= 0)) {
            // 年假
            showToast("您的可用假期时间不足");
            return;
        }
        if(mDynamicRadioBuilder == null) {
            showToast("数据获取失败...");
            return;
        }
        String timeStart = mTimeStartView.getText().toString();
        String timeEnd = mTimeEndView.getText().toString();
        String remark = mRemarkView.getText().toString();
        if(StringUtils.isEmpty(timeStart)) {
            showToast("请选择开始时间");
            return;
        }
        if(StringUtils.isEmpty(timeEnd)) {
            showToast("请选择结束时间");
            return;
        }
        long start = StringUtils.getFormatData(timeStart, StringUtils.FORMAT);
        long end = StringUtils.getFormatData(timeEnd, StringUtils.FORMAT);
        long id = mClockType.getType() == -1 ? mLeaveClockType.getId() : mClockType.getId();
        String name = mClockType.getType() == -1 ? mLeaveClockType.getName() : mClockType.getName();
        long approverId = mDynamicRadioBuilder.getValue();
        if(approverId == 0) {
            showToast("请选择审批人");
            return;
        }
        OaApi.confirmApplyApprover(start, end, id, name, remark, approverId, new RequestCallback<String>() {
            @Override
            public void onSuccess(String result) {
                showToast("申请提交成功");
                hideLoadingDialog();
                setResult(200);
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
