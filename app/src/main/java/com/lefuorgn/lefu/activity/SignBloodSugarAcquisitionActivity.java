package com.lefuorgn.lefu.activity;

import android.view.View;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.lefu.bean.SignInfo;

/**
 * 血糖数据采集页面
 */

public class SignBloodSugarAcquisitionActivity extends BaseActivity {

    // 设备名称显示控件, 当前测试的老人名称
    private TextView mDeviceView, mElderlyView;
    private TextView mStartView, mEndView;

    private String mAddress; // 蓝牙设备物理地址
    private String mName; // 蓝牙设备名称
    private SignInfo mSignInfo; // 当前条目信息

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_blood_sugar_acquisition;
    }

    @Override
    protected void initView() {
        mDeviceView = (TextView) findViewById(R.id.tv_activity_blood_sugar_acquisition_device_name);
        mElderlyView = (TextView) findViewById(R.id.tv_activity_blood_sugar_acquisition_elderly);
        mStartView = (TextView) findViewById(R.id.tv_activity_blood_sugar_acquisition_start_time);
        mEndView = (TextView) findViewById(R.id.tv_activity_blood_sugar_acquisition_end_time);
        findViewById(R.id.btn_activity_blood_sugar_acquisition_receive).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setToolBarTitle("血糖设备采集");
        mAddress = getIntent().getStringExtra("address");
        mName = getIntent().getStringExtra("name");
        mSignInfo = (SignInfo) getIntent().getSerializableExtra("SignInfo");
        mDeviceView.setText(mName);
        mElderlyView.setText(mSignInfo.getOldPeopleName());
        mStartView.setText("----");
        mEndView.setText("----");
    }

    @Override
    public void onClick(View v) {

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
