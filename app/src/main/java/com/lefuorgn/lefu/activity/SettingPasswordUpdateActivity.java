package com.lefuorgn.lefu.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.util.StringUtils;

/**
 * 修改密码
 */

public class SettingPasswordUpdateActivity extends BaseActivity {

    private EditText mOldPswView;
    private EditText mNewPswView;
    private EditText mConfirmPswView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_password_update;
    }

    @Override
    protected void initView() {
        mOldPswView = (EditText) findViewById(R.id.et_activity_setting_password_update_old);
        mNewPswView = (EditText) findViewById(R.id.et_activity_setting_password_update_new);
        mConfirmPswView = (EditText) findViewById(R.id.et_activity_setting_password_update_confirm);
        findViewById(R.id.btn_activity_setting_password_update_input).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setToolBarTitle(R.string.setting_password_update);
    }

    @Override
    public void onClick(View v) {
        String oldPsw = mOldPswView.getText().toString().trim();
        final String newPsw = mNewPswView.getText().toString().trim();
        String confirmPsw = mConfirmPswView.getText().toString().trim();
        if(StringUtils.isEmpty(oldPsw) || StringUtils.isEmpty(newPsw) || StringUtils.isEmpty(confirmPsw)) {
            // 输入框有为空的情况
            showToast("输入框不能为空");
            return;
        }
        if(newPsw.length() < 6 || newPsw.length() >24) {
            // 新密码长度小于6位
            showToast("密码长度只能为6~24位");
            mNewPswView.setText("");
            mConfirmPswView.setText("");
            return;
        }
        if(!TextUtils.equals(newPsw, confirmPsw)) {
            // 新密码前后俩次输入不一致
            showToast("俩次密码输入不一致,请重新输入");
            mNewPswView.setText("");
            mConfirmPswView.setText("");
            return;
        }
        LefuApi.updatePassword(oldPsw, newPsw, new RequestCallback<String>() {
            @Override
            public void onSuccess(String result) {
                // 保存新的密码
                AppContext.getInstance().updateUserPassword(newPsw);
                showToast("密码修改成功");
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
