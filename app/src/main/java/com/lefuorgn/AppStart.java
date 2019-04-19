package com.lefuorgn;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.bean.User;
import com.lefuorgn.gov.GovMainActivity;
import com.lefuorgn.lefu.MainActivity;
import com.lefuorgn.util.NetworkUtils;
import com.lefuorgn.util.ToastUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 应用启动界面
 */

public class AppStart extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = View.inflate(this, R.layout.activity_app_start, null);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setContentView(view);
        AppContext appContext = AppContext.getInstance();
        User user = appContext.getUser();
        if(appContext.isLogin()) {
            // 保存登录状态
            if(user.isGov() || user.isGroup()) {
                // 特殊用户主页面(自动登录)
                autoLogin(user.getMobile(), user.getPassword());
            }else {
                // 普通用户主页面
                delayedSkip(true, view);
                // 登录成功发送广播
                Intent broadcast = new Intent(AppConfig.INTENT_ACTION_NOTICE_LOGIN);
                sendBroadcast(broadcast);
            }
        }else {
            // 未登录
            delayedSkip(false, view);
        }
    }

    /**
     * 自动登录
     * @param name 用户名称
     * @param password 用户登录密码
     */
    private void autoLogin(String name, String password) {
        if(NetworkUtils.NETWORK_NONE == AppContext.getInstance().getNetState()) {
            ToastUtils.show(this, "网络不可用", 1);
            Intent intent = new Intent(AppStart.this, AppLogin.class);
            startActivity(intent);
            finish();
            return;
        }
        final long startTime = System.currentTimeMillis();
        LefuApi.login(name, password, new RequestCallback<User>() {
            @Override
            public void onSuccess(User result) {
                // 保存登录信息
                AppContext.getInstance().saveUserInfo(result);
                // 登录成功发送广播
                Intent broadcast = new Intent(AppConfig.INTENT_ACTION_NOTICE_LOGIN);
                sendBroadcast(broadcast);
                // 延时跳转
                long time = System.currentTimeMillis() - startTime;
                if(time >= 1500) {
                    time = 0;
                }else {
                    time = 1500 - time;
                }
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {

                    @Override
                    public void run() {
                        Intent intent = new Intent(AppStart.this, GovMainActivity.class);
                        if(getIntent().getExtras() != null) {
                            intent.putExtras(getIntent().getExtras());
                        }
                        startActivity(intent);
                        finish();
                    }
                };
                timer.schedule(task, time);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                // 自动登录失败, 跳转到登录页面
                ToastUtils.show(AppStart.this, e.getMessage(), 1);
                Intent intent = new Intent(AppStart.this, AppLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * 延时登录
     * @param login 是否登录
     * @param view 背景页面
     */
    private void delayedSkip(final boolean login, View view) {
        AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
        aa.setDuration(1500);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                redirectTo(login);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 跳转到...(未登录或者机构页面)
     */
    private void redirectTo(boolean login) {
        Intent intent;
        if(login) {
            // 普通用户主页面
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if(getIntent().getExtras() != null) {
                intent.putExtras(getIntent().getExtras());
            }
            intent.putExtra(MainActivity.FIRST_LEVEL_PAGE, true);
        }else {
            // 用户未登录, 跳转到登录页面
            intent = new Intent(this, AppLogin.class);
        }
        startActivity(intent);
        finish();
    }

}
