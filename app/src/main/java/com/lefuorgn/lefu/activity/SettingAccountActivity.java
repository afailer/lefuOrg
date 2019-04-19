package com.lefuorgn.lefu.activity;

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
import com.lefuorgn.util.StringUtils;

/**
 * 账户设置
 */

public class SettingAccountActivity extends BaseActivity {

    private EditText mNameView;
    private TextView mPhoneView;
    private EditText mEmailView;

    private User mUser;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_account;
    }

    @Override
    protected void initView() {
        mNameView = (EditText) findViewById(R.id.et_activity_setting_account_name);
        mPhoneView = (TextView) findViewById(R.id.tv_activity_setting_account_phone);
        mEmailView = (EditText) findViewById(R.id.et_activity_setting_account_email);
        findViewById(R.id.btn_dialog_nursing_info_input).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setToolBarTitle(R.string.setting_account);
        mUser = AppContext.getInstance().getUser();
        mNameView.setText(mUser.getUser_name());
        mNameView.setSelection(mNameView.length());
        mPhoneView.setText(mPhoneView.getText() + "  " + mUser.getMobile());
        mEmailView.setText(mUser.getMailbox());
    }

    @Override
    public void onClick(View v) {
        //
        final String name = mNameView.getText().toString().trim();
        final String email = mEmailView.getText().toString().trim();
        if(StringUtils.isEmpty(name) || StringUtils.isEmpty(email)) {
           showToast("输入框不能为空");
            return;
        }
        LefuApi.updateUserInfo(name, email, mUser, new RequestCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mUser.setUser_name(name);
                mUser.setMailbox(email);
                AppContext.getInstance().updateUserInfo(mUser);
                showToast("修改成功");
                finish();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
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
