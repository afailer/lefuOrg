package com.lefuorgn.lefu.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lefuorgn.R;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.lefu.bean.Staff;

/**
 * 通讯录中员工详情页面
 */

public class ContactDetailsActivity extends BaseActivity {

    private ImageView mStaffImg;
    private TextView mNameView, mSexView;
    private TextView mPhoneView, mTelView;
    private RelativeLayout mCallPhoneBtn, mCallTelBtn;
    private ImageView mCallTelImg;
    private TextView mMailView, mDepartmentsView, mJobView;
    private Staff mStaff;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contact_details;
    }

    @Override
    protected void initView() {
        setToolBarTitle("员工详情");
        mStaffImg = (ImageView) findViewById(R.id.img_contact_details_activity);
        mNameView = (TextView) findViewById(R.id.name_contact_details_activity);
        mSexView = (TextView) findViewById(R.id.sex_contact_details_activity);
        mPhoneView = (TextView) findViewById(R.id.phone_contact_details_activity);
        mTelView = (TextView) findViewById(R.id.tel_contact_details_activity);
        mCallTelImg = (ImageView) findViewById(R.id.img_call_tel_contact_details_activity);
        mCallPhoneBtn = (RelativeLayout) findViewById(R.id.call_phone_contact_details_activity);
        mCallTelBtn = (RelativeLayout) findViewById(R.id.call_tel_contact_details_activity);
        mMailView = (TextView) findViewById(R.id.mail_contact_details_activity);
        mDepartmentsView = (TextView) findViewById(R.id.departments_contact_details_activity);
        mJobView = (TextView) findViewById(R.id.job_contact_details_activity);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mStaff = (Staff) intent.getSerializableExtra("staff");
        ImageLoader.loadImg(mStaff.getIcon(), mStaffImg);
        mNameView.setText(mStaff.getStaff_name());
        mSexView.setText(mStaff.getGender() == 14 ? "男" : "女");
        mPhoneView.setText(mStaff.getMobile());
        mTelView.setText(mStaff.getPhone());
        mMailView.setText(mStaff.getMailbox());
        mDepartmentsView.setText(mStaff.getDept_name());
        mJobView.setText(mStaff.getPost_name());
        mCallPhoneBtn.setOnClickListener(this);
        if("".equals(mStaff.getPhone())) {
            mCallTelImg.setVisibility(View.INVISIBLE);
        }else {
            mCallTelBtn.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.call_phone_contact_details_activity:
                // 手机
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + mStaff.getMobile()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.call_tel_contact_details_activity:
                // 固话
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + mStaff.getPhone()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

            default:
                break;
        }
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
