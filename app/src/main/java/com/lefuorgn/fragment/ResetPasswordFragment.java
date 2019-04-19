package com.lefuorgn.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lefuorgn.AppLogin;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseFragment;
import com.lefuorgn.bean.User;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;
import com.lefuorgn.widget.togglebutton.ToggleButton;
import com.lefuorgn.widget.togglebutton.ToggleButton.OnToggleChanged;


/**
 * 忘记密码
 */

public class ResetPasswordFragment extends BaseFragment {

    // 注册用户所需手机号、密码和短信验证码
    private EditText mPhoneView, mPasswordView, mVerifyCodeView;
    private TextView mVerifyCodeBtn;

    /**
     * 记录验证码是否获取成功
     */
    protected boolean isSendSuccess;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reset_password;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mPhoneView = (EditText) view.findViewById(R.id.et_fragment_reset_phone);
        mPasswordView = (EditText) view.findViewById(R.id.et_fragment_reset_psw);
        mVerifyCodeView = (EditText) view.findViewById(R.id.et_fragment_reset_verify_code);
        mVerifyCodeBtn = (TextView) view.findViewById(R.id.btn_fragment_reset_verify_code);
        mVerifyCodeBtn.setOnClickListener(this);
        view.findViewById(R.id.btn_fragment_reset_login_skip).setOnClickListener(this);
        view.findViewById(R.id.btn_fragment_reset_login).setOnClickListener(this);
        ToggleButton tButton = (ToggleButton) view.findViewById(R.id.tb_fragment_reset);
        tButton.setOnToggleChanged(new OnToggleChanged() {

            @Override
            public void onToggle(boolean on) {
                if(on) {
                    // 密码可见
                    mPasswordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mPasswordView.setSelection(mPasswordView.length());
                }else {
                    mPasswordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mPasswordView.setSelection(mPasswordView.length());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fragment_reset_verify_code:
                // 获取验证码
                getVerifyCode();
                break;
            case R.id.btn_fragment_reset_login_skip:
                // 跳转到登录页面
                ((AppLogin)getActivity()).skipLoginFragment();
                break;
            case R.id.btn_fragment_reset_login:
                // 登录
                resetPassword();
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        final String phone = mPhoneView.getText().toString();
        if(!checkPhoneLegal(phone)) {
            return;
        }
        LefuApi.isRegister(phone, new RequestCallback<User>() {
            @Override
            public void onSuccess(User result) {
                showToast("该号码未注册,请先注册");
            }

            @Override
            public void onFailure(ApiHttpException e) {
                if(e.getErrorCode() == 6) {
                    getMobileCode(phone, mVerifyCodeBtn);
                }else {
                    showToast(e.getMessage());
                }
            }
        });
    }

    /**
     * 重置密码
     */
    private void resetPassword() {
        if(!isSendSuccess) {
            showToast("请获取验证码");
            return;
        }
        final String phone = mPhoneView.getText().toString().replace(" ", "");
        final String password = mPasswordView.getText().toString().trim();
        String verifyCode = mVerifyCodeView.getText().toString().trim();
        if(!checkPhoneLegal(phone)) {
            return;
        }
        if(StringUtils.isEmpty(password)) {
            showToast("密码不能为空");
            return;
        }
        if(StringUtils.isEmpty(verifyCode)) {
            showToast("验证码不能为空");
            return;
        }
        LefuApi.resetPassword(phone, password, verifyCode,new RequestCallback<String>() {

            @Override
            public void onSuccess(String result) {
                TLog.log("密码重置成功 == " + result);
                ((AppLogin) getActivity()).login(phone, password);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
            }
        });
    }

    /**
     * 检验手机号是否为空,或者手机号是否为11位
     * @param phone 电话号码
     */
    private boolean checkPhoneLegal(String phone) {
        if(StringUtils.isEmpty(phone)) {
            showToast("手机号不能为空");
            return false;
        }
        if(phone.length() != 11) {
            showToast("手机号格式不正确");
            return false;
        }
        return true;
    }

    private void getMobileCode(String phone, final TextView view) {
        final CountDownTimer timer = verifyCodeCountdown(view);
        isSendSuccess = false;
        LefuApi.getMobileCode(phone, new RequestCallback<String>() {

            @Override
            public void onSuccess(String result) {
                TLog.log("获取验证码result == " + result);
                showToast("验证码发送成功,注意查收");
                isSendSuccess = true;
            }

            @Override
            public void onFailure(ApiHttpException e) {
                TLog.log("验证码获取失败result == " + e.toString());
                showToast(e.getMessage());
                view.setText("重新发送");
                view.setClickable(true);
                view.setFocusable(true);
                timer.cancel();
            }
        });
    }

    /**
     * 获取倒计时时间控制对象
     * @param view 获取验证码按钮
     * @return 倒计时控件
     */
    private CountDownTimer verifyCodeCountdown(final TextView view) {
        return new CountDownTimer(160000, 1000) {

            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                view.setClickable(false);
                view.setFocusable(false);
                view.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                view.setText("重新发送");
                view.setClickable(true);
                view.setFocusable(true);
            }
        }.start();
    }

}
