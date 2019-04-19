package com.lefuorgn.lefu.activity;

import android.widget.EditText;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.lefu.bean.Leave;
import com.lefuorgn.util.StringUtils;

/**
 * 老人外出请假详情
 */

public class LeaveDetailsActivity extends BaseActivity {

    private TextView mElderName;

    private TextView mOutTime, mBackTime, mRealBackTime, mSignature, mReason;

    private EditText mNotes;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_leave_details;
    }

    @Override
    protected void initView() {
        // 初始化信息展示控件
        mElderName = (TextView) findViewById(R.id.tv_activity_leave_details_name);
        mOutTime = (TextView) findViewById(R.id.tv_activity_leave_details_rl_time);
        mBackTime = (TextView) findViewById(R.id.tv_activity_leave_details_eb_time);
        mRealBackTime = (TextView) findViewById(R.id.tv_activity_leave_details_rb_time);
        mSignature = (TextView) findViewById(R.id.tv_activity_leave_details_signature);
        mReason = (TextView) findViewById(R.id.tv_activity_leave_details_reason);
        mNotes = (EditText) findViewById(R.id.tv_activity_leave_details_note);
    }

    @Override
    protected void initData() {
        setToolBarTitle("请假详情");
        Leave leave = (Leave) getIntent().getSerializableExtra("leave");
        mElderName.setText(leave.getElderly_name());
        mOutTime.setText(StringUtils.getFormatData(leave.getLeave_hospital_dt(), StringUtils.FORMAT));
        mBackTime.setText(leave.getExpected_return_dt() == 0 ? "" : StringUtils
                .getFormatData(leave.getExpected_return_dt(), StringUtils.FORMAT));
        mRealBackTime.setText(StringUtils.getFormatData(leave.getReal_return_dt(), StringUtils.FORMAT));
        mSignature.setText(leave.getParty_signature());
        mReason.setText(leave.getLeave_reason());
        mNotes.setText(leave.getNotes_matters());
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
