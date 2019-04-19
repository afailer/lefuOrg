package com.lefuorgn.oa.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.util.StringUtils;

/**
 * 签到或者签退结果展示页面
 */

public class SignOrSignOutResultActivity extends BaseActivity {

    private TextView mDateView, mTimeView, mStateView;
    private TextView mSkipBtn, mPinkBtn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_or_out_result;
    }

    @Override
    protected void initView() {
        mDateView = (TextView) findViewById(R.id.tv_activity_sign_out_result_date);
        mTimeView = (TextView) findViewById(R.id.tv_activity_sign_out_result_time);
        mStateView = (TextView) findViewById(R.id.tv_activity_sign_out_result_state);
        mSkipBtn = (TextView) findViewById(R.id.btn_activity_sign_out_result);
        mPinkBtn = (TextView) findViewById(R.id.btn_activity_sign_out_result_pink);
    }

    @Override
    protected void initData() {
        setToolBarTitle("日常考勤");
        int type = getIntent().getIntExtra("type", 0);
        int state = getIntent().getIntExtra("state", 0);
        long time = getIntent().getLongExtra("time", 0);
        time += StringUtils.getSectionTime(System.currentTimeMillis())[0];
        String info = getIntent().getStringExtra("info");
        mTimeView.setText(StringUtils.getFormatData(time, "HH:mm:ss"));
        String date = StringUtils.getFormatData(System.currentTimeMillis(), "yyyy年MM月dd日");
        mDateView.setText(String.format("%s时间: %s", type == 2 ? "签退" : "签到", date));
        mStateView.setText(info);
        if(state == 202) {
            // 迟到
            mStateView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.daily_attendance_late, 0, 0);
            clockAbnormal();
        }else if(state == 203) {
            // 旷工
            mStateView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.daily_attendance_absenteeism, 0, 0);
            clockAbnormal();
        }else if(state == 204) {
            // 早退
            mStateView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.daily_attendance_leave_early, 0, 0);
            clockAbnormal();
        }else {
            // 打卡成功
            clockSuccess();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity_sign_out_result:
            case R.id.btn_activity_sign_out_result_pink:
                // 跳转到考勤记录页面
                startActivity(new Intent(getApplicationContext(),AttendanceRecordActivity.class));
                break;
        }
    }

    /**
     * 打卡成功
     */
    private void clockSuccess() {
        mStateView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.daily_attendance_success, 0, 0);
        mDateView.setTextColor(Color.parseColor("#1E88F5"));
        mTimeView.setTextColor(Color.parseColor("#1E88F5"));
        mSkipBtn.setOnClickListener(this);
        mPinkBtn.setVisibility(View.GONE);
    }

    /**
     * 打卡异常
     */
    private void clockAbnormal() {
        mDateView.setTextColor(Color.parseColor("#FF5064"));
        mTimeView.setTextColor(Color.parseColor("#FF5064"));
        mPinkBtn.setOnClickListener(this);
        mSkipBtn.setVisibility(View.GONE);
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
