package com.lefuorgn.fragment;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lefuorgn.AppContext;
import com.lefuorgn.AppLogin;
import com.lefuorgn.R;
import com.lefuorgn.base.BaseFragment;
import com.lefuorgn.widget.togglebutton.ToggleButton;

/**
 * 登录页面
 */

public class LoginFragment extends BaseFragment {

    private EditText mNameEdit, mPasswordEdit;
    private TextView mVersionView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mNameEdit = (EditText) view.findViewById(R.id.et_fragment_login_name);
        mPasswordEdit = (EditText) view.findViewById(R.id.et_fragment_login_psw);
        view.findViewById(R.id.btn_fragment_login).setOnClickListener(this);
        view.findViewById(R.id.btn_fragment_login_forget).setOnClickListener(this);
        mVersionView = (TextView) view.findViewById(R.id.tv_fragment_login_version);
        ToggleButton tButton = (ToggleButton) view.findViewById(R.id.tb_fragment_login);
        tButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {

            @Override
            public void onToggle(boolean on) {
                if(on) {
                    // 密码可见
                    mPasswordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mPasswordEdit.setSelection(mPasswordEdit.length());
                }else {
                    mPasswordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mPasswordEdit.setSelection(mPasswordEdit.length());
                }
            }
        });
    }

    @Override
    protected void initData() {
        mVersionView.setText(String.format("ver: %s", AppContext.getInstance().getPackageInfo().versionName));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fragment_login:
                // 登录
                String name = mNameEdit.getText().toString().trim();
                String password = mPasswordEdit.getText().toString().trim();
                ((AppLogin)getActivity()).login(name, password);
                break;
            case R.id.btn_fragment_login_forget:
                // 跳转密码找回页面
                ((AppLogin)getActivity()).skipResetPasswordFragment();
                break;
        }
    }
}
