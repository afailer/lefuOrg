package com.lefuorgn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.bean.User;
import com.lefuorgn.fragment.LoginFragment;
import com.lefuorgn.fragment.ResetPasswordFragment;
import com.lefuorgn.gov.GovMainActivity;
import com.lefuorgn.lefu.MainActivity;
import com.lefuorgn.util.DoubleClickExitUtils;
import com.lefuorgn.util.StringUtils;

/**
 * 用户登录页面
 */

public class AppLogin extends BaseActivity {

    private AppContext mAppContext;

    private FragmentManager mFManager;
    private LoginFragment mLoginFragment; // 登录页面
    private ResetPasswordFragment mResetPasswordFragment; // 忘记密码登录页面

    private DoubleClickExitUtils mDoubleClickExit;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_app_login;
    }

    @Override
    protected void initView() {
        addAllFragment();
        skipLoginFragment();
    }

    @Override
    protected void initData() {
        mDoubleClickExit = new DoubleClickExitUtils(this);
        mAppContext = AppContext.getInstance();
    }

    /**
     * 添加登录页面和密码找回页面
     */
    private void addAllFragment() {
        if (mFManager == null) {
            mFManager = getSupportFragmentManager();
        }
        mLoginFragment = (LoginFragment) Fragment
                .instantiate(this, LoginFragment.class.getName(), new Bundle());
        mResetPasswordFragment = (ResetPasswordFragment) Fragment
                .instantiate(this, ResetPasswordFragment.class.getName(), new Bundle());
        FragmentTransaction transaction = mFManager.beginTransaction();
        transaction.add(R.id.fl_activity_app_login, mLoginFragment,
                "login");
        transaction.add(R.id.fl_activity_app_login, mResetPasswordFragment,
                "reset");
        transaction.commit();
    }

    /**
     * 跳转到登录页面
     */
    public void skipLoginFragment() {
        if(mLoginFragment == null) {
            mLoginFragment = (LoginFragment) mFManager.findFragmentByTag("login");
        }
        FragmentTransaction transaction = mFManager.beginTransaction();
        transaction.show(mLoginFragment);
        transaction.hide(mResetPasswordFragment);
        transaction.commit();
    }

    /**
     * 跳转到密码找回页面
     */
    public void skipResetPasswordFragment() {
        if(mResetPasswordFragment == null) {
            mResetPasswordFragment = (ResetPasswordFragment) mFManager.findFragmentByTag("reset");
        }
        FragmentTransaction transaction = mFManager.beginTransaction();
        transaction.show(mResetPasswordFragment);
        transaction.hide(mLoginFragment);
        transaction.commit();
    }

    /**
     * 登录
     * @param name 用户名称
     * @param password 用户密码
     */
    public void login(String name, String password) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
            showToast("用户名或密码不能为空");
            return;
        }
        View view = getCurrentFocus();
        if(view != null) {
            InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken() ,InputMethodManager.HIDE_NOT_ALWAYS);
        }
        showLoadingDialog("登录中...");
        LefuApi.login(name, password, new RequestCallback<User>() {
            @Override
            public void onSuccess(User result) {
                redirectTo(result);
                hideLoadingDialog();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
                hideLoadingDialog();
            }
        });
    }

    /**
     * 监听返回--是否退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 是否退出应用
            return mDoubleClickExit.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 跳转到...
     */
    private void redirectTo(User user) {
        if(user.getAgency_id() == 0 && !user.isGroup() && !user.isGov()) {
            showToast("个人用户禁止登录");
            return;
        }
        if(AppContext.getLastUserId() != user.getUser_id()) {
            // 用户变化
            AppContext.getInstance().cleanAllInfo();
        }
        // 登录成功, 保存当前用户信息
        mAppContext.saveUserInfo(user);
        // 登录成功发送广播
        Intent broadcast = new Intent(AppConfig.INTENT_ACTION_NOTICE_LOGIN);
        sendBroadcast(broadcast);
        Intent intent;
        // 用户登录, 根据用户类型跳转到不同的主页面
        if(user.isGroup() || user.isGov()) {
            // 特殊用户主页面
            intent = new Intent(this, GovMainActivity.class);
        }else {
            // 普通用户主页面
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(MainActivity.FIRST_LEVEL_PAGE, true);
        }
        startActivity(intent);
        finish();
    }

}
